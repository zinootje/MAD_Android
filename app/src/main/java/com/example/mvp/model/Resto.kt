package com.example.mvp.model

import androidx.compose.runtime.Immutable

@Immutable
data class Resto(
    val name: String,
    val isFavorite: Boolean = false
)