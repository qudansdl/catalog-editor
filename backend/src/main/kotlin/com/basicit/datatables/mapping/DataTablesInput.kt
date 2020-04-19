package com.basicit.datatables.mapping

data class DataTablesInput (
    var start: Int = 0,
    var length:  Int = 10,
    val order: MutableList<Order> = mutableListOf(),
    val columns: MutableList<Column> = mutableListOf()
)
