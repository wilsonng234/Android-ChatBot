package com.example.android_chatbot

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.mediumTopAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.ui.StartScreen
import com.example.android_chatbot.ui.chat_screen.ChatScreen
import com.example.android_chatbot.ui.components.ChatHistoryCard
import com.example.android_chatbot.ui.components.MenuItemCard
import com.example.android_chatbot.ui.setting_screen.SettingScreen
import kotlinx.coroutines.launch

enum class ChatBotScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name), AllChats(title = R.string.all_chats), SelectBot(title = R.string.select_bot), Settings(
        title = R.string.settings
    ),
    Chat(title = R.string.chat)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotTopAppBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    handleNavigationIconClicked: (Boolean) -> () -> Unit = { {} },
    modifier: Modifier = Modifier
) {
    TopAppBar(title = { Text("") }, colors = mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ), modifier = modifier, navigationIcon = {
        IconButton(onClick = handleNavigationIconClicked(canNavigateBack)) {
            Icon(
                imageVector = if (!canNavigateBack) Icons.Filled.Menu else Icons.Filled.ArrowBack,
                contentDescription = if (!canNavigateBack) stringResource(R.string.drawer_button) else stringResource(
                    R.string.back_button
                )
            )
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotApp(
    channelDAO: ChannelDAO,
    messageDAO: MessageDAO,
    settingDAO: SettingDAO,
    navHostController: NavHostController = rememberNavController()
) {
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: ChatBotScreen.Start.name

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val channels by channelDAO.getFiveRecentChannels().collectAsState(initial = emptyList())

    fun handleChatCardClicked(Id: Int) {
        scope.launch {
            drawerState.close()
        }

        navHostController.navigate(ChatBotScreen.Chat.name + "/" + Id.toString())
    }

    fun handleNavigationIconClicked(canNavigateBack: Boolean): () -> Unit {
        return if (!canNavigateBack) {
            {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
        } else {
            {
                navHostController.navigateUp()
            }
        }
    }

    fun handleMenuItemClicked(menuItemId: Int) {
        scope.launch {
            drawerState.close()
        }

        when (menuItemId) {
            ChatBotScreen.AllChats.title -> {
                navHostController.navigate(ChatBotScreen.AllChats.name)
            }

            ChatBotScreen.SelectBot.title -> {
                navHostController.navigate(ChatBotScreen.SelectBot.name)
            }

            ChatBotScreen.Settings.title -> {
                navHostController.navigate(ChatBotScreen.Settings.name)
            }
        }
    }

    val menuItemIds = listOf(
        ChatBotScreen.AllChats.title, ChatBotScreen.SelectBot.title, ChatBotScreen.Settings.title
    )

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text("Android ChatBot", modifier = Modifier.padding(16.dp))
            Divider()

            for (channel in channels) {
                val messages by messageDAO.getMessagesByChannelId(channel.id)
                    .collectAsState(initial = emptyList())
                val lastMessage = messages.lastOrNull()
                val ser = when (channel.service) {
                    "Azure OpenAI" -> {
                        R.drawable.azure
                    }

                    "OpenAI" -> {
                        R.drawable.openai
                    }

                    else -> {
                        throw IllegalStateException("Unknown service")
                    }
                }

                ChatHistoryCard(
                    iconId = ser,
                    channelId = channel.id,
                    service = channel.service,
                    model = "gpt-4",
                    title = "Title",
                    recentChat = lastMessage?.content ?: "",
                    time = lastMessage?.createdTime,
                    onClick = ::handleChatCardClicked,
                    modifier = Modifier
                )
            }

            Divider()

            menuItemIds.map { menuItemId ->
                MenuItemCard(
                    icon = when (menuItemId) {
                        ChatBotScreen.AllChats.title -> Icons.Outlined.Face
                        ChatBotScreen.SelectBot.title -> Icons.Outlined.Person
                        ChatBotScreen.Settings.title -> Icons.Outlined.Settings

                        else -> {
                            throw IllegalStateException("Unknown menu item id: $menuItemId")
                        }
                    },
                    content = stringResource(id = menuItemId),
                    handleMenuItemClicked = { handleMenuItemClicked(menuItemId) },
                    modifier = Modifier.height(60.dp)
                )

                Divider()
            }
        }
    }) {
        Scaffold(topBar = {
            ChatBotTopAppBar(currentScreen = currentScreen,
                canNavigateBack = navHostController.previousBackStackEntry != null,
                handleNavigationIconClicked = { handleNavigationIconClicked(it) })
        }) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                NavHost(
                    navController = navHostController, startDestination = ChatBotScreen.Start.name
                ) {
                    composable(route = ChatBotScreen.Start.name) {
                        StartScreen(
                            channelDAO = channelDAO,
                            messageDAO = messageDAO,
                            onClick = ::handleChatCardClicked
                        )
                    }
                    composable(route = ChatBotScreen.AllChats.name) {
                        Text("AllChats Screen")
                    }
                    composable(route = ChatBotScreen.SelectBot.name) {
                        Text("SelectBot Screen")
                    }
                    composable(route = ChatBotScreen.Settings.name) {
                        SettingScreen(settingDAO = settingDAO)
                    }
                    composable(
                        route = ChatBotScreen.Chat.name + "/{channelId}",
                        arguments = listOf(navArgument("channelId") { type = NavType.IntType })
                    ) {
                        ChatScreen(
                            channelDAO = channelDAO,
                            messageDAO = messageDAO,
                            settingDAO = settingDAO,
                            channelId = it.arguments!!.getInt("channelId")
                        )
                    }
                }
            }
        }
    }
}