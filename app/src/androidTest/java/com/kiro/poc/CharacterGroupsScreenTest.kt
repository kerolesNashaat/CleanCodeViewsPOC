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
import com.kiro.poc.screen.CharacterGroupsScreen
import com.kiro.poc.screen.CharactersScreen
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterGroupsScreenTest : TestCase() {

    private lateinit var fakeRepository: FakeCharacterRepository
    private var scenario: ActivityScenario<MainActivity>? = null

    private fun launchOnGroupsScreen() {
        fakeRepository = FakeCharacterRepository()
        val app = ApplicationProvider.getApplicationContext<MainApp>()
        app.appComponent = FakeAppComponent(fakeRepository)
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    // 2 Alive + 1 Dead character -> Header(Alive), Rick, Morty, Header(Dead), Summer
    @Test
    fun displaysCharactersGroupedByStatusWithHeaders() = run {
        before {
            launchOnGroupsScreen()
        }.after {
            scenario?.close()
        }.run {
            step("Navigate to the grouped-by-status screen") {
                CharactersScreen { groupedViewFab.click() }
            }
            step("Groups are shown in Alive/Dead order with their characters") {
                CharacterGroupsScreen {
                    recyclerView.hasSize(5)
                    recyclerView.childAt<CharacterGroupsScreen.HeaderItem>(0) {
                        hasTitle("Alive")
                    }
                    recyclerView.childAt<CharacterGroupsScreen.CharacterRowItem>(1) {
                        name.hasText("Rick Sanchez")
                    }
                    recyclerView.childAt<CharacterGroupsScreen.CharacterRowItem>(2) {
                        name.hasText("Morty Smith")
                    }
                    recyclerView.childAt<CharacterGroupsScreen.HeaderItem>(3) {
                        hasTitle("Dead")
                    }
                    recyclerView.childAt<CharacterGroupsScreen.CharacterRowItem>(4) {
                        name.hasText("Summer Smith")
                    }
                }
            }
        }
    }

    @Test
    fun clickingCharacterInGroupNavigatesToDetailsScreen() = run {
        before {
            launchOnGroupsScreen()
        }.after {
            scenario?.close()
        }.run {
            step("Navigate to the grouped-by-status screen") {
                CharactersScreen { groupedViewFab.click() }
            }
            step("Open the first character in the Alive group") {
                CharacterGroupsScreen {
                    recyclerView.childAt<CharacterGroupsScreen.CharacterRowItem>(1) {
                        click()
                    }
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
            step("Navigate to the grouped-by-status screen") {
                CharactersScreen { groupedViewFab.click() }
            }
            step("Error message is shown") {
                CharacterGroupsScreen {
                    errorText {
                        isVisible()
                        hasText(R.string.error_no_internet)
                    }
                }
            }
        }
    }
}
