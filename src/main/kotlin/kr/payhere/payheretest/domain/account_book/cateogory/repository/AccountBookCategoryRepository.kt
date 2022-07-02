package kr.payhere.payheretest.domain.account_book.cateogory.repository

import kr.payhere.payheretest.domain.account_book.cateogory.model.entity.AccountBookCategory
import org.springframework.data.jpa.repository.JpaRepository

interface AccountBookCategoryRepository: JpaRepository<AccountBookCategory, Long> {
    fun findByDeletableFalse(): List<AccountBookCategory>
    fun findByDeletableFalseAndParentIdIsNull(): List<AccountBookCategory>
    fun findByUserIdAndDeletedAtIsNotNull(userId: Long): List<AccountBookCategory>
    fun findByNameAndUserIdAndDeletedAtIsNull(name: String, userId: Long): List<AccountBookCategory>
}