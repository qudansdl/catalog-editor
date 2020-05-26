package com.basicit.datatables.repository

import com.basicit.config.GraphQLConfig
import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.domain.QQuery
import com.basicit.model.AbstractEntity
import com.basicit.service.QClassService
import com.querydsl.jpa.impl.JPAQueryFactory
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import java.io.Serializable
import javax.persistence.EntityManager


/*
* T : Entity,
 */
class DataTablesRepositoryImpl<T, ID : Serializable, P: Comparable<P>>(
        val entityInformation: JpaEntityInformation<T, ID>,
        val entityManager: EntityManager
) :
        SimpleJpaRepository<T, ID>(entityInformation, entityManager),
        DataTablesRepository<T, ID>
{
    private val logger = LoggerFactory.getLogger(GraphQLConfig::class.java.name)

    override fun <T> findAll(input: DataTablesInput?): DataTablesOutput<T> {
        val qClass = QClassService.instance.getQClass(entityInformation.javaType as Class<AbstractEntity>)

        val queryFactory = JPAQueryFactory(entityManager)
        val recordsTotal = queryFactory.from(qClass.instance).select(qClass.id.countDistinct()).fetchOne()
        val qQuery = QQuery(
                queryFactory,
                entityInformation.javaType as Class<AbstractEntity>,
                QClassService.instance,
                input)

        val totalCount = qQuery.query.select(qClass.id.countDistinct()).fetchOne()

        val list = if(input == null)
        {
            qQuery.query.select(qClass.instance).fetch()
        } else {
            qQuery.query.select(qClass.instance).offset(input.start.toLong()).limit(input.length.toLong()).fetch()
        } as List<T>

        val output = DataTablesOutput<T>()
        output.recordsFiltered = totalCount!!
        output.recordsTotal = recordsTotal!!
        output.data = list

        return output
    }

}
