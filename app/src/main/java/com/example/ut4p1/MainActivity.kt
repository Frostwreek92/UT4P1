package com.example.ut4p1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.ut4p1.coffeeShops.CoffeeShopsApp
import com.example.ut4p1.elSol.ElSolApp
import com.example.ut4p1.myPhotos.MyPhotosApp
import com.example.ut4p1.ui.theme.UT4P1Theme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UT4P1Theme {
                AppNavigation()
            }
        }
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { HomeScreen() }
            composable("myphotos") { MyPhotosApp() }
            composable("coffeeshops") { CoffeeShopsApp() }
            composable("elsol") { ElSolApp() }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {

    val items = listOf(
        NavItem("Inicio", "home", Icons.Default.Home),
        NavItem("Fotos", "myphotos", Icons.Default.AccountBox),
        NavItem("Cafeterías", "coffeeshops", Icons.Default.Favorite),
        NavItem("El Sol", "elsol", Icons.Default.Face),
    )

    NavigationBar {
        val route = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}

data class NavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
