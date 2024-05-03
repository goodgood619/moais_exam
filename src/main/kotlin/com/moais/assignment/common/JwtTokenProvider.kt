package com.moais.assignment.common

import com.moais.assignment.service.UserDetailService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret-key}") val secretKey: String,
    @Value("\${jwt.valid-access-time}") val validAccessTime: Int,
    private val userDetailService: UserDetailService,
) {

    private val cookiePath: String = "/"

    private val accessTokenHeader: String = "Authorization"

    private val accessTokenValueKey: String = "Bearer"

    fun createAccessToken(userId: String): String {
        val claims = Jwts.claims().setSubject(userId)
        val now = Date()
        val token = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + (1000 * 60 * validAccessTime)))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
        val accessCookie = Cookie(accessTokenHeader, token)
        accessCookie.path = cookiePath
        accessCookie.maxAge = validAccessTime
        return token
    }


    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailService.loadUserByUsername(username = getUserPk(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUserPk(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }


    fun getAccessToken(request: HttpServletRequest): String? {
        return request.getHeader(accessTokenHeader)
            ?: request.cookies?.find { cookie -> cookie.name == accessTokenValueKey }?.value
    }

    fun validateToken(token: String): Boolean {
        val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            ?: return false
        return claims.body.expiration.after(Date())
    }

}