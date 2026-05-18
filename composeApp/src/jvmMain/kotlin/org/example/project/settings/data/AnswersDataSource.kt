package org.example.project.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.prefs.Preferences
import kotlin.String
import kotlin.collections.List

private const val SERIALIZER_SEPARATOR = "|"

private const val INIT_FLAG_ID = "defaults_initialized"

class AnswersDataSource(
    preferences: Preferences,
    dataStoreFactory: DataStoreFactory,
) {
    private val initFlagPrefs = preferences.node("initFlag")

    private val answersDataStore: DataStore<List<String>> = dataStoreFactory.create(
        serializer = ListStringSerializer,
        produceFile = { File(System.getProperty("java.io.tmpdir"), "magicEightBall.preferences_pb") }
    )

    suspend fun init() {
        if (!initFlagPrefs.getBoolean(INIT_FLAG_ID, false)) {
            answersDataStore.updateData {
                listOf(
                    "Да",
                    "Нет",
                    "Скорее всего да",
                    "Скорее всего нет",
                    "Возможно",
                    "Имеются перспективы",
                    "Вопрос задан неверно",
                )
            }

            initFlagPrefs.putBoolean(INIT_FLAG_ID, true)
        }
    }

    fun observe() = answersDataStore.data

    suspend fun addAnswer(newAnswer: String) {
        answersDataStore.updateData {
            it + newAnswer
        }
    }

    suspend fun removeAnswer(index: Int) {
        answersDataStore.updateData {
            it.toMutableList().apply { removeAt(index) }
        }
    }
}

private object ListStringSerializer : Serializer<List<String>> {
    override val defaultValue: List<String>
        get() = emptyList()

    override suspend fun readFrom(input: InputStream): List<String> {
        val answers = input.readBytes().decodeToString()
        return answers.split(SERIALIZER_SEPARATOR)
    }

    override suspend fun writeTo(t: List<String>, output: OutputStream) {
        output.write(
            t.joinToString(SERIALIZER_SEPARATOR).encodeToByteArray()
        )
    }
}
