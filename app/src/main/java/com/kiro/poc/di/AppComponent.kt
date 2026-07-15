package com.kiro.poc.di

import android.content.Context
import com.kiro.poc.presentation.MainActivity
import com.kiro.poc.presentation.characterDetails.CharacterDetailsFragment
import com.kiro.poc.presentation.characterGroups.CharacterGroupsFragment
import com.kiro.poc.presentation.characterRows.CharacterRowsFragment
import com.kiro.poc.presentation.characters.CharactersFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: CharactersFragment)
    fun inject(fragment: CharacterDetailsFragment)
    fun inject(fragment: CharacterGroupsFragment)
    fun inject(fragment: CharacterRowsFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}
