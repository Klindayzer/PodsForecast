package com.xfashion.pods.app.sheet.dto

import java.time.LocalDate

data class SheetRowModel(
    val date: LocalDate,
    val gmv: Double,
    val users: Double,
    val marketingCost: Double,
    val fePods: Double?,
    val bePods: Double?
)