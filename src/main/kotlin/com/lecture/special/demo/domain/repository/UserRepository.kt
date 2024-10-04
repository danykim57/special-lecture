package com.lecture.special.demo.domain.repository

import com.lecture.special.demo.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun save(user: User): User
}