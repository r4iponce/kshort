package wf.ada.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import wf.ada.services.LinkService

fun Route.redirect() {
    get("/{short}") {
        val linkService = LinkService()
        val link = linkService.read(call.parameters["short"]!!)
        if (link != null) {
            call.respondRedirect(link.url)
            return@get
        }

        call.respond(HttpStatusCode.NotFound)
    }
}
