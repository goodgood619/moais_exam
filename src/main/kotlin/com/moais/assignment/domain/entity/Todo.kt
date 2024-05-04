package com.moais.assignment.domain.entity

import com.moais.assignment.domain.request.TodoListCreationRequest
import com.moais.assignment.domain.type.PriorityStatus
import com.moais.assignment.domain.type.TodoStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "todo")
class Todo(
    @Id
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    @Enumerated(EnumType.STRING)
    var status: TodoStatus = TodoStatus.TODO,
    @Enumerated(EnumType.STRING)
    var priorityStatus: PriorityStatus = PriorityStatus.NORMAL,
    val dueDate: LocalDateTime,

    @ManyToOne
    val user: User,
): BaseEntity() {

    companion object {
        fun of(todoListCreationRequest: TodoListCreationRequest, user: User): Todo = with(todoListCreationRequest) {

            return Todo(
                title = title,
                description = description,
                user = user,
                dueDate = dueDate.atTime(LocalTime.MAX)
            )
        }
    }
}