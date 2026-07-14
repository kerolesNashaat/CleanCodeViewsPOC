package com.kiro.poc.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kiro.poc.common.Resource
import com.kiro.poc.data.mapper.toDomain
import com.kiro.poc.data.remote.ApiService
import com.kiro.poc.data.remote.safeApiCall
import com.kiro.poc.domain.model.Character
import com.kiro.poc.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CharacterRepository {

    override fun getCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharacterPagingSource(apiService) }
        ).flow
    }

    override fun getCharacterById(id: Int): Flow<Resource<Character>> = flow {
        val result = safeApiCall { apiService.getCharacterById(id) }
        when (result) {
            is Resource.Success -> emit(Resource.Success(result.data.toDomain()))
            is Resource.Error -> emit(result)
        }
    }

    override fun getCharacterList(page: Int): Flow<Resource<List<Character>>> = flow {
        val result = safeApiCall { apiService.getCharacters(page) }
        when (result) {
            is Resource.Success -> emit(Resource.Success(result.data.results.orEmpty().toDomain()))
            is Resource.Error -> emit(result)
        }
    }
}
