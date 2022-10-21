package com.example

import com.example.data.models.MongoSubscriberDataSource
import com.example.plugins.*
import com.example.security.hashing.SHA256HashingService
import com.example.security.token.JwtTokenService
import com.example.security.token.TokenConfig
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.util.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val mongoPW = System.getenv("MONGO_PW")
    val dbName = "ktor-unico"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://angelomarzo:$mongoPW@cluster0.d2bzqsu.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(dbName)
    val subDataSource = MongoSubscriberDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L, //1 year
        secret = System.getenv("JWT_SECRET")

    )
    val hashingService = SHA256HashingService()


    configureSecurity(tokenConfig)
    configureMonitoring()
    configureSerialization()
    configureRouting(subDataSource,hashingService,tokenService,tokenConfig)
}
