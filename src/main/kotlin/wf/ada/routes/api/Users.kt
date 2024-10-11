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
import wf.ada.dto.tokenToUserDto
import wf.ada.entities.ExposedUser
import wf.ada.entities.Roles
import wf.ada.services.UserService

fun Route.users() {
    get("/users") {
        val userService = UserService()
        val users: List<ExposedUser> = userService.readAll()
        call.respond(users)
    }

    authenticate("auth-jwt", optional = true) {
        get("/users/self") {
            val token = call.principal<JWTPrincipal>()

            if (token != null) {
                call.respond(tokenToUserDto(token))
                return@get
            }

            call.respond(HttpStatusCode.NotFound)
        }
    }

    get("/users/{name}") {
        val userService = UserService()
        val name: String? = call.parameters["name"]

        if (name != null) {
            val user: ExposedUser? = userService.read(name)

            if (user != null) {
                call.respond(user)
                return@get
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }

        call.respond(HttpStatusCode.NotFound)
    }

    post("/users") {
        val userService = UserService()
        val user: ExposedUser = call.receive<ExposedUser>()
        if (user.role == Roles.ADMIN) {
            call.respond(HttpStatusCode.Forbidden)
            return@post
        }

        userService.create(user)
        call.respond(HttpStatusCode.Created)
    }

    delete("/users/{name}") {
        val userService = UserService()
        var name: String = call.parameters["name"] ?: throw IllegalArgumentException("Name cannot be null")

        if (name == "self") {
            userService.delete(name)
            val token = call.principal<JWTPrincipal>()

            if (token != null) {
                val userDto = tokenToUserDto(token)
                name = userDto.name
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        userService.delete(name)
        call.respond(HttpStatusCode.NoContent)
    }
}
