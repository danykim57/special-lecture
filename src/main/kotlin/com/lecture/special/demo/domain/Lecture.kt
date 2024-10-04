package com.lecture.special.demo.domain

import jakarta.persistence.*
import jakarta.validation.constraints.Max
import java.time.LocalDateTime

@Entity
class Lecture(
    name: String,
    lecturer: String,
    registeredDate: LocalDateTime,
    registeredNumber: Int
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
        protected set

    @Column
    var name: String = name
        protected set

    @Column
    var lecturer: String = lecturer
        protected set

    @Column
    @field:Max(30, message = "수강자 수가 30 이하여야 합니다.")
    var registeredNumber: Int = registeredNumber
        protected set

    @Column
    var maxRegisteredNumber: Int = 30
        protected set

    @Column
    var date: LocalDateTime = registeredDate
        protected set

    fun incrementRegisteredNumber() {
        registeredNumber++
    }

}