package com.basicit.datatables.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import java.io.Serializable
import javax.persistence.EntityManager

/**
 * [FactoryBean] creating DataTablesRepositoryFactory instances.
 *
 * @author Damien Arrachequesne
 */
class DataTablesRepositoryFactoryBean<R : JpaRepository<T, ID>?, T, ID : Serializable?>(repositoryInterface: Class<out R>?) : JpaRepositoryFactoryBean<R, T, ID>(repositoryInterface!!) {
    override fun createRepositoryFactory(entityManager: EntityManager): RepositoryFactorySupport {
        return DataTablesRepositoryFactory<T, ID>(entityManager)
    }

    private class DataTablesRepositoryFactory<T, ID : Serializable?>(entityManager: EntityManager?) : JpaRepositoryFactory(entityManager!!) {
        override fun getRepositoryBaseClass(metadata: RepositoryMetadata): Class<*> {
            val repositoryInterface = metadata.repositoryInterface
            return if (DataTablesRepository::class.java.isAssignableFrom(repositoryInterface)) {
                DataTablesRepositoryImpl::class.java
            } else {
                super.getRepositoryBaseClass(metadata)
            }
        }
    }
}