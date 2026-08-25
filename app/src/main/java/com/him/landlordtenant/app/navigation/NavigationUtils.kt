package com.him.landlordtenant.app.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavController.navigateAndPopUp(
    route: String,
    popUpToRoute: String,
    inclusive: Boolean = true
) {
    this.navigate(route) {
        popUpTo(popUpToRoute) {
            this.inclusive = inclusive
        }
    }
}

fun NavController.clearBackStackAndNavigate(route: String) {
    this.navigate(route) {
        popUpTo(0)
    }
}
