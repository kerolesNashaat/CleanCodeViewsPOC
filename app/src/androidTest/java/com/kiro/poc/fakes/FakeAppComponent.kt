package com.kiro.poc.fakes

import androidx.lifecycle.ViewModelProvider
import com.kiro.poc.di.AppComponent
import com.kiro.poc.domain.repository.CharacterRepository
import com.kiro.poc.presentation.MainActivity
import com.kiro.poc.presentation.characterDetails.CharacterDetailsFragment
import com.kiro.poc.presentation.characterGroups.CharacterGroupsFragment
import com.kiro.poc.presentation.characterRows.CharacterRowsFragment
import com.kiro.poc.presentation.characters.CharactersFragment

/**
 * Test-only [AppComponent] implementation. Assigning an instance to `MainApp.appComponent`
 * before a fragment's `onCreate()` runs makes every fragment in the nav graph inject a
 * [FakeViewModelFactory] backed by the given repository, with no Dagger/KSP involved.
 */
class FakeAppComponent(repository: CharacterRepository) : AppComponent {

    private val viewModelFactory: ViewModelProvider.Factory = FakeViewModelFactory(repository)

    override fun inject(activity: MainActivity) = Unit

    override fun inject(fragment: CharactersFragment) {
        fragment.viewModelFactory = viewModelFactory
    }

    override fun inject(fragment: CharacterDetailsFragment) {
        fragment.viewModelFactory = viewModelFactory
    }

    override fun inject(fragment: CharacterGroupsFragment) {
        fragment.viewModelFactory = viewModelFactory
    }

    override fun inject(fragment: CharacterRowsFragment) {
        fragment.viewModelFactory = viewModelFactory
    }
}
