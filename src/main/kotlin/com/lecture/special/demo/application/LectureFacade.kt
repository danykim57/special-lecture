package com.lecture.special.demo.application

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
        if (lecture.registeredNumber > lecture.maxRegisteredNumber) {
            log.warn("특강 신청 실패: lecture id: {}", lecture.id)
            return false
        }
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