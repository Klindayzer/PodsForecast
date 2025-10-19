package com.xfashion.pods.config.dto

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SheetProps::class)
@ConfigurationProperties(prefix = "sheets")
data class SheetProps(
    var spreadsheetId: String = "",
    var range: String = "",
    var credentials: String = ""
)
