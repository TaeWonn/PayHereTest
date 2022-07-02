package kr.payhere.payheretest.domain.account_book.model.param

import kr.payhere.payheretest.domain.account_book.model.entity.AccountBook
import java.time.LocalDate
import java.time.LocalDateTime

class AccountBookListDTO (
    val id: Long,
    val payDate: LocalDate,
    val memo: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
) {
    constructor(accountBook: AccountBook): this(
        id = accountBook.id,
        payDate = accountBook.payDate,
        memo = accountBook.memo,
        createdAt = accountBook.createdAt,
        updatedAt = accountBook.updatedAt,
    )
}