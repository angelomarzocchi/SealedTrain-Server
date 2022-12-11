package com.example.data.models



import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Clock
import java.time.DayOfWeek
import java.time.Month
import java.time.MonthDay

import java.time.Year
import java.time.YearMonth

@Serializable
data class Ticket(
    val startValidation: LocalDateTime,
    val type: TicketType,
    val startingPoint: String,
    val endingPoint: String,
    var qrcode: String =   type.toString() + "_" + startingPoint + "_" + "endingPoint" + "_" + startValidation.toString(),
    @BsonId @Serializable(with = ObjectIdAsStringSerializer::class) val id: ObjectId = ObjectId()
) {
    fun checkValidity(): Boolean {
        var isValid: Boolean = false
        val today = LocalDateTime(YearMonth.now().year,YearMonth.now().month.value,MonthDay.now().dayOfMonth,0,0)
        when(type) {
            TicketType.ONE_DAY -> {
                if(today.dayOfYear - startValidation.dayOfMonth <= 1){
                    isValid = true
                }
            }
            TicketType.SEVEN_DAYS -> {
               if(today.dayOfYear - startValidation.dayOfMonth <= 7)
                   isValid = true
            }
            TicketType.FULL_YEAR -> {
                if(today.dayOfYear - startValidation.dayOfMonth <= 365)
                    isValid = true
            }

        }

        return isValid
    }
}

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






