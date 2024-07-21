package wf.ada.routes.api

import io.ktor.server.routing.Route
import io.ktor.server.routing.route

fun Route.api() {
    route("/api") {
        v1()
    }
}
