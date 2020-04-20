package com.basicit.config

import com.basicit.datatables.repository.DataTablesRepositoryFactoryBean
import graphql.kickstart.servlet.apollo.ApolloScalars
import graphql.language.StringValue
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
    fun customScalarLocalDate(): GraphQLScalarType = GraphQLScalarType.newScalar()
            .name("LocalDate")
            .description("Simple LocalDate, i.e. 2017-12-01")
            .coercing(object : Coercing<LocalDate, String> {
                override fun parseLiteral(input: Any?): LocalDate? {
                    return when (input) {
                        is StringValue -> LocalDate.parse(input.value)
                        is String -> LocalDate.parse(input)
                        else -> null
                    }
                }

                override fun parseValue(input: Any?): LocalDate? = parseLiteral(input)

                override fun serialize(dataFetcherResult: Any?): String? = when (dataFetcherResult) {
                    is String -> dataFetcherResult
                    is LocalDate -> dataFetcherResult.toString()
                    else -> null
                }
            }).build()

    @Bean
    fun customScalarLocalTime(): GraphQLScalarType =
            GraphQLScalarType.newScalar()
                    .name("LocalTime")
                    .description("Simple LocalTime, i.e. 19:37:21")
                    .coercing(object : Coercing<LocalTime, String> {
                        override fun parseLiteral(input: Any?): LocalTime? {
                            return when (input) {
                                is StringValue -> LocalTime.parse(input.value)
                                is String -> LocalTime.parse(input)
                                else -> null
                            }
                        }

                        override fun parseValue(input: Any?): LocalTime? = parseLiteral(input)

                        override fun serialize(dataFetcherResult: Any?): String? = when (dataFetcherResult) {
                            is String -> dataFetcherResult
                            is LocalTime -> dataFetcherResult.toString()
                            else -> null
                        }
                    }).build()



}
