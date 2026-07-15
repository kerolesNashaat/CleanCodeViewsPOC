package com.kiro.poc.domain.repository

import androidx.paging.PagingData
import com.kiro.poc.common.Resource
import com.kiro.poc.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(): Flow<PagingData<Character>>
    fun getCharacterById(id: Int): Flow<Resource<Character>>
    fun getCharacterList(page: Int): Flow<Resource<List<Character>>>
}