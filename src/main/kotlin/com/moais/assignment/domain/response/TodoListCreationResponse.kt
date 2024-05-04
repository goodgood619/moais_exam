package com.moais.assignment.domain.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.moais.assignment.domain.entity.Todo
import com.moais.assignment.domain.request.TodoListCreationRequest
import com.moais.assignment.domain.type.PriorityStatus
import com.moais.assignment.domain.type.TodoStatus
import java.time.LocalDateTime
import java.util.UUID

data class TodoListCreationResponse(
    val id: UUID,
    val title: String,
    val description: String,
    @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val dueDate: LocalDateTime,
    val todoStatus: TodoStatus,
    val priorityStatus: PriorityStatus,
) {

    companion object {
        fun from(todo: Todo): TodoListCreationResponse = with(todo) {
            return TodoListCreationResponse(
                id = id,
                title = title,
                description = description,
                dueDate = dueDate,
                todoStatus = status,
                priorityStatus = priorityStatus
            )
        }
    }
}