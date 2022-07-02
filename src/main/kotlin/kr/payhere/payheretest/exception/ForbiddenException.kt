package kr.payhere.payheretest.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class ForbiddenException(
    override var errorCode: ErrorCode,
): BaseException(errorCode)