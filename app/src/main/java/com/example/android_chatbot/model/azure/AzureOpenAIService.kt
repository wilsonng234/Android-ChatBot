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

    /**
        * Get the response from the Azure OpenAI service.
     * @param messages The list of messages to be sent to the service.
     * @return The response from the service.
     *   The first element is the response content.
     *   The second element is whether the response is successful.
     **/
    suspend fun getChatResponse(messages: List<Message>): Pair<String, Boolean> {
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

            Pair(content.toString(), true)
        } catch (e: Exception) {
            Log.e("AzureOpenAIService", "getChatResponse: ${e.message}")

            Pair("The service is not available now, please try again later.", false)
        }
    }
}
