package com.example.mypokedex.presentation.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mypokedex.R
import com.example.mypokedex.domain.model.PokemonDetail
import com.example.mypokedex.domain.model.Stat
import com.example.mypokedex.presentation.components.LogoutConfirmationDialog
import java.util.Locale

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onLogout: () -> Unit
) {
    val pokemonDetail by viewModel.pokemonDetail.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val showLogoutDialog by viewModel.showLogoutDialog.collectAsState()

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = { viewModel.onLogoutConfirm() },
            onDismiss = { viewModel.onLogoutDismiss() }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.logoutEvent.collect {
            onLogout()
        }
    }

    Surface(
        color = Color(0xFFF5F5F5),
        modifier = Modifier.fillMaxSize()
    ) {
        pokemonDetail?.let { pokemon ->
            Box {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    PokemonImageSection(pokemon = pokemon)
                    PokemonDetailSection(pokemon = pokemon)
                }
                TopBar(
                    onBackClick = onBackClick,
                    onFavoriteClick = { viewModel.toggleFavorite() },
                    onLogoutClick = { viewModel.onLogoutClick() },
                    isFavorite = isFavorite
                )
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onLogoutClick: () -> Unit,
    isFavorite: Boolean
) {
    TopAppBar(
        title = { /* No title */ },
        navigationIcon = {
            val iconModifier = Modifier
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.4f))

            IconButton(onClick = onBackClick, modifier = iconModifier) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        },
        actions = {
            val iconModifier = Modifier
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.4f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onFavoriteClick, modifier = iconModifier) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
                Spacer(modifier = Modifier.padding(end = 8.dp))
                IconButton(onClick = onLogoutClick, modifier = iconModifier) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun PokemonImageSection(pokemon: PokemonDetail) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = pokemon.name,
            modifier = Modifier
                .size(250.dp)
                .offset(y = 50.dp)
        )
    }
}

@Composable
fun PokemonDetailSection(pokemon: PokemonDetail) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                pokemon.types.forEach {
                    TypeChip(type = it)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            PokemonMeasurements(pokemon = pokemon)
            Spacer(modifier = Modifier.height(32.dp))
            PokemonStats(pokemon = pokemon)
        }
    }
}

@Composable
fun TypeChip(type: String) {
    val typeColor = when (type.lowercase()) {
        "fire" -> Color(0xFFF08030)
        "water" -> Color(0xFF6890F0)
        "grass" -> Color(0xFF78C850)
        "electric" -> Color(0xFFF8D030)
        "ice" -> Color(0xFF98D8D8)
        "fighting" -> Color(0xFFC03028)
        "poison" -> Color(0xFFA040A0)
        "ground" -> Color(0xFFE0C068)
        "flying" -> Color(0xFFA890F0)
        "psychic" -> Color(0xFFF85888)
        "bug" -> Color(0xFFA8B820)
        "rock" -> Color(0xFFB8A038)
        "ghost" -> Color(0xFF705898)
        "dragon" -> Color(0xFF7038F8)
        "dark" -> Color(0xFF705848)
        "steel" -> Color(0xFFB8B8D0)
        "fairy" -> Color(0xFFEE99AC)
        else -> Color.Gray
    }
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(typeColor)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PokemonMeasurements(pokemon: PokemonDetail) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Weight", fontWeight = FontWeight.Bold)
            Text(text = "${pokemon.weight / 10f} kg")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Height", fontWeight = FontWeight.Bold)
            Text(text = "${pokemon.height / 10f} m")
        }
    }
}

@Composable
fun PokemonStats(pokemon: PokemonDetail) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Base Stats",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        pokemon.stats.forEach {
            StatRow(stat = it)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun StatRow(stat: Stat) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if(animationPlayed) (stat.value / 100f) else 0f,
        animationSpec = tween(1000)
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = stat.name.replaceFirstChar { it.titlecase(Locale.getDefault()) }, modifier = Modifier.weight(0.3f))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.weight(0.7f).height(10.dp).clip(CircleShape),
            color = if (stat.value >= 50) Color(0xFF78C850) else Color(0xFFF08030)
        )
    }
}
