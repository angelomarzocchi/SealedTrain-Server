package com.example.data.models


import org.apache.commons.codec.binary.Hex
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

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

}


