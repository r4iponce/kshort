package wf.ada.dto

import io.ktor.server.auth.jwt.JWTPrincipal
import kotlinx.serialization.Serializable
import wf.ada.entities.Roles
import wf.ada.utils.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class UserDto(
    val name: String,
    val email: String,
    val role: Roles,
    @Serializable(with = LocalDateTimeSerializer::class) val creationDate: LocalDateTime? = null,
)

fun tokenToUserDto(token: JWTPrincipal): UserDto {
    val name = token.payload.getClaim("name").asString()
    val email = token.payload.getClaim("email").asString()
    val role = Roles.valueOf(token.payload.getClaim("role").asString())
    val creationDate = LocalDateTime.parse(token.payload.getClaim("creationDate").asString())

    return UserDto(name, email, role, creationDate)
}
