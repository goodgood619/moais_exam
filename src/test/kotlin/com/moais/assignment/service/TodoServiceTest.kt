package com.moais.assignment.service

import com.moais.assignment.domain.entity.Todo
import com.moais.assignment.domain.entity.User
import com.moais.assignment.domain.request.SignUpRequest
import com.moais.assignment.domain.request.TodoListCreationRequest
import com.moais.assignment.domain.request.TodoListUpdateRequest
import com.moais.assignment.domain.type.TodoStatus
import com.moais.assignment.exception.CustomException
import com.moais.assignment.exception.ErrorCode
import com.moais.assignment.repository.TodoRepository
import com.moais.assignment.repository.UserRepository
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.util.UUID

@ActiveProfiles(value = ["test"])
@SpringBootTest
class TodoServiceTest @Autowired constructor(
    private val todoService: TodoService,
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    @Value("\${aes.key}") val aesKey: String,
)  {

    private lateinit var userId: String
    private lateinit var userWithNotTodoId: String
    private lateinit var todoId: UUID

    @BeforeEach
    fun setUp() {

        val signUpRequest = SignUpRequest(
            userId = "010-1234-1234",
            nickName = "goodgood",
            password = "password"
        )

        val signUpRequest2 = SignUpRequest(
            userId = "010-5678-5678",
            nickName = "goodgood2",
            password = "password2"
        )


        val user = User.of(signUpRequest, encoder, aesKey)
        val user2 = User.of(signUpRequest2, encoder, aesKey)

        val createdUser = userRepository.save(user)
        val createdUser2 = userRepository.save(user2)

        val title = "테스트1"
        val description = "상세 설명 테스트 1"
        val dueDate = LocalDate.now()
        val todoListCreationRequest = TodoListCreationRequest(
            title = title,
            description = description,
            dueDate = dueDate,
            userId = user.id
        )
        val todo = todoRepository.save(
            Todo.of(
                todoListCreationRequest = todoListCreationRequest,
                user = createdUser
            )
        )

        userId = user.id
        userWithNotTodoId = createdUser2.id
        todoId = todo.id
    }


    @Test
    fun `getTodoLast - Todo가 없을 경우 Error`() {

        val errorCode = assertThrows<CustomException> {
            todoService.getTodoLast(userId = userWithNotTodoId)
        }.errorCode

        assertThat(errorCode).isEqualTo(ErrorCode.NOT_EXIST_TODO)

    }

    @ParameterizedTest
    @EnumSource(TodoStatus::class, names = ["DONE", "PENDING"])
    fun `updateTodoListStatus - TODO 상태에서 IN_PROGRESS를 제외하면 상태를 변경할수 없다`(status: TodoStatus) {

        val errorCode = assertThrows<CustomException> {
            todoService.updateTodoListStatus(todoId = todoId, todoListUpdateRequest = TodoListUpdateRequest(
                todoStatus = status
            ))
        }.errorCode

        assertThat(errorCode).isEqualTo(ErrorCode.INVALID_TODO_STATUS)
    }


}