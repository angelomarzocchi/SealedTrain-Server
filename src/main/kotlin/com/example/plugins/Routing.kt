package com.example.plugins

import com.example.*
import com.example.data.models.SubscriberDataSource
import com.example.security.hashing.HashingService
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    subscriberDataSource: SubscriberDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {

    routing {
        signUp(hashingService, subscriberDataSource)
        signIn(subscriberDataSource, hashingService, tokenService, tokenConfig)
        authenticate()
        getTickets(subscriberDataSource)
        validateTicket(subscriberDataSource)

    }
}
