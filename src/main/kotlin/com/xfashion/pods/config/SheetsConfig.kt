package com.xfashion.pods.config

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import com.xfashion.pods.config.dto.SheetProps
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class SheetsConfig(private val sheetProps: SheetProps) {

    @Bean
    fun sheetsService(): Sheets {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()

        // Load from resources folder, this should be in pipeline config
        val resource = ClassPathResource(sheetProps.credentials)
        val credentials = resource.inputStream.use { input ->
            GoogleCredentials.fromStream(input)
                .createScoped(listOf(SheetsScopes.SPREADSHEETS_READONLY))
        }

        return Sheets.Builder(httpTransport, jsonFactory, HttpCredentialsAdapter(credentials))
            .setApplicationName("XFashion-Forecasting")
            .build()
    }
}