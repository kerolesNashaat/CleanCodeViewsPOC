package com.kiro.poc

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kiro.poc.data.error.DataError
import com.kiro.poc.fakes.FakeAppComponent
import com.kiro.poc.fakes.FakeCharacterRepository
import com.kiro.poc.presentation.MainActivity
import com.kiro.poc.presentation.MainApp
import com.kiro.poc.screen.CharacterDetailsScreen
import com.kiro.poc.screen.CharacterRowsScreen
import com.kiro.poc.screen.CharactersScreen
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterRowsScreenTest : TestCase() {

    private lateinit var fakeRepository: FakeCharacterRepository
    private var scenario: ActivityScenario<MainActivity>? = null

    private fun launchOnRowsScreen() {
        fakeRepository = FakeCharacterRepository()
        val app = ApplicationProvider.getApplicationContext<MainApp>()
        app.appComponent = FakeAppComponent(fakeRepository)
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    // 2 Alive + 1 Dead character -> 2 rows: Alive (Rick, Morty), Dead (Summer)
    @Test
    fun displaysRowsGroupedByStatus() = run {
        before {
            launchOnRowsScreen()
        }.after {
            scenario?.close()
        }.run {
            step("Navigate to the rows screen") {
                CharactersScreen { rowsViewFab.click() }
            }
            step("Rows are shown in Alive/Dead order") {
                CharacterRowsScreen {
                    recyclerView.hasSize(2)
                    recyclerView.childAt<CharacterRowsScreen.RowItem>(0) {
                        title.hasText("Alive")
                    }
                    recyclerView.childAt<CharacterRowsScreen.RowItem>(1) {
                        title.hasText("Dead")
                    }
                }
            }
        }
    }

    @Test
    fun clickingCharacterInCarouselNavigatesToDetailsScreen() = run {
        before {
            launchOnRowsScreen()
        }.after {
            scenario?.close()
        }.run {
            step("Navigate to the rows screen") {
                CharactersScreen { rowsViewFab.click() }
            }
            step("Open Rick Sanchez from the Alive carousel") {
                CharacterRowsScreen {
                    carouselCharacter("Rick Sanchez").click()
                }
            }
            step("Details screen shows the selected character") {
                CharacterDetailsScreen {
                    nameText.hasText("Rick Sanchez")
                }
            }
        }
    }

    @Test
    fun displaysErrorMessageWhenRepositoryFails() = run {
        before {
            fakeRepository = FakeCharacterRepository()
            fakeRepository.setError(DataError.NoInternet)
            val app = ApplicationProvider.getApplicationContext<MainApp>()
            app.appComponent = FakeAppComponent(fakeRepository)
            scenario = ActivityScenario.launch(MainActivity::class.java)
        }.after {
            scenario?.close()
        }.run {
            step("Navigate to the rows screen") {
                CharactersScreen { rowsViewFab.click() }
            }
            step("Error message is shown") {
                CharacterRowsScreen {
                    errorText {
                        isVisible()
                        hasText(R.string.error_no_internet)
                    }
                }
            }
        }
    }
}
