package wf.ada.entities

import kotlinx.serialization.Serializable

@Serializable
data class WebError(
    val code: String,
    val message: String,
)
