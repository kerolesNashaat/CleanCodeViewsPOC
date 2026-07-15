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
class CharacterGroupsFragmentTest {

    private lateinit var fakeRepository: FakeCharacterRepository
    private var scenario: ActivityScenario<MainActivity>? = null
    private var idlingResource: ConditionIdlingResource? = null

    @Before
    fun setUp() {
        fakeRepository = FakeCharacterRepository()
        val app = ApplicationProvider.getApplicationContext<MainApp>()
        app.appComponent = FakeAppComponent(fakeRepository)
    }

    @After
    fun tearDown() {
        idlingResource?.let { IdlingRegistry.getInstance().unregister(it) }
        scenario?.close()
    }

    // 2 Alive + 1 Dead character -> 2 headers + 3 character rows = 5 list items
    private fun navigateToGroupsScreenAndAwaitItems(expectedItemCount: Int = 5) {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.groupedViewFab)).perform(click())

        lateinit var recyclerView: RecyclerView
        scenario!!.onActivity { activity ->
            recyclerView = activity.findViewById(R.id.characterGroupsRecyclerView)
        }
        val idling = ConditionIdlingResource("characterGroupsPopulated") {
            (recyclerView.adapter?.itemCount ?: 0) >= expectedItemCount
        }
        idlingResource = idling
        IdlingRegistry.getInstance().register(idling)
    }

    @Test
    fun displaysCharactersGroupedByStatusWithHeaders() {
        navigateToGroupsScreenAndAwaitItems()

        onView(withText("Alive")).check(matches(isDisplayed()))
        onView(withText("Dead")).check(matches(isDisplayed()))
        onView(withText("Rick Sanchez")).check(matches(isDisplayed()))
        onView(withText("Summer Smith")).check(matches(isDisplayed()))
    }

    @Test
    fun clickingCharacterInGroupNavigatesToDetailsScreen() {
        navigateToGroupsScreenAndAwaitItems()

        onView(withText("Rick Sanchez")).perform(click())

        onView(withId(R.id.nameTv)).check(matches(withText("Rick Sanchez")))
    }

    @Test
    fun displaysErrorMessageWhenRepositoryFails() {
        fakeRepository.setError(DataError.NoInternet)
        scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.groupedViewFab)).perform(click())

        onView(withId(R.id.errorTv))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.error_no_internet)))
    }
}
