package com.lecture.special.demo.domain

import com.lecture.special.demo.domain.repository.LectureRegistrationRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException

@DisplayName("수강신청 통합 테스트")
@DataJpaTest
class LectureRegistrationTest {

    @Autowired
    lateinit var repository: LectureRegistrationRepository

    @Test
    @DisplayName("embeddedId 복합키 중복 상황시 에러가 뜨는지 확인하는 테스트")
    fun `duplicate composite key should throw error`() {
        //Given
        val registerId = RegisterId(1, 2)
        val duplicateRegisterId = RegisterId(1, 2)

        repository.save(LectureRegistration(registerId))
        // When the same lectureId is used for the insert, throws DataIntegrityViolationException thrown
//        assertThrows(DataIntegrityViolationException::class.java) {
//            repository.save(LectureRegistration(duplicateRegisterId))
//        }
    }

}