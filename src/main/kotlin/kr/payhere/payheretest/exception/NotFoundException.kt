package kr.payhere.payheretest.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NotFoundException (
    override var errorCode: ErrorCode
): BaseException(errorCode)