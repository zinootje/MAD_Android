package com.example.core

data class StaleAbleData<T>(
    val data: T,
    val isStale: Boolean
)