package com.example.mypokedex.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypokedex.domain.model.PokemonDetail
import com.example.mypokedex.domain.usecase.AddFavoriteUseCase
import com.example.mypokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.mypokedex.domain.usecase.IsFavoriteUseCase
import com.example.mypokedex.domain.usecase.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _pokemonDetail = MutableStateFlow<PokemonDetail?>(null)
    val pokemonDetail = _pokemonDetail.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    init {
        viewModelScope.launch {
            val pokemonName = savedStateHandle.get<String>("pokemonName") ?: return@launch
            getPokemonDetailUseCase(pokemonName).onSuccess {
                _pokemonDetail.value = it
                isFavoriteUseCase(it.id).collect {
                    _isFavorite.value = it
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val pokemon = _pokemonDetail.value ?: return@launch
            if (_isFavorite.first()) {
                removeFavoriteUseCase(pokemon)
            } else {
                addFavoriteUseCase(pokemon)
            }
        }
    }
}
