package br.com.gdarlan.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

//@Configuration
class WebServerConfiguration {

    @Value("\${cors.originPatterns:default}")
    private val corsOriginPatterns: String = ""

    fun addCorsConfig():WebMvcConfigurer{
        return object : WebMvcConfigurer{

        }
    }
}