package com.example.android_chatbot.ui.setting_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_chatbot.R
import com.example.android_chatbot.data.DataSource
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.ui.components.FormInputField

data class ApiKeyInput(
    val service: String = "", val apiKey: String = ""
)

@Composable
fun SettingScreen(
    settingDAO: SettingDAO, modifier: Modifier = Modifier
) {
    val viewModel: SettingViewModel = viewModel(factory = SettingViewModel.Factory(settingDAO))
    val settings by settingDAO.getAll().collectAsState(initial = emptyList())
    val (servicesOption, setServicesOption) = remember { mutableStateOf(DataSource.services) }
    val (apiKeyInputFields, setApiKeyInputFields) = remember {
        mutableStateOf<List<ApiKeyInput>>(
            emptyList()
        )
    }
    val (reset, setReset) = remember { mutableStateOf(false) }

    LaunchedEffect(settings) {
        setServicesOption(DataSource.services - settings.map { it.service }.toSet())
        setApiKeyInputFields(settings.map { ApiKeyInput(it.service, it.apiKey) })
    }

    LaunchedEffect(reset) {
        if (reset) {
            setServicesOption(DataSource.services - settings.map { it.service }.toSet())
            setApiKeyInputFields(settings.map { ApiKeyInput(it.service, it.apiKey) })
            setReset(false)
        }
    }

    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        for (apiKeyInputField in apiKeyInputFields) {
            val (expanded, setExpanded) = remember { mutableStateOf(false) }
            val (selectedOptionText, setSelectedOptionText) = remember {
                mutableStateOf(apiKeyInputField.service)
            }
            val (apiKey, setApiKey) = remember { mutableStateOf(apiKeyInputField.apiKey) }

            LaunchedEffect(reset) {
                setSelectedOptionText(apiKeyInputField.service)
                setApiKey(apiKeyInputField.apiKey)
            }

            ApiKeyInputSection(
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

        if (apiKeyInputFields.size < DataSource.services.size) {
            AddApiKeyInputSectionButton(
                addApiKeyInputSection = {
                    setApiKeyInputFields(apiKeyInputFields.plus(ApiKeyInput()))
                }, modifier = modifier
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 32.dp)
        ) {
            SubmitFormButton(modifier = modifier.weight(0.2f))
            ResetFormButton(setReset, modifier = modifier.weight(0.2f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ApiKeyInputSection(
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
            ExposedDropdownMenuBox(expanded = expanded,
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

@Composable
private fun AddApiKeyInputSectionButton(
    addApiKeyInputSection: () -> Unit, modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = { addApiKeyInputSection() }, colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ), modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_api_key_input_section)
        )
    }
}

@Composable
private fun SubmitFormButton(
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = { }, colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ), modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text(text = stringResource(R.string.submit))
    }
}

@Composable
private fun ResetFormButton(
    setReset: (Boolean) -> Unit, modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = { setReset(true) }, colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ), modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text(text = stringResource(R.string.reset))
    }
}
