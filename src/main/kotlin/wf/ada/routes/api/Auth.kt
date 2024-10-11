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
import wf.ada.dto.AuthDto
import wf.ada.entities.Token
import wf.ada.entities.WebError
import wf.ada.services.UserService
import wf.ada.utils.Config
import java.util.Date

fun Route.auth() {
    post("/auth/tokens") {
        val userService = UserService()
        val authDto: AuthDto = call.receive<AuthDto>()

        val user = userService.checkAuth(authDto.email, authDto.password)
        if (user != null) {
            val token =
                JWT
                    .create()
                    .withAudience("${Config.url}api")
                    .withIssuer(Config.url)
                    .withClaim("name", user.name)
                    .withClaim("id", user.id)
                    .withClaim("email", user.email)
                    .withClaim("role", user.role.toString())
                    .withClaim("creationDate", user.creationDate.toString())
                    .withExpiresAt(Date(System.currentTimeMillis() + 86400)) // 24h
                    .sign(Algorithm.HMAC256(Config.jwtSecret))
            call.respond(Token(token))
        } else {
            call.respond(
                HttpStatusCode.Unauthorized,
                WebError(HttpStatusCode.Unauthorized.toString(), "bad username or password"),
            )
        }
    }

    authenticate("auth-jwt") {
        get("/auth/verify") {
            val token = call.principal<JWTPrincipal>()

            val name = token!!.payload.getClaim("name").asString()
            val email = token.payload.getClaim("email").asString()
            val role = token.payload.getClaim("role").asString()
            val creationDate = token.payload.getClaim("creationDate").asString()
            val expiresAt = token.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText(
                "Hello, $name! Token is expired at $expiresAt ms. " +
                    "email: $email\n" +
                    "role: $role\n" +
                    "creationDate: $creationDate\n",
            )
        }
    }
}
