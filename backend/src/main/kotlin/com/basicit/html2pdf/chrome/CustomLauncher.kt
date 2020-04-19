package com.basicit.html2pdf.chrome

import io.webfolder.cdp.Launcher
import io.webfolder.cdp.session.SessionFactory
import java.util.*

/**
 * Created by Pavel_Dzunovich on 8/2/2017.
 */
class CustomLauncher(port: Int) : Launcher(SessionFactory(port)) {
    private var customChromePaths = emptyList<String>()
    override fun getChromeWinPaths(): List<String> {
        val result: MutableList<String> = LinkedList(super.getChromeWinPaths())
        result.addAll(0, customChromePaths)
        return result
    }

    fun setCustomChromePaths(customChromePaths: List<String>): CustomLauncher {
        this.customChromePaths = customChromePaths
        return this
    }
}