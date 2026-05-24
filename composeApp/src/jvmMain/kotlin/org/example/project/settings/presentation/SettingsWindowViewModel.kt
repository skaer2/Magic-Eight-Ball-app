package org.example.project.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.project.common.presentation.Constants
import org.example.project.settings.data.AnswersDataSource

// Управление состоянием экрана настроек
class SettingsWindowViewModel(
    private val stateMapper: SettingsWindowMapper,
    private val answersDataSource: AnswersDataSource,
) : ViewModel() {

    private val configurationFlow = MutableStateFlow(SettingsWindowConfiguration())

    private var answerAddJob: Job? = null
    private var answerRemoveJob: Job? = null

    // Объединение потока данных из источника и текущей конфигурации в единый Flow объект
    val state = combine(
        answersDataSource.observe(),
        configurationFlow
    ) { additionalAnswers, configuration ->
        stateMapper.getState(additionalAnswers, configuration)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        stateMapper.getState(emptyList(), configurationFlow.value)
    )

    fun onEvent(event: SettingsWindowEvent) {
        when (event) {
            is SettingsWindowEvent.OnNewAnswerAdded -> {
                val trimmedValue = event.newAnswer.trim()
                if (answerAddJob?.isActive == true || trimmedValue.isBlank()) return
                answerAddJob = viewModelScope.launch {
                    answersDataSource.addAnswer(trimmedValue)
                    configurationFlow.update {
                        it.copy(newAnswerValue = "")
                    }
                }
            }

            is SettingsWindowEvent.OnNewAnswerChanged -> {
                if (event.newAnswerValue.length <= Constants.DEFAULT_ANSWER_LENGTH) {
                    configurationFlow.update {
                        it.copy(newAnswerValue = event.newAnswerValue)
                    }
                }
            }

            is SettingsWindowEvent.OnAnswerRemoved -> {
                if (answerRemoveJob?.isActive == true) return
                answerRemoveJob = viewModelScope.launch {
                    answersDataSource.removeAnswer(event.index)
                }
            }
        }
    }
}