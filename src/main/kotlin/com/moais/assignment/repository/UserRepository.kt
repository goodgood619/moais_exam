package com.moais.assignment.repository

import com.moais.assignment.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, String>{

    fun findByToken(token: String): User?
}