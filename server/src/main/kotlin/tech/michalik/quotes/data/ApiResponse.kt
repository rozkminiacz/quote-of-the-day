package tech.michalik.quotes.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val records: List<Record>,
    val offset: String? = null
)

@Serializable
data class Record(
    val id: String,
    @SerialName("createdTime") val createdTime: String,
    val fields: Fields
)

@Serializable
data class Fields(
    @SerialName("Quote") val quote: String,
    @SerialName("Author") val author: String,
    @SerialName("Categories") val categories: List<String>,
    @SerialName("ID") val id: Int
)