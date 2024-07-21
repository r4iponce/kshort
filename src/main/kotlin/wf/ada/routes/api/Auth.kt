package wf.ada.routes.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import wf.ada.dto.UserDto
import wf.ada.entities.Token
import wf.ada.entities.WebError
import wf.ada.services.UserService
import wf.ada.utils.Config

fun Route.auth() {
    post("/auth/tokens") {
        val userService = UserService()
        val userDto: UserDto = call.receive<UserDto>()

        val user = userService.checkAuth(userDto.email, userDto.password)
        if (user != null) {
            val token =
                JWT
                    .create()
                    .withAudience("${Config.url}api")
                    .withIssuer(Config.url)
                    .withClaim("username", user.name)
                    .withClaim("email", user.email)
                    .withClaim("creationDate", user.creationDate.toString())
                    .withClaim("role", user.creationDate.toString())
//                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                    .sign(Algorithm.HMAC256(Config.jwtSecret))
            call.respond(Token(token))
        } else {
            call.respond(HttpStatusCode.Unauthorized, WebError(HttpStatusCode.Unauthorized.toString(), "bad username or password"))
        }
    }

    authenticate("auth-jwt") {
        get("/auth/current") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val test = principal.payload.getClaim("test").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText("Hello, $username! Token is expired at $expiresAt ms. $test")
        }
    }
}
