package com.dilip.qrventory

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dilip.qrventory.navigation.Graph
import kotlinx.coroutines.delay

class SplashScreen : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Pass the NavController to SplashScreenUI
            SplashScreenUI(rememberNavController())
        }
    }

    @Composable
    fun SplashScreenUI(navController: NavController) {
        // Box layout to hold the background and logo
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.mahi),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Center-Top Logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { it }, // Slide in from bottom
                        animationSpec = tween(durationMillis = 1000) // Animation duration
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mahi), // Your logo image
                        contentDescription = "Logo",
                        modifier = Modifier.size(210.dp)
                    )
                }
            }
        }

        LaunchedEffect(Unit) {
            delay(5000) // Show splash for 5 seconds

            // Check if it's the first launch
            sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)
            if (isFirstLaunch) {
                // Set first launch to false
                sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
                navController.navigate(Graph.LoginGraph) {
                    popUpTo(Graph.SplashScreenGraph) { inclusive = true }
                }
            } else {
                navController.navigate(Graph.MainScreenGraph) {
                    popUpTo(Graph.MainScreenGraph) { inclusive = true }
                }
            }
        }
    }
}
