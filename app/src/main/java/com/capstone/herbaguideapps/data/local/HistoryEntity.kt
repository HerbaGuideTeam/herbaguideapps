package com.capstone.herbaguideapps.data.local

data class HistoryEntity(
    val id: Int,
    val name: String?,
    val photoUrl: String?,
    val createAt: String?
)

val dummyHistory = listOf(
    HistoryEntity(1, "Pandan", "", ""),
    HistoryEntity(2, "Sereh", "", ""),
    HistoryEntity(3, "Jahe", "", ""),
    HistoryEntity(4, "Daun Pepaya", "", ""),
    HistoryEntity(5, "Pandan", "", ""),
)
