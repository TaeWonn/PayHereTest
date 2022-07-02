package kr.payhere.payheretest.domain.account_book.model.error_code

import kr.payhere.payheretest.exception.ErrorCode

enum class AccountBookErrorCode(override val message: String) : ErrorCode {
    TodayAlreadySave("오늘 이미 등록된 가계부가 있습니다."),
    HistoryNotFound("가계부 내역을 찾을 수 없습니다."),
    ReadOnlyWriter("내가 작성한 가계부만 접근 가능합니다."),
    ModifyOnlyWriter("내가 작성한 가계부 내역만 변경 가능합니다."),
    ModifyCanSameDateOnly("같은 날짜에 내역들만 한번에 수정 가능합니다."),
}