package com.xfashion.pods.app.sheet.repository

import com.google.api.services.sheets.v4.Sheets
import com.xfashion.pods.app.sheet.dto.SheetRowModel
import com.xfashion.pods.config.dto.SheetProps
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@Repository
class SheetsRepository(
    private val sheets: Sheets,
    private val sheetProps: SheetProps
) {

    fun fetchSheetRows(): List<SheetRowModel> {

        val response = sheets.spreadsheets().values()
            .get(sheetProps.spreadsheetId, sheetProps.range)
            .execute()

        val values = response.getValues() ?: emptyList()
        if (values.isEmpty())
            return emptyList()

        // dropping header row: date, gmv, users, marketing_cost, fe_pods, be_pods
        return values.drop(1).mapNotNull { row ->
            try {
                val date = formattedDate(row[0].toString().trim())
                val gmv = row[1].toString().replace(",", "")
                    .toDouble() // replace comma separator or we can use number formatter
                val users = row[2].toString().toDouble()
                val marketingCost = row[3].toString().toDouble()
                val fePods = row.getOrNull(4)?.toString()?.takeIf { it.isNotBlank() }?.toDoubleOrNull()
                val bePods = row.getOrNull(5)?.toString()?.takeIf { it.isNotBlank() }?.toDoubleOrNull()

                SheetRowModel(
                    date = date,
                    gmv = gmv,
                    users = users,
                    marketingCost = marketingCost,
                    fePods = fePods,
                    bePods = bePods
                )
            } catch (e: Exception) {
                error(e.localizedMessage)
            }
        }.sortedBy { it.date }
    }

    // Formate raw date and try multiple date formatter, we can use ready libraries for a cleaner code
    private fun formattedDate(rawDate: String): LocalDate {

        val date: LocalDate = try {
            LocalDate.parse(rawDate)
        } catch (_: DateTimeParseException) {
            try {
                val fmt = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.ENGLISH)
                LocalDate.parse(rawDate, fmt)
            } catch (_: DateTimeParseException) {
                val altFmt = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.ENGLISH)
                LocalDate.parse(rawDate, altFmt)
            }
        }
        return date
    }
}