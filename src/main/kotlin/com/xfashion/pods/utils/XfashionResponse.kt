package com.xfashion.pods.utils

data class XfashionMeta(var code: Int, var error: String)
data class XfashionResponse<T>(var meta: XfashionMeta, var data: T?)