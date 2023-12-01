package com.example.android_chatbot.model.azure

import android.util.Log
import com.example.android_chatbot.data.message.Message
import com.example.android_chatbot.data.setting.SettingDAO
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


object AzureOpenAIService {
    private lateinit var apiKey: String
    private lateinit var endPoint: String

    fun init(settingDAO: SettingDAO) {
        CoroutineScope(Dispatchers.IO).launch {
            apiKey = settingDAO.getSettingByService(service = "azure").apiKey
            endPoint =
                "https://hkust.azure-api.net/openai/deployments/gpt-35-turbo/chat/completions?api-version=2023-05-15"
        }
    }

    suspend fun getChatResponse(messages: List<Message>): String {
        val client = HttpClient()
        val responseBody: HttpResponse = client.post {
            url(endPoint)
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
