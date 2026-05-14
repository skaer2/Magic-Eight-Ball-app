package org.example.project.settings.presentation

sealed interface SettingsWindowEvent {
    data class OnNewAnswerChanged(
        val newAnswerValue: String,
    ) : SettingsWindowEvent

    data class OnNewAnswerAdded(
        val newAnswer: String,
    ) : SettingsWindowEvent

    data class OnAnswerRemoved(
        val index: Int,
    ) : SettingsWindowEvent
}
