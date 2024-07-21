package wf.ada.routes.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import wf.ada.entities.ExposedUser
import wf.ada.entities.Roles
import wf.ada.services.UserService

fun Route.users() {
    get("/users") {
        val userService = UserService()
        val users: List<ExposedUser> = userService.readAll()
        call.respond(users)
    }

    post("/users") {
        val userService = UserService()
        val user: ExposedUser = call.receive<ExposedUser>()
        if (user.role == Roles.ADMIN) {
            call.respond(HttpStatusCode.Forbidden)
            return@post
        }
        val id: Int = userService.create(user)
        call.respond(id)
    }

    delete("/users/{id}") {
        val userService = UserService()
        val id: Int = call.parameters["id"]!!.toInt()
        userService.delete(id)
        call.respond(HttpStatusCode.NoContent)
    }
}
