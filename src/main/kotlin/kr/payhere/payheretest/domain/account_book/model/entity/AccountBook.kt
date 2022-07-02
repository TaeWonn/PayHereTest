package kr.payhere.payheretest.domain.account_book.model.entity

import kr.payhere.payheretest.config.entity.BaseEntity
import kr.payhere.payheretest.domain.account_book.model.error_code.AccountBookErrorCode
import kr.payhere.payheretest.exception.ForbiddenException
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Entity

@Entity
class AccountBook (
    var payDate: LocalDate = LocalDate.now(),
    var memo: String?,
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,
    var userId: Long,
): BaseEntity() {

    fun updateMemo(memo: String?, userId: Long) {
        if (this.userId != userId) {
            throw ForbiddenException(AccountBookErrorCode.ModifyOnlyWriter)
        }
        this.memo = memo
    }
}