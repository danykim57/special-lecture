package com.lecture.special.demo.application

import com.lecture.special.demo.domain.User
import com.lecture.special.demo.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {

    fun get(id: Long): User {
        return userRepository.findById(id).get()
    }

    fun save(id: Long): User {
        val user = userRepository.findById(id).get()
        if (user != null) throw RuntimeException("User already exists")

        return userRepository.save(user)
    }
}