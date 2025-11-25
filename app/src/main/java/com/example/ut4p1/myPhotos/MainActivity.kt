// MainActivity.kt
package com.example.ut3p1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ut4p1.R

@Composable
fun PhotoGallery() {
    // Lista de URLs de ejemplo
    val images = listOf(
        "https://picsum.photos/id/230/200/300",
        "https://picsum.photos/id/231/200/300",
        "https://picsum.photos/id/232/200/300",
        "https://picsum.photos/id/233/200/300",
        "https://picsum.photos/id/234/200/300",
        "https://picsum.photos/id/235/200/300",
        "https://picsum.photos/id/236/200/300",
        "https://picsum.photos/id/237/200/300",
        "https://picsum.photos/id/238/200/300",
        "https://picsum.photos/id/239/200/300",
        "https://picsum.photos/id/240/200/300"
    )

    // Estado de la imagen seleccionada
    var selectedImage by remember { mutableStateOf<String?>(null) }
    Box(
        modifier = Modifier
            .padding(top = 30.dp)
            .background(colorResource(id = R.color.darker_gray))
    ) {
        Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp)
            ) {
                items(images) { imageUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUrl),
                        contentDescription = "Thumbnail",
                        modifier = Modifier
                            .size(100.dp)
                            .clickable { selectedImage = imageUrl },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Imagen seleccionada en la parte inferior
            selectedImage?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "Selected",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
