package com.example.mypokedex.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypokedex.domain.model.Pokemon
import com.example.mypokedex.domain.usecase.GetPokemonListUseCase
import com.example.mypokedex.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var currentPage = 0

    private val _fullPokemonList = MutableStateFlow<List<Pokemon>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _logoutEvent = Channel<Unit>()
    val logoutEvent = _logoutEvent.receiveAsFlow()

    val pokemonList = _searchQuery
        .combine(_fullPokemonList) { query, pokemons ->
            if (query.isBlank()) {
                pokemons
            } else {
                pokemons.filter {
                    it.name.contains(query, ignoreCase = true) || it.id.toString().contains(query)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _fullPokemonList.value
        )

    init {
        loadMorePokemons()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun loadMorePokemons() {
        viewModelScope.launch {
            if (isLoading.value) return@launch
            _isLoading.value = true
            getPokemonListUseCase(currentPage).onSuccess { newPokemons ->
                _fullPokemonList.value += newPokemons
                currentPage++
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _logoutEvent.send(Unit)
        }
    }
}
