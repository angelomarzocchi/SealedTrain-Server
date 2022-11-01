package com.example.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.Date

data class Ticket(
    val startValidation: Date,
    val type: TicketType,
    val startingpoint: String,
    val endingpoint: String,
    val qrcode: String,
    @BsonId val id: ObjectId = ObjectId()
)
