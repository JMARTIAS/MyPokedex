package com.example.mypokedex.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypokedex.domain.usecase.GetLanguageUseCase
import com.example.mypokedex.domain.usecase.SetLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    getLanguageUseCase: GetLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase
) : ViewModel() {

    val language = getLanguageUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Locale.getDefault().language)

    fun setLanguage(language: String) {
        viewModelScope.launch {
            setLanguageUseCase(language)
        }
    }
}
