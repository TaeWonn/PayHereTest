package kr.payhere.payheretest.domain.account_book.cateogory.model.dto

import kr.payhere.payheretest.domain.account_book.cateogory.model.entity.AccountBookCategory
import java.io.Serializable
import java.time.LocalDateTime

class AccountBookCategoryDTO (
    val id: Long,
    val name: String,
    val depth: Int,
    val userId: Long?,
    val parentId: Long?,
    val deletable: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?,
    val parent: AccountBookCategoryDTO?,
): Serializable {
    constructor(category: AccountBookCategory): this(
        id = category.id,
        name = category.name,
        depth = category.depth,
        userId = category.userId,
        parentId = category.parentId,
        deletable = category.deletable,
        createdAt = category.createdAt,
        updatedAt = category.updatedAt,
        deletedAt = category.deletedAt,
        parent = if (category.parent != null) AccountBookCategoryDTO(category.parent!!)
                    else null
    )
}