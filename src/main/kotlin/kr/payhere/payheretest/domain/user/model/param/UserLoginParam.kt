package kr.payhere.payheretest.domain.user.model.param

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class UserLoginParam (
    @field:NotBlank(message = "이메일을 입력해주세요.")
    @field:Email(message = "이메일 형식으로 보내주세요.")
    var email: String,
    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    var password: String,
)