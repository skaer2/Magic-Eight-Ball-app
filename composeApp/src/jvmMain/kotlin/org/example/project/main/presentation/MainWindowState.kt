package org.example.project.main.presentation

data class MainWindowState(
    val ball: Ball,
    val question: String
) {
    companion object {
        val INITIAL = MainWindowState(ball = Ball.Showing(""), question = "")
    }
}

sealed interface Ball {
    data object Shaking : Ball
    data class Showing(
        val text: String
    ) : Ball
}