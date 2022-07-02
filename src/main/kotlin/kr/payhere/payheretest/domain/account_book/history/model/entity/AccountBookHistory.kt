package kr.payhere.payheretest.domain.account_book.history.model.entity

import kr.payhere.payheretest.config.entity.BaseEntity
import kr.payhere.payheretest.domain.account_book.cateogory.model.entity.AccountBookCategory
import kr.payhere.payheretest.domain.account_book.model.entity.AccountBook
import kr.payhere.payheretest.domain.account_book.model.error_code.AccountBookErrorCode
import kr.payhere.payheretest.exception.ForbiddenException
import org.springframework.data.annotation.LastModifiedDate
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class AccountBookHistory(
    var cash: BigDecimal,
    var card: BigDecimal,
    var accountBookId: Long,
    var accountBookCategoryId: Long,
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
): BaseEntity() {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountBookId", insertable = false, updatable = false)
    lateinit var accountBook: AccountBook

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountBookCategoryId", insertable = false, updatable = false)
    lateinit var accountBookCategory: AccountBookCategory

    fun updateCashAndCard(cash: BigDecimal, card: BigDecimal) {
        this.cash = cash
        this.card = card
    }

    fun delete(userId: Long) {
        if (accountBook.userId != userId) {
            throw ForbiddenException(AccountBookErrorCode.ModifyOnlyWriter)
        }

        deletedAt = LocalDateTime.now()
    }

    fun restore(userId: Long) {
        if (accountBook.userId != userId) {
            throw ForbiddenException(AccountBookErrorCode.ModifyOnlyWriter)
        }

        deletedAt = null
    }
}