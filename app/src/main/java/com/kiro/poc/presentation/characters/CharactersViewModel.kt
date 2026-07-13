package com.kiro.poc.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kiro.poc.domain.model.Character
import com.kiro.poc.domain.useCase.GetCharactersUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    val characters: Flow<PagingData<Character>> = getCharactersUseCase()
        .cachedIn(viewModelScope)
}
