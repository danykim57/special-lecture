package com.lecture.special.demo.domain.dto.request

import jakarta.validation.constraints.NotNull

data class RegistrationRequest(

    @field:NotNull(message = "사용자 아이디가 없습니다")
    val userId: Long,

    @field:NotNull(message = "강의 아이디가 없습니다")
    val lectureId: Long,
)
