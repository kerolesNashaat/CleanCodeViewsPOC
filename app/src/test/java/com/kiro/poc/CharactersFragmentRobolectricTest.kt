package com.kiro.poc

import android.os.Looper
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.kiro.poc.fakes.FakeAppComponent
import com.kiro.poc.fakes.FakeCharacterRepository
import com.kiro.poc.fakes.TestCharacters
import com.kiro.poc.presentation.MainApp
import com.kiro.poc.presentation.characters.CharactersFragment
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

/**
 * Fragment-level test running entirely on the JVM: Robolectric provides a real (shadowed)
 * Android environment so the fragment, its data-bound layout and RecyclerView can be
 * exercised without an emulator/device, unlike the Espresso/Kaspresso equivalents of this
 * same scenario which need one.
 */
@RunWith(RobolectricTestRunner::class)
class CharactersFragmentRobolectricTest {

    private lateinit var fakeRepository: FakeCharacterRepository
    private var scenario: FragmentScenario<CharactersFragment>? = null

    @Before
    fun setUp() {
        fakeRepository = FakeCharacterRepository()
        val app = ApplicationProvider.getApplicationContext<MainApp>()
        app.appComponent = FakeAppComponent(fakeRepository)
    }

    @After
    fun tearDown() {
        scenario?.close()
    }

    // PagingDataAdapter diffs its list on a background executor before posting the update
    // back to the main thread, so the RecyclerView isn't populated the instant the fragment
    // is launched - poll until it is, idling the (paused-by-default) Robolectric main looper
    // between attempts so any posted callback actually runs.
    private fun waitUntil(timeoutMs: Long = 2_000, condition: () -> Boolean) {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (!condition()) {
            shadowOf(Looper.getMainLooper()).idle()
            check(System.currentTimeMillis() <= deadline) { "Condition not met within ${timeoutMs}ms" }
            Thread.sleep(20)
        }
    }

    private fun recyclerViewItemCount(): Int {
        var count = 0
        scenario!!.onFragment { fragment ->
            val recyclerView = fragment.requireView().findViewById<RecyclerView>(R.id.charactersRecyclerView)
            count = recyclerView.adapter?.itemCount ?: 0
        }
        return count
    }

    @Test
    fun displaysCharacterListFromRepository() {
        scenario = launchFragmentInContainer<CharactersFragment>(themeResId = R.style.Theme_CleanCodeViewsPOC)

        waitUntil { recyclerViewItemCount() >= TestCharacters.sampleList().size }

        assertTrue(recyclerViewItemCount() == TestCharacters.sampleList().size)
    }
}
