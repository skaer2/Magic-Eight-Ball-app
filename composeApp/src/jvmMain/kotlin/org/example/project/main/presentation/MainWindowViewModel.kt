package org.example.project.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.common.presentation.Constants
import org.example.project.settings.data.AnswersDataSource

class MainWindowViewModel : ViewModel() {
    private val answersDataSource = AnswersDataSource.instance

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

    //TODO: move to file
    private fun mapPredictionText(text: String): String {
        var boundedText = text.take(Constants.DEFAULT_ANSWER_LENGTH)
        val currentBoundaries = PredictionTextBoundaries.first {
            boundedText.length in it.first
        }
        val lines = currentBoundaries.second.map {
            val line = boundedText.take(it)
            boundedText = boundedText.drop(it)
            line
        }
        return lines.joinToString("\n")
    }
}

private val PredictionTextBoundaries = listOf(
    0..8 to listOf(8),
    9..15 to listOf(9, 6),
    16..24 to listOf(11, 8, 5),
    25..30 to listOf(12, 9, 6, 3),
    31..40 to listOf(14, 11, 8, 5, 2),
)
