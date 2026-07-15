package com.kiro.poc

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kiro.poc.data.error.DataError
import com.kiro.poc.fakes.FakeAppComponent
import com.kiro.poc.fakes.FakeCharacterRepository
import com.kiro.poc.presentation.MainActivity
import com.kiro.poc.presentation.MainApp
import com.kiro.poc.util.ConditionIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterRowsFragmentTest {

    private lateinit var fakeRepository: FakeCharacterRepository
    private var scenario: ActivityScenario<MainActivity>? = null
    private val registeredIdlingResources = mutableListOf<ConditionIdlingResource>()

    @Before
    fun setUp() {
        fakeRepository = FakeCharacterRepository()
        val app = ApplicationProvider.getApplicationContext<MainApp>()
        app.appComponent = FakeAppComponent(fakeRepository)
    }

    @After
    fun tearDown() {
        registeredIdlingResources.forEach { IdlingRegistry.getInstance().unregister(it) }
        registeredIdlingResources.clear()
        scenario?.close()
    }

    private fun register(resource: ConditionIdlingResource) {
        registeredIdlingResources += resource
        IdlingRegistry.getInstance().register(resource)
    }

    // 2 Alive + 1 Dead character -> 2 rows (Alive row, Dead row)
    private fun navigateToRowsScreenAndAwaitRows(expectedRowCount: Int = 2): RecyclerView {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.rowsViewFab)).perform(click())

        lateinit var recyclerView: RecyclerView
        scenario!!.onActivity { activity ->
            recyclerView = activity.findViewById(R.id.characterRowsRecyclerView)
        }
        register(
            ConditionIdlingResource("characterRowsPopulated") {
                (recyclerView.adapter?.itemCount ?: 0) >= expectedRowCount
            }
        )
        return recyclerView
    }

    // Each row's carousel is its own nested RecyclerView/adapter, diffed and bound
    // independently of the outer rows list - wait for it separately.
    private fun awaitNestedCarousel(rowsRecyclerView: RecyclerView, rowIndex: Int, expectedCount: Int) {
        register(
            ConditionIdlingResource("carouselRow$rowIndex") {
                val holder = rowsRecyclerView.findViewHolderForAdapterPosition(rowIndex)
                val nested = holder?.itemView?.findViewById<RecyclerView>(R.id.rowRecyclerView)
                (nested?.adapter?.itemCount ?: 0) >= expectedCount
            }
        )
    }

    @Test
    fun displaysRowsGroupedByStatus() {
        navigateToRowsScreenAndAwaitRows()

        onView(withId(R.id.characterRowsRecyclerView)).check(matches(isDisplayed()))
        onView(withText("Alive")).check(matches(isDisplayed()))
        onView(withText("Dead")).check(matches(isDisplayed()))
    }

    @Test
    fun clickingCharacterInCarouselNavigatesToDetailsScreen() {
        val recyclerView = navigateToRowsScreenAndAwaitRows()
        // Row 0 is "Alive" (Rick, Morty) per STATUS_ORDER
        awaitNestedCarousel(recyclerView, rowIndex = 0, expectedCount = 2)

        onView(withText("Rick Sanchez")).perform(click())

        onView(withId(R.id.nameTv)).check(matches(withText("Rick Sanchez")))
    }

    @Test
    fun displaysErrorMessageWhenRepositoryFails() {
        fakeRepository.setError(DataError.NoInternet)
        scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.rowsViewFab)).perform(click())

        onView(withId(R.id.errorTv))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.error_no_internet)))
    }
}
