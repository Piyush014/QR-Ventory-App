package com.dilip.qrventory.navigation

object Graph {
    const val RootGraph = "rootGraph"
    const val MainScreenGraph = "mainScreenGraph"
    const val SettingsGraph = "settingsGraph"
    const val DevicesGraph = "devicesGraph"
    const val LoginGraph = "loginGraph"
    const val RegistrationGraph = "registerGraph"
}

sealed class MainRouteScreen(var route: String) {
    data object HomeScreen : MainRouteScreen("home")
    data object SettingsScreen : SettingsRouteScreen("settings")
    data object DevicesScreen : DevicesRouteScreen("devices")
}

sealed class SettingsRouteScreen(var route: String) {
    data object AboutScreen : SettingsRouteScreen("about")
    data object SelectQR : SettingsRouteScreen("selectQRSettings")
}

sealed class DevicesRouteScreen(var route: String)
