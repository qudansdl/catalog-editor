package com.basicit.config

import com.basicit.html2pdf.common.DirectoryHelper
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebApplicationConfig: WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val rootPath = DirectoryHelper.rootDirectory
        val strAbsolutePath = "file://$rootPath/webapp/documents/"
        registry.addResourceHandler("/documents/**").addResourceLocations(strAbsolutePath)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**");
    }


    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/404").setViewName("forward:/index")
    }

    @Bean
    fun containerCustomizer(): WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>? {
        return WebServerFactoryCustomizer { container: ConfigurableServletWebServerFactory ->
            container.addErrorPages(
                    ErrorPage(
                            HttpStatus.NOT_FOUND,
                            "/404"
                    )
            )
        }
    }



}
