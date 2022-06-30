package kr.payhere.payheretest.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class BadRequestException (
    override var errorCode: ErrorCode,
): BaseException(errorCode)