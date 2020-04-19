package com.basicit.datatables.repository

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository
import java.io.Serializable
import java.util.function.Function

/**
 * Convenience interface to allow pulling in [PagingAndSortingRepository] and
 * [JpaSpecificationExecutor] functionality in one go.
 *
 * @author Damien Arrachequesne
 */
@NoRepositoryBean
interface DataTablesRepository<T, ID : Serializable> : PagingAndSortingRepository<T, ID>, JpaSpecificationExecutor<T> {
    /**
     * Returns the filtered list for the given [DataTablesInput].
     *
     * @param input the [DataTablesInput] mapped from the Ajax request
     * @return a [DataTablesOutput]
     */
    fun findAll(input: DataTablesInput?): DataTablesOutput<T>

    /**
     * Returns the filtered list for the given [DataTablesInput].
     *
     * @param input the [DataTablesInput] mapped from the Ajax request
     * @param additionalSpecification an additional [Specification] to apply to the query (with
     * an "AND" clause)
     * @return a [DataTablesOutput]
     */
    fun findAll(input: DataTablesInput?, additionalSpecification: Specification<T>?): DataTablesOutput<T>

    /**
     * Returns the filtered list for the given [DataTablesInput].
     *
     * @param input the [DataTablesInput] mapped from the Ajax request
     * @param additionalSpecification an additional [Specification] to apply to the query (with
     * an "AND" clause)
     * @param preFilteringSpecification a pre-filtering [Specification] to apply to the query
     * (with an "AND" clause)
     * @return a [DataTablesOutput]
     */
    fun findAll(input: DataTablesInput?, additionalSpecification: Specification<T>?,
                preFilteringSpecification: Specification<T>?): DataTablesOutput<T>

    /**
     * Returns the filtered list for the given [DataTablesInput].
     *
     * @param input the [DataTablesInput] mapped from the Ajax request
     * @param converter the [Function] to apply to the results of the query
     * @return a [DataTablesOutput]
     */
    fun <R> findAll(input: DataTablesInput?, converter: Function<T, R>?): DataTablesOutput<R>

    /**
     * Returns the filtered list for the given [DataTablesInput].
     *
     * @param input the [DataTablesInput] mapped from the Ajax request
     * @param additionalSpecification an additional [Specification] to apply to the query (with
     * an "AND" clause)
     * @param preFilteringSpecification a pre-filtering [Specification] to apply to the query
     * (with an "AND" clause)
     * @param converter the [Function] to apply to the results of the query
     * @return a [DataTablesOutput]
     */
    fun <R> findAll(input: DataTablesInput?, additionalSpecification: Specification<T>?,
                    preFilteringSpecification: Specification<T>?, converter: Function<T, R>?): DataTablesOutput<R>
}