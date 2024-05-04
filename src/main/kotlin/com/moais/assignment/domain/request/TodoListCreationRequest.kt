package com.moais.assignment.domain.request

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class TodoListCreationRequest(
    val title: String,
    val description: String,
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val dueDate: LocalDate,
    val userId: String,
) {
}