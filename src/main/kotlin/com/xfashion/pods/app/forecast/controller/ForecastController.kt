package com.xfashion.pods.app.forecast.controller

import com.xfashion.pods.app.forecast.dto.ForecastResponseModel
import com.xfashion.pods.app.forecast.service.ForecastService
import com.xfashion.pods.utils.XfashionMeta
import com.xfashion.pods.utils.XfashionResponse
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/forecast")
class ForecastController(
    private val service: ForecastService
) {
    /**
     * Example:
     * GET /api/v1/forecast/pods?startDate=2024-07-01&endDate=2024-12-31
     */

    @GetMapping("/pods")
    fun forecast(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): XfashionResponse<ForecastResponseModel> {

        val data = service.getForecast(startDate, endDate)
        return XfashionResponse(
            meta = XfashionMeta(code = 200, error = ""),
            data = data
        )
    }
}