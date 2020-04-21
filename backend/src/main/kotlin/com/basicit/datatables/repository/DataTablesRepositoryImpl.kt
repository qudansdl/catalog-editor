package com.basicit.datatables.repository

import com.basicit.config.GraphQLConfig
import com.basicit.datatables.SpecificationBuilder
import com.basicit.datatables.filter.FilterCriteria
import com.basicit.datatables.filter.FilterOperation
import com.basicit.datatables.filter.FilterSpecifications
import com.basicit.datatables.mapping.Column
import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.QCatalog
import com.basicit.util.CoreUtil
import com.basicit.util.SpringUtils
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.ComparablePath
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.SetPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.reflect.FieldUtils
import org.apache.commons.lang3.reflect.FieldUtils.*
import org.slf4j.LoggerFactory
import org.springframework.core.convert.ConversionService
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import java.io.Serializable
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.util.*
import java.util.function.Function
import javax.persistence.EntityManager
import javax.persistence.criteria.*

/*
* T : Entity,
 */
class DataTablesRepositoryImpl<T, ID : Serializable, P: Comparable<P>>(
        entityInformation: JpaEntityInformation<T, ID>,
        val entityManager: EntityManager
) :
        SimpleJpaRepository<T, ID>(entityInformation, entityManager),
        DataTablesRepository<T, ID>
{
    private val logger = LoggerFactory.getLogger(GraphQLConfig::class.java.name)

    private var filterSpecifications: FilterSpecifications<P>? = null
    private var specificationBuilder: SpecificationBuilder<T, P>? = null
    private var conversionServiceList: List<ConversionService>? = null

    fun init() {
        this.conversionServiceList = SpringUtils.getBeans(ConversionService::class.java)
        this.filterSpecifications = FilterSpecifications<P>(conversionServiceList)
        this.specificationBuilder = SpecificationBuilder<T, P>(this.filterSpecifications!!)
    }

    private fun getEntityClassName(): String {
        return (domainClass.genericSuperclass as ParameterizedType).getActualTypeArguments().get(0).getTypeName()
    }

    private fun getEntityName(): String {
        val className = getEntityClassName()
        val list: List<String> = CoreUtil.split(className, "\\.").toList()
        return CoreUtil.toFirstCharLowerCase(list[list.size - 1])
    }

    private fun getQClassName(): String {
        val className = getEntityClassName()
        return className.replace("model\\.".toRegex(), "model.Q")
    }

    private fun getQClass(): Class<*> {
        return Class.forName(getQClassName())
    }

    private fun getQClassInstance(): EntityPathBase<T> {
        val fieldList =  getAllFieldsList(domainClass)
        val qClass = getQClass()
        var qClassInstance: EntityPathBase<T>? = null
        for(field in fieldList)
        {
            if(Modifier.isStatic(field.modifiers) &&   field.type == qClass)
            {
                qClassInstance  = field.get(null) as EntityPathBase<T>
            }
        }

        return qClassInstance!!
    }


    fun addWhere(column: Column, where: BooleanBuilder, qClass: EntityPathBase<*>) {
        val path = getField(qClass.javaClass, column.name).get(qClass)  as StringPath
        val criteria = FilterCriteria(column, path.type, conversionServiceList!!)

        where.and(
            when (criteria.operation) {
                FilterOperation.EQUAL -> path.eq(criteria.convertedSingleValue)
                FilterOperation.NOT_EQUAL-> path.ne(criteria.convertedSingleValue)
                FilterOperation.GREATER_THAN -> path.gt(criteria.convertedSingleValue)
                FilterOperation.GREATER_THAN_OR_EQUAL_TO-> path.goe(criteria.convertedSingleValue)
                FilterOperation.LESS_THAN-> path.lt(criteria.convertedSingleValue)
                FilterOperation.LESSTHAN_OR_EQUAL_TO-> path.loe(criteria.convertedSingleValue)
                FilterOperation.IN-> path.`in`(criteria.convertedValues)
                FilterOperation.NOT_IN-> path.notIn(criteria.convertedValues)
                FilterOperation.BETWEEN-> path.between(criteria.minValue, criteria.maxValue)
                FilterOperation.CONTAINS-> path.like("%" + criteria.convertedSingleValue + "%")
            }
        )
    }

    private fun initPredicatesRecursively(column: Column, query: JPAQuery<*>, where: BooleanBuilder, qClass: EntityPathBase<*>) {
        if (column.isLeaf) {
            addWhere(column, where, qClass)
        }else {
            for (child in column.columns) {
                val path = getField(qClass.javaClass, child.name).get(qClass)  as SetPath<*, *>
                // initPredicatesRecursively(child, join, childFetch, criteriaBuilder)
                // path.queryType
                query.join(path, QAddress.address)


            }
        }
    }


    private fun createWhere(columns: List<Column>, qClass: EntityPathBase<*>)
    {
        val where = BooleanBuilder()
        for (column in columns) {
            initPredicatesRecursively(column, where, qClass)
        }
    }


    override fun findAll(input: DataTablesInput?): DataTablesOutput<T> {
        if(this.specificationBuilder == null)
        {
            this.init()
        }

        val output = DataTablesOutput<T>()

        val queryFactory = JPAQueryFactory(entityManager)
        val qClass = getQClass()
        val qClassInstance = getQClassInstance()

        if(input == null)
        {
            queryFactory.selectFrom(qClassInstance)
                    .fetchAll()
        }else {
            if (input.length === 0) {
                return output
            }
            val recordsTotal = count()
            if (recordsTotal == 0L) {
                return output
            }
            try {
                val recordsTotal = count()
                if (recordsTotal == 0L) {
                    return output
                }
                output.recordsTotal = recordsTotal

                val idField = getField(qClass, "id")
                val id = idField.get(qClassInstance) as ComparablePath<UUID>

                val countQuery = queryFactory.select(id.countDistinct())
                val query = queryFactory.select(qClassInstance)

                val where = BooleanBuilder()


                /*
                val data = findAll(
                        Specification
                                .where(specificationBuilder.build(input))!!
                                .and(additionalSpecification as Specification<T?>?)!!
                                .and(preFilteringSpecification as Specification<T?>?),
                        specificationBuilder.createPageable(input)
                )

                output.data = content

                output.recordsFiltered = data.totalElements
                */
                 */
            } catch (e: Exception) {
                logger.error(e.message, e)
                output.error = e.toString()
            }


        }



        //return findAll(input, null, null, null)
    }


    /*
    override fun findAll(input: DataTablesInput?,
                         additionalSpecification: Specification<T>?): DataTablesOutput<T> {
        return findAll(input, additionalSpecification, null, null)
    }

    override fun findAll(input: DataTablesInput?,
                         additionalSpecification: Specification<T>?,
                         preFilteringSpecification: Specification<T>?): DataTablesOutput<T> {
        return findAll(input, additionalSpecification, preFilteringSpecification, null)
    }

    override fun <R> findAll(input: DataTablesInput?, converter: Function<T, R>?): DataTablesOutput<R> {
        return findAll(input, null, null, converter)
    }

    override fun <R> findAll(ipt: DataTablesInput?,
                             additionalSpecification: Specification<T>?,
                             preFilteringSpecification: Specification<T>?,
                             converter: Function<T, R>?): DataTablesOutput<R> {
        if(this.specificationBuilder == null)
        {
            this.init()
        }

        val output = DataTablesOutput<R>()
        val input  = ipt ?: DataTablesInput()

        if (input.length === 0) {
            return output
        }
        try {
            val recordsTotal = preFilteringSpecification?.let { count(it) } ?: count()
            if (recordsTotal == 0L) {
                return output
            }
            output.recordsTotal = recordsTotal

            val specificationBuilder = this.specificationBuilder!!

            val data = findAll(
                    Specification
                            .where(specificationBuilder.build(input))!!
                            .and(additionalSpecification as Specification<T?>?)!!
                            .and(preFilteringSpecification as Specification<T?>?),
                    specificationBuilder.createPageable(input))
            val content = if (converter == null) data.content as List<R> else data.map(converter).content
            output.data = content
            output.recordsFiltered = data.totalElements
        } catch (e: Exception) {
            logger.error(e.message, e)
            output.error = e.toString()
        }
        return output
    }
    */
}
