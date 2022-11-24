package com.example.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.Date

data class Ticket(
    val startValidation: Date,
    val type: TicketType,
    val startingPoint: String,
    val endingPoint: String,
    val qrcode: String = type.toString() + "_" + startingPoint + "_" + "endingPoint" + "_" + startValidation.toString(),
    @BsonId val id: ObjectId = ObjectId()
)
