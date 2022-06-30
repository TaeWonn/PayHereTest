package kr.payhere.payheretest.domain.user.model.param

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class UserSignUpParam (
    @field:NotBlank(message = "이메일을 입력해주세요.")
    @field:Email(message = "이메일 형식으로 보내주세요.")
    @field:Size(max = 50, message = "이메일은 최대 50글자 까지 입니다.")
    var email: String,
    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    @field:Size(min = 5, max = 30, message = "비밀번호는 5~30자 사이로 입력해주세요.")
    var password: String,
) {
    fun generateEmailBody(key: String): String {
        return String.format(SingUpEmailBody, key)
    }

    companion object {
        const val SingUpEmailSubject = "PayHere 과제 - 이메일 인증"
        const val SingUpEmailBody = """
<html>
    <body>
        <p>30분 이내로 번호를 입력해야 인증가능합니다.</p>
        <p>인증 번호: %s</p>
    </body>
</html>
"""
    }
}