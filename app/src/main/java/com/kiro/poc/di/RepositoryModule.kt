package com.kiro.poc.di

import com.kiro.poc.data.repository.CharacterRepositoryImpl
import com.kiro.poc.domain.repository.CharacterRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository
}
