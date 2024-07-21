package wf.ada.entities

import kotlinx.serialization.Serializable
import wf.ada.utils.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class ExposedUser(
    val id: Int? = null,
    val name: String,
    val email: String,
    val password: String,
    val description: String? = null,
    val role: Roles = Roles.USER,
    @Serializable(with = LocalDateTimeSerializer::class) val creationDate: LocalDateTime? = null,
)
