package com.basicit.datatables.filter

import com.basicit.datatables.mapping.Column
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.Validate
import org.springframework.core.convert.ConversionService

/**
 * Filter Criteria Holder
 *
 *
 * @author Vijjayy
 *
 * @param <T>
 * is the java type of the DB table column
</T> */
class FilterCriteria<T: Comparable<T>> {

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

    val conversionServiceList: List<ConversionService>
    val javaType: Class<T>

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
    constructor(column: Column, javaType: Class<T>, conversionServiceList: List<ConversionService>)
    {

        // Validations
        Validate.notEmpty(column.name, "Field name can't be empty")
        // Validate.notEmpty(column.value, "Filter criteria can't be empty")
        Validate.notEmpty(column.operation, "Filter operation can't be empty")

        this.conversionServiceList = conversionServiceList
        this.javaType = javaType
        this.fieldName = column.name

        val operation = column.operation
        val operationValue = column.value

        // Convert the operation name to enum
        this.operation = FilterOperation.fromValue(operation)!!

        if(!operationValue.isNullOrBlank()) {
            // Split the filter value as comma separated.
            val operationValues = StringUtils.split(operationValue, ",")
            require(operationValues.size >= 1) { "Operation value can't be empty" }
            this.originalValues = listOf(*operationValues)
            this.convertedValues = mutableListOf()

            // Validate other conditions
            validateAndAssign(operationValues)
        }else
        {
            this.originalValues = listOf()
            this.convertedValues = mutableListOf()
        }
    }

    private fun convert(value: Any, t: Class<*>): T? {

        for(conversionService in this.conversionServiceList)
        {
            if(conversionService.canConvert(value.javaClass, t))
             return conversionService.convert(value, t) as T
        }
        return null
    }
    private fun validateAndAssign(operationValues: Array<String>) {

        //For operation 'btn'
        if (FilterOperation.BETWEEN == operation) {
            //Convert

            //Set min and max values
            require(operationValues.size == 2) { "For 'btn' operation two values are expected" }

            //Convert
            val value1 = convert(operationValues[0], javaType)!!
            val value2 = convert(operationValues[1], javaType)!!

            //Set min and max values
            if (value1 > value2) {
                minValue = value2
                maxValue = value1
            } else {
                minValue = value1
                maxValue = value2
            }

            //For 'in' or 'nin' operation
        } else if (FilterOperation.IN == operation || FilterOperation.NOT_IN == operation) {
            convertedValues.addAll(originalValues.map { it ->  this.convert(it , javaType)!! })
        } else if (FilterOperation.NULL == operation || FilterOperation.NOT_NULL == operation) {
        } else {
            //All other operation
            convertedSingleValue = this.convert(operationValues[0] , javaType)
        }
    }

}
