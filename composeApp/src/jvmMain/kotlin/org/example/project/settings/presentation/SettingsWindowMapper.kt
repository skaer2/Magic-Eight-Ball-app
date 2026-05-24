package org.example.project.settings.presentation

// Подготовка данных для экрана настроек
class SettingsWindowMapper {
    fun getState(answers: List<String>, configuration: SettingsWindowConfiguration) =
        SettingsWindowState(answers, configuration.newAnswerValue)
}