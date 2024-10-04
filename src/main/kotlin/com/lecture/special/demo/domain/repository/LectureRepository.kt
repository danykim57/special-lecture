package com.lecture.special.demo.domain.repository

import com.lecture.special.demo.domain.Lecture
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.time.LocalDateTime
import java.util.*

interface LectureRepository: JpaRepository<Lecture, Long> {

    fun findLectureByDateEquals(date: LocalDateTime): List<Lecture>

    fun findByDate(date : LocalDateTime) : List<Lecture>

    fun findAllByIdIn(ids: Collection<Long>) : List<Lecture>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    override fun findById(id: Long) : Optional<Lecture>

    fun save(lecture: Lecture): Lecture

}