package com.moais.assignment.domain.response

import com.moais.assignment.domain.entity.Todo
import com.moais.assignment.domain.type.TodoStatus
import java.util.UUID

data class TodoListUpdateResponse(
    val id: UUID,
    val todoStatus: TodoStatus
) {

    companion object {
        fun from(todo: Todo): TodoListUpdateResponse = with(todo) {
            return TodoListUpdateResponse(
                id = id,
                todoStatus = status,
            )
        }
    }
}