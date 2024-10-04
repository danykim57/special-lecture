package com.lecture.special.demo.domain

import jakarta.persistence.*

@Entity
@Table(name = "`user`")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
        protected set

}