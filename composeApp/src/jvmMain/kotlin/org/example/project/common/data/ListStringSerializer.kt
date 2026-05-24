package org.example.project.common.data

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

private const val SERIALIZER_SEPARATOR = "|"

object ListStringSerializer : Serializer<List<String>> {
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