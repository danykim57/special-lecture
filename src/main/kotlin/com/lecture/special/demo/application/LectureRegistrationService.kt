package com.lecture.special.demo.application

import com.lecture.special.demo.domain.Lecture
import com.lecture.special.demo.domain.LectureRegistration
import com.lecture.special.demo.domain.RegisterId
import com.lecture.special.demo.domain.User
import com.lecture.special.demo.domain.repository.LectureRegistrationRepository
import org.springframework.stereotype.Service

@Service
class LectureRegistrationService(
    val repository: LectureRegistrationRepository
) {
    fun save(user: User, lecture: Lecture): LectureRegistration {
        val registerId = RegisterId(user.id, lecture.id)
        return repository.save(LectureRegistration(registerId))
    }

    fun get(userId: Long): List<LectureRegistration> {
        return repository.findLectureRegistrationsByRegisterIdUserId(userId)
    }
}