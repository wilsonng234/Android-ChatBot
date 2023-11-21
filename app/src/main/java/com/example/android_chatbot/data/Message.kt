package com.example.android_chatbot.data

data class Message(val role: String, val content: String) {
    override fun toString(): String {
        return """
            {
                "role": "$role",
                "content": "$content"
            }
        """.trimIndent()
    }
}
