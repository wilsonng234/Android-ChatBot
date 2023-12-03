package com.example.android_chatbot.ui.setting_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.example.android_chatbot.data.setting.Setting
import com.example.android_chatbot.data.setting.SettingDAO
import com.example.android_chatbot.ui.components.FormInputField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val temp = DataSource.services - settings.map { it.service }
        setServicesOption(temp.sorted())
        setApiKeyInputFields(settings.map { ApiKeyInput(it.service, it.apiKey) })
    }

    LaunchedEffect(reset) {
        if (reset) {
            val temp = DataSource.services - settings.map { it.service }
            setServicesOption(temp.sorted())
            setApiKeyInputFields(settings.map { ApiKeyInput(it.service, it.apiKey) })
            setReset(false)
        }
    }

    val handleResetForm: () -> Unit = {
        setReset(true)
    }

    val handleSubmitForm: () -> Unit = {
        CoroutineScope(Dispatchers.IO).launch {
            settingDAO.deleteAll()

            for (apiKeyInputField in apiKeyInputFields) {
                val service = apiKeyInputField.service
                val apiKey = apiKeyInputField.apiKey

                if (service.isNotEmpty()) {
                    settingDAO.insertAll(
                        Setting(service = service, apiKey = apiKey)
                    )
                }
            }
        }
    }

    val removeApiKeyInputSection: (ApiKeyInput) -> Unit = {
        val temp = apiKeyInputFields.map { it }.toMutableList()
        temp.remove(it)
        setApiKeyInputFields(temp)
    }

    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        for (apiKeyInputField in apiKeyInputFields) {
            val (expanded, setExpanded) = remember { mutableStateOf(false) }

            ApiKeyInputSection(
                expanded = expanded,
                setExpanded = setExpanded,
                selectedOptionText = apiKeyInputField.service,
                setSelectedOptionText = {
                    val temp = apiKeyInputFields.map { it }.toMutableList()
                    temp[apiKeyInputFields.indexOf(apiKeyInputField)] = ApiKeyInput(
                        it, apiKeyInputField.apiKey
                    )
                    setApiKeyInputFields(temp)
                },
                apiKey = apiKeyInputField.apiKey,
                setApiKey = {
                    val temp = apiKeyInputFields.map { it }.toMutableList()
                    temp[apiKeyInputFields.indexOf(apiKeyInputField)] = ApiKeyInput(
                        apiKeyInputField.service, it
                    )
                    setApiKeyInputFields(temp)
                },
                servicesOption = servicesOption,
                setServicesOption = setServicesOption,
                removeApiKeyInputSection = {
                    removeApiKeyInputSection(apiKeyInputField)
                    setServicesOption(servicesOption.plus(apiKeyInputField.service).sorted())
                },
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
            ResetFormButton(handleResetForm = handleResetForm, modifier = modifier.weight(0.2f))
            SubmitFormButton(
                handleSubmitForm = handleSubmitForm, modifier = modifier.weight(0.2f)
            )
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
    removeApiKeyInputSection: () -> Unit,
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
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                                val temp = if (selectedOptionText.isEmpty()) servicesOption.minus(
                                    serviceOption
                                ) else servicesOption.plus(selectedOptionText).minus(serviceOption)

                                setServicesOption(temp.sorted())
                                setSelectedOptionText(serviceOption)
                                setExpanded(false)
                            })
                        }
                    }
                }

                RemoveApiKeyInputSectionButton(
                    removeApiKeyInputSection = removeApiKeyInputSection, modifier = modifier
                )
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
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary
        ), modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_api_key_input_section)
        )
    }
}

@Composable
private fun RemoveApiKeyInputSectionButton(
    removeApiKeyInputSection: () -> Unit, modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = { removeApiKeyInputSection() }, colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.secondary
        ), modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.remove_api_key_input_section)
        )
    }
}

@Composable
private fun SubmitFormButton(
    handleSubmitForm: () -> Unit, modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = { handleSubmitForm() }, colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary
        ), modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text(text = stringResource(R.string.submit))
    }
}

@Composable
private fun ResetFormButton(
    handleResetForm: () -> Unit, modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = { handleResetForm() }, colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary
        ), modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text(text = stringResource(R.string.reset))
    }
}
