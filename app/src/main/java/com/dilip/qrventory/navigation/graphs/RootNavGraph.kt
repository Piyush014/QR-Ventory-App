package com.dilip.qrventory.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dilip.qrventory.navigation.Graph
import com.dilip.qrventory.presentation.MainScreen
import com.dilip.qrventory.presentation.login.LoginPage
import com.dilip.qrventory.presentation.register.RegistrationPage

@Composable
fun RootNavGraph(startDestination: String) {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = startDestination,
    ) {
        composable(route = Graph.LoginGraph) {
            LoginPage(
                onLogin = { username ->
                    // Handle successful login
                    rootNavController.navigate(Graph.MainScreenGraph) {
                        // Clear back stack to prevent returning to login page
                        popUpTo(Graph.LoginGraph) { inclusive = true }
                    }
                },
                onSignUp = {
                    // Navigate to registration page
                    rootNavController.navigate(Graph.RegistrationGraph)
                }
            )
        }

        composable(route = Graph.RegistrationGraph) {
            RegistrationPage(
                onRegister = {
                    // Navigate back to login page after successful registration
                    rootNavController.navigate(Graph.LoginGraph) {
                        // Clear back stack to prevent returning to registration page
                        popUpTo(Graph.RegistrationGraph) { inclusive = true }
                    }
                },
                onLogin = {
                    // Navigate back to login page
                    rootNavController.navigate(Graph.LoginGraph) {
                        // Clear back stack to prevent returning to registration page
                        popUpTo(Graph.RegistrationGraph) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Graph.MainScreenGraph) {
            MainScreen(rootNavController = rootNavController)
        }

        // You can add additional composable screens or settingsNavGraph here if needed
        // settingsNavGraph(rootNavController)
    }
}
