package com.xfashion.pods

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class XfashionForecastApplication

fun main(args: Array<String>) {
	runApplication<XfashionForecastApplication>(*args)
}
