package com.moais.assignment.service

import com.moais.assignment.domain.entity.QTodo.todo
import com.moais.assignment.domain.entity.Todo
import com.moais.assignment.domain.request.TodoListCreationRequest
import com.moais.assignment.domain.request.TodoListUpdateRequest
import com.moais.assignment.domain.response.TodoListCreationResponse
import com.moais.assignment.domain.response.TodoListResponse
import com.moais.assignment.domain.response.TodoListUpdateResponse
import com.moais.assignment.domain.type.TodoStatus
import com.moais.assignment.exception.CustomException
import com.moais.assignment.exception.ErrorCode
import com.moais.assignment.repository.TodoRepository
import com.moais.assignment.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class TodoService(
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository,
) {

    fun getTodoList(userId: String): List<TodoListResponse> {
        val user = userRepository.findById(userId).getOrNull()
            ?: throw CustomException(ErrorCode.NOT_EXIST_USER)

        val todoList = todoRepository.findByUser(user)

        return TodoListResponse.from(todoList)
    }

    fun getTodoLast(userId: String): List<TodoListResponse> {
        val user = userRepository.findById(userId).getOrNull()
            ?: throw CustomException(ErrorCode.NOT_EXIST_USER)

        val todo = todoRepository.findFirstByUserOrderByIdDesc(user) ?: throw CustomException(ErrorCode.NOT_EXIST_TODO)

        val list = listOf(todo)
        return TodoListResponse.from(todoList = list )
    }

    @Transactional
    fun makeTodoList(todoListCreationRequest: TodoListCreationRequest): TodoListCreationResponse {
        val user = userRepository.findById(todoListCreationRequest.userId).getOrNull()
            ?: throw CustomException(ErrorCode.NOT_EXIST_USER)

        val savedTodo = todoRepository.save(Todo.of(todoListCreationRequest, user))

        return TodoListCreationResponse.from(savedTodo)

    }

    @Transactional
    fun updateTodoListStatus(todoId: UUID, todoListUpdateRequest: TodoListUpdateRequest): TodoListUpdateResponse {

        val todo = todoRepository.findById(todoId).getOrNull() ?: throw CustomException(ErrorCode.NOT_EXIST_TODO)

        if (!validateTodoStatus(fromTodoStatus = todo.status, toTodoStatus = todoListUpdateRequest.todoStatus)) {
            throw CustomException(ErrorCode.INVALID_TODO_STATUS)
        }

        todo.status = todoListUpdateRequest.todoStatus


        return TodoListUpdateResponse.from(todo)
    }

    private fun validateTodoStatus(fromTodoStatus: TodoStatus, toTodoStatus: TodoStatus) = when {
        fromTodoStatus == toTodoStatus -> true
        fromTodoStatus == TodoStatus.TODO && toTodoStatus in listOf(TodoStatus.IN_PROGRESS) -> true
        fromTodoStatus == TodoStatus.IN_PROGRESS &&
                toTodoStatus in listOf(TodoStatus.DONE, TodoStatus.PENDING) -> true
        fromTodoStatus == TodoStatus.PENDING -> true
        else -> false
    }
}