package com.example.mypokedex.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypokedex.domain.model.Pokemon
import com.example.mypokedex.domain.usecase.GetFavoritePokemonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritePokemonsUseCase: GetFavoritePokemonsUseCase
) : ViewModel() {

    private val _favoritePokemons = MutableStateFlow<List<Pokemon>>(emptyList())
    val favoritePokemons = _favoritePokemons.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoritePokemonsUseCase().collect {
                _favoritePokemons.value = it
            }
        }
    }
}
