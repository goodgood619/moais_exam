package com.moais.assignment.domain.request


data class SignUpRequest(
    val userId: String,
    val nickName: String,
    val password: String,
)