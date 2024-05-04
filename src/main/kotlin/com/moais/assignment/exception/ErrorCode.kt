package com.moais.assignment.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val status: HttpStatus, val message: String) {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 파라미터 입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "사용할 수 없는 토큰입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재 하지 않는 유저 입니다."),
    NOT_EXIST_TODO(HttpStatus.BAD_REQUEST, "존재 하지 않는 TODO 입니다."),
    INVALID_TODO_STATUS(HttpStatus.BAD_REQUEST, "Todo 상태를 바꿀수 없습니다.")

}