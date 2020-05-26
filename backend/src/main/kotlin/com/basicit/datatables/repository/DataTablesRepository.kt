package com.basicit.datatables.repository

import com.basicit.datatables.mapping.DataTablesInput
import com.basicit.datatables.mapping.DataTablesOutput
import com.basicit.model.AbstractEntity
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
    fun <T> findAll(input: DataTablesInput?): DataTablesOutput<T>

}
