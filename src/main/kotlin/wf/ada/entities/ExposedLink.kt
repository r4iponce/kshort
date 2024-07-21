package wf.ada.entities

import kotlinx.serialization.Serializable
import wf.ada.utils.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class ExposedLink(
    val short: String,
    val url: String,
    val ownerId: Int? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val creationDate: LocalDateTime? = LocalDateTime.now(),
    @Serializable(with = LocalDateTimeSerializer::class) val expirationDate: LocalDateTime? = null,
)
