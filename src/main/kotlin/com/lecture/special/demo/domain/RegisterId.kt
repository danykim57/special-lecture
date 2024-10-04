package com.lecture.special.demo.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.io.Serializable

@Embeddable
@Table(
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id", "lecture_id"])
    ]
)
data class RegisterId(
    @Column(name = "user_id")
    var userId: Long = 0,

    @Column(name = "lecture_id")
    var lectureId: Long = 0
) : Serializable
