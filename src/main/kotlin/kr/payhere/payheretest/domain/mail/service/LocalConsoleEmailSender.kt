package kr.payhere.payheretest.domain.mail.service

import mu.KotlinLogging
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Primary
@Service
class LocalConsoleEmailSender: EmailSender {
    private val log = KotlinLogging.logger {  }

    override fun sendEmail(to: String, subject: String, body: String) {
        log.info {
            """email - ${this::class.simpleName}::
            |$subject
            |$body
            |""".trimMargin()
        }
    }
}