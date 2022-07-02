package kr.payhere.payheretest.domain.account_book.model.param

import kr.payhere.payheretest.domain.account_book.history.model.entity.AccountBookHistory
import kr.payhere.payheretest.domain.account_book.model.entity.AccountBook
import java.math.BigDecimal
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

class AccountBookCreateParam (
    @field:Size(max = 300, message = "메모는 최대 300자 까지 입력 가능합니다.")
    val memo: String?,
    @field:Valid
    @field:NotEmpty(message = "사용 내역을 추가 해주세요.")
    val histories: List<AccountBookHistoryCreateParam>,
) {
    fun toEntity(userId: Long): AccountBook {
        return AccountBook(
            memo = memo,
            userId = userId,
        )
    }

    fun toHistoryEntity(accountBookId: Long,): List<AccountBookHistory> {
        return histories.map {
            AccountBookHistory(
                cash = it.cash,
                card = it.card,
                accountBookId = accountBookId,
                accountBookCategoryId = it.categoryId,
            )
        }
    }
}

class AccountBookHistoryCreateParam(
    @field:DecimalMin("0.0", message = "현금 사용 금액을 최소 0원 이상으로 입력해주세요")
    val cash: BigDecimal,
    @field:DecimalMin("0.0", message = "카드 사용 금액을 최소 0원 이상으로 입력해주세요")
    val card: BigDecimal,
    @field:Positive(message = "분류를 선택해주세요.")
    val categoryId: Long,
)