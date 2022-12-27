package com.example.data.models



interface SubscriberDataSource {

    suspend fun getSubscriberByUsername(username: String): Subscriber?

    suspend fun insertSubscriber(sub: Subscriber): Boolean

    suspend fun getSubscriberBySubId(subId: String): Subscriber?

    suspend fun getSubscriberByQrCode(qrcode: String): Subscriber?

    suspend fun getAllSubscribers(): List<Subscriber>

    suspend fun updateTicket(iv:ByteArray,qrcode:String)

    suspend fun getSubscriberByTicketId(ticketId: String): Subscriber?



}