package com.moais.assignment.domain.request

data class LoginRequest(
    val userId: String,
    val password: String
)