package com.basicit.datatables

import com.basicit.datatables.filter.FilterSpecifications
import com.basicit.datatables.mapping.Column
import com.basicit.datatables.mapping.DataTablesInput
import org.hibernate.query.criteria.internal.path.AbstractPathImpl
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.*

class SpecificationBuilder<E, P: Comparable<P>>(
        private val filterSpecifications: FilterSpecifications<P>
) : AbstractPredicateBuilder<Specification<E>>() {

    override fun build(input: DataTablesInput): Specification<E> {
        return DataTablesSpecification<E>(input)
    }

    private inner class DataTablesSpecification<E>(val input: DataTablesInput) : Specification<E> {
        private val columnPredicates: MutableList<Predicate?> = ArrayList()

        override fun toPredicate(root: Root<E>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate? {
            for (column in input.columns) {
                initPredicatesRecursively(column, root, root, criteriaBuilder)
            }
            val isCountQuery = query.resultType == Long::class.java
            if (isCountQuery) {
                root.fetches.clear()
            }
            return createFinalPredicate(criteriaBuilder)
        }

        private fun initPredicatesRecursively(column: Column, from: From<*, *>, fetch: FetchParent<E, E>, criteriaBuilder: CriteriaBuilder) {
            if (column.isLeaf) {
                //column: Column, criteriaBuilder: CriteriaBuilder, from: From<E, E>
                val predicate = filterSpecifications.getPredicate(column, criteriaBuilder, from)
                columnPredicates.add(predicate)
            }
            for (child in column.columns) {
                val path = from.get<Any>(child.name)
                if (path is AbstractPathImpl<*>) {
                    if ((path as AbstractPathImpl<*>).attribute.isCollection) {
                        // ignore OneToMany and ManyToMany relationships
                        continue
                    }
                }
                if (child.isLeaf) {
                    initPredicatesRecursively(child, from, fetch, criteriaBuilder)
                } else {
                    val join: Join<E, E> = from.join(child.name, JoinType.LEFT)
                    val childFetch: Fetch<E, E> = fetch.fetch(child.name, JoinType.LEFT)
                    initPredicatesRecursively(child, join, childFetch, criteriaBuilder)
                }
            }
        }

        private fun createFinalPredicate(criteriaBuilder: CriteriaBuilder): Predicate {
            val allPredicates: MutableList<Predicate?> = ArrayList(columnPredicates)
            return if (allPredicates.isEmpty()) criteriaBuilder.conjunction() else criteriaBuilder.and(*allPredicates.toTypedArray())
        }
    }
}
