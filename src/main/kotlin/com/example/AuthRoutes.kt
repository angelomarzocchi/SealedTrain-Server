package com.example


import com.example.data.models.Subscriber
import com.example.data.models.SubscriberDataSource
import com.example.data.models.Ticket
import com.example.data.models.TicketType
import com.example.data.requests.AuthRequest
import com.example.data.requests.TicketQrCode
import com.example.data.responses.AuthResponse
import com.example.security.encryption.CryptoService
import com.example.security.hashing.HashingService
import com.example.security.hashing.SaltedHash
import com.example.security.token.TokenClaim
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

fun Route.signUp(
    hashingService: HashingService,
    subDataSource: SubscriberDataSource
) {
    post("/signup") {
        val request = call.receive<AuthRequest>()
        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        //effettuare altri controlli e richiedere molti piú dati per la registrazione
        if (areFieldsBlank) {
            call.respondText("Credentials not entered correctly", status = HttpStatusCode.BadRequest)
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val sub = Subscriber(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt,
            tickets = mutableListOf<Ticket>()
        )

        sub.tickets.add(
            Ticket(
                LocalDateTime(LocalDate(2022, 11, 29), LocalTime(12, 50)),
                TicketType.FULL_YEAR,
                startingPoint = "Giugliano",
                endingPoint = "Napoli"
            )
        )
        val clearQrCode =sub.tickets[0].qrcode
        val cryptoService = CryptoService()
        sub.tickets[0].qrcode = cryptoService.encrypt(clearQrCode,sub.id.toString(),sub.password)
        sub.iv = cryptoService.iv

        val wasAcknowledged = subDataSource.insertSubscriber(sub)
        if (!wasAcknowledged) {
            call.respondText("Couldn't insert this sub", status = HttpStatusCode.Conflict)
        }
        call.respondText("Subscriber inserted correctly", status = HttpStatusCode.OK)

    }

}

fun Route.signIn(
    subDataSource: SubscriberDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("/signin") {
        val request = call.receive<AuthRequest>()
        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        //effettuare altri controlli
        if (areFieldsBlank) {
            call.respondText("Credentials not entered correctly", status = HttpStatusCode.BadRequest)
        }
        val sub = subDataSource.getSubscriberByUsername(request.username)
        if (sub == null) {
            call.respondText("One of the fields was empty", status = HttpStatusCode.Conflict)
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = sub!!.password,
                salt = sub.salt
            )
        )
        if (!isValidPassword) {
            call.respondText("Incorrect username or password", status = HttpStatusCode.Conflict)
        }
        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "subId",
                value = sub.id.toString()
            )
        )
        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(token)
        )
    }
}

fun Route.authenticate() {
    authenticate {//non dobbiamo fare nulla perché il controllo é gia effettuato nel file plugins/Security.kt
        get(" authenticate") {
            call.respond(HttpStatusCode.OK)
        }

    }
}

fun Route.getTickets(subDataSource: SubscriberDataSource) {
    authenticate {
        get("tickets") {

            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("subId", String::class)
            if (userId == null) {
                call.respond(HttpStatusCode.Conflict, "Couldnt find a user")
            }
            val tickets = subDataSource.getSubscriberBySubId(userId!!)?.tickets
            if (tickets == null) {
                call.respond(HttpStatusCode.Conflict, "No tickets found")
            }
            call.respond(HttpStatusCode.OK, message = tickets!!)
            //call.respond(HttpStatusCode.OK, "your username is $userId")

        }
    }
}

fun Route.validateTicket(subDataSource: SubscriberDataSource) {
post("/validateTicket") {

    val request = call.receive<TicketQrCode>()
    val areFieldsBlank = request.qrCode.isBlank()
    if(areFieldsBlank) {
        call.respondText("QrCode is empty", status = HttpStatusCode.BadRequest)
    }
    val sub = subDataSource.getSubscriberByQrCode(request.qrCode)
    if (sub == null) {
        call.respondText("Probabilmente il filtro é sbagliato", status = HttpStatusCode.Conflict)
    }

    /*
    val cryptoService = CryptoService()
    cryptoService.iv = sub!!.iv
    val plainTextQrCode = cryptoService.decrypt(request.qrCode,sub.id.toString(),sub.password)
     */

    val ticket = sub!!.tickets.find {  it.qrcode == request.qrCode }
    if(ticket != null){
        if(ticket.checkValidity()) {
            call.respond(HttpStatusCode.OK)
        }else {
            call.respondText("Il ticket non é valido",status=HttpStatusCode.Conflict)
        }
    } else {
        call.respondText("Non ho trovato il ticket",status=HttpStatusCode.Conflict)
    }


}
}









