package com.xfashion.pods.config.dto

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ForecastProps::class)
@ConfigurationProperties(prefix = "forecast")
data class ForecastProps(
    var ridgeLambda: Double = 0.1,
    var roundUp: Boolean = true
)