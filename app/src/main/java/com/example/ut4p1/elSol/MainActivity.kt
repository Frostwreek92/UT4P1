package com.example.ut3p3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.delay
import android.app.DatePickerDialog
import java.util.*
import com.example.ut4p1.R

data class ImagenSolar(
    val id: Int,
    val nombre: String,
    val idUnico: String = UUID.randomUUID().toString()
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElSolApp() {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    var favoritos by remember { mutableStateOf(0) }
    val navController = rememberNavController()
    val imagenes = remember {
        mutableStateListOf(
            ImagenSolar(R.drawable.corona_solar, "Corona Solar"),
            ImagenSolar(R.drawable.erupcionsolar, "Erupción Solar"),
            ImagenSolar(R.drawable.espiculas, "Espículas"),
            ImagenSolar(R.drawable.filamentos, "Filamentos"),
            ImagenSolar(R.drawable.magnetosfera, "Magnetosfera"),
            ImagenSolar(R.drawable.manchasolar, "Mancha Solar")
        )
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.corona_solar), // imagen superior
                        contentDescription = "Logo Sol",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                    HorizontalDivider()
                    DrawerItem(
                        icono = Icons.Default.Build,
                        texto = "Build"
                    ) {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("build")
                        }
                    }
                    DrawerItem(
                        icono = Icons.Default.Info,
                        texto = "Info"
                    ) {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("info")
                        }
                    }
                    DrawerItem(
                        icono = Icons.Default.Email,
                        texto = "Email"
                    ) {
                        scope.launch { drawerState.close() }
                    }
                }
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Abrir menú lateral")
                        }
                        BadgedBox(
                            badge = {
                                if (favoritos > 0) {
                                    Badge { Text(favoritos.toString()) }
                                }
                            }
                        ) {
                            IconButton(onClick = { favoritos++ }) {
                                Icon(Icons.Default.Favorite, contentDescription = "Favoritos")
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { /* sin funcionalidad */ }) {
                            Text("+")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "build",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("build") {
                    GaleriaSol(
                        imagenes = imagenes,
                        snackbarHostState = snackbarHostState,
                        scope = scope
                    )
                }
                composable("info") { PantallaInfo() }
            }
        }
    }
}
@Composable
fun DrawerItem(icono: androidx.compose.ui.graphics.vector.ImageVector, texto: String, onClick: () -> Unit) {
    NavigationDrawerItem(
        icon = { Icon(icono, contentDescription = texto) },
        label = { Text(texto) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}
@Composable
fun PantallaInfo() {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var showProgress by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(targetValue = progress)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SnackbarHost(hostState = snackbarHostState)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (!loading) {
                    loading = true
                    showProgress = true
                    progress = 0f
                    scope.launch {
                        repeat(12) {
                            delay(500)
                            progress = (progress + 0.1f).coerceAtMost(1f)
                        }
                        progress = 1f
                        delay(300)
                        snackbarHostState.showSnackbar("Download complete")
                        delay(500)
                        showProgress = false
                        loading = false
                        progress = 0f
                    }
                }
            },
            enabled = !loading
        ) {
            Text(text = if (loading) "Downloading..." else "Download more info")
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (showProgress) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                DatePickerDialog(
                    context,
                    { _, _, _, _ ->
                    },
                    year,
                    month,
                    day
                ).show()
            }
        ) {
            Text("Visit planetarium. Select date")
        }
    }
}
@Composable
fun GaleriaSol(
    imagenes: MutableList<ImagenSolar>,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(imagenes, key = { it.idUnico }) { imagen ->
        SolarCard(
                imagen = imagen,
                onClick = {
                    scope.launch { snackbarHostState.showSnackbar(imagen.nombre) }
                },
                onCopiar = {
                    val nuevaImagen = ImagenSolar(
                        id = imagen.id,
                        nombre = imagen.nombre
                    )
                    imagenes.add(nuevaImagen)
                },
                onEliminar = {
                    imagenes.remove(imagen)
                    scope.launch {
                        snackbarHostState.showSnackbar("Eliminada: ${imagen.nombre}")
                    }
                }
            )
        }
    }
}
@Composable
fun SolarCard(
    imagen: ImagenSolar,
    onClick: () -> Unit,
    onCopiar: () -> Unit,
    onEliminar: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = imagen.id),
                contentDescription = imagen.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(imagen.nombre, style = MaterialTheme.typography.bodyMedium)
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Copiar") },
                            onClick = {
                                expanded = false; onCopiar()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar") },
                            onClick = {
                                expanded = false; onEliminar()
                            }
                        )
                    }
                }
            }
        }
    }
}
