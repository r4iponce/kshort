package wf.ada.routes

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import wf.ada.routes.api.api

fun Application.configureRouting() {
    routing {
        api()
        redirect()
    }
}
