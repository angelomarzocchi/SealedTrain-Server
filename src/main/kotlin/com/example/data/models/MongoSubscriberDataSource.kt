package com.example.data.models


import com.mongodb.client.model.Filters.eq
import com.mongodb.reactivestreams.client.MongoCollection
import kotlinx.coroutines.reactive.asFlow
import org.apache.commons.codec.binary.Hex
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.litote.kmongo.EMPTY_BSON
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.CoroutineFindPublisher
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.withKMongo

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
        val str = "\"$qrcode\""
        return users.findOne(Subscriber::tickets / Ticket::qrcode eq qrcode)
    }

    override suspend fun getAllSubscribers(): List<Subscriber> {
        return users.find().toList()
    }


}


