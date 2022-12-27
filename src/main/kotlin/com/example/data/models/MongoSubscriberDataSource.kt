package com.example.data.models


import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.apache.commons.codec.binary.Hex
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoSubscriberDataSource(
    db: CoroutineDatabase
): SubscriberDataSource {

    private val users = db.getCollection<Subscriber>()

    override suspend fun getSubscriberByUsername(username: String): Subscriber? {
        return users.findOne(Subscriber::username eq username)
    }

    override suspend fun insertSubscriber(sub: Subscriber): Boolean {
        return users.insertOne(sub).wasAcknowledged()
    }

    override suspend fun getSubscriberBySubId(subId: String): Subscriber? {
          val  hexId =Hex.decodeHex(subId)
        return users.findOneById(ObjectId(hexId))
    }

    override suspend fun getSubscriberByQrCode(qrcode: String): Subscriber? {

        return users.findOne(Subscriber::tickets / Ticket::qrcode eq qrcode)
    }

    override suspend fun getAllSubscribers(): List<Subscriber> {
        return users.find().toList()
    }

    override suspend fun updateTicket(iv:ByteArray, qrcode:String) {
        val filter: Bson = Filters.elemMatch("tickets", Filters.eq("qrcode", qrcode))

// crea l'update che specifica il nuovo valore del campo iv
        val updateIv: Bson = Updates.set("tickets.$.iv", iv)
        //val updateQr: Bson = Updates.set("tickets.$.qrcode",encryptedQrcode)

// esegui l'update sul database
        users.updateOne(filter, updateIv)
       // users.updateOne(filter,updateQr)
    }

    override suspend fun getSubscriberByTicketId(ticketId: String): Subscriber? {
        val hexId = Hex.decodeHex(ticketId)
        return users.findOne(Subscriber::tickets / Ticket::id eq ObjectId(hexId))
    }


}


