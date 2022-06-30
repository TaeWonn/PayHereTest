package kr.payhere.payheretest.config

import kr.payhere.payheretest.config.interceptor.UserPermissionInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(UserPermissionInterceptor())
            .addPathPatterns("/api/**")
    }
}