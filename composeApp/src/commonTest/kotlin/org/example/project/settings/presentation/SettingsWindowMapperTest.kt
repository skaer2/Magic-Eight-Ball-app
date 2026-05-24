package org.example.project.settings.presentation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
//import kotlin.test.Test
//import kotlin.test.assertEquals

class SettingsWindowMapperTest {

    @Test
    fun test() {
        // Создание тестируемого экземпляра маппера
        val mapper = SettingsWindowMapper()

        // Инициализация тестовых данных
        val answers = listOf("Answer1", "Answer2", "Answer3")
        val newAnswerValue = "newAnswerValue"
        val configuration = SettingsWindowConfiguration(newAnswerValue)
        // Формирование ожидаемого результата для проверки
        val expected = SettingsWindowState(answers, newAnswerValue)

        // Сравнение фактического результата с ожидаемым
        assertEquals(expected, mapper.getState(answers, configuration))
    }
}