package org.example.project.settings.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import magiceight_ball.composeapp.generated.resources.Res
import magiceight_ball.composeapp.generated.resources.check_icon
import magiceight_ball.composeapp.generated.resources.delete_icon
import org.example.project.settings.presentation.SettingsWindowEvent
import org.example.project.settings.presentation.SettingsWindowState
import org.example.project.settings.presentation.SettingsWindowViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsWindow(onBack: () -> Unit) {
    val viewModel = koinViewModel<SettingsWindowViewModel>()
    val state: SettingsWindowState by viewModel.state.collectAsStateWithLifecycle()
    SettingsWindowContent(state, viewModel::onEvent, onBack)
}

@Composable
private fun SettingsWindowContent(state: SettingsWindowState, onEvent: (SettingsWindowEvent) -> Unit, onBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.5f).padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier.weight(1f).verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    state.answers.forEachIndexed { index, answer ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(5.dp))
                        ) {
                            Text(
                                text = answer,
                                modifier = Modifier.weight(1f).padding(start = 5.dp),
                            )

                            IconButton(
                                onClick = {
                                    onEvent(SettingsWindowEvent.OnAnswerRemoved(index))
                                }
                            ) {
                                Icon(painterResource(Res.drawable.delete_icon), null, tint = Color.Unspecified)
                            }
                        }
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = state.newAnswerValue,
                    onValueChange = {
                        onEvent(SettingsWindowEvent.OnNewAnswerChanged(it))
                    },
                    modifier = Modifier.weight(1f).onPreviewKeyEvent{ keyEvent ->
                        if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyUp) {
                            onEvent(SettingsWindowEvent.OnNewAnswerAdded(state.newAnswerValue))
                            true
                        } else {
                            false
                        }
                    },
                    singleLine = true,
                )
                IconButton(
                    onClick = {
                        onEvent(SettingsWindowEvent.OnNewAnswerAdded(state.newAnswerValue))
                    }
                ) {
                    Icon(painterResource(Res.drawable.check_icon), null, tint = Color.Unspecified)
                }
            }
        }

        Button(
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
            onClick = onBack,
        ) {
            Text("Назад")
        }
    }
}
