package kr.payhere.payheretest.config.secudler

import kr.payhere.payheretest.domain.user.service.UserService
import org.springframework.boot.task.TaskSchedulerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import java.time.LocalDateTime

@Configuration
class CronScheduler (
    private val userService: UserService,
) {

    @Bean
    fun scheduler(): ThreadPoolTaskScheduler {
        val taskSchedulerBuilder = TaskSchedulerBuilder()
        return taskSchedulerBuilder.build()
    }

    @Scheduled(cron = "* */30 * * * *")
    fun deleteNotAuthorizedUser() {
        val datetime = LocalDateTime.now().minusMinutes(30)
        userService.deleteNotMailAuthentication(datetime)
    }
}
