package com.moais.assignment.service

import com.moais.assignment.exception.CustomException
import com.moais.assignment.exception.ErrorCode
import com.moais.assignment.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailService(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findById(username).orElseThrow{
            CustomException(ErrorCode.NOT_EXIST_USER)
        }
    }
}