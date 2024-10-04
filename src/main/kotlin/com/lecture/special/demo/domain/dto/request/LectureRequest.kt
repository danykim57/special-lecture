package com.lecture.special.demo.domain.dto.request

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class LectureRequest(
    @field:NotNull(message = "날짜가 없습니다")
    val date: LocalDateTime,
)
