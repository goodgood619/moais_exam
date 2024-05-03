package com.moais.assignment.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.moais.assignment.domain.response.GeneralResponse
import com.moais.assignment.exception.ErrorCode
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.annotation.Order
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

@Order(0)
@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper,
) : GenericFilterBean() {

    private val allowedUrls = arrayOf("/", "/swagger-ui/**", "/v3/**", "/user/signup", "/user/login", "/error")
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val req = request as HttpServletRequest

        if (passCurrentFilter(req)) {
            chain?.doFilter(request, response)
        }
        else if (!checkAccessToken(req)) {
            val errorCode = ErrorCode.INVALID_TOKEN
            val generalResponse =
                GeneralResponse(
                    code = errorCode.status.value(),
                    message = errorCode.message,
                    data = null
                )
            val valueResult = objectMapper.writeValueAsString(generalResponse)

            response?.contentType = "application/json;charset=utf-8"
            response?.writer?.write(valueResult)
        } else {
            chain?.doFilter(request, response)
        }
    }

    // 의문 : 특정 Url에 대한 PermitAll이 먹지 않는다 -> permitAll은 SecurityContext가 없어도 괜찮다는 의미이지, 특정 Filter를 타냐 안타냐는 무관
    private fun passCurrentFilter(request: HttpServletRequest): Boolean {
        if (request.requestURI.startsWith("/swagger-ui")) return true
        return allowedUrls.any { it == request.requestURI }
    }

    private fun checkAccessToken(httpServletRequest: HttpServletRequest): Boolean {
        val token = jwtTokenProvider.getAccessToken(httpServletRequest)
        if (token != null) {
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    val authentication = jwtTokenProvider.getAuthentication(token)
                    SecurityContextHolder.getContext().authentication = authentication
                    return true
                }
            } catch (exception: Exception) {
                return false
            }
        }
        return false
    }

}