package kr.payhere.payheretest.domain.account_book.cateogory.model.param

import kr.payhere.payheretest.domain.account_book.cateogory.model.entity.AccountBookCategory
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

class AccountBookCategoryCreateParam (
    @field:NotBlank(message = "분류명을 입력해주세요.")
    val name: String,
    @field:Positive(message = "상위 분류를 선택해주세요.")
    val parentId: Long,
) {
    fun toEntity(userId: Long): AccountBookCategory {
        return AccountBookCategory(
            name = name,
            userId = userId,
        )
    }
}