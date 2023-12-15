package tech.michalik.quotes.data

import data.Category
import data.QuoteJson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.prepareGet
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Created by jaroslawmichalik on 15/12/2023
 */
class AirtableRepository(val url: String, val key: String) {
    val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json)
        }
    }

    suspend fun getAll(): List<QuoteJson> = client.prepareGet(url) {
        header("Authorization", "Bearer $key")
    }.execute<ApiResponse> {
        it.body()
    }.map()
}

fun ApiResponse.map(): List<QuoteJson> {
    return this.records.map { record ->
        QuoteJson(
            quote = record.fields.quote,
            author = record.fields.author,
            categories = record.fields.categories.map { Category(it) }
        )
    }
}