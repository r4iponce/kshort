package wf.ada.routes.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import wf.ada.dto.ApiResponse
import wf.ada.entities.ExposedLink
import wf.ada.services.LinkService

fun Route.links() {
    get("/links") {
        val linkService = LinkService()
        val links: List<ExposedLink> = linkService.readAll()
        call.respond(links)
    }

    post("/links") {
        val linkService = LinkService()
        val link: ExposedLink = call.receive<ExposedLink>()
        linkService.create(link)
        call.respond(HttpStatusCode.Created, ApiResponse(201, "Shortened link ${link.short} now redirect to ${link.url}"))
    }

    get("/links/{short}") {
        val linkService = LinkService()
        val short: String = call.parameters["short"]!!
        val link: ExposedLink? = linkService.read(short)
        if (link != null) {
            call.respond(link)
            return@get
        }

        call.respond(HttpStatusCode.NotFound)
    }

    delete("/links/{short}") {
        val linkService = LinkService()
        val short: String = call.parameters["short"]!!
        linkService.delete(short)
        call.respond(HttpStatusCode.NoContent)
    }
}
