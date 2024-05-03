package com.moais.assignment.domain.entity

import com.moais.assignment.domain.request.SignUpRequest
import com.moais.assignment.util.encrypt
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
@Table(name = "users")
class User(
    @Id
    val id: String,
    val nickname: String,
    val userPassword: String,
    var token: String = "",
) : BaseEntity(), UserDetails {

    companion object {
        fun of(signUpRequest: SignUpRequest, encoder: PasswordEncoder, key: String) =
            User(
                id = encrypt(signUpRequest.userId, key),
                nickname = signUpRequest.nickName,
                userPassword = encoder.encode(signUpRequest.password),
            )
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null
    }

    override fun getPassword(): String {
        return userPassword
    }

    override fun getUsername(): String {
        return id
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return true
    }
}