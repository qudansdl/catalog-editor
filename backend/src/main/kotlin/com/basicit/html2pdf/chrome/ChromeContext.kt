package com.basicit.html2pdf.chrome

import com.basicit.html2pdf.controllers.PdfController
import io.webfolder.cdp.Launcher
import io.webfolder.cdp.session.Session
import io.webfolder.cdp.session.SessionFactory
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.ServerSocket
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.function.Consumer

class ChromeContext {
    private val _logger = LoggerFactory.getLogger(PdfController::class.java)

    private val HEADLESS_CHROME_ARGUMENTS = Arrays.asList("--headless", "--no-sandbox")
    private val CHROME_STARTUP_TIMEOUT = 0
    private var globalPort = 0

    @Throws(IOException::class)
    fun generatePDF(path: Path?, url: String?, customChromePath: String?) {
        executeInHeadlessChrome(customChromePath, Consumer { session: Session ->
            try {
                session.navigate(url)
                session.waitDocumentReady()
                _logger.info("wait until loaded")
                session.waitUntil({ s -> s.matches("#catalogLoaded") }, 10 * 1000)
                _logger.info("update called loaded")
                Thread.sleep(5000)
                val preRender = session
                        .command
                        .page
                        .printToPDF()
                Files.write(path, preRender)
            } catch (e: Exception) {
                throw RuntimeException("Could not execute pdf export in chrome session", e)
            }
        })
    }

    private fun executeInHeadlessChrome(customChromePath: String?, consumer: Consumer<Session>) {
        val factory = createOrGetSessionFactory(customChromePath)
        try {
            Thread.sleep(CHROME_STARTUP_TIMEOUT.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val context = factory!!.createBrowserContext()
        factory.create(context).use { session -> consumer.accept(session) }
    }

    private fun createOrGetSessionFactory(customChromePath: String?): SessionFactory? {
        synchronized(ChromeContext::class.java) {
            if (globalSessionFactory == null) {
                val launcher = getLauncher(customChromePath)
                globalSessionFactory = launcher.launch(HEADLESS_CHROME_ARGUMENTS)
            }
        }
        return globalSessionFactory
    }

    private fun getLauncher(customChromePath: String?): Launcher {
        globalPort = findAvailablePort()
        val result = CustomLauncher(globalPort)
        if (customChromePath != null) result.setCustomChromePaths(Arrays.asList(customChromePath))
        return result
    }

    private fun findAvailablePort(): Int {
        return try {
            val serverSocket = ServerSocket(0)
            val result = serverSocket.localPort
            serverSocket.close()
            result
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private var globalSessionFactory: SessionFactory? = null
    }
}
