package org.example.project.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.main.presentation.Ball
import org.example.project.main.presentation.MainWindowEvent
import org.example.project.main.presentation.MainWindowState
import org.example.project.main.presentation.MainWindowViewModel

@Composable
fun MainWindow(onSettingsNavigate: () -> Unit) {
    val viewModel: MainWindowViewModel = viewModel { MainWindowViewModel() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    MainWindowContent(state, viewModel::onEvent, onSettingsNavigate)
}

@Composable
private fun MainWindowContent(state: MainWindowState, onEvent: (MainWindowEvent) -> Unit, onSettingsNavigate: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Ball(
                    ball = state.ball,
                    onShakingEnd = {
                        onEvent(MainWindowEvent.OnShakingEnd)
                    }
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier.weight(1f).onPreviewKeyEvent{ keyEvent ->
                        if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyUp) {
                            onEvent(MainWindowEvent.OnButtonPressed)
                            true
                        } else {
                            false
                        }
                    },
                    value = state.question,
                    onValueChange = {
                        onEvent(MainWindowEvent.OnQuestionChanged(it))
                    },
                    placeholder = {
                        Text("Введите вопрос")
                    },
                    label = {
                        Text("Введите вопрос")
                    },
                    readOnly = state.ball is Ball.Shaking,
                    singleLine = true,
                )
                Button(onClick = {
                    onEvent(MainWindowEvent.OnButtonPressed)
                }) {
                    Text("Готово")
                }
            }
        }
        Button(
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
            onClick = {
                onSettingsNavigate()
            }
        ) {
            Text("Настройки")
        }
    }
}


