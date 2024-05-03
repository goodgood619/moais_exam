package com.moais.assignment.controller

import com.moais.assignment.common.JwtTokenProvider
import com.moais.assignment.domain.request.LoginRequest
import com.moais.assignment.domain.request.SignUpRequest
import com.moais.assignment.domain.response.GeneralResponse
import com.moais.assignment.domain.response.LoginResponse
import com.moais.assignment.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    @PostMapping("/user/signup")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): GeneralResponse<Any> {
        userService.signIn(signUpRequest)
        return GeneralResponse()
    }

    @PostMapping("/user/login")
    fun login(@RequestBody loginRequest: LoginRequest): GeneralResponse<LoginResponse> {
        val loginResponse = userService.login(loginRequest)
        return GeneralResponse(data = loginResponse)
    }

    @GetMapping("/user/logout")
    fun logout(httpServletRequest: HttpServletRequest): GeneralResponse<Any> {
        val accessToken = jwtTokenProvider.getAccessToken(request = httpServletRequest)
        userService.logout(accessToken)
        return GeneralResponse()
    }

    @DeleteMapping("/user/delete")
    fun delete(httpServletRequest: HttpServletRequest): GeneralResponse<Any> {
        val accessToken = jwtTokenProvider.getAccessToken(request = httpServletRequest)
        userService.delete(accessToken)
        return GeneralResponse()
    }

}