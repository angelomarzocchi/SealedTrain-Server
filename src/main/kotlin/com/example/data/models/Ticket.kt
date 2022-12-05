package com.example.data.models


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Ticket(
    val startValidation: LocalDateTime,
    val type: TicketType,
    val startingPoint: String,
    val endingPoint: String,
    val qrcode: String = type.toString() + "_" + startingPoint + "_" + "endingPoint" + "_" + startValidation.toString(),
    @BsonId @Serializable(with = ObjectIdAsStringSerializer::class) val id: ObjectId = ObjectId()
)

object ObjectIdAsStringSerializer : KSerializer<ObjectId> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("ObjectId", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ObjectId {
       // return IdGenerator.defaultGenerator.create(decoder.decodeString()) as ObjectId
        return ObjectId(decoder.decodeString().toByteArray())
    }

    override fun serialize(encoder: Encoder, value: ObjectId) {
        encoder.encodeString(value.toString())
    }
}






