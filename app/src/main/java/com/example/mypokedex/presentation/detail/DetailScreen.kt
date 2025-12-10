package com.example.mypokedex.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val pokemonDetail by viewModel.pokemonDetail.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pokemonDetail?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            )
        }
    ) { padding ->
        pokemonDetail?.let { pokemon ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = pokemon.imageUrl,
                    contentDescription = pokemon.name,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Height: ${pokemon.height}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Weight: ${pokemon.weight}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Abilities", style = MaterialTheme.typography.titleMedium)
                pokemon.abilities.forEach {
                    Text(text = it.name)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Stats", style = MaterialTheme.typography.titleMedium)
                pokemon.stats.forEach {
                    Text(text = "${it.name}: ${it.value}")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Types", style = MaterialTheme.typography.titleMedium)
                pokemon.types.forEach {
                    Text(text = it)
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
