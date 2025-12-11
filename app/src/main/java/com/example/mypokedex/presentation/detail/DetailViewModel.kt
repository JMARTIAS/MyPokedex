package com.example.mypokedex.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypokedex.domain.model.PokemonDetail
import com.example.mypokedex.domain.usecase.AddFavoriteUseCase
import com.example.mypokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.mypokedex.domain.usecase.IsFavoriteUseCase
import com.example.mypokedex.domain.usecase.LogoutUseCase
import com.example.mypokedex.domain.usecase.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _pokemonDetail = MutableStateFlow<PokemonDetail?>(null)
    val pokemonDetail = _pokemonDetail.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val _logoutEvent = Channel<Unit>()
    val logoutEvent = _logoutEvent.receiveAsFlow()

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog = _showLogoutDialog.asStateFlow()

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

    fun onLogoutClick() {
        _showLogoutDialog.value = true
    }

    fun onLogoutConfirm() {
        viewModelScope.launch {
            logoutUseCase()
            _logoutEvent.send(Unit)
        }
        _showLogoutDialog.value = false
    }

    fun onLogoutDismiss() {
        _showLogoutDialog.value = false
    }
}
