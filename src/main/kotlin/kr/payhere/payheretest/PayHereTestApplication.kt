package kr.payhere.payheretest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@EnableCaching
@SpringBootApplication
class PayHereTestApplication

fun main(args: Array<String>) {
    runApplication<PayHereTestApplication>(*args)
}
