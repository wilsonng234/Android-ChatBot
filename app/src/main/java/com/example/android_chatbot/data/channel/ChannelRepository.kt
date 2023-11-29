package com.example.android_chatbot.data.channel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChannelRepository(private val channelDAO: ChannelDAO) {
    suspend fun insertChannels(vararg channels: Channel) {
        withContext(Dispatchers.IO) {
            channelDAO.insertAll(*channels)
        }
    }

    suspend fun getAllChannels(): List<Channel> {
        return withContext(Dispatchers.IO) {
             channelDAO.getAll()
        }
    }

    suspend fun getChannelById(id: Int): Channel {
        return withContext(Dispatchers.IO) {
             channelDAO.getChannelById(id)
        }
    }
}