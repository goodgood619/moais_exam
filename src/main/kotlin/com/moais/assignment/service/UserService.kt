package com.moais.assignment.service

import com.moais.assignment.common.JwtTokenProvider
import com.moais.assignment.domain.entity.User
import com.moais.assignment.domain.request.LoginRequest
import com.moais.assignment.domain.request.SignUpRequest
import com.moais.assignment.domain.response.LoginResponse
import com.moais.assignment.exception.CustomException
import com.moais.assignment.exception.ErrorCode
import com.moais.assignment.repository.UserRepository
import com.moais.assignment.util.encrypt
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    @Value("\${aes.key}") val aesKey: String,
) {

    @Transactional
    fun signIn(signUpRequest: SignUpRequest) {
        val existUser = userRepository.findById(encrypt(signUpRequest.userId, aesKey))
        if (existUser.isPresent) {
            throw CustomException(ErrorCode.INVALID_PARAMETER)
        }

        userRepository.save(User.of(signUpRequest, encoder, aesKey))
    }

    @Transactional
    fun login(loginRequest: LoginRequest): LoginResponse {

        val user = userRepository.findById(encrypt(loginRequest.userId, aesKey)).getOrNull()
            ?: throw CustomException(ErrorCode.NOT_EXIST_USER)

        if (!encoder.matches(loginRequest.password, user.password)) {
            throw CustomException(ErrorCode.INVALID_PARAMETER)
        }

        val accessToken = jwtTokenProvider.createAccessToken(userId = user.id)

        user.token = accessToken // JPA dirty Checking 으로 인한 Entity update
        return LoginResponse(token = accessToken, id = user.id)
    }

    @Transactional
    fun logout(accessToken: String?) {
        val user = userRepository.findByToken(accessToken ?: "")
            ?: throw CustomException(ErrorCode.NOT_EXIST_USER)

        user.token = ""
    }

    @Transactional
    fun delete(accessToken: String?) {
        val user = userRepository.findByToken(accessToken ?: "")
            ?: throw CustomException(ErrorCode.NOT_EXIST_USER)

        println("user : ${user}")
        userRepository.delete(user)
    }
}