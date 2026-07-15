package com.kiro.poc.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kiro.poc.presentation.characterDetails.CharacterDetailsViewModel
import com.kiro.poc.presentation.characterGroups.CharacterGroupsViewModel
import com.kiro.poc.presentation.characterRows.CharacterRowsViewModel
import com.kiro.poc.presentation.characters.CharactersViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CharactersViewModel::class)
    abstract fun bindCharactersViewModel(viewModel: CharactersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CharacterDetailsViewModel::class)
    abstract fun bindCharacterDetailsViewModel(viewModel: CharacterDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CharacterGroupsViewModel::class)
    abstract fun bindCharacterGroupsViewModel(viewModel: CharacterGroupsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CharacterRowsViewModel::class)
    abstract fun bindCharacterRowsViewModel(viewModel: CharacterRowsViewModel): ViewModel
}
