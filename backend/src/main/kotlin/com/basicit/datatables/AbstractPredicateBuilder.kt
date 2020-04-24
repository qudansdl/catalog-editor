package com.basicit.datatables

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import com.basicit.datatables.mapping.DataTablesInput
import org.springframework.data.jpa.domain.Specification
import org.springframework.lang.NonNull
import java.util.*

abstract class AbstractPredicateBuilder<S> {
    /**
     * Creates a 'LIMIT .. OFFSET .. ORDER BY ..' clause for the given [DataTablesInput].
     *
     * @return a [Pageable], must not be null.
     */
    fun createPageable(input: DataTablesInput): Pageable {
        val orders: MutableList<Sort.Order> = ArrayList()
        for (order in input.order) {
            val sortDirection = Sort.Direction.fromString(order.dir)
            orders.add(Sort.Order(sortDirection, order.column))
        }
        val sort = if (orders.isEmpty()) Sort.unsorted() else Sort.by(orders)
        if (input.length === -1) {
            input.start = 0
            input.length = Int.MAX_VALUE
        }
        return DataTablesPageRequest(input.start, input.length, sort)
    }

    abstract fun build(input: DataTablesInput): S
    private inner class DataTablesPageRequest(private val offset: Int, private val pageSize: Int, private val sort: Sort) : Pageable {
        override fun getOffset(): Long {
            return offset.toLong()
        }

        override fun getPageSize(): Int {
            return pageSize
        }

        @NonNull
        override fun getSort(): Sort {
            return sort
        }

        @NonNull
        override fun next(): Pageable {
            throw UnsupportedOperationException()
        }

        @NonNull
        override fun previousOrFirst(): Pageable {
            throw UnsupportedOperationException()
        }

        @NonNull
        override fun first(): Pageable {
            throw UnsupportedOperationException()
        }

        override fun hasPrevious(): Boolean {
            throw UnsupportedOperationException()
        }

        override fun getPageNumber(): Int {
            throw UnsupportedOperationException()
        }

    }

}
