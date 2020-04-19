package com.basicit.html2pdf.chrome

import io.webfolder.cdp.event.Events
import io.webfolder.cdp.event.network.RequestWillBeSent
import io.webfolder.cdp.event.network.ResponseReceived
import io.webfolder.cdp.session.Session
import io.webfolder.cdp.type.network.Request
import java.util.*
import java.util.stream.Collectors

/**
 * Created by Pavel_Dzunovich on 7/17/2017.
 */
class ChromeRequestPool(session: Session) {
    private val requests: MutableMap<String, Request> = HashMap()
    val requestEndpoints: List<String>
        get() = requests.values.stream()
                .map { obj: Request -> obj.url }
                .collect(Collectors.toList())

    val isEmpty: Boolean
        get() = requests.isEmpty()

    init {
        session.command.network.enable()
        session.addEventListener { e: Events, d: Any ->
            if (Events.NetworkRequestWillBeSent == e) {
                val rr = d as RequestWillBeSent
                requests[rr.requestId] = rr.request
            } else if (Events.NetworkResponseReceived == e) {
                val rr = d as ResponseReceived
                requests.remove(rr.requestId)
            }
        }
    }
}