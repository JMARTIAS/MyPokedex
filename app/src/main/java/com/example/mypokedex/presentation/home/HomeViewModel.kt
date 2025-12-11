package com.example.mypokedex.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypokedex.domain.model.Pokemon
import com.example.mypokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.mypokedex.domain.usecase.GetPokemonListUseCase
import com.example.mypokedex.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
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

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog = _showLogoutDialog.asStateFlow()

    private val _showLanguageDialog = MutableStateFlow(false)
    val showLanguageDialog = _showLanguageDialog.asStateFlow()

    private val searchedPokemonFlow = _searchQuery
        .debounce(500L)
        .distinctUntilChanged()
        .transformLatest { query ->
            if (query.isBlank()) {
                emit(null) // null indicates not in search mode
                return@transformLatest
            }

            _isLoading.value = true
            getPokemonDetailUseCase(query.lowercase().trim()).fold(
                onSuccess = { pokemonDetail ->
                    emit(
                        listOf(
                            Pokemon(
                                pokemonDetail.id,
                                pokemonDetail.name,
                                pokemonDetail.imageUrl
                            )
                        )
                    )
                },
                onFailure = { emit(emptyList()) } // empty list indicates search with no results
            )
            _isLoading.value = false
        }

    val pokemonList = combine(
        _fullPokemonList,
        searchedPokemonFlow
    ) { fullList, searchResult ->
        searchResult ?: fullList
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _fullPokemonList.value)

    init {
        loadMorePokemons()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun loadMorePokemons() {
        if (_searchQuery.value.isNotBlank()) return

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

    fun onLanguageClick() {
        _showLanguageDialog.value = true
    }

    fun onLanguageDismiss() {
        _showLanguageDialog.value = false
    }
}
