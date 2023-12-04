package com.example.android_chatbot.model.openai

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.chat.TextContent
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.android_chatbot.data.message.Message
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.model.ChatBotService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

object OpenAIService: ChatBotService() {
    private lateinit var openAI: OpenAI

    override suspend fun init(settingDAO: SettingDAO) {
        try {
            withContext(Dispatchers.IO) {
                openAI = OpenAI(
                    token = settingDAO.getSettingByService(service = "OpenAI").apiKey,
                    timeout = Timeout(socket = 60.seconds)
                )
            }
        } catch (e: Exception) {
            Log.e("OpenAIService", "init: ${e.message}")
        }
    }

    /**
     * Get the response from the OpenAI service.
     * @param messages The list of messages to be sent to the service.
     * @return The response from the service.
     *   The first element is the response content.
     *   The second element is whether the response is successful.
     **/
    override suspend fun getChatResponse(messages: List<Message>, model: String): Pair<String, Boolean> {
        return try {
            val chatCompletionRequest =
                ChatCompletionRequest(model = ModelId(model), messages = messages.map {
                    ChatMessage(
                        role = if (it.role == "user") ChatRole.User else ChatRole.System,
                        content = it.content
                    )
                })

            val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)

            Log.d("OpenAIService", "completion: $completion")
            Pair(
                (completion.choices[0].message.messageContent as TextContent).content, true
            )
        } catch (e: Exception) {
            Pair("The service is not available now, please try again later.\n${e.message}", false)
        }
    }
}
