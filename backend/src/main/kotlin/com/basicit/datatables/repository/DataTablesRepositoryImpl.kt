package com.basicit.datatables.repository

import com.basicit.config.GraphQLConfig
import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.AbstractEntity
import com.basicit.model.QCatalog
import com.basicit.service.QClassService
import com.basicit.util.SpringUtils
import com.querydsl.jpa.impl.JPAQueryFactory
import org.slf4j.LoggerFactory
import org.springframework.core.convert.ConversionService
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import java.io.Serializable
import javax.persistence.EntityManager


/*
* T : Entity,
 */
class DataTablesRepositoryImpl<T, ID : Serializable, P: Comparable<P>>(
        val entityInformation: JpaEntityInformation<T, ID>,
        val entityManager: EntityManager,
        val qClassService: QClassService<T>
) :
        SimpleJpaRepository<T, ID>(entityInformation, entityManager),
        DataTablesRepository<T, ID>
{
    private val logger = LoggerFactory.getLogger(GraphQLConfig::class.java.name)

    override fun <T> findAll(input: DataTablesInput?): DataTablesOutput<T> {
        val qClass = qClassService.getQClass(entityInformation.javaType as Class<AbstractEntity>)

        val conversionServiceList = SpringUtils.getBeans(ConversionService::class.java)

        val queryFactory = JPAQueryFactory(entityManager)
        val recordsTotal = queryFactory.from(qClass.qClassInstance).select(qClass.id.countDistinct()).fetchOne()

        input?.let {
            input.columns.forEach { column ->

            }
        }
        val output = DataTablesOutput<T>()


        return output
    }
}
