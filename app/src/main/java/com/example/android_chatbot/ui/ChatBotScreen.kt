package com.example.android_chatbot.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

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
                contentDescription = if (!canNavigateBack) "Drawer" else "Back"
            )
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotApp(
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

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            Text("Android ChatBot", modifier = Modifier.padding(16.dp))
            Divider()
        }
    }) {
        Scaffold(topBar = {
            ChatBotTopAppBar(handleNavigationIconClicked = { handleNavigationIconClicked(it) })
        }) { paddingValues ->
            // Screen content
        }
    }
}