package com.kiro.poc.presentation.characterDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiro.poc.common.Resource
import com.kiro.poc.common.UiText
import com.kiro.poc.domain.model.Character
import com.kiro.poc.domain.useCase.GetCharacterByIdUseCase
import com.kiro.poc.presentation.mapper.toUiText
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase
) : ViewModel() {

    private val _character = MutableLiveData<Character?>()
    val character: LiveData<Character?> = _character

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<UiText?>()
    val error: LiveData<UiText?> = _error

    fun getCharacterById(id: Int) {
        _isLoading.value = true
        getCharacterByIdUseCase(id)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _character.value = result.data
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
