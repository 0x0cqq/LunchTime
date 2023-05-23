package com.thss.lunchtime.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.flow.consumeAsFlow
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

private const val BASE_WS_URL = "ws://82.156.30.206:8000/ws/"

class LunchTimeChatService {
    private val client = HttpClient(OkHttp) {
        engine {
            preconfigured = OkHttpClient.Builder()
                .pingInterval(20, TimeUnit.SECONDS)
                .build()
        }
        install(WebSockets)
    }
    suspend fun test() {
        client.webSocket("$BASE_WS_URL/chat/123/456/") {
            incoming.consumeAsFlow().collect {
                println(it)
                send(Frame.Text("hello"))
            }
        }
    }
}

object LunchTimeChat {
    val service = LunchTimeChatService()
}