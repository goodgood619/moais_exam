package com.moais.assignment.domain.request

import com.moais.assignment.domain.type.TodoStatus

data class TodoListUpdateRequest(
    val todoStatus: TodoStatus,
) {
}