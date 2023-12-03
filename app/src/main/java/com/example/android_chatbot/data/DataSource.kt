package com.example.android_chatbot.data

object DataSource {
    val servicesToModels = mapOf(
        "Azure OpenAI" to listOf("gpt-35-turbo", "gpt-4"),
        "OpenAI" to listOf("gpt-35-turbo", "gpt-4")
    )
}
