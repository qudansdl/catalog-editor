package com.basicit.service

import com.basicit.html2pdf.chrome.ChromeContext
import com.basicit.html2pdf.common.DirectoryHelper
import com.basicit.html2pdf.common.Settings
import com.basicit.html2pdf.controllers.PdfController
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.FileNotFoundException
import java.io.UnsupportedEncodingException
import java.nio.file.Paths
import java.util.*

@Service
class PDFService(val _settings: Settings) {
    private val _logger = LoggerFactory.getLogger(PdfController::class.java)

    init{
        DirectoryHelper.rootDirectory = _settings.rootPath!!
    }
    fun generate(id: UUID, type: String)  {
        try {
            val templatesDirectory = "html-templates"
            val documentsDirectory = "webapp/documents"
            DirectoryHelper.createIfNotExists(templatesDirectory)
            DirectoryHelper.createIfNotExists(documentsDirectory)
            _logger.info("Creating files with name : {}", id)

            val context = ChromeContext()
            val pdf = DirectoryHelper.joinFromRoot(documentsDirectory) + String.format("/%s.pdf", id)
            val url = _settings.editorPath + "#/?id=" + id + "&type=" + type
            _logger.info("editor url : " + url)
            context.generatePDF(Paths.get(pdf), url, _settings.chromePath)
        } catch (e: FileNotFoundException) {
            _logger.error("FileNotFoundException: Failed to generate the pdf document", e)
        } catch (e: UnsupportedEncodingException) {
            _logger.error("UnsupportedEncodingException: Failed to generate the pdf document", e)
        } catch (e: Exception) {
            _logger.error("Exception: Failed to generate the pdf document", e)
        }

    }
}
