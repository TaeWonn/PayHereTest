package kr.payhere.payheretest.domain.account_book.cateogory.model.dto

import com.fasterxml.jackson.annotation.JsonIgnore

class AccountBookCategoryListDTO (
    val id: Long,
    val name: String,
    val depth: Int,
    val deletable: Boolean,
    @JsonIgnore
    val parentId: Long,
) {
    constructor(category: AccountBookCategoryDTO): this(
        id = category.id,
        name = category.name,
        depth = category.depth,
        deletable = category.deletable,
        parentId = category.parentId ?: 0,
    )
}