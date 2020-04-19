package com.basicit.datatables.mapping

data class DataTablesOutput<T> (
    var recordsTotal: Long = 0L,
    var recordsFiltered: Long = 0L,
    var data: List<T> = emptyList(),
    var error: String? = null

)