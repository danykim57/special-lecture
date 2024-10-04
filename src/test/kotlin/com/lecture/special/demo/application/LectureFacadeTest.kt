package com.lecture.special.demo.application

import com.lecture.special.demo.domain.Lecture
import com.lecture.special.demo.domain.LectureRegistration
import com.lecture.special.demo.domain.RegisterId
import com.lecture.special.demo.domain.User
import com.lecture.special.demo.domain.repository.LectureRegistrationRepository
import com.lecture.special.demo.domain.repository.LectureRepository
import com.lecture.special.demo.domain.repository.UserRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest
open class LectureFacadeTest {

    private val log = LoggerFactory.getLogger(LectureFacadeTest::class.java)

    @Autowired
    private lateinit var lectureFacade: LectureFacade

    @Autowired
    private lateinit var lectureRepository: LectureRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var lectureService: LectureService

    @Autowired
    private lateinit var lectureRegistrationRepository: LectureRegistrationRepository

    @Autowired
    private lateinit var lectureRegistrationService: LectureRegistrationService

    @Autowired
    private lateinit var entityManagerFactory: EntityManagerFactory

    private val date = LocalDateTime.now()
    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        for (i in 1..40) {
            userRepository.save(User())
        }
        lectureRepository.deleteAll()
        resetSequences()
        lectureRepository.save(Lecture("lecture1", "lecturer",date, registeredNumber = 0))
        lectureRepository.save(Lecture("lecture2", "lecturer",date, registeredNumber = 25))
        lectureRepository.save(Lecture("lecture3", "lecturer",date, registeredNumber = 30))
        lectureRegistrationRepository.deleteAll()
        for (i in 1L..3L) {
            for (j in 1L..3L)
            lectureRegistrationRepository.save(LectureRegistration(RegisterId(i, j)))
        }
    }

    private fun resetSequences() {
        val entityManager: EntityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()
        try {
            // Use the appropriate SQL for your database
            entityManager.createNativeQuery("ALTER TABLE lecture ALTER COLUMN id RESTART WITH 1").executeUpdate()
            entityManager.transaction.commit()
        } catch (e: Exception) {
            entityManager.transaction.rollback()
            throw e
        } finally {
            entityManager.close()
        }

    }

    @Test
    @DisplayName("동시에 40명이 신청하면 30명만 성공하는 케이스")
    fun `Concurrency test for the 30 lecture registration`() {
        //given: 특강
        val lecture = lectureRepository.findById(1L).orElseThrow()
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

    @Test
    @DisplayName("같은 사용자가 5번 시도하면 1번 성공하는 케이스")
    fun `Redundant requests test, tries 5 and succed once`() {
        val lecture = Lecture("lecture10", "lecturer",date, registeredNumber = 0)
        val savedLecture = lectureRepository.save(lecture)
        val executorService = Executors.newFixedThreadPool(5)
        val successResults = mutableListOf<Boolean>()
        for (i in 1..5) {
            var userId = 10L
            executorService.submit {
                val success = lectureFacade.register(userId, savedLecture.id)
                synchronized(successResults) {
                    if (success) successResults.add(success)
                }
            }
        }

        executorService.shutdown()
        executorService.awaitTermination(10, TimeUnit.SECONDS)
        assertEquals(1, successResults.size)
    }

    @Test
    @DisplayName("userId로 신청 완료 목록 조회 성공 테스트")
    fun `get the list of registration by userId`() {
        //given
        val userId = 1L;
        //when BeforeEach에서 유저당 3개의 Registration 등록
        //then 일치하는지 확인  너무 억지스러운 테스트
        val result = lectureFacade.getRegistered(userId)
        assertEquals(3, result.size)

    }
}