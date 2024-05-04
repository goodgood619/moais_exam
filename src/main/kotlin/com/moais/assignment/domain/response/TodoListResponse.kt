package com.moais.assignment.domain.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.moais.assignment.domain.entity.Todo
import com.moais.assignment.domain.type.PriorityStatus
import com.moais.assignment.domain.type.TodoStatus
import java.time.LocalDateTime
import java.util.UUID

data class TodoListResponse(
    val id: UUID,
    val title: String,
    val description: String,
    @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val dueDate: LocalDateTime,
    val todoStatus: TodoStatus,
    val priorityStatus: PriorityStatus,
) {

    companion object {
        fun from(todoList: List<Todo>): List<TodoListResponse> = with(todoList) {

            this.map {
                TodoListResponse(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    dueDate = it.dueDate,
                    todoStatus = it.status,
                    priorityStatus = it.priorityStatus
                )
            }
        }
    }
}