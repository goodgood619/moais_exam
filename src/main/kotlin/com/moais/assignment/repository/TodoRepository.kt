package com.moais.assignment.repository

import com.moais.assignment.domain.entity.Todo
import com.moais.assignment.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TodoRepository: JpaRepository<Todo, UUID> {
    fun findByUser(user: User): List<Todo>
    fun findFirstByUserOrderByIdDesc(user: User): Todo?
}