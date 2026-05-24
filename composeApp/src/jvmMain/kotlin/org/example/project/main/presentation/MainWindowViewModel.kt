package org.example.project.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.common.presentation.Constants
import org.example.project.common.presentation.mapPredictionText
import org.example.project.settings.data.AnswersDataSource

class MainWindowViewModel(private val answersDataSource: AnswersDataSource) : ViewModel() {

    // Внутреннее поле для управления состоянием, закрытое от доступа извне
    private val _state = MutableStateFlow(MainWindowState.INITIAL)

    // Публичный поток состояния, на который подписывается UI-слой
    val state = _state.asStateFlow()

    init {
        // Асинхронная инициализация источника данных при создании ViewModel
        viewModelScope.launch {
            answersDataSource.init()
        }
    }

    // Обработчик всех событий, поступающих от графического интерфейса
    fun onEvent(event: MainWindowEvent) {
        when (event) {
            // Реакция на нажатие кнопки получения предсказания
            MainWindowEvent.OnButtonPressed -> {
                _state.update { it.copy(ball = Ball.Shaking) }
            }

            // Обновление текста введенного пользователем
            is MainWindowEvent.OnQuestionChanged -> {
                _state.update { it.copy(question = event.question) }
            }

            // Обработка завершения анимации тряски шара
            MainWindowEvent.OnShakingEnd -> {
                viewModelScope.launch {
                    // Получение случайного ответа и его форматирование для корректного отображения
                    _state.update { it.copy(ball = Ball.Showing(mapPredictionText(getPredictionText()))) }
                }
            }
        }
    }


    suspend fun getPredictionText(): String {
        val answers = answersDataSource.observe().first()
        return answers.random()
    }

}

