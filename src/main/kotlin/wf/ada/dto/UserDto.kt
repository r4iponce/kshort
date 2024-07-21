package wf.ada.dto

import kotlinx.serialization.Serializable
import wf.ada.utils.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class UserDto(
    val id: Int? = null,
    val name: String? = null,
    val email: String,
    val password: String,
    @Serializable(with = LocalDateTimeSerializer::class) val creationDate: LocalDateTime? = null,
)
