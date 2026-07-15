package com.kiro.poc.presentation.characterGroups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiro.poc.common.Resource
import com.kiro.poc.common.UiText
import com.kiro.poc.domain.useCase.GetCharacterListUseCase
import com.kiro.poc.presentation.mapper.toUiText
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class CharacterGroupsViewModel @Inject constructor(
    private val getCharacterListUseCase: GetCharacterListUseCase
) : ViewModel() {

    private val _items = MutableLiveData<List<CharacterListItem>>(emptyList())
    val items: LiveData<List<CharacterListItem>> = _items

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<UiText?>()
    val error: LiveData<UiText?> = _error

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        _isLoading.value = true
        getCharacterListUseCase(page = 1)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _items.value = result.data.toCharacterListItems()
                        _isLoading.value = false
                        _error.value = null
                    }
                    is Resource.Error -> {
                        _error.value = result.error.toUiText()
                        _isLoading.value = false
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
