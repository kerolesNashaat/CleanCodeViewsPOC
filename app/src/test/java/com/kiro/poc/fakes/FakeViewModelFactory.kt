package com.kiro.poc.fakes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kiro.poc.domain.repository.CharacterRepository
import com.kiro.poc.domain.useCase.GetCharacterByIdUseCase
import com.kiro.poc.domain.useCase.GetCharacterListUseCase
import com.kiro.poc.domain.useCase.GetCharactersUseCase
import com.kiro.poc.presentation.characterDetails.CharacterDetailsViewModel
import com.kiro.poc.presentation.characterGroups.CharacterGroupsViewModel
import com.kiro.poc.presentation.characterRows.CharacterRowsViewModel
import com.kiro.poc.presentation.characters.CharactersViewModel

/** Builds real ViewModels wired to a fake [CharacterRepository], bypassing Dagger entirely. */
class FakeViewModelFactory(private val repository: CharacterRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel: ViewModel = when (modelClass) {
            CharactersViewModel::class.java -> CharactersViewModel(GetCharactersUseCase(repository))
            CharacterDetailsViewModel::class.java -> CharacterDetailsViewModel(GetCharacterByIdUseCase(repository))
            CharacterGroupsViewModel::class.java -> CharacterGroupsViewModel(GetCharacterListUseCase(repository))
            CharacterRowsViewModel::class.java -> CharacterRowsViewModel(GetCharacterListUseCase(repository))
            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
        return viewModel as T
    }
}
