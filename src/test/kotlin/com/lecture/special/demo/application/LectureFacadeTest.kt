package com.lecture.special.demo.application

import com.lecture.special.demo.domain.Lecture
import com.lecture.special.demo.domain.User
import com.lecture.special.demo.domain.repository.LectureRepository
import com.lecture.special.demo.domain.repository.UserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest
class LectureFacadeTest {

    private val log = LoggerFactory.getLogger(LectureFacadeTest::class.java)

    @Autowired
    private lateinit var lectureFacade: LectureFacade

    @Autowired
    private lateinit var lectureRepository: LectureRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        for (i in 1..40) {
            userRepository.save(User())
        }
    }

    @Test
    fun `동시에 40명이 신청하면 30명만 성공하는 케이스`() {
        //given: 특강 생성
        val lecture = Lecture("name", "lecturer", LocalDateTime.now())
        val savedLecture = lectureRepository.save(lecture)

        //when: 40명 신청
        val executorService = Executors.newFixedThreadPool(40)
        val successResults = mutableListOf<Boolean>()

        for (i in 1..40) {
            var userId = i.toLong()
            executorService.submit {
                val success = lectureFacade.register(userId, savedLecture.id)
                synchronized(successResults) {
                    successResults.add(success)
                }
            }
        }

        executorService.shutdown()
        executorService.awaitTermination(10, TimeUnit.SECONDS)

        //Then: 30명만 성공
        val successCount = successResults.size
        val failedCount = 40 - successCount

        assertEquals(30, successCount)
        assertEquals(10, failedCount)
    }
}