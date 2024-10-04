package com.lecture.special.demo.interfaces

import com.fasterxml.jackson.databind.ObjectMapper
import com.lecture.special.demo.application.LectureFacade
import com.lecture.special.demo.application.LectureService
import com.lecture.special.demo.domain.Lecture
import com.lecture.special.demo.domain.dto.request.LectureRequest
import com.lecture.special.demo.domain.repository.LectureRepository
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(controllers = [LectureController::class])
class LectureControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var lectureFacade: LectureFacade

    @MockBean
    private lateinit var lectureService: LectureService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("특강 목록 조회 - 등록 인원이 30 이하인 특강만 조회")
    fun `find all lectures, registeredNumber is below 30`() {
        val date = LocalDateTime.now()

        //given
        val lectures = listOf(
            Lecture("lecture1", "lecturer",date, registeredNumber = 20),
            Lecture("lecture2", "lecturer",date, registeredNumber = 25),
            Lecture("lecture3", "lecturer",date, registeredNumber = 30)
        )

        given(lectureService.get(date)).willReturn(lectures.filter {it.registeredNumber < 30})
        println(objectMapper.writeValueAsString(lectures.filter {it.registeredNumber < 30}))
        mockMvc.perform(post("/lectures/available")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(LectureRequest(date))))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("lecture1"))
            .andExpect(jsonPath("$[1].name").value("lecture2"))
            .andExpect(jsonPath("$[2]").doesNotExist())
    }
}