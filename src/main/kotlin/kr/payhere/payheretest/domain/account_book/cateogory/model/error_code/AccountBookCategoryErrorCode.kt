package kr.payhere.payheretest.domain.account_book.cateogory.model.error_code

import kr.payhere.payheretest.exception.ErrorCode

enum class AccountBookCategoryErrorCode(override val message: String): ErrorCode {
    NameAlreadyUse("이미 사용중인 카테고리 명입니다."),
    NotFound("카테고리를 찾을 수 없습니다."),
    BaseCategoryDelete("기본 카테고리는 삭제 할 수 없습니다."),
    OtherUserCategoryDelete("본인이 등록한 카테고리만 삭제할 수 있습니다."),
    ;
}