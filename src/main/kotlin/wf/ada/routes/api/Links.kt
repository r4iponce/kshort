package wf.ada.routes.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import wf.ada.dto.ApiResponse
import wf.ada.entities.ExposedLink
import wf.ada.services.LinkService
import wf.ada.services.UserService

fun Route.links() {
    post("/links/") {
        val linkService = LinkService()
        val link: ExposedLink = call.receive<ExposedLink>()
        link.ownerId = null

        linkService.create(link)
        call.respond(
            HttpStatusCode.Created,
            ApiResponse(201, "Shortened link ${link.short} now redirect to ${link.url}"),
        )
    }

    authenticate("auth-jwt") {
        // FIXME make optional=true working
        post("/links/authenticated") {
            val token = call.principal<JWTPrincipal>()
            val linkService = LinkService()
            val userService = UserService()
            val link: ExposedLink = call.receive<ExposedLink>()
            if (token != null) {
                val name = token.payload.getClaim("name").asString()
                link.ownerId = userService.read(name)?.id
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }

            linkService.create(link)
            call.respond(
                HttpStatusCode.Created,
                ApiResponse(201, "Shortened link ${link.short} now redirect to ${link.url}"),
            )
        }
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
