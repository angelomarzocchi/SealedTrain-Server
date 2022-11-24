package com.example.data.models


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

}