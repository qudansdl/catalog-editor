package com.basicit.datatables.filter

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Holds Filter Operation for symbol from <br></br>
 *
 * <table border="1">
 * <tr><td> Symbol   </td><td> Operation                   </td><td>Example filter query param</td>
</tr> * <tr><td>eq       </td><td> Equals                     </td><td>city=eq:Sydney	         </td>
</tr> * <tr><td>neq      </td><td> Not Equals                 </td><td>country=neq:uk          </td>
</tr> * <tr><td>gt       </td><td> Greater Than               </td><td>amount=gt:10000         </td>
</tr> * <tr><td>gte      </td><td> Greater Than or equals to  </td><td>amount=gte:10000        </td>
</tr> * <tr><td>lt       </td><td> Less Than                  </td><td>amount=lt:10000         </td>
</tr> * <tr><td>lte      </td><td> Less Than or equals to     </td><td>amount=lte:10000        </td>
</tr> * <tr><td>in       </td><td> IN                         </td><td>country=in:uk, usa, au  </td>
</tr> * <tr><td>nin      </td><td> Not IN                     </td><td>country=nin:fr, de, nz  </td>
</tr> * <tr><td>btn      </td><td> Between                    </td><td>joiningDate=btn:2018-01-01, 2016-01-01   </td>
</tr> * <tr><td>like     </td><td> Like                       </td><td>firstName=like:John     </td>
</tr> *
</table> *  *
 *
 *
 * @author Vijjayy
 */
enum class FilterOperation(private val value: String) {
    EQUAL("eq"),
    NOT_EQUAL("neq"),
    GREATER_THAN("gt"),
    GREATER_THAN_OR_EQUAL_TO("gte"),
    LESS_THAN("lt"),
    LESSTHAN_OR_EQUAL_TO("lte"),
    IN("in"),
    NOT_IN("nin"),
    BETWEEN("btn"),
    NULL("null"),
    NOT_NULL("notnull"),
    CONTAINS("like");

    @JsonValue
    override fun toString(): String {
        return value
    }

    companion object {
        fun fromValue(value: String?): FilterOperation? {
            for (op in values()) {

                //Case insensitive operation name
                if (op.value.equals(value, ignoreCase = true)) {
                    return op
                }
            }
            return null
        }
    }

}
