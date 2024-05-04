package com.moais.assignment.controller

import com.moais.assignment.domain.request.TodoListCreationRequest
import com.moais.assignment.domain.request.TodoListUpdateRequest
import com.moais.assignment.domain.response.GeneralResponse
import com.moais.assignment.domain.response.TodoListCreationResponse
import com.moais.assignment.domain.response.TodoListResponse
import com.moais.assignment.domain.response.TodoListUpdateResponse
import com.moais.assignment.service.TodoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class TodoController(
    private val todoService: TodoService,
) {

    @GetMapping("/todo")
    fun getTodoList(@RequestParam userId: String): GeneralResponse<List<TodoListResponse>> {
        val todoList = todoService.getTodoList(userId)
        return GeneralResponse(data = todoList)
    }

    @GetMapping("/todo/last")
    fun getTodoLast(@RequestParam userId: String): GeneralResponse<List<TodoListResponse>> {
        val todoLast = todoService.getTodoLast(userId)
        return GeneralResponse(data = todoLast)
    }

    @PostMapping("/todo")
    fun makeTodoList(@RequestBody todoListCreationRequest: TodoListCreationRequest):
            GeneralResponse<TodoListCreationResponse> {
        val makeTodoList = todoService.makeTodoList(todoListCreationRequest)
        return GeneralResponse(data = makeTodoList)
    }

    @PutMapping("/todo/{id}/status")
    fun updateTodoListStatus(@PathVariable id: UUID, @RequestBody todoListUpdateRequest: TodoListUpdateRequest):
            GeneralResponse<TodoListUpdateResponse> {
        val updateTodoListStatus =
            todoService.updateTodoListStatus(todoId = id, todoListUpdateRequest = todoListUpdateRequest)
        return GeneralResponse(data = updateTodoListStatus)
    }

}