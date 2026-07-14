package com.kiro.poc.domain.useCase

import com.kiro.poc.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterListUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(page: Int) = repository.getCharacterList(page)
}
