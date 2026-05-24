package org.example.project.settings.presentation

sealed interface SettingsWindowEvent {
    // Изменение текста в поле ввода
    data class OnNewAnswerChanged(
        val newAnswerValue: String,
    ) : SettingsWindowEvent

    // Добавление нового ответа в список
    data class OnNewAnswerAdded(
        val newAnswer: String,
    ) : SettingsWindowEvent

    // Удаление существующего ответа по его индексу
    data class OnAnswerRemoved(
        val index: Int,
    ) : SettingsWindowEvent
}
