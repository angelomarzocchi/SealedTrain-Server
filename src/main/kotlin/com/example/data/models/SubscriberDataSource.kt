package com.example.data.models

import com.mongodb.reactivestreams.client.MongoCollection
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineFindPublisher

interface SubscriberDataSource {

    suspend fun getSubscriberByUsername(username: String): Subscriber?

    suspend fun insertSubscriber(sub: Subscriber): Boolean

    suspend fun getSubscriberBySubId(subId: String): Subscriber?

    suspend fun getSubscriberByQrCode(qrcode: String): Subscriber?

    suspend fun getAllSubscribers(): List<Subscriber>



}