package com.basicit

import com.basicit.datatables.repository.DataTablesRepositoryFactoryBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean::class)
class GraphqlApplication

fun main(args: Array<String>) {
	runApplication<GraphqlApplication>(*args)

}

