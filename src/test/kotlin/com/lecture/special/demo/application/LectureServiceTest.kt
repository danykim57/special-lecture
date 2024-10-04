package com.lecture.special.demo.application

import com.lecture.special.demo.domain.Lecture
import com.lecture.special.demo.domain.repository.LectureRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class LectureServiceTest {

    @Mock
    private lateinit var lectureRepository: LectureRepository

    @InjectMocks
    private lateinit var lectureService: LectureService

    @Test
    @DisplayName("특정 날짜의 가능한 강의 목록 조회")
    fun `get available lectures at the date`() {
        // Given: 특정 날짜와 그 날짜에 대한 강의 목록
        val date = LocalDateTime.now()
        val lectures = listOf(
            Lecture("lecture1", "lecturer",date, registeredNumber = 20),
            Lecture("lecture2", "lecturer",date, registeredNumber = 25),
            Lecture("lecture3", "lecturer",date, registeredNumber = 30)
        )

        `when`(lectureRepository.findLectureByDateEquals(date)).thenReturn(lectures)

        // Then: 서비스에서 호출한 결과가 예상한 결과와 일치하는지 확인
        val result = lectureService.get(date)
        assertEquals(2, result.size)
        assertEquals("lecture1", result[0].name)
        assertEquals("lecture2", result[1].name)

        verify(lectureRepository, times(1)).findLectureByDateEquals(date)
    }

    @Test
    @DisplayName("특정 날짜의 강의 목록 조회 - 강의 없음")
    fun `No lecture at the date`() {
        // Given: 특정 날짜와 빈 강의 목록
        val date = LocalDateTime.now()
        val lectures: List<Lecture> = emptyList()

        // When: lectureRepository의 findByDate 메서드가 호출될 때 빈 리스트를 반환하도록 설정
        `when`(lectureRepository.findLectureByDateEquals(date)).thenReturn(lectures)

        // Then: 서비스에서 호출한 결과가 빈 리스트인지 확인
        val result = lectureService.get(date)
        assertEquals(0, result.size)

        verify(lectureRepository, times(1)).findLectureByDateEquals(date)
    }
}

