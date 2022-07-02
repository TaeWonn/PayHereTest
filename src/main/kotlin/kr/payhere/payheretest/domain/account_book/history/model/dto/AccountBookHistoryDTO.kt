package kr.payhere.payheretest.domain.account_book.history.model.dto

import kr.payhere.payheretest.domain.account_book.history.model.entity.AccountBookHistory
import java.math.BigDecimal
import java.time.LocalDateTime

class AccountBookHistoryDTO (
    val id: Long,
    val cash: BigDecimal,
    val card: BigDecimal,
    val categoryName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
) {
    constructor(history: AccountBookHistory): this(
        id = history.id,
        cash = history.cash,
        card = history.card,
        categoryName = history.accountBookCategory.name,
        createdAt = history.createdAt,
        updatedAt = history.updatedAt,
    )
}