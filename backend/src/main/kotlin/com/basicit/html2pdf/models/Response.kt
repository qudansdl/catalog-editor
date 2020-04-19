package com.basicit.html2pdf.models

import java.io.PrintWriter
import java.io.StringWriter

class Response {
    var success: Boolean
        protected set
    var message: String?

    constructor(success: Boolean, message: String?) {
        this.message = message
        this.success = success
    }

    constructor() {
        success = true
        message = ""
    }

    constructor(exception: Exception) {
        success = false
        val sw = StringWriter()
        exception.printStackTrace(PrintWriter(sw))
        message = sw.toString()
    }

}