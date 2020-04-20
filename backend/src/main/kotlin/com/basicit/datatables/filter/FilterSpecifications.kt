package com.basicit.datatables.filter

import com.basicit.datatables.mapping.Column
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service
import java.lang.reflect.TypeVariable
import java.util.*
import javax.persistence.criteria.*

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
class FilterSpecifications<P: Comparable<P>>(private val conversionServiceList: List<ConversionService>) {
    fun getPredicate(column: Column, criteriaBuilder: CriteriaBuilder, from: From<*, *>): Predicate {
        val expression = from.get<Any>(column.name) as Expression<P>
        val criteria = FilterCriteria(column, expression, conversionServiceList)

        return when (criteria.operation) {
            FilterOperation.EQUAL -> criteriaBuilder.equal(expression, criteria.convertedSingleValue)
            FilterOperation.NOT_EQUAL-> criteriaBuilder.notEqual(expression, criteria.convertedSingleValue)
            FilterOperation.GREATER_THAN -> criteriaBuilder.greaterThan(expression, criteria.convertedSingleValue!!)
            FilterOperation.GREATER_THAN_OR_EQUAL_TO-> criteriaBuilder.greaterThanOrEqualTo(expression, criteria.convertedSingleValue!!)
            FilterOperation.LESS_THAN-> criteriaBuilder.lessThan(expression, criteria.convertedSingleValue!!)
            FilterOperation.LESSTHAN_OR_EQUAL_TO-> criteriaBuilder.lessThanOrEqualTo(expression, criteria.convertedSingleValue!!)
            FilterOperation.IN-> expression.`in`(criteria.convertedValues!!)
            FilterOperation.NOT_IN-> criteriaBuilder.not(expression.`in`(criteria.convertedValues))
            FilterOperation.BETWEEN-> criteriaBuilder.between(expression, criteria.minValue!!, criteria.maxValue!!)
            FilterOperation.CONTAINS-> criteriaBuilder.like(expression as Expression<String>, "%" + criteria.convertedSingleValue + "%")
            else -> { // Note the block
                criteriaBuilder.equal(expression, criteria.convertedSingleValue)
            }
        }

    }
}

