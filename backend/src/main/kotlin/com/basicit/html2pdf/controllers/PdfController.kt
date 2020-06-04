package com.basicit.html2pdf.controllers

import com.basicit.html2pdf.chrome.ChromeContext
import com.basicit.html2pdf.common.DirectoryHelper
import com.basicit.html2pdf.common.Settings
import com.basicit.html2pdf.models.Response
import com.basicit.model.Catalog
import com.basicit.service.CatalogService
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.stringtemplate.v4.ST
import java.io.File
import java.io.FileNotFoundException
import java.io.UnsupportedEncodingException
import java.nio.file.Paths
import java.util.*


@RestController
class PdfController(
        val _settings: Settings,
        val _catalogService: CatalogService
) {
    private val _logger = LoggerFactory.getLogger(PdfController::class.java)

    init{
        DirectoryHelper.rootDirectory = _settings.rootPath!!
    }
    @RequestMapping("/pdf/create")
    fun generate(@RequestParam("catalogId") catalogId: UUID): Response {
        var response: String? = ""

        try {
            //val catalog = _catalogService.getCatalogById(catalogId).get()

            val catalog = Catalog(name="aaaa", content = "bbbbb")
            val guid = UUID.randomUUID().toString()
            val templatesDirectory = "html-templates"
            val documentsDirectory = "webapp/documents"
            DirectoryHelper.createIfNotExists(templatesDirectory)
            DirectoryHelper.createIfNotExists(documentsDirectory)
            _logger.info("Creating files with name : {}", guid)

            val objectMapper = ObjectMapper()
            val catalogJson = objectMapper.writeValueAsString(catalog)

            val htmlContent = File(_settings.editorPath + "/index.html").readText(Charsets.UTF_8)

            val createdAt = File(DirectoryHelper.createFromString(
                    _settings.editorPath!! + "/" + String.format("%s.html", guid),
                    htmlContent.replace(
                            "false //\"<catalog>\"",
                            "catalog = ${catalogJson}"))
            )
            _logger.info("Created the html file at {}", createdAt)
            val context = ChromeContext()
            val pdf = DirectoryHelper.joinFromRoot(documentsDirectory) + String.format("/%s.pdf", guid)
            context.generatePDF(Paths.get(pdf), createdAt.toURI().toURL().toString(), _settings.chromePath)
            response = DirectoryHelper.toUrl("documents", String.format("%s.pdf", guid), _settings)

            createdAt.delete()
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
