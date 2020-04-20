package com.basicit.datatables.mapping

data class Column (
    val name: String,
    val value: String? = null,
    val operation: String? = null,
    val columns: MutableList<Column> = mutableListOf()
)
{
    val isLeaf = columns.size == 0
}
