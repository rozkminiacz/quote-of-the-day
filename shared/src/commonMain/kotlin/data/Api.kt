package data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.prepareGet
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.*


/**
 * Created by jaroslawmichalik on 15/12/2023
 */
class Api(val url: String) {
    val api = HttpClient{
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun getAll(): List<QuoteJson> {
        return api.prepareGet(url)
            .execute<List<QuoteJson>> {
                it.body()
            }
    }
}