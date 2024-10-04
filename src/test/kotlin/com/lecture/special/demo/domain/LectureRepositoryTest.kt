package com.lecture.special.demo.domain

import com.lecture.special.demo.domain.repository.LectureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class LectureRepositoryTest {

    @Autowired
    lateinit var repository: LectureRepository


}