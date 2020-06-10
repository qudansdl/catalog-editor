package com.basicit.html2pdf.controllers

import com.basicit.html2pdf.chrome.ChromeContext
import com.basicit.html2pdf.common.DirectoryHelper
import com.basicit.html2pdf.common.Settings
import com.basicit.html2pdf.models.Response
import com.basicit.model.Catalog
import com.basicit.service.CatalogService
import com.basicit.service.PDFService
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
        val pdfService: PDFService
) {
    private val _logger = LoggerFactory.getLogger(PdfController::class.java)


    @RequestMapping("/pdf/create")
    fun generate(@RequestParam("catalogId") catalogId: UUID): Response {
        try {
            pdfService.generate(catalogId, "CATALOG")
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
        return Response(true, "SUCCESS")
    }
}
