package com.kiro.poc

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kiro.poc.fakes.FakeAppComponent
import com.kiro.poc.fakes.FakeCharacterRepository
import com.kiro.poc.fakes.TestCharacters
import com.kiro.poc.presentation.MainActivity
import com.kiro.poc.presentation.MainApp
import com.kiro.poc.screen.CharacterDetailsScreen
import com.kiro.poc.screen.CharacterGroupsScreen
import com.kiro.poc.screen.CharacterRowsScreen
import com.kiro.poc.screen.CharactersScreen
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharactersScreenTest : TestCase() {

    private lateinit var fakeRepository: FakeCharacterRepository
    private var scenario: ActivityScenario<MainActivity>? = null

    // MainApp.appComponent must be swapped before MainActivity's fragments run onCreate(),
    // so the fake is installed in `before` and the activity is launched manually here rather
    // than via an activityScenarioRule, which would launch before `before` runs.
    private fun launchWithFake() {
        fakeRepository = FakeCharacterRepository()
        val app = ApplicationProvider.getApplicationContext<MainApp>()
        app.appComponent = FakeAppComponent(fakeRepository)
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun displaysCharacterListFromRepository() = run {
        before {
            launchWithFake()
        }.after {
            scenario?.close()
        }.run {
            step("Character list is populated from the repository") {
                CharactersScreen {
                    recyclerView.hasSize(TestCharacters.sampleList().size)
                    recyclerView.childAt<CharactersScreen.CharacterItem>(0) {
                        name.hasText("Rick Sanchez")
                        status.hasText("Alive - Human")
                    }
                    recyclerView.childAt<CharactersScreen.CharacterItem>(2) {
                        name.hasText("Summer Smith")
                    }
                }
            }
        }
    }

    @Test
    fun clickingCharacterNavigatesToDetailsScreen() = run {
        before {
            launchWithFake()
        }.after {
            scenario?.close()
        }.run {
            step("Open the first character's details") {
                CharactersScreen {
                    recyclerView.childAt<CharactersScreen.CharacterItem>(0) {
                        click()
                    }
                }
            }
            step("Details screen shows the selected character") {
                CharacterDetailsScreen {
                    nameText.hasText("Rick Sanchez")
                    statusSpeciesText.hasText("Alive - Human")
                    locationText.hasText("Earth (C-137)")
                }
            }
        }
    }

    @Test
    fun groupedViewFabNavigatesToCharacterGroupsScreen() = run {
        before {
            launchWithFake()
        }.after {
            scenario?.close()
        }.run {
            step("Tap the grouped-view FAB") {
                CharactersScreen {
                    groupedViewFab.click()
                }
            }
            step("Character groups screen is shown") {
                CharacterGroupsScreen {
                    recyclerView.isVisible()
                }
            }
        }
    }

    @Test
    fun rowsViewFabNavigatesToCharacterRowsScreen() = run {
        before {
            launchWithFake()
        }.after {
            scenario?.close()
        }.run {
            step("Tap the rows-view FAB") {
                CharactersScreen {
                    rowsViewFab.click()
                }
            }
            step("Character rows screen is shown") {
                CharacterRowsScreen {
                    recyclerView.isVisible()
                }
            }
        }
    }
}
