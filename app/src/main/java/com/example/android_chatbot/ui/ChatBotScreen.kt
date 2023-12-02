package com.example.android_chatbot.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.android_chatbot.R
import com.example.android_chatbot.data.channel.ChannelDAO
import com.example.android_chatbot.data.message.MessageDAO
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.ui.components.MenuItemCard
import kotlinx.coroutines.launch

enum class ChatBotScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name), AllChats(title = R.string.all_chats), SelectBot(title = R.string.select_bot), Settings(
        title = R.string.settings
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotTopAppBar(
    canNavigateBack: Boolean = false,
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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

    fun handleMenuItemClicked() {
        scope.launch {
            drawerState.close()
        }
    }

    val menuItemIds = listOf(
        R.string.all_chats, R.string.select_bot, R.string.settings
    )
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            Text("Android ChatBot", modifier = Modifier.padding(16.dp))
            Divider()

            menuItemIds.map { menuItemId ->
                MenuItemCard(
                    icon = when (menuItemId) {
                        R.string.all_chats -> Icons.Outlined.Face
                        R.string.select_bot -> Icons.Outlined.Person
                        R.string.settings -> Icons.Outlined.Settings

                        else -> {
                            throw IllegalStateException("Unknown menu item id: $menuItemId")
                        }
                    },
                    content = stringResource(id = menuItemId),
                    handleMenuItemClicked = { handleMenuItemClicked() },
                    modifier = Modifier.height(60.dp)
                )

                Divider()
            }
        }
    }) {
        Scaffold(topBar = {
            ChatBotTopAppBar(handleNavigationIconClicked = { handleNavigationIconClicked(it) })
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
                        Text("Start Screen")
                    }
                    composable(route = ChatBotScreen.AllChats.name) {
                        Text("AllChats Screen")
                    }
                    composable(route = ChatBotScreen.SelectBot.name) {
                        Text("SelectBot Screen")
                    }
                    composable(route = ChatBotScreen.Settings.name) {
                        Text("Settings Screen")
                    }
                }
            }
        }
    }
}