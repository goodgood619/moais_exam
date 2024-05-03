package com.moais.assignment.domain.response

data class GeneralResponse<T>(
    val code: Int = 200,
    val message: String = "ok",
    val data: T? = null
)