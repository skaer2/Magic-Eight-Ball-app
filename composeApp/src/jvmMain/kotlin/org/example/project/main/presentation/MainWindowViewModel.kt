package org.example.project.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.common.presentation.Constants
import org.example.project.common.presentation.mapPredictionText
import org.example.project.settings.data.AnswersDataSource

class MainWindowViewModel(private val answersDataSource: AnswersDataSource) : ViewModel() {
//    private val answersDataSource = AnswersDataSource.instance

    private val _state = MutableStateFlow(MainWindowState.INITIAL)

    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            answersDataSource.init()
        }
    }

    fun onEvent(event: MainWindowEvent) {
        when (event) {
            MainWindowEvent.OnButtonPressed -> {
                _state.update { it.copy(ball = Ball.Shaking) }
            }

            is MainWindowEvent.OnQuestionChanged -> {
                _state.update { it.copy(question = event.question) }
            }

            MainWindowEvent.OnShakingEnd -> {
                viewModelScope.launch {
                    _state.update { it.copy(ball = Ball.Showing(mapPredictionText(getPredictionText()))) }
                }
            }
        }
    }


    suspend fun getPredictionText(): String {
        val answers = answersDataSource.observe().first()
        return answers.random()
    }

}

