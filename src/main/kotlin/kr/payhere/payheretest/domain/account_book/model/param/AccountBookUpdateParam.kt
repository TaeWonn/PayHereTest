package kr.payhere.payheretest.domain.account_book.model.param

import java.math.BigDecimal
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

class AccountBookUpdateParam (
    @field:Size(max = 300, message = "메모는 최대 300자 까지 입력 가능합니다.")
    val memo: String?,
    @field:Valid
    @field:NotEmpty(message = "변경할 내역을 선택해주세요.")
    val histories: List<AccountBookHistoryUpdateParam>
)

class AccountBookHistoryUpdateParam (
    @field:Positive(message = "변경할 내역을 선택해주세요.")
    val historyId: Long,
    @field:DecimalMin("0.0", message = "현금 사용 금액을 최소 0원 이상으로 입력해주세요")
    val cash: BigDecimal,
    @field:DecimalMin("0.0", message = "카드 사용 금액을 최소 0원 이상으로 입력해주세요")
    val card: BigDecimal,
)