package com.thss.lunchtime.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.websocket.close
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object LunchTimeNotificationService {
    private val client = HttpClient(OkHttp) {
        engine {
            preconfigured = OkHttpClient.Builder()
                .pingInterval(20, TimeUnit.SECONDS)
                .build()
        }
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }
    private var webSocketSession: DefaultClientWebSocketSession? = null
    suspend fun connect(userName: String, onReceive: (realtimeNotice: RealtimeNotice) -> Unit) {
        if (webSocketSession != null) close()
        Log.d("LunchTime Chat", "Connect to ${LunchTimeNetworkParams.BASE_WS_URL}/notice/, params: $userName")
        webSocketSession = client.webSocketSession {
            url(
                LunchTimeNetworkParams.SCHEMA,
                LunchTimeNetworkParams.HOST,
                LunchTimeNetworkParams.PORT, "ws/notice/") {
                parameters.append("user_name", userName)
            }
        }
        webSocketSession!!.incoming.consumeAsFlow().collect { frame ->
            onReceive(webSocketSession!!.converter?.deserialize(frame) as RealtimeNotice)
        }
    }
    suspend fun close() {
        webSocketSession!!.close()
        webSocketSession = null
    }
}