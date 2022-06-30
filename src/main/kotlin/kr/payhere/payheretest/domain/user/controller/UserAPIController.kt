package kr.payhere.payheretest.domain.user.controller

import kr.payhere.payheretest.config.auth.Restricted
import kr.payhere.payheretest.config.lock.UserLevelLock
import kr.payhere.payheretest.config.toekn.AuthToken
import kr.payhere.payheretest.domain.mail.service.EmailSender
import kr.payhere.payheretest.domain.user.model.dto.SessionContext
import kr.payhere.payheretest.domain.user.model.dto.UserSessionForSingUp
import kr.payhere.payheretest.domain.user.model.error_code.UserErrorCode
import kr.payhere.payheretest.domain.user.model.error_code.UserMailCertificationParam
import kr.payhere.payheretest.domain.user.model.param.UserLoginParam
import kr.payhere.payheretest.domain.user.model.param.UserSignUpParam
import kr.payhere.payheretest.domain.user.model.param.UserSignUpParam.Companion.SingUpEmailSubject
import kr.payhere.payheretest.domain.user.service.UserService
import kr.payhere.payheretest.exception.BadRequestException
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserAPIController (
    private val userService: UserService,
    private val userLevelLock: UserLevelLock,
    private val emailSender: EmailSender,
) {

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody @Valid param: UserSignUpParam, req: HttpServletRequest) {
        val lockName = "${SignUpLockName}${param.email}"
        val signUp = {
            userService.signUp(param.email, param.password)
        }
        userLevelLock.executeWithLock(lockName, signUp)

        val randomKey = RandomStringUtils.random(5, true, true)
        val body = param.generateEmailBody(randomKey)
        emailSender.sendEmail(param.email, SingUpEmailSubject, body)

        val session = req.getSession(true)
        val signUpContext = UserSessionForSingUp(param.email, randomKey)
        session.setAttribute(emailSessionKey, signUpContext)
    }

    @PutMapping("/email-certification")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun emailCertification(@RequestBody param: UserMailCertificationParam, req: HttpServletRequest) {
        val session = req.getSession(false)
            ?: throw BadRequestException(UserErrorCode.EmailSessionNull)

        val signUpContext = session.getAttribute(emailSessionKey) as? UserSessionForSingUp
            ?: throw BadRequestException(UserErrorCode.EmailSessionNull)

        if (signUpContext.key != param.key) {
            throw BadRequestException(UserErrorCode.EmailCertificationKeyNotEqual)
        }

        userService.emailCertification(signUpContext.email, signUpContext.key)
    }

    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun login(
        @RequestBody @Valid param: UserLoginParam,
        req: HttpServletRequest,
        res: HttpServletResponse,
    ) {
        val beforeSession = req.getSession(false)
        beforeSession?.invalidate()

        val user = userService.login(param.email, param.password)

        val session = req.getSession(true)
        val sessionContext = SessionContext(user)
        session.setAttribute(UserSessionContextKey, sessionContext)

        res.setHeader(AuthToken, session.id)
    }

    @Restricted
    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(req: HttpServletRequest) {
        val session = req.getSession(false)
        session?.invalidate()
    }




    companion object {
        private const val SignUpLockName: String = "user:sign-up:"
        private const val emailSessionKey = "SessionForSignUp"
        const val UserSessionContextKey = "USER_SESSION_CONTEXT_ATTR"
    }
}