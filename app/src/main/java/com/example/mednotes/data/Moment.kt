package com.example.mednotes.data

import java.util.*

data class Moment(
    val title: String,
    val at: Date,
    val withIcon: Int,
    val withMedicines: List<Medicine>,
    var isCollapsed: Boolean = false
)