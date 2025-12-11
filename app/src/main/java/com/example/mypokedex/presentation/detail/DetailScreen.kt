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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mypokedex.R
import com.example.mypokedex.domain.model.PokemonDetail
import com.example.mypokedex.domain.model.Stat
import com.example.mypokedex.presentation.LanguageViewModel
import com.example.mypokedex.presentation.components.LanguageSelectionDialog
import com.example.mypokedex.presentation.components.LogoutConfirmationDialog
import java.util.Locale

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    languageViewModel: LanguageViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onLogout: () -> Unit
) {
    val pokemonDetail by viewModel.pokemonDetail.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val showLogoutDialog by viewModel.showLogoutDialog.collectAsState()
    val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()
    val language by languageViewModel.language.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onLanguageDismiss()
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = { viewModel.onLogoutConfirm() },
            onDismiss = { viewModel.onLogoutDismiss() }
        )
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = language,
            onLanguageSelected = {
                viewModel.onLanguageDismiss()
                languageViewModel.setLanguage(it)
            },
            onDismiss = { viewModel.onLanguageDismiss() }
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
                    onLanguageClick = { viewModel.onLanguageClick() },
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
    onLanguageClick: () -> Unit,
    isFavorite: Boolean
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { /* No title */ },
        navigationIcon = {
            val iconModifier = Modifier
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.4f))

            IconButton(onClick = onBackClick, modifier = iconModifier) {
                Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back), tint = Color.White)
            }
        },
        actions = {
            val iconModifier = Modifier
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.4f))

            IconButton(onClick = { showMenu = true }, modifier = iconModifier) {
                Icon(Icons.Default.MoreVert, contentDescription = stringResource(id = R.string.more_options), tint = Color.White)
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text(if (isFavorite) stringResource(id = R.string.remove_from_favorites) else stringResource(id = R.string.add_to_favorites)) },
                    onClick = {
                        onFavoriteClick()
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.logout)) },
                    onClick = {
                        onLogoutClick()
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.change_language)) },
                    onClick = {
                        onLanguageClick()
                        showMenu = false
                    }
                )
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
            text = stringResource(id = getTypeResource(type)),
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
            Text(text = stringResource(id = R.string.weight), fontWeight = FontWeight.Bold)
            Text(text = "${pokemon.weight / 10f} kg")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.height), fontWeight = FontWeight.Bold)
            Text(text = "${pokemon.height / 10f} m")
        }
    }
}

@Composable
fun PokemonStats(pokemon: PokemonDetail) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.base_stats),
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
        Text(text = stringResource(id = getStatResource(stat.name)), modifier = Modifier.weight(0.4f))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.weight(0.6f).height(10.dp).clip(CircleShape),
            color = if (stat.value >= 50) Color(0xFF78C850) else Color(0xFFF08030)
        )
    }
}

fun getStatResource(statName: String): Int {
    return when (statName.lowercase()) {
        "hp" -> R.string.hp
        "attack" -> R.string.attack
        "defense" -> R.string.defense
        "special-attack" -> R.string.special_attack
        "special-defense" -> R.string.special_defense
        "speed" -> R.string.speed
        else -> R.string.hp // Default to HP
    }
}

fun getTypeResource(typeName: String): Int {
    return when (typeName.lowercase()) {
        "fire" -> R.string.fire
        "water" -> R.string.water
        "grass" -> R.string.grass
        "electric" -> R.string.electric
        "ice" -> R.string.ice
        "fighting" -> R.string.fighting
        "poison" -> R.string.poison
        "ground" -> R.string.ground
        "flying" -> R.string.flying
        "psychic" -> R.string.psychic
        "bug" -> R.string.bug
        "rock" -> R.string.rock
        "ghost" -> R.string.ghost
        "dragon" -> R.string.dragon
        "dark" -> R.string.dark
        "steel" -> R.string.steel
        "fairy" -> R.string.fairy
        "normal" -> R.string.normal
        else -> R.string.normal // Default to Normal
    }
}
