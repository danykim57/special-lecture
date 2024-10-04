package com.lecture.special.demo.application

import com.lecture.special.demo.common.exception.ExceedRegistration
import com.lecture.special.demo.domain.Lecture
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LectureFacade(
    val lectureService: LectureService,
    val userService: UserService,
    val lectureRegistrationService: LectureRegistrationService
) {

    private val log = LoggerFactory.getLogger(LectureFacade::class.java)
    @Transactional
    fun register(userId: Long, lectureId: Long): Boolean {
        val user = userService.get(userId)
        var lecture = lectureService.get(lectureId)
        lecture.incrementRegisteredNumber()
        val lectureRegistration = lectureRegistrationService.get(userId)
        if (lectureRegistration != null && lectureRegistration.map {it -> it.registerId.lectureId}.contains(lectureId)) {
            //should throw exception
            throw ExceedRegistration()
        }
        if (lecture.registeredNumber > lecture.maxRegisteredNumber) {
            //should throw exception
            throw ExceedRegistration()
        }
        var result = lectureService.save(lecture)
        lectureRegistrationService.save(user, result)
        return true
    }

    fun getRegistered(userId: Long): List<Lecture> {
        val lectureRegistrations = lectureRegistrationService.get(userId);
        val lectures = lectureService.get(lectureRegistrations)
        return lectures
    }
}