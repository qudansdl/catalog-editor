package com.basicit.html2pdf.controllers

import com.basicit.html2pdf.chrome.ChromeContext
import com.basicit.html2pdf.common.DirectoryHelper
import com.basicit.html2pdf.common.Settings
import com.basicit.html2pdf.json.JSONObject
import com.basicit.html2pdf.models.Response
import com.mashape.unirest.http.Unirest
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.stringtemplate.v4.ST
import java.io.FileNotFoundException
import java.io.UnsupportedEncodingException
import java.nio.file.Paths
import java.util.*
import java.util.function.Consumer

@RestController
class PdfController(val _settings: Settings) {
    private val _logger = LoggerFactory.getLogger(PdfController::class.java)

    @RequestMapping("/ping")
    fun ping(): Response {
        return Response(true, String.format("%s", Calendar.getInstance().time))
    }

    @RequestMapping("/pdf/create")
    fun generate(@RequestParam params: Map<String, Any>): Response {
        var response: String? = ""
        var template = ""
        if (!params.containsKey("template")) {
            return Response(false, "required [template] parameter is missing")
        }
        try {
            val requestUrl = _settings.repository + "/" + params["template"] + ".html"
            var getRequest = Unirest.get(requestUrl)

            //check if github requires authorization
            if (!StringUtils.isEmpty(_settings.authorizationToken)) {
                getRequest = getRequest.header("Authorization", "bearer " + _settings.authorizationToken)
            }
            val html = getRequest.asString()
            template = html.body
            val stringTemplate = ST(template)
            params.keys.forEach(Consumer { param: String ->
                if (param != "template") {
                    val value = params[param]
                    if (param == "data") {
                        val data = JSONObject(value.toString())
                        stringTemplate.add(param, data)
                    } else stringTemplate.add(param, value)
                }
            })
            val guid = UUID.randomUUID().toString()
            val templatesDirectory = "html-templates"
            val documentsDirectory = "webapp/documents"
            DirectoryHelper.createIfNotExists(templatesDirectory)
            DirectoryHelper.createIfNotExists(documentsDirectory)
            _logger.info("Creating files with name : {}", guid)

            //prepare the html file
            val createdAt = DirectoryHelper.createFromString(templatesDirectory, String.format("%s.html", guid), stringTemplate.toString())
            _logger.info("Created the html file at {}", createdAt)
            val context = ChromeContext()
            val pdf = DirectoryHelper.joinFromRoot(documentsDirectory) + String.format("/%s.pdf", guid)
            context.generatePDF(Paths.get(pdf), DirectoryHelper.getLocalUrl(templatesDirectory + String.format("/%s.html", guid)), _settings.chromePath)
            response = DirectoryHelper.toUrl("documents", String.format("%s.pdf", guid), _settings)
        } catch (e: FileNotFoundException) {
            _logger.error("FileNotFoundException: Failed to generate the pdf document", e)
            return Response(e)
        } catch (e: UnsupportedEncodingException) {
            _logger.error("UnsupportedEncodingException: Failed to generate the pdf document", e)
            return Response(e)
        } catch (e: Exception) {
            _logger.error("Exception: Failed to generate the pdf document", e)
            return Response(e)
        }
        return Response(true, response)
    }
}