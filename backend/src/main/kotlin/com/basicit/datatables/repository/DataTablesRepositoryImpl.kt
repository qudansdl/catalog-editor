package com.basicit.datatables.repository

import com.basicit.config.GraphQLConfig
import com.basicit.datatables.SpecificationBuilder
import com.basicit.datatables.filter.FilterSpecifications
import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.util.SpringUtils
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.convert.ConversionService
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import java.io.Serializable
import java.util.function.Function
import javax.annotation.PostConstruct
import javax.persistence.EntityManager

/*
* T : Entity,
 */
class DataTablesRepositoryImpl<T, ID : Serializable, P: Comparable<P>>(
        entityInformation: JpaEntityInformation<T, ID>,
        entityManager: EntityManager
) : SimpleJpaRepository<T, ID>(entityInformation, entityManager), DataTablesRepository<T, ID> {
    private val logger = LoggerFactory.getLogger(GraphQLConfig::class.java.name)

    private var filterSpecifications: FilterSpecifications<P>? = null
    private var specificationBuilder: SpecificationBuilder<T, P>? = null


    fun init() {
        val conversionServiceList = SpringUtils.getBeans(ConversionService::class.java)
        this.filterSpecifications = FilterSpecifications<P>(conversionServiceList)
        this.specificationBuilder = SpecificationBuilder<T, P>(this.filterSpecifications!!)
    }



    override fun findAll(input: DataTablesInput?): DataTablesOutput<T> {
        return findAll(input, null, null, null)
    }

    override fun findAll(input: DataTablesInput?,
                         additionalSpecification: Specification<T>?): DataTablesOutput<T> {
        return findAll(input, additionalSpecification, null, null)
    }

    override fun findAll(input: DataTablesInput?,
                         additionalSpecification: Specification<T>?, preFilteringSpecification: Specification<T>?): DataTablesOutput<T> {
        return findAll(input, additionalSpecification, preFilteringSpecification, null)
    }

    override fun <R> findAll(input: DataTablesInput?, converter: Function<T, R>?): DataTablesOutput<R> {
        return findAll(input, null, null, converter)
    }

    override fun <R> findAll(ipt: DataTablesInput?,
                             additionalSpecification: Specification<T>?, preFilteringSpecification: Specification<T>?,
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
                    Specification.where(specificationBuilder.build(input))!!
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

}
