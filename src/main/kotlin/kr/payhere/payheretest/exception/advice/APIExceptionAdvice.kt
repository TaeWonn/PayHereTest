package kr.payhere.payheretest.exception.advice

import com.fasterxml.jackson.core.JsonProcessingException
import kr.payhere.payheretest.exception.BaseException
import mu.KotlinLogging
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class APIExceptionAdvice {
    private val log = KotlinLogging.logger {  }

    @ExceptionHandler(BaseException::class)
    fun baseExceptionHandler(ex: BaseException): ResponseEntity<ErrorResponse> {
        val annotation: ResponseStatus = AnnotationUtils.findAnnotation(
            ex::class.java,
            ResponseStatus::class.java
        )!!

        val status: HttpStatus = annotation.value
        val message: String = ex.errorCode.message

        return generateResponse(status, message)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun validationException(ex: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.BAD_REQUEST
        val message = ex.constraintViolations
            .map { exception: ConstraintViolation<*> -> exception.message }
            .joinToString("\n")
        return generateResponse(status, message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun notValidException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.BAD_REQUEST
        val message = ex.bindingResult
            .fieldErrors
            .map { obj: FieldError -> obj.defaultMessage }
            .joinToString("\n")
        return generateResponse(status, message)
    }

    @ExceptionHandler(Exception::class)
    fun exception(ex: Exception): ResponseEntity<ErrorResponse> {
        log.error(ex.message, ex)
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val message = "알 수 없는 오류가 발생했습니다."
        return generateResponse(status, message)
    }

    private fun generateResponse(status: HttpStatus, message: String): ResponseEntity<ErrorResponse> {
        return try {
            val errorResponse = ErrorResponse(message)
            ResponseEntity<ErrorResponse>(errorResponse, status)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }
}
