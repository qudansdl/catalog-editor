package com.basicit.datatables

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.From
import javax.persistence.criteria.Predicate

interface Filter {
    fun createPredicate(from: From<*, *>, criteriaBuilder: CriteriaBuilder): Predicate?
}