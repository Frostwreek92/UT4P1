package com.example.ut4p1.coffeeShops

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ut3p2.CafeteriasScreen
import com.example.ut3p2.DetallesCafeteriaScreen
import com.example.ut3p2.MyTopAppBar

@Composable
fun CoffeeShopsApp() {
    // Usas tu pantalla principal de UT3P2

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "cafeterias"
    ) {
        composable("cafeterias") {
            Scaffold(
                topBar = { MyTopAppBar("UT3P2 - Cafeterías") }
            ) { innerPadding ->
                CafeteriasScreen(
                    navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
        composable(
            "detalle/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            Scaffold(
                topBar = { MyTopAppBar("Detalles") }
            ) { innerPadding ->
                DetallesCafeteriaScreen(
                    nombre,
                    navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
