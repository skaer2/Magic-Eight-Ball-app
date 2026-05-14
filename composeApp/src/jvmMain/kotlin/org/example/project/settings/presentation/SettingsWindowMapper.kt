package org.example.project.settings.presentation

class SettingsWindowMapper {
    //TODO: rename
    fun getState(additionalAnswers: List<String>, configuration: SettingsWindowConfiguration) =
        SettingsWindowState(additionalAnswers, configuration.newAnswerValue)
}