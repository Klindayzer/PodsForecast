package com.xfashion.pods.app.forecast.dto

import java.time.LocalDate

data class ForecastResponseModel(
    val start: LocalDate,
    val end: LocalDate,
    val pods: List<ForecastPodsModel>
)

data class ForecastPodsModel(
    val date: LocalDate,
    val fePods: Int,
    val bePods: Int
)