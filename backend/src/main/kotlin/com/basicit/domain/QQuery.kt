package com.basicit.domain

import com.basicit.datatables.filter.FilterCriteria
import com.basicit.datatables.mapping.Column
import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.Order
import com.basicit.model.AbstractEntity
import com.basicit.model.QCatalog
import com.basicit.service.QClassService
import com.basicit.util.SpringUtils
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.ComparableExpressionBase
import com.querydsl.jpa.impl.JPAQueryFactory
import org.apache.commons.lang3.reflect.ConstructorUtils
import org.apache.commons.lang3.reflect.MethodUtils
import org.springframework.core.convert.ConversionService

class QQuery(
        private val queryFactory: JPAQueryFactory,
        private val entityClass: Class<AbstractEntity>,
        private val qClassService: QClassService,
        private val input: DataTablesInput?
) {
    private val conversionServiceList  = SpringUtils.getBeans(ConversionService::class.java)
    private val qClass = qClassService.getQClass(entityClass)
    private val where = BooleanBuilder()
    private val order = mutableListOf<OrderSpecifier<*>>()

    val query = this.queryFactory.from(qClass.instance)
    init {
        input?.let {
            if(input.columns.size > 0) {
                input.columns.forEach { column ->
                    addWhere(column, qClass)
                }
                query.where(where)
            }
        }
    }

    private fun addSort(order: Order, qClass: QClass)
    {
        val queryPath = qClass.getPath(order.column) as ComparableExpressionBase<*>

        if(order.dir == "desc")
        {
            this.order.add(queryPath.desc())
        }else {
            this.order.add(queryPath.asc())
        }
    }

    private fun addWhere(column: Column, qClass: QClass)
    {
        val queryPath = qClass.getPath(column.name)
        if (column.isLeaf()) {
            if (!column.value.isNullOrBlank() || column.operation == "null" || column.operation == "notnull") {
                val criteria = ConstructorUtils.invokeConstructor(FilterCriteria::class.java, column, queryPath.type, conversionServiceList!!)
                when (column.operation) {
                  "eq" -> MethodUtils.invokeMethod(queryPath, "eq", criteria.convertedSingleValue)
                  "null" -> MethodUtils.invokeMethod(queryPath, "isNull")
                  "notnull" -> MethodUtils.invokeMethod(queryPath, "isNotNull")
                  "neq" -> MethodUtils.invokeMethod(queryPath, "ne", criteria.convertedSingleValue)
                  "gt" -> MethodUtils.invokeMethod(queryPath, "gt", criteria.convertedSingleValue)
                  "gte" -> MethodUtils.invokeMethod(queryPath, "goe", criteria.convertedSingleValue)
                  "lte" ->  MethodUtils.invokeMethod(queryPath, "loe", criteria.convertedSingleValue)
                  "in" ->  MethodUtils.invokeMethod(queryPath, "in", criteria.convertedValues)
                  "nin" ->  MethodUtils.invokeMethod(queryPath, "notIn", criteria.convertedValues)
                  "btn" ->  MethodUtils.invokeMethod(queryPath, "between", criteria.minValue, criteria.maxValue)
                  "like" ->  MethodUtils.invokeMethod(queryPath, "like", "%" + criteria.convertedSingleValue + "%")
                  else -> null
                }?.let {
                    where.and(it as Predicate)
                }
            }
        }else {
            val elementType = MethodUtils.invokeMethod(queryPath, "getElementType") as Class<AbstractEntity>
            val subQClass = this.qClassService.getQClass(elementType)

            MethodUtils.invokeMethod(this.query, "join", queryPath, subQClass.instance)

            column.columns.forEach {
                addWhere(it, subQClass)
            }
        }
    }

    fun selectTotal(): Long {
        return this.query.select(qClass.id.countDistinct()).fetchOne()!!
    }

    fun select(): List<*> {

        if(input != null && input.order.size > 0) {
            input.order.forEach { order ->
                addSort(order, qClass)
            }
            query.orderBy(*order.toTypedArray())
        }

        return if(input == null)
        {
            this.query.select(qClass.instance).fetch()
        } else {
            this.query.select(qClass.instance)
            if(input.start > 0) {
                this.query.offset(input.start.toLong())
            }
            if(input.length > 0) {
                this.query.limit(input.length.toLong())
            }
            this.query.fetch()
        }
    }
}
