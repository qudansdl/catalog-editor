package com.basicit.html2pdf.common

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("html2pdf")
data class Settings(
    var editorPath: String? = null,
    var chromePath: String? = null,
    var rootPath: String? = null
)
