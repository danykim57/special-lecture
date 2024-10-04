package com.lecture.special.demo.domain

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity

@Entity
class LectureRegistration(
    @EmbeddedId
    var registerId: RegisterId
)