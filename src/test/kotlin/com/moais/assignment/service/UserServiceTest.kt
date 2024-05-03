package com.moais.assignment.service

import com.moais.assignment.domain.entity.User
import com.moais.assignment.domain.request.LoginRequest
import com.moais.assignment.domain.request.SignUpRequest
import com.moais.assignment.exception.CustomException
import com.moais.assignment.exception.ErrorCode
import com.moais.assignment.repository.UserRepository
import com.moais.assignment.util.encrypt
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(value = ["test"])
@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    @Value("\${aes.key}") val aesKey: String,
) {

    @BeforeEach
    fun setUp() {
        userRepository.save(
            User(id = encrypt("010-1234-1234", aesKey),
                nickname = "goodgood",
                userPassword = encoder.encode("password"),
            )
        )
    }

    @Test
    fun `signin - 이미 존재하는 계정 실패`() {
        val errorCode = assertThrows<CustomException> {
            userService.signIn(signUpRequest =
            SignUpRequest(
                    userId = "010-1234-1234",
                    nickName = "goodgood",
                    password = "password2",
                )
            )
        }.errorCode

        assertThat(errorCode).isEqualTo(ErrorCode.INVALID_PARAMETER)
    }

    @Test
    fun `login - ID가 없을 경우 테스트`() {
        val errorCode = assertThrows<CustomException> {
            userService.login(loginRequest = LoginRequest("010-1234-5678", password = "password"))
        }.errorCode

        assertThat(errorCode).isEqualTo(ErrorCode.NOT_EXIST_USER)
    }

    @Test
    fun `login - 비밀번호 틀릴 경우 테스트`() {

        val errorCode = assertThrows<CustomException> {
            userService.login(loginRequest = LoginRequest("010-1234-1234", password = "password2"))
        }.errorCode

        assertThat(errorCode).isEqualTo(ErrorCode.INVALID_PARAMETER)

    }

}