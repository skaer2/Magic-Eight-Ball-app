package org.example.project.settings.presentation

class SettingsWindowMapper {
    fun getState(answers: List<String>, configuration: SettingsWindowConfiguration) =
        SettingsWindowState(answers, configuration.newAnswerValue)
}