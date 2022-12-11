package com.example.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Subscriber(
    val username: String,
    val password: String,
    val salt: String,
    val tickets: MutableList<Ticket>,
    @BsonId val id: ObjectId = ObjectId()
){
    lateinit var iv: ByteArray
}
