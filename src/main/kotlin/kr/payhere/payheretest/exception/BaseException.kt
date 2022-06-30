package kr.payhere.payheretest.exception

open class BaseException(
    open var errorCode: ErrorCode
): RuntimeException() {
}