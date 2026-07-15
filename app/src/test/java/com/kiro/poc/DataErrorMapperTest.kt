package com.kiro.poc

import androidx.test.core.app.ApplicationProvider
import com.kiro.poc.data.error.DataError
import com.kiro.poc.presentation.mapper.toUiText
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * [DataError.toUiText] returns a [com.kiro.poc.common.UiText], which only resolves to a real
 * string once given an Android [android.content.Context] (for string resources/formatting).
 * Robolectric provides that Context on the JVM, so these run without a device/emulator.
 */
@RunWith(RobolectricTestRunner::class)
class DataErrorMapperTest {

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()

    @Test
    fun noInternetMapsToTheNoInternetString() {
        val text = DataError.NoInternet.toUiText().asString(context)

        assertEquals(context.getString(R.string.error_no_internet), text)
    }

    @Test
    fun serializationErrorMapsToTheSerializationString() {
        val text = DataError.Serialization("ignored raw message").toUiText().asString(context)

        assertEquals(context.getString(R.string.error_serialization), text)
    }

    @Test
    fun unknownErrorUsesItsOwnMessageVerbatim() {
        val text = DataError.Unknown("Something exploded").toUiText().asString(context)

        assertEquals("Something exploded", text)
    }

    @Test
    fun unknownErrorWithNoMessageFallsBackToADefault() {
        val text = DataError.Unknown(message = null).toUiText().asString(context)

        assertEquals("Unknown error", text)
    }

    @Test
    fun networkErrorWithMessageUsesThatMessageVerbatim() {
        val text = DataError.Network(code = 500, message = "Server exploded").toUiText().asString(context)

        assertEquals("Server exploded", text)
    }

    @Test
    fun networkErrorWithoutMessageFormatsTheCodeIntoTheTemplate() {
        val text = DataError.Network(code = 404, message = null).toUiText().asString(context)

        assertEquals(context.getString(R.string.error_network, 404, "Unknown"), text)
    }
}
