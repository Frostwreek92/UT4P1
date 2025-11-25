package com.example.ut4p1.myPhotos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.ut3p1.PhotoGallery
import com.example.ut4p1.R

@Composable
fun MyPhotosApp() {
    Surface(
    modifier = Modifier
        .fillMaxSize()
    ) {
        PhotoGallery()
    }
}
