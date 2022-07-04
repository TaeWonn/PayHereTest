package kr.payhere.payheretest.config.interceptor

import kr.payhere.payheretest.config.auth.Restricted
import kr.payhere.payheretest.config.toekn.AuthToken
import kr.payhere.payheretest.domain.user.controller.UserAPIController.Companion.UserSessionContextKey
import kr.payhere.payheretest.domain.user.model.dto.SessionContext
import org.springframework.core.BridgeMethodResolver
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.reflect.AnnotatedElement
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UserPermissionInterceptor: HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod)
            return true

        val handlerMethod: HandlerMethod = handler
        val bridgeMethod = BridgeMethodResolver.findBridgedMethod(handlerMethod.method) as AnnotatedElement

        var restricted: Restricted? = AnnotationUtils.findAnnotation(bridgeMethod, Restricted::class.java)

        if (restricted == null) {
            restricted = AnnotationUtils.findAnnotation(handlerMethod.method.declaringClass, Restricted::class.java)
        }

        if (restricted == null) return true

        val token = request.getHeader(AuthToken)
        val session = request.getSession(false)
        val sessionContext = session?.getAttribute(UserSessionContextKey) as? SessionContext

        // 세션이 없다면 401
        if (token == null || session == null || sessionContext == null) {
            reject(response)
            return false
        }

        // Token 과 session 이 다르다면 401
        if (session.id != token) {
            reject(response)
            return false
        }

        return true
    }

    private fun reject(response: HttpServletResponse) {
        val status = 401
        val message = "로그인 후 이용 가능합니다,"
        response.sendError(status, message)
    }
}