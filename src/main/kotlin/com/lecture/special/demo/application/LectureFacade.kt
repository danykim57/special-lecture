package com.lecture.special.demo.application

import com.lecture.special.demo.domain.Lecture
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class LectureFacade(
    val lectureService: LectureService,
    val userService: UserService,
    val lectureRegistrationService: LectureRegistrationService
) {
    @Transactional
    fun register(userId: Long, lectureId: Long): Boolean {
        val user = userService.get(userId)
        var lecture = lectureService.get(lectureId)
        lecture.incrementRegisteredNumber()
        if (lecture.registeredNumber > lecture.maxRegisteredNumber) return false
        var result = lectureService.save(lecture)
        lectureRegistrationService.save(user, result)
        return true
    }

    fun getAvailable(userId: Long): List<Lecture> {
        val lectureRegistrations = lectureRegistrationService.get(userId);
        val lectures = lectureService.get(lectureRegistrations).filter {it -> it.registeredNumber < 30}
        return lectures
    }
}