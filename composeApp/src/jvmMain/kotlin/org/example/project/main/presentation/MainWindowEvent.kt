package org.example.project.main.presentation

sealed interface MainWindowEvent {
    data class OnQuestionChanged(
        val question: String
    ) : MainWindowEvent

    data object OnButtonPressed : MainWindowEvent

    data object OnShakingEnd : MainWindowEvent
}