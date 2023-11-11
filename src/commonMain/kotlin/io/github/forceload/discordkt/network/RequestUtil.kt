package io.github.forceload.discordkt.network

import io.github.forceload.discordkt.util.DiscordConstants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object ClientContainer {
    val client: HttpClient
    val platform: String
}

object RequestUtil {
    private val client: HttpClient = ClientContainer.client

    fun getRaw(url: String) = runBlocking { client.get(url).readBytes() }

    fun get(url: String, authorization: String, vararg params: Pair<String, Any>) =
        runBlocking {
            client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "discord.com"

                    appendPathSegments("api", "v${DiscordConstants.apiVersion}")
                    appendPathSegments(url)

                    params.forEach {
                        parameters.append(it.first, it.second.toString())
                    }
                }

                headers {
                    append("Authorization", "Bot $authorization")
                }
            }.body<String>()
        }

    fun post(url: String, authorization: String, data: String, type: ContentType = ContentType.Application.Json) =
        runBlocking {
            client.post {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "discord.com"

                    appendPathSegments("api", "v${DiscordConstants.apiVersion}")
                    appendPathSegments(url)
                }

                headers {
                    append("Authorization", "Bot $authorization")
                }

                contentType(type)
                setBody(data)
            }.body<String>()
        }

    fun delete(url: String, authorization: String): String =
        runBlocking {
            client.delete {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "discord.com"

                    appendPathSegments("api", "v${DiscordConstants.apiVersion}")
                    appendPathSegments(url)
                }

                headers {
                    append("Authorization", "Bot $authorization")
                }
            }.body<String>()
        }
}