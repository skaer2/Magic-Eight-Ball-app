package org.example.project.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import kotlinx.serialization.serializer
import org.junit.Before
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
//import org.junit.jupiter.api.BeforeAll
//import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.util.prefs.Preferences
import kotlin.test.Test

class AnswersDataSourceTest{
    private val preferences: Preferences = mock()
    private val initFlagPreferences: Preferences = mock()
    private val dataStoreFactory: DataStoreFactory = mock()
    private val dataStore: DataStore<List<String>> = mock()
//    private val dataSource = AnswersDataSource(preferences, dataStoreFactory)

    @Before
    fun setup(){
        Mockito.`when`(preferences.node(anyString())).thenReturn(initFlagPreferences)
        Mockito.`when`(dataStoreFactory.create(serializer = any<Serializer<List<String>>>(), produceFile = any() ))
            .thenReturn(dataStore)
    }

//    @Test
//    fun
}