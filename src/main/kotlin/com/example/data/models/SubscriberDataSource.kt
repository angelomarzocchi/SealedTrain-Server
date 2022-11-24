package com.example.data.models

interface SubscriberDataSource {

    suspend fun getSubscriberByUsername(username: String): Subscriber?
    suspend fun insertSubscriber(sub: Subscriber): Boolean


}