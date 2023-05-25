package com.thss.lunchtime.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.websocket.close
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

private const val BASE_WS_URL = "ws://82.156.30.206:6001/ws"

class LunchTimeChatService(private val senderID: Int, private val receiverID: Int) {
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
    suspend fun connect(onReceive: (chatResponse: ChatResponse) -> Unit) {
        if (webSocketSession == null) {
            Log.d("LunchTime Chat", "Connect to $BASE_WS_URL/chat/$senderID/$receiverID/")
            webSocketSession = client.webSocketSession {
                url("$BASE_WS_URL/chat/$senderID/$receiverID/")
            }
        }
        getChatHistory()
        webSocketSession!!.incoming.consumeAsFlow().collect { frame ->
            onReceive(webSocketSession!!.converter?.deserialize(frame) as ChatResponse)
        }
    }
    suspend fun send(onSend: (chatMessage: ChatMessage) -> Unit, value : String) {
        val chatMessage = ChatMessage(senderID, value)
        webSocketSession!!.sendSerialized(
            ChatRequest("message", chatMessage)
        )
//        onSend(chatMessage)
        // TODO: 这里浪费比较大
        getChatHistory()
    }
    suspend fun getChatHistory() {
        webSocketSession!!.sendSerialized(
            ChatRequest("history")
        )
    }
    suspend fun close() {
        webSocketSession!!.close()
    }
}
