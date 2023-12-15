package tech.michalik.quotes

import SERVER_PORT
import data.QuoteJson
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import tech.michalik.quotes.data.AirtableRepository

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    val db = AirtableRepository(
        url = environment.config.property("ktor.airtable.url").getString(),
        key = environment.config.property("ktor.airtable.key").getString(),
    )

    configureSerialization()
    routing {
        get("/") {
            call.respond(db.getAll())
        }
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}


class QuotesDatabase {

    val content by lazy {
        object {}.javaClass.classLoader.getResource("quotes.json").readText()
    }

    fun getAll(): List<QuoteJson> {
        return Json.decodeFromString<List<QuoteJson>>(content)
    }
}

