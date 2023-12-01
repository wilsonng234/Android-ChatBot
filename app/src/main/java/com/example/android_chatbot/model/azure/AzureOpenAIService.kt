package com.example.android_chatbot.model.azure

import android.util.Log
import com.example.android_chatbot.data.message.Message
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.json.JSONArray
import org.json.JSONObject


class AzureOpenAIService(
    private val apiKey: String, private val endpoint: String
) {
    suspend fun getChatResponse(messages: List<Message>): String {
        val client = HttpClient()
        val responseBody: HttpResponse = client.post {
            url(endpoint)
            header("api-key", apiKey)
            contentType(ContentType.Application.Json)

            setBody("{\"messages\":${messages.joinToString(prefix = "[", postfix = "]")}}")
        }
        client.close()

        return try {
            val responseJson = JSONObject(responseBody.bodyAsText())

            val choices = responseJson.getJSONArray("choices") as JSONArray
            val message = choices.getJSONObject(0).get("message") as JSONObject
            val content = message.get("content")

            content.toString()
        } catch (e: Exception) {
            Log.e("AzureOpenAIService", "getChatResponse: ${e.message}")

            "The service is not available now, please try again later."
        }
    }
}
