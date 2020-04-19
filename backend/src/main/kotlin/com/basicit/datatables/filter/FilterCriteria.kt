package com.basicit.datatables.filter

import com.basicit.datatables.mapping.Column
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.Validate
import org.springframework.core.convert.ConversionService
import javax.persistence.criteria.Expression

/**
 * Filter Criteria Holder
 *
 *
 * @author Vijjayy
 *
 * @param <T>
 * is the java type of the DB table column
</T> */
class FilterCriteria<T: Comparable<in T>> {

    /**
     * Holds the operation [FilterOperation]
     *
     */
    val operation: FilterOperation

    /**
     * Table column name
     */
    val fieldName: String

    /**
     * Converted value
     */
    var convertedSingleValue: T? = null

    /**
     * minimum value - application only for [FilterOperation.BETWEEN]
     */
    var minValue: T? = null

    /**
     * maximum value - application only for [FilterOperation.BETWEEN]
     */
    var maxValue: T? = null

    /**
     * Holds the filter criteria
     *
     */
    val originalValues: Collection<String>

    /**
     * Holds the filter criteria as type <T>
     *
    </T> */
    val convertedValues: MutableCollection<T>

    val conversionService: ConversionService
    val expression: Expression<*>

    /**
     *
     * Constructor for Filter Criteria with DB table column name, filter string and
     * converter function <br></br>
     *
     * Filter Criteria Structure: <br></br>
     *
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
     *
     *
     * @param column
     * @param expression
     * @param conversionService
     */
    constructor(column: Column, expression: Expression<T>, conversionService: ConversionService)
    {

        // Validations
        Validate.notEmpty(column.name, "Field name can't be empty")
        Validate.notEmpty(column.value, "Filter criteria can't be empty")
        Validate.notEmpty(column.operation, "Filter operation can't be empty")

        this.conversionService = conversionService
        this.expression = expression
        this.fieldName = column.name

        val operation = column.operation
        val operationValue = column.value

        // Convert the operation name to enum
        this.operation = FilterOperation.fromValue(operation)!!

        // Split the filter value as comma separated.
        val operationValues = StringUtils.split(operationValue, ",")
        require(operationValues.size >= 1) { "Operation value can't be empty" }
        this.originalValues = listOf(*operationValues)
        this.convertedValues = mutableListOf()

        // Validate other conditions
        validateAndAssign(operationValues)
    }

    private fun validateAndAssign(operationValues: Array<String>) {

        //For operation 'btn'
        if (FilterOperation.BETWEEN == operation) {
            //Convert

            //Set min and max values
            require(operationValues.size == 2) { "For 'btn' operation two values are expected" }

            //Convert
            val value1 = this.conversionService.convert(operationValues[0], expression.javaType) as T
            val value2 = this.conversionService.convert(operationValues[1], expression.javaType) as T

            //Set min and max values
            if (value1!! > value2) {
                minValue = value2
                maxValue = value1
            } else {
                minValue = value1
                maxValue = value2
            }

            //For 'in' or 'nin' operation
        } else if (FilterOperation.IN == operation || FilterOperation.NOT_IN == operation) {
            convertedValues.addAll(originalValues.map { it ->  this.conversionService.convert(it , expression.javaType) as T })
        } else {
            //All other operation
            convertedSingleValue = this.conversionService.convert(operationValues[0] , expression.javaType) as T
        }
    }

}