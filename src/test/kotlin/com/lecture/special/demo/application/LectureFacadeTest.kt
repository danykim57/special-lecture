package com.lecture.special.demo.application

import com.lecture.special.demo.domain.Lecture
import com.lecture.special.demo.domain.repository.LectureRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest
class LectureFacadeTest {

    @Autowired
    private lateinit var lectureFacade: LectureFacade

    @Autowired
    private lateinit var lectureRepository: LectureRepository

    @Test
    fun `동시에 40명이 신청하면 30명만 성공하는 케이스`() {
        //given: 특강 생성
        val lecture = Lecture("name", "lecturer", LocalDateTime.now())
        val savedLecture = lectureRepository.save(lecture)

        //when: 40명 신청
        val executorService = Executors.newFixedThreadPool(40)
        val successResults = mutableListOf<Boolean>()

        for (i in 1..40) {
            var userId = 1L
            executorService.submit {
                val success = lectureFacade.register(userId, savedLecture.id)
                userId++
                synchronized(successResults) {
                    successResults.add(success)
                }
            }
        }

        executorService.shutdown()
        executorService.awaitTermination(10, TimeUnit.SECONDS)

        //Then: 30명만 성공
        val successCount = successResults.count {it}
        val failedCount = successResults.size - successCount

        assertEquals(30, successCount)
        assertEquals(10, failedCount)
    }
}