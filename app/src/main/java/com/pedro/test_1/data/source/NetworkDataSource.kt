package com.pedro.test_1.data.source

import com.pedro.test_1.domain.entities.AuthResult
import com.pedro.test_1.domain.entities.ListItem
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object NetworkDataSource {
    private val client by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = true
                        encodeDefaults = true
                    }
                )
            }
        }
    }

    suspend fun checkAuth(code: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get("URL")
            when (response.status) {
                HttpStatusCode.OK -> true
                HttpStatusCode.Unauthorized -> error("Code doesn't exist")
                else -> error("Something went wrong")
            }
        }
    }

    //EXAMPLE
}
