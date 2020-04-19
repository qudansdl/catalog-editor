package com.basicit.datatables.filter

import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

/**
 *
 * Generic filter specification which holds [EnumMap] for
 * [FilterOperation] with Lambda functions <br></br>
 *
 * Refer <br></br>
 * <table border="1">
 * <tr>
 * <td>Symbol</td>
 * <td>Operation</td>
 * <td>Example filter query param</td>
</tr> * <tr>
 * <td>eq</td>
 * <td>Equals</td>
 * <td>city=eq:Sydney</td>
</tr> * <tr>
 * <td>neq</td>
 * <td>Not Equals</td>
 * <td>country=neq:uk</td>
</tr> * <tr>
 * <td>gt</td>
 * <td>Greater Than</td>
 * <td>amount=gt:10000</td>
</tr> * <tr>
 * <td>gte</td>
 * <td>Greater Than or equals to</td>
 * <td>amount=gte:10000</td>
</tr> * <tr>
 * <td>lt</td>
 * <td>Less Than</td>
 * <td>amount=lt:10000</td>
</tr> * <tr>
 * <td>lte</td>
 * <td>Less Than or equals to</td>
 * <td>amount=lte:10000</td>
</tr> * <tr>
 * <td>in</td>
 * <td>IN</td>
 * <td>country=in:uk, usa, au</td>
</tr> * <tr>
 * <td>nin</td>
 * <td>Not IN</td>
 * <td>country=nin:fr, de, nz</td>
</tr> * <tr>
 * <td>btn</td>
 * <td>Between</td>
 * <td>joiningDate=btn:2018-01-01, 2016-01-01</td>
</tr> * <tr>
 * <td>like</td>
 * <td>Like</td>
 * <td>firstName=like:John</td>
</tr> *
</table> *
 * *
 *
 * @author Vijjayy
 *
 * @param <E>
 * @param <T>
</T></E> */
@Service
class FilterSpecifications<E, T : Comparable<T>> {
    private var map: MutableMap<FilterOperation, Function<FilterCriteria<T>, Specification<E>>> = mutableMapOf()
    fun getSpecification(operation: FilterOperation): Function<FilterCriteria<T>, Specification<E>> {
        return map[operation]!!
    }

    /**
     *
     * Forms the generic filter specifications for the operations
     * [FilterOperation]
     *
     * @return
     */
    private fun initSpecifications(): Map<FilterOperation, Function<FilterCriteria<T>, Specification<E>>> {
        // Equal
        map[FilterOperation.EQUAL] = Function { filterCriteria: FilterCriteria<T> ->
            Specification { root: Root<E>, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.equal(root.get<Any>(filterCriteria.fieldName), filterCriteria.convertedSingleValue)
            }
        }
        map[FilterOperation.NOT_EQUAL] = Function { filterCriteria: FilterCriteria<T> ->
            Specification { root: Root<E>, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.notEqual(root.get<Any>(filterCriteria.fieldName), filterCriteria.convertedSingleValue)
            }
        }
        map[FilterOperation.GREATER_THAN] = Function { filterCriteria: FilterCriteria<T> ->
            Specification { root: Root<E>, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.greaterThan(root[filterCriteria.fieldName], filterCriteria.convertedSingleValue!!)
            }
        }
        map[FilterOperation.GREATER_THAN_OR_EQUAL_TO] = Function { filterCriteria: FilterCriteria<T> ->
            Specification { root: Root<E>, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.greaterThanOrEqualTo(root[filterCriteria.fieldName], filterCriteria.convertedSingleValue!!)
            }
        }
        map[FilterOperation.LESS_THAN] = Function { filterCriteria: FilterCriteria<T> ->
            Specification { root: Root<E>, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.lessThan(root[filterCriteria.fieldName], filterCriteria.convertedSingleValue!!)
            }
        }
        map[FilterOperation.LESSTHAN_OR_EQUAL_TO] = Function { filterCriteria: FilterCriteria<T> ->
            Specification { root: Root<E>, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.lessThanOrEqualTo(root[filterCriteria.fieldName], filterCriteria.convertedSingleValue!!)
            }
        }
        map[FilterOperation.IN] = Function { filterCriteria: FilterCriteria<T> ->
            Specification { root: Root<E>, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                root.get<Any>(filterCriteria.fieldName).`in`(filterCriteria.convertedValues)
            }
        }
        map[FilterOperation.NOT_IN] = Function { filterCriteria: FilterCriteria<T> ->
            Specification { root: Root<E>, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder
                        .not(root.get<Any>(filterCriteria.fieldName).`in`(filterCriteria.convertedSingleValue))
            }
        }
        map[FilterOperation.BETWEEN] = Function { filterCriteria: FilterCriteria<T> ->
            Specification { root: Root<E>, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.between(root[filterCriteria.fieldName], filterCriteria.minValue!!, filterCriteria.maxValue!!)
            }
        }
        map[FilterOperation.CONTAINS] = Function { filterCriteria: FilterCriteria<T> ->
            Specification { root: Root<E>, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.like(root.get(filterCriteria.fieldName), "%" + filterCriteria.convertedSingleValue + "%")
            }
        }
        return map
    }

    init {
        initSpecifications()
    }
}