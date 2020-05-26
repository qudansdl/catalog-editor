package com.basicit.config

import com.basicit.datatables.repository.DataTablesRepositoryFactoryBean
import graphql.kickstart.servlet.apollo.ApolloScalars
import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.time.LocalDate
import java.time.LocalTime
import java.util.*


@Configuration
class GraphQLConfig {
    private val logger = LoggerFactory.getLogger(GraphQLConfig::class.java.name)

    @Bean
    fun uploadScalarType(): GraphQLScalarType? = ApolloScalars.Upload

    fun uuidCoercing(builder: GraphQLScalarType.Builder) {
        builder.coercing(object : Coercing<UUID, String> {
            override fun parseLiteral(input: Any?): UUID {
                return when (input) {
                    is StringValue -> UUID.fromString(input.value)
                    is String -> UUID.fromString(input)
                    else -> throw IllegalArgumentException("Unable to parse UUID from $input")
                }
            }

            override fun parseValue(input: Any?): UUID = parseLiteral(input)

            override fun serialize(dataFetcherResult: Any?): String? = when (dataFetcherResult) {
                is String -> dataFetcherResult
                is UUID -> dataFetcherResult.toString()
                else -> null
            }
        })
    }


    @Bean
    fun customScalarID(): GraphQLScalarType {
        val builder = GraphQLScalarType.newScalar()
                .name("ID")
                .description("ID")

        uuidCoercing(builder)

        return builder.build()
    }

    @Bean
    fun customScalarUUID(): GraphQLScalarType {
        val builder =  GraphQLScalarType.newScalar()
                .name("UUID")
                .description("UUID")

        uuidCoercing(builder)
        return builder.build()
    }


    @Bean
    fun customScalarLong(): GraphQLScalarType = GraphQLScalarType.newScalar()
            .name("Long")
            .description("Long, i.e. 12454")
            .coercing(object : Coercing<Long, Long> {
                override fun parseLiteral(input: Any?): Long? {
                    return when (input) {
                        is StringValue -> input.value.toLong()
                        is String -> input.toLong()
                        else -> null
                    }
                }

                override fun parseValue(input: Any?): Long? = parseLiteral(input)

                override fun serialize(longFetcherResult: Any?): Long? = when (longFetcherResult) {
                    is String -> longFetcherResult.toLong()
                    is Long -> longFetcherResult
                    else -> null
                }
            }).build()


    @Bean
    fun customScalarTime(): GraphQLScalarType = ExtendedScalars.Time

    @Bean
    fun customScalarDate(): GraphQLScalarType = ExtendedScalars.Date


    @Bean
    fun customScalarDateTime(): GraphQLScalarType = ExtendedScalars.DateTime
}
