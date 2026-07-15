package com.kiro.poc

import android.os.Looper
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.kiro.poc.data.error.DataError
import com.kiro.poc.fakes.FakeAppComponent
import com.kiro.poc.fakes.FakeCharacterRepository
import com.kiro.poc.presentation.MainApp
import com.kiro.poc.presentation.characterGroups.CharacterGroupsFragment
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class CharacterGroupsFragmentRobolectricTest {

    private var scenario: FragmentScenario<CharacterGroupsFragment>? = null

    @After
    fun tearDown() {
        scenario?.close()
    }

    private fun launchWith(repository: FakeCharacterRepository) {
        val app = ApplicationProvider.getApplicationContext<MainApp>()
        app.appComponent = FakeAppComponent(repository)
        scenario = launchFragmentInContainer<CharacterGroupsFragment>(themeResId = R.style.Theme_CleanCodeViewsPOC)
    }

    // ListAdapter diffs off the main thread too, so wait for the update the same way as
    // the Paging-based CharactersFragment test.
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
            val recyclerView = fragment.requireView().findViewById<RecyclerView>(R.id.characterGroupsRecyclerView)
            count = recyclerView.adapter?.itemCount ?: 0
        }
        return count
    }

    @Test
    fun displaysCharactersGroupedByStatusWithHeaders() {
        // 2 Alive + 1 Dead -> Header(Alive), Rick, Morty, Header(Dead), Summer = 5 items
        launchWith(FakeCharacterRepository())

        waitUntil { recyclerViewItemCount() >= 5 }

        assertEquals(5, recyclerViewItemCount())
    }

    @Test
    fun displaysErrorMessageWhenRepositoryFails() {
        val repository = FakeCharacterRepository()
        repository.setError(DataError.NoInternet)
        launchWith(repository)
        shadowOf(Looper.getMainLooper()).idle()

        scenario!!.onFragment { fragment ->
            val errorTv = fragment.requireView().findViewById<TextView>(R.id.errorTv)
            assertTrue(errorTv.isVisible)
            assertEquals(fragment.getString(R.string.error_no_internet), errorTv.text.toString())
        }
    }
}
