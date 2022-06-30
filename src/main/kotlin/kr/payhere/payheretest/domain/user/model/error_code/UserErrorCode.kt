package kr.payhere.payheretest.domain.user.model.error_code

import kr.payhere.payheretest.exception.ErrorCode

enum class UserErrorCode(override val message: String): ErrorCode {
    UserEmailNotFound("등록된 이메일이 아닙니다."),
    PassWordNotEqual("패스워드가 일치 하지 않습니다."),
    ExitsEmail("이미 사용중인 아이디 입니다."),
    EmailNotAuthentication("이메일 인증 이후 이용 가능합니다."),
    EmailSessionNull("이메일 인증이 30분이 지난 경우 더 이상 인증이 불가능 합니다."),
    EmailCertificationKeyNotEqual("인증 번호가 일치하지 않습니다."),
    ExitsEmailAuthentication("이미 인증이 완료된 계정입니다."),
    ;

}