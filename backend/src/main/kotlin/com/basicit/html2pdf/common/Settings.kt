package com.basicit.html2pdf.common

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("html2pdf")
class Settings {
    /**
     * @return the _authorizationToken
     */
    /**
     * @param _authorizationToken the _authorizationToken to set
     */
    var authorizationToken: String? = null
    /**
     * @return the _repository
     */
    /**
     * @param _repository the _repository to set
     */
    var repository: String? = null
    /**
     * @return the _chromePath
     */
    /**
     * @param _chromePath the _chromePath to set
     */
    var chromePath: String? = null
    /**
     * @return the _serverHostName
     */
    /**
     * @param _serverHostName the _serverHostName to set
     */
    var serverHostName: String? = null

    @Value("\${server.port}")
    var port = 0

}