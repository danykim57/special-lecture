package com.lecture.special.demo.interfaces

import com.lecture.special.demo.application.LectureFacade
import com.lecture.special.demo.application.LectureService
import com.lecture.special.demo.domain.Lecture
import com.lecture.special.demo.domain.dto.request.RegistrationRequest
import com.lecture.special.demo.domain.dto.response.RegistrationResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController(value = "/lecture")
class LectureController(
    val lectureFacade: LectureFacade,
    val lectureService: LectureService
) {

    //특강 신청 API
    @PostMapping("/apply")
    fun apply(@Valid @RequestBody request:RegistrationRequest): RegistrationResponse {
        val bool = lectureFacade.register(request.userId, request.lectureId)
        return RegistrationResponse(bool.toString())
    }

    //특강 선택 API
    @PostMapping
    fun getAvailableLectures(@RequestBody date : LocalDateTime): List<Lecture> {
        return lectureService.get(date)
    }

    //특강 신청 완료 목록 조회 API
    @GetMapping("{id}")
    fun getLectures(@PathVariable id: Long): List<Lecture> {
        return lectureFacade.getAvailable(id)
    }

}