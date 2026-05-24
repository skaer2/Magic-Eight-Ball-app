package org.example.project.main.presentation

import org.example.project.common.presentation.mapPredictionText

// Состояние основного экрана
data class MainWindowState(
    val ball: Ball,
    val question: String
) {
    // Начальное состояние приложения
    companion object {
        private val startingText = "Задай вопрос и нажми на кнопку готово"
        val INITIAL = MainWindowState(ball = Ball.Showing(mapPredictionText(startingText)), question = "")
    }
}

// Возможные взаимодействия с основным экраном
sealed interface Ball {
    data object Shaking : Ball
    data class Showing(
        val text: String
    ) : Ball
}