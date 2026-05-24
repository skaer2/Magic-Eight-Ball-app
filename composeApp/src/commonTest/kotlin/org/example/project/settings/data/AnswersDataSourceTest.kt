package org.example.project.settings.data

//import org.junit.Before
//import org.junit.jupiter.api.BeforeAll
//import org.junit.jupiter.api.BeforeEach
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.example.project.common.data.ListStringSerializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import java.util.prefs.Preferences

//import kotlin.test.Test

class AnswersDataSourceTest {
    // Создание заглушек для зависимостей
    private val preferences: Preferences = mock()
    private val initFlagPreferences: Preferences = mock()
    private val dataStore: DataStore<List<String>> = mock()

    private lateinit var dataSource: AnswersDataSource

    @BeforeEach
    fun setup() {
        // Настройка мока preferences перед каждым тестом
        Mockito.`when`(preferences.node(anyString())).thenReturn(initFlagPreferences)
        dataSource = AnswersDataSource(preferences, dataStore)
    }

    @Test
    fun testInitWhenFirstLaunch() = runTest {
        // Моделирование первого запуска приложения
        Mockito.`when`(initFlagPreferences.getBoolean(anyString(), any())).thenReturn(false)
        dataSource.init()
        // Проверка, что данные были обновлены
        Mockito.verify(dataStore).updateData(any())
    }

    @Test
    fun testInitWhenSecondLaunch() = runTest {
        // Моделирование повторного запуска приложения
        Mockito.`when`(initFlagPreferences.getBoolean(anyString(), any())).thenReturn(true)
        dataSource.init()
        // Проверка отсутствия взаимодействия с хранилищем
        Mockito.verifyNoInteractions(dataStore)
    }
}