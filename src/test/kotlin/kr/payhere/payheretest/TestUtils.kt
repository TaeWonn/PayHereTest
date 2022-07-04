package kr.payhere.payheretest

import kr.payhere.payheretest.exception.BaseException
import kr.payhere.payheretest.exception.ErrorCode
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.InstanceOfAssertFactories
import org.assertj.core.api.ObjectAssert

fun AbstractThrowableAssert<*, *>.assertionsErrorCode(): ObjectAssert<ErrorCode?> {
    return this.asInstanceOf(InstanceOfAssertFactories.type(BaseException::class.java))
        .extracting(BaseException::errorCode) as ObjectAssert<ErrorCode?>
}

infix fun Any?.isEqual(other: Any?) {
    assertThat(this).isEqualTo(other)
}
