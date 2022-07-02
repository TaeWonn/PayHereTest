package kr.payhere.payheretest.domain.account_book.repository

import kr.payhere.payheretest.domain.account_book.model.entity.AccountBook
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface AccountBookRepository: JpaRepository<AccountBook, Long> {
    fun findByPayDateAndUserId(payDate: LocalDate, userId: Long): AccountBook?
    fun findByUserId(userId: Long, pageable: Pageable): Page<AccountBook>
}