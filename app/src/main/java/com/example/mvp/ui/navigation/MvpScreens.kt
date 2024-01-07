package com.example.mvp.ui.navigation

import androidx.annotation.StringRes
import com.example.mvp.R

/**
 * Enumeration class representing the screens in the MVP app.
 *
 * @param title The string resource ID for the screen title.
 * @param arguments Optional string value representing the screen argument.
 *
 *
 */
enum class MvpScreens(@StringRes val title: Int, val arguments: String? = null) {
    /**
     * This class represents the Start screen in the MVP app.
     *
     */
    Start(title = R.string.app_name),

    /**
     * Represents a restaurant menu in the MVP app.
     *
     */
    RestoMenu(title = R.string.resto_menu_title, arguments = "restoName");


    /**
     * Converts the enumeration class representing the screens in the MVP app to a route string.
     *
     * @return The route string corresponding to the screen.
     */
    fun toRoute(): String = buildString {
        append(name)
        arguments?.let { append("/{$it}") }
    }


}