package com.kiro.poc

import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.kiro.poc.common.BindingAdaptersJava
import com.kiro.poc.common.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * These `@BindingAdapter` methods are wired into layouts via data binding and only ever run
 * against real Android views in production. Robolectric constructs real (shadowed) View/
 * TextView instances on the JVM so the adapter logic can be exercised without a device.
 */
@RunWith(RobolectricTestRunner::class)
class BindingAdaptersTest {

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()

    @Test
    fun setUiTextRendersDynamicStringOnTheTextView() {
        val textView = TextView(context)

        BindingAdaptersJava.setUiText(textView, UiText.DynamicString("Hello there"))

        assertEquals("Hello there", textView.text.toString())
    }

    @Test
    fun setUiTextWithNullLeavesTheTextViewUntouched() {
        val textView = TextView(context)
        textView.text = "unchanged"

        BindingAdaptersJava.setUiText(textView, null)

        assertEquals("unchanged", textView.text.toString())
    }

    @Test
    fun setStatusColorMarksAliveAsGreen() {
        val view = android.view.View(context)

        BindingAdaptersJava.setStatusColor(view, "Alive")

        assertEquals(android.graphics.Color.GREEN, (view.background as GradientDrawable).color?.defaultColor)
    }

    @Test
    fun setStatusColorMarksDeadAsRed() {
        val view = android.view.View(context)

        BindingAdaptersJava.setStatusColor(view, "Dead")

        assertEquals(android.graphics.Color.RED, (view.background as GradientDrawable).color?.defaultColor)
    }

    @Test
    fun setStatusColorMarksUnknownStatusAsGray() {
        val view = android.view.View(context)

        BindingAdaptersJava.setStatusColor(view, "unknown")

        assertEquals(android.graphics.Color.GRAY, (view.background as GradientDrawable).color?.defaultColor)
    }

    @Test
    fun setStatusColorWithNullLeavesBackgroundUnset() {
        val view = android.view.View(context)

        BindingAdaptersJava.setStatusColor(view, null)

        assertNull(view.background)
    }

    @Test
    fun setStatusColorProducesAnOvalShape() {
        val view = android.view.View(context)

        BindingAdaptersJava.setStatusColor(view, "Alive")

        assertTrue((view.background as GradientDrawable).shape == GradientDrawable.OVAL)
    }
}
