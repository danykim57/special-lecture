package com.lecture.special.demo.domain

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@DisplayName("Lecture 유효성 테스트")
class LectureTest {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @DisplayName("등록된 수강자 수가 30이하면 유효성 검사에 성공한다.")
    @Test
    fun `Lecture Construction should be succeeded when registeredNumber is 30`() {
        val lecture = Lecture("name", "lecturer", LocalDateTime.now(), 30)
        val violations = validator.validate(lecture)

        assertEquals(0, violations.size)
    }

    @DisplayName("등록된 수강자 수가 30이 넘으면 유효성 검사에 실패한다.")
    @Test
    fun `Lecture Construction should be failed when registeredNumber is 31`() {
        val lecture = Lecture("name", "lecturer", LocalDateTime.now(), 31)
        val violations = validator.validate(lecture)

        assertEquals(1, violations.size)
        assertEquals("수강자 수가 30 이하여야 합니다.", violations.first().message)
    }
}