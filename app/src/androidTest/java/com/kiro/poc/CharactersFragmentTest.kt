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
import com.kiro.poc.fakes.FakeAppComponent
import com.kiro.poc.fakes.FakeCharacterRepository
import com.kiro.poc.fakes.TestCharacters
import com.kiro.poc.presentation.MainActivity
import com.kiro.poc.presentation.MainApp
import com.kiro.poc.util.ConditionIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharactersFragmentTest {

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

    private fun launchAndAwaitList(expectedItemCount: Int) {
        scenario = ActivityScenario.launch(MainActivity::class.java)

        lateinit var recyclerView: RecyclerView
        scenario!!.onActivity { activity ->
            recyclerView = activity.findViewById(R.id.charactersRecyclerView)
        }
        val idling = ConditionIdlingResource("charactersRecyclerViewPopulated") {
            (recyclerView.adapter?.itemCount ?: 0) >= expectedItemCount
        }
        idlingResource = idling
        IdlingRegistry.getInstance().register(idling)
    }

    @Test
    fun displaysCharacterListFromRepository() {
        launchAndAwaitList(TestCharacters.sampleList().size)

        onView(withId(R.id.charactersRecyclerView)).check(matches(isDisplayed()))
        onView(withText("Rick Sanchez")).check(matches(isDisplayed()))
        onView(withText("Morty Smith")).check(matches(isDisplayed()))
        onView(withText("Summer Smith")).check(matches(isDisplayed()))
    }

    @Test
    fun clickingCharacterNavigatesToDetailsScreen() {
        launchAndAwaitList(TestCharacters.sampleList().size)

        onView(withText("Rick Sanchez")).perform(click())

        onView(withId(R.id.nameTv)).check(matches(withText("Rick Sanchez")))
        onView(withId(R.id.statusSpeciesTv)).check(matches(withText("Alive - Human")))
        onView(withId(R.id.locationTv)).check(matches(withText("Earth (C-137)")))
    }

    @Test
    fun groupedViewFabNavigatesToCharacterGroupsScreen() {
        launchAndAwaitList(TestCharacters.sampleList().size)

        onView(withId(R.id.groupedViewFab)).perform(click())

        onView(withId(R.id.characterGroupsRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun rowsViewFabNavigatesToCharacterRowsScreen() {
        launchAndAwaitList(TestCharacters.sampleList().size)

        onView(withId(R.id.rowsViewFab)).perform(click())

        onView(withId(R.id.characterRowsRecyclerView)).check(matches(isDisplayed()))
    }
}
