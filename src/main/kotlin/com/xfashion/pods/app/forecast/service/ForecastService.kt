package com.xfashion.pods.app.forecast.service

import com.xfashion.pods.app.forecast.dto.ForecastPodsModel
import com.xfashion.pods.app.forecast.dto.ForecastResponseModel
import com.xfashion.pods.app.forecast.ml.ForecastFeatures
import com.xfashion.pods.app.forecast.ml.ForecastRidge
import com.xfashion.pods.app.sheet.repository.SheetsRepository
import com.xfashion.pods.config.dto.ForecastProps
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.math.ceil
import kotlin.math.max

@Service
class ForecastService(
    private val sheetsRepository: SheetsRepository,
    private val forecastProps: ForecastProps
) {

    fun getForecast(startDate: LocalDate, endDate: LocalDate): ForecastResponseModel {

        val rows = sheetsRepository.fetchSheetRows()
        if (rows.isEmpty())
            error("No data returned from Google Sheets")

        /**
         * Start of chatGPT generated code
         */
        // Training rows: June entries
        val trainingDataFe = rows.filter { it.fePods != null }
        val trainingDataBe = rows.filter { it.bePods != null }
        require(trainingDataFe.size >= 7 && trainingDataBe.size >= 7) { "Not enough labeled days to train." }

        // Fit FE model
        val feMat = ForecastFeatures.design(rows = trainingDataFe, targetFe = true)
        val feFit = ForecastRidge.fit(feMat.X, feMat.y, forecastProps.ridgeLambda)
        val feModel = Model(feFit.coefficients, feMat.featureNames)

        // Fit BE model
        val beMat = ForecastFeatures.design(rows = trainingDataBe, targetFe = false)
        val beFit = ForecastRidge.fit(beMat.X, beMat.y, forecastProps.ridgeLambda)
        val beModel = Model(beFit.coefficients, beMat.featureNames)

        // Candidate rows to predict: budgeted days when pods are null
        val toPredict = rows.filter { it.fePods == null || it.bePods == null }
            .filter { row ->
                val afterStart = row.date >= startDate
                val beforeEnd = row.date <= endDate
                afterStart && beforeEnd
            }

        val pods = toPredict.map { row ->
            val x = ForecastFeatures.designX(row)
            val fe = max(0.0, ForecastRidge.predict(feModel.beta, x))
            val be = max(0.0, ForecastRidge.predict(beModel.beta, x))
            ForecastPodsModel(
                date = row.date,
                fePods = if (forecastProps.roundUp) ceil(fe).toInt() else fe.toInt(),
                bePods = if (forecastProps.roundUp) ceil(be).toInt() else be.toInt()
            )
        }.sortedBy { it.date }

        /**
         * End of chatGPT generated code
         */

        val start = pods.firstOrNull()?.date ?: startDate
        val end = pods.lastOrNull()?.date ?: endDate

        return ForecastResponseModel(
            start = start,
            end = end,
            pods = pods
        )
    }

    private data class Model(val beta: DoubleArray, val featureNames: List<String>)
}