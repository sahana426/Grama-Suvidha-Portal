package com.gramasuvidha.portal.ui.navigation

object Routes {
    const val Splash = "splash"
    const val Home = "home"
    const val Details = "details/{projectId}"
    fun details(projectId: Int) = "details/$projectId"
}
