package com.example.data.responses

import com.example.data.models.TicketType
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class TicketResponse(
    val startValidation: LocalDateTime,
    val type: TicketType,
    val startingPoint: String,
    val endingPoint: String,
    var qrcode: String,
    val id: String
)
