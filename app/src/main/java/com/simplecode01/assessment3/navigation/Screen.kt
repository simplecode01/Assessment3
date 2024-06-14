package com.simplecode01.assessment3.navigation

import com.simplecode01.assessment3.ui.screen.KEY_ID_USER

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object AboutApp: Screen("AboutAppsScreen")
    data object Login: Screen("LoginScreen")
    data object List: Screen("ListItemScreen/{$KEY_ID_USER}"){
        fun withID(id: String)="ListItemScreen/$id"
    }
    data object Add: Screen("AddItemScreen")
}