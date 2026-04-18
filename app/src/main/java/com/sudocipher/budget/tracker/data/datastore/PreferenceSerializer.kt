package com.sudocipher.budget.tracker.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object PreferenceSerializer : Serializer<Preference> {

    override val defaultValue: Preference = Preference.DEFAULT

    override suspend fun readFrom(input: InputStream): Preference =
        try {
            Json.decodeFromString<Preference>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read Preference", serialization)
        }

    override suspend fun writeTo(t: Preference, output: OutputStream) {
        output.write(
            Json
                .encodeToString(t)
                .encodeToByteArray()
        )
    }

}