package kr.payhere.payheretest.domain.user.respository

import kr.payhere.payheretest.domain.user.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}