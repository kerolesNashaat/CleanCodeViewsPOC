package com.kiro.poc.fakes

import androidx.paging.PagingData
import com.kiro.poc.common.Resource
import com.kiro.poc.data.error.DataError
import com.kiro.poc.domain.model.Character
import com.kiro.poc.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * In-memory [CharacterRepository] double for JVM tests. Emits synchronously so ViewModel
 * `init` blocks resolve before a test observes their LiveData/Flow.
 */
class FakeCharacterRepository(
    private var characters: List<Character> = TestCharacters.sampleList()
) : CharacterRepository {

    private var errorToReturn: DataError? = null

    fun setCharacters(newCharacters: List<Character>) {
        characters = newCharacters
    }

    fun setError(error: DataError?) {
        errorToReturn = error
    }

    override fun getCharacters(): Flow<PagingData<Character>> =
        flowOf(PagingData.from(characters))

    override fun getCharacterById(id: Int): Flow<Resource<Character>> {
        errorToReturn?.let { return flowOf(Resource.Error(it)) }
        val character = characters.find { it.id == id }
            ?: return flowOf(Resource.Error(DataError.Unknown("Character $id not found")))
        return flowOf(Resource.Success(character))
    }

    override fun getCharacterList(page: Int): Flow<Resource<List<Character>>> {
        errorToReturn?.let { return flowOf(Resource.Error(it)) }
        return flowOf(Resource.Success(characters))
    }
}
