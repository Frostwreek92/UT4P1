package com.example.ut3p2
import com.example.ut4p1.R

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

val MiFuentePersonal = FontFamily(
    Font(R.font.aliviaregular) // el nombre del archivo sin extensión
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(title: String) {
    var expanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { Text(title) },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Compartir") },
                    leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                    onClick = { expanded = false /* acción compartir */ }
                )
                DropdownMenuItem(
                    text = { Text("Album") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    onClick = { expanded = false /* acción album */ }
                )
            }
        }
    )
}

data class Cafeteria(
    val nombre: String,
    val direccion: String,
    val imagenResId: Int,
    val comentarios: MutableList<String> = mutableListOf()
)
fun generarComentariosEjemplo(): List<String> {
    return List(50) { index -> "Comentario de prueba #${index + 1}" }
}

val cafeteriasEjemplo = listOf(
    Cafeteria("Antico Caffé Greco",
        "St. Italy, Rome",
        R.drawable.images,
        comentarios = generarComentariosEjemplo().toMutableList()),
    Cafeteria("Coffe Room",
        "St. Germany, Berlin",
        R.drawable.images1,
        comentarios = generarComentariosEjemplo().toMutableList()),
    Cafeteria("Coffe Ibiza",
        "St. Colon, Madrid",
        R.drawable.images2,
        comentarios = generarComentariosEjemplo().toMutableList()),
    Cafeteria("Pudding Coffee Shop",
        "St. Diagonal, Barcelona",
        R.drawable.images3,
        comentarios = generarComentariosEjemplo().toMutableList()),
    Cafeteria("LÉxpress",
        "St. Picadilly Circus, London",
        R.drawable.images4,
        comentarios = generarComentariosEjemplo().toMutableList()),
    Cafeteria("Coffee Corner",
        "St. Ángel Guimerà, Valencia",
        R.drawable.images5,
        comentarios = generarComentariosEjemplo().toMutableList()),
    Cafeteria("Sweet Cup",
        "St. Kinkerstraat, Amsterdam",
        R.drawable.images6,
        comentarios = generarComentariosEjemplo().toMutableList())
)

@Composable
fun CafeteriasScreen(navController: NavController, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(cafeteriasEjemplo) { cafeteria ->
            CafeteriaCard(cafeteria) {
                navController.navigate("detalle/${cafeteria.nombre}")
            }
        }
    }
}

@Composable
fun CafeteriaCard(cafeteria: Cafeteria, onClick: () -> Unit) {
    var rating by rememberSaveable { mutableStateOf(0f) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(cafeteria.imagenResId),
                contentDescription = cafeteria.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(cafeteria.nombre, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontFamily = MiFuentePersonal)
            Text(cafeteria.direccion, fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(8.dp))
            RatingBar(rating = rating, onRatingChanged = { rating = it })
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            Spacer(Modifier.height(8.dp))
            Button(onClick = { /* Reservar */ }, modifier = Modifier.align(Alignment.End)) { Text("Reservar") }
        }
    }
}

@Composable
fun RatingBar(rating: Float, maxRating: Int = 5, onRatingChanged: (Float) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..maxRating) {
            IconButton(onClick = { onRatingChanged(i.toFloat()) }) {
                Icon(
                    imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Star $i",
                    tint = if (i <= rating) Color(0xFFFFC107) else Color.Gray
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Text("${rating.toInt()} / $maxRating", fontSize = 14.sp)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetallesCafeteriaScreen(nombre: String, navController: NavController, modifier: Modifier = Modifier) {
    val cafeteria = cafeteriasEjemplo.find { it.nombre == nombre } ?: return
    val gridState = rememberLazyStaggeredGridState()
    var showButton by remember { mutableStateOf(false) }
    var lastScrollOffset by remember { mutableStateOf(0) }

    // Detectar scroll
    LaunchedEffect(gridState.firstVisibleItemScrollOffset, gridState.firstVisibleItemIndex) {
        val currentOffset = gridState.firstVisibleItemScrollOffset
        val currentIndex = gridState.firstVisibleItemIndex
        val totalOffset = currentIndex * 1000 + currentOffset
        showButton = totalOffset > lastScrollOffset
        lastScrollOffset = totalOffset
    }

    var newComment by remember { mutableStateOf("") }

    Box(modifier.fillMaxSize()) {
        Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
            Text(cafeteria.nombre, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontFamily = MiFuentePersonal)
            Spacer(Modifier.height(16.dp))
            Text("Comentarios:", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            LazyVerticalStaggeredGrid(
                state = gridState,
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(cafeteria.comentarios.size) { index ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Text(
                            text = cafeteria.comentarios[index],
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showButton,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            FloatingActionButton(onClick = {
                if (newComment.isNotBlank()) {
                    cafeteria.comentarios.add(newComment)
                    newComment = ""
                }
            }) {
                Text(" + Añadir Comentario ")
            }
        }
    }
}
