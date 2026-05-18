package org.example.project.main.presentation

import org.example.project.common.presentation.mapPredictionText

data class MainWindowState(
    val ball: Ball,
    val question: String
) {
    companion object {
        private val startingText = "Задай вопрос и нажми на кнопку готово"
        val INITIAL = MainWindowState(ball = Ball.Showing(mapPredictionText(startingText)), question = "")
    }
}

sealed interface Ball {
    data object Shaking : Ball
    data class Showing(
        val text: String
    ) : Ball
}