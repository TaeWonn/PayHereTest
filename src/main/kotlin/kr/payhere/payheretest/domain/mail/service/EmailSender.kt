package kr.payhere.payheretest.domain.mail.service

interface EmailSender {
    fun sendEmail(to: String, subject: String, body: String)
}