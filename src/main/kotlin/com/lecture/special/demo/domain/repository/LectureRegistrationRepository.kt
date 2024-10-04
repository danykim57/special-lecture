package com.lecture.special.demo.domain.repository

import com.lecture.special.demo.domain.LectureRegistration
import com.lecture.special.demo.domain.RegisterId
import org.springframework.data.jpa.repository.JpaRepository

interface LectureRegistrationRepository : JpaRepository<LectureRegistration, RegisterId> {

    fun findLectureRegistrationsByRegisterIdUserId(userId: Long): List<LectureRegistration>
}