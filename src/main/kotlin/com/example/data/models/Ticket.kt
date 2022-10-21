package com.example.data.models

import java.util.Date

data class Ticket(
    val startValidation: Date,
    val type: TicketType
)
