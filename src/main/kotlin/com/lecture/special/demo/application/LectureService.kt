package com.lecture.special.demo.application

import com.lecture.special.demo.domain.Lecture
import com.lecture.special.demo.domain.LectureRegistration
import com.lecture.special.demo.domain.repository.LectureRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LectureService(val lectureRepository: LectureRepository) {

    fun get(id: Long): Lecture {
        return lectureRepository.findById(id).get()
    }

    fun get(date: LocalDateTime): List<Lecture> {
        return lectureRepository.findLectureByDateEquals(date).filter {it -> it.registeredNumber < 30}
    }

    fun get(lectureRegistrations: List<LectureRegistration>): List<Lecture> {
        val ids = lectureRegistrations.map{ it -> it.registerId.lectureId}
        return lectureRepository.findAllByIdIn(ids)
    }

    fun save(lecture: Lecture): Lecture {
        val currentLecture = get(lecture.id)
        if (currentLecture.registeredNumber > lecture.registeredNumber) {
            throw IllegalArgumentException("이미 도중에 업데이트가 되어버린 강의 입니다.")
        }
        lecture.registeredNumber.inc()
        return lectureRepository.save(lecture)
    }
}