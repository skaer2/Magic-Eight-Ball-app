package org.example.project.settings.presentation

//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsWindowMapperTest {

    @Test
    fun test() {
        val mapper = SettingsWindowMapper()

        val answers = listOf("Answer1", "Answer2", "Answer3")
        val newAnswerValue = "newAnswerValue"
        val configuration = SettingsWindowConfiguration(newAnswerValue)
        val expected = SettingsWindowState(answers, newAnswerValue)

        assertEquals(expected, mapper.getState(answers, configuration))
    }
}