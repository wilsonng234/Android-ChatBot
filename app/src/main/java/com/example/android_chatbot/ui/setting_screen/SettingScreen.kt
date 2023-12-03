package com.example.android_chatbot.ui.setting_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_chatbot.data.DataSource
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.ui.components.FormInputField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    settingDAO: SettingDAO, modifier: Modifier = Modifier
) {
    val viewModel: SettingViewModel = viewModel(factory = SettingViewModel.Factory(settingDAO))
    val settings by settingDAO.getAll().collectAsState(initial = emptyList())
    val (servicesOption, setServicesOption) = remember { mutableStateOf(DataSource.services) }
    
    LaunchedEffect(settings) {
        setServicesOption(DataSource.services - settings.map { it.service }.toSet())
    }

    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        for (setting in settings) {
            val (expanded, setExpanded) = remember { mutableStateOf(false) }
            val (selectedOptionText, setSelectedOptionText) = remember { mutableStateOf(setting.service) }
            val (apiKey, setApiKey) = remember { mutableStateOf(setting.apiKey) }

            apiKeyInputSection(
                expanded = expanded,
                setExpanded = setExpanded,
                selectedOptionText = selectedOptionText,
                setSelectedOptionText = setSelectedOptionText,
                apiKey = apiKey,
                setApiKey = setApiKey,
                servicesOption = servicesOption,
                setServicesOption = setServicesOption,
                modifier = modifier
            )

            Divider(modifier = modifier.padding(horizontal = 8.dp, vertical = 32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun apiKeyInputSection(
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    selectedOptionText: String,
    setSelectedOptionText: (String) -> Unit,
    apiKey: String,
    setApiKey: (String) -> Unit,
    servicesOption: List<String>,
    setServicesOption: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Icon(imageVector = Icons.Filled.Lock, contentDescription = null)

        Column {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { setExpanded(!expanded) }) {
                OutlinedTextField(readOnly = true,
                    value = selectedOptionText,
                    onValueChange = { },
                    label = { Text("Service") },
                    trailingIcon = {
                        TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = modifier.menuAnchor()
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
                    setExpanded(false)
                }) {
                    servicesOption.forEach { serviceOption ->
                        DropdownMenuItem(text = { Text(text = serviceOption) }, onClick = {
                            setServicesOption(
                                servicesOption.plus(selectedOptionText).minus(serviceOption)
                            )
                            setSelectedOptionText(serviceOption)
                            setExpanded(false)
                        })
                    }
                }
            }

            FormInputField(
                value = apiKey,
                onValueChange = { setApiKey(it) },
                label = "Your API Key",
                modifier = Modifier.fillMaxWidth(0.97f)
            )
        }
    }
}
