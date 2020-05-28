package com.basicit.html2pdf.common

import com.basicit.GraphqlApplication
import org.slf4j.LoggerFactory
import org.springframework.boot.system.ApplicationHome
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Paths

object DirectoryHelper {
    private val _logger = LoggerFactory.getLogger(DirectoryHelper::class.java)
    val rootDirectory: String = ""

    fun joinFromRoot(path: String): String {
        val out = "$rootDirectory/$path"
        _logger.info("Joining path and returning {}", out)
        return out
    }

    fun createIfNotExists(directory: String): Boolean {
        val dir = File(joinFromRoot(directory))
        if (!dir.exists()) {
            _logger.info("Creating directory {}", dir.toString())
            dir.mkdirs()
        }
        return true
    }

    @Throws(IOException::class)
    fun createFromString(container: String, path: String, input: String?): String {
        val fileWriter = FileWriter(joinFromRoot(container) + "/" + path)
        fileWriter.write(input)
        fileWriter.flush()
        fileWriter.close()
        return joinFromRoot(container) + "/" + path
    }

    fun clean(path: String?) {
        val file = File(path)
        file.delete()
    }

    fun toUrl(parent: String, file: String, settings: Settings?): String {
        return "$parent/$file"
    }

    val unixFile: String
        get() = "file:///"

    fun getLocalUrl(path: String): String {
        return "$unixFile/$rootDirectory/$path"
    }
}
