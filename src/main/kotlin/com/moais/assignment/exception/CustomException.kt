package com.moais.assignment.exception

class CustomException(
    val errorCode: ErrorCode
) : RuntimeException()