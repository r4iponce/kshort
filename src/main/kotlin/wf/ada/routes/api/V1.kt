package wf.ada.routes.api

import io.ktor.server.routing.Route
import io.ktor.server.routing.route

fun Route.v1() {
    route("v1") {
        auth()
        users()
        links()
    }
}
