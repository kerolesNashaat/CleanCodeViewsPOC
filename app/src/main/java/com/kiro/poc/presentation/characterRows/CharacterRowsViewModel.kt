package com.kiro.poc.presentation.characterRows

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

class CharacterRowsViewModel @Inject constructor(
    private val getCharacterListUseCase: GetCharacterListUseCase
) : ViewModel() {

    private val _rows = MutableLiveData<List<CharacterRow>>(emptyList())
    val rows: LiveData<List<CharacterRow>> = _rows

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
                        _rows.value = result.data.toCharacterRows()
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
