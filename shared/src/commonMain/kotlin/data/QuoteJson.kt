package data

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
data class QuoteJson(
    val quote: String,
    val author: String,
    val categories: List<Category>
)

@Serializable
@JvmInline
value class Category(val value: String)