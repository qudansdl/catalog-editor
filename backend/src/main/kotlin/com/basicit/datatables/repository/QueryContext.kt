package com.basicit.datatables.repository

import com.basicit.datatables.filter.FilterCriteria
import com.basicit.datatables.filter.FilterOperation
import com.basicit.datatables.mapping.Column
import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.SimpleExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.apache.commons.lang3.reflect.FieldUtils
import org.springframework.core.convert.ConversionService
import java.lang.reflect.Modifier

data class QueryContext<T>(
        val input: DataTablesInput?,
        val output: DataTablesOutput<T>,
        val queryFactory: JPAQueryFactory,
        val domainClass: Class<T>,
        val conversionServiceList: List<ConversionService>
) {
    var qClassInstance: EntityPathBase<T>
    var qClass: Class<*>
    var query: JPAQuery<T>
    var where: BooleanBuilder

    init{
        this.qClassInstance = getQClassInstance(domainClass) as EntityPathBase<T>
        this.qClass = getQClass(domainClass)

        this.query = this.queryFactory.from(qClassInstance) as JPAQuery<T>
        this.where = BooleanBuilder()
    }

    fun getClassName(clazz: Class<*>): String = clazz.typeName

    fun getQClass(clazz: Class<*>): Class<*> = Class.forName(getQClassName(getClassName(clazz))) as Class<*>

    fun getQClassInstance(clazz: Class<*>): EntityPathBase<*> {
        val qClass = getQClass(clazz)
        val fieldList = FieldUtils.getAllFieldsList(qClass)
        var qClassInstance: EntityPathBase<*>? = null
        for(field in fieldList)
        {
            if(Modifier.isStatic(field.modifiers) &&   field.type == qClass)
            {
                qClassInstance  = field.get(null) as EntityPathBase<*>
                break
            }
        }

        return qClassInstance!!
    }



    fun getQClassName(domainClassName: String): String {
        return domainClassName.replace("model\\.".toRegex(), "model.Q")
    }

    fun where(): Predicate = where

    fun <S: Comparable<S>>  filterCriteria(column: Column, path: SimpleExpression<S>):  FilterCriteria<*>  = FilterCriteria(column, path.type, conversionServiceList!!)
}
