package kr.payhere.payheretest.domain.user.respository

import kr.payhere.payheretest.domain.user.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun findByMailAuthenticationFalseAndCreatedAtBefore(datetime: LocalDateTime): List<User>

}