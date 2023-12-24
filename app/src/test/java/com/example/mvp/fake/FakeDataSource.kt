package com.example.mvp.fake

import com.example.mvp.Network.ApiRestoMenu
import com.example.mvp.Network.Day
import com.example.mvp.Network.Dish
import com.example.mvp.Network.special

object FakeDataSource {
    val restoList = listOf("Resto 1", "Resto 2", "Resto 3")
    val restoMenu = ApiRestoMenu(
        location = "Resto 1",
        days = listOf(
            Day(
                dag = "Monday",
                message = "CLOSED",
                menu = mapOf(
                )
            ),
            Day(
                dag = "Tuesday",
                message = "",
                menu = mapOf(
                    "Lunch" to listOf(
                        Dish(
                            name = "Dish 1",
                            special = special.NONE
                        ),
                        Dish(
                            name = "Dish 2",
                            special = special.VEGAN
                        ),
                        Dish(
                            name = "Dish 3",
                            special = special.VEGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        Dish(
                            name = "Dish 4",
                            special = special.NONE
                        ),
                        Dish(
                            name = "Dish 5",
                            special = special.VEGAN
                        ),
                        Dish(
                            name = "Dish 6",
                            special = special.VEGIE
                        ),
                    )
                )
            ),
            Day(
                dag = "Wednesday",
                message = "",
                menu = mapOf(
                    "Lunch" to listOf(
                        Dish(
                            name = "Dish 1",
                            special = special.NONE
                        ),
                        Dish(
                            name = "Dish 2",
                            special = special.VEGAN
                        ),
                        Dish(
                            name = "Dish 3",
                            special = special.VEGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        Dish(
                            name = "Dish 4",
                            special = special.NONE
                        ),
                        Dish(
                            name = "Dish 5",
                            special = special.VEGAN
                        ),
                        Dish(
                            name = "Dish 6",
                            special = special.VEGIE
                        ),
                    )
                )
            ),
            Day(
                dag = "Thursday",
                message = "",
                menu = mapOf(
                    "Lunch" to listOf(
                        Dish(
                            name = "Dish 1",
                            special = special.NONE
                        ),
                        Dish(
                            name = "Dish 2",
                            special = special.VEGAN
                        ),
                        Dish(
                            name = "Dish 3",
                            special = special.VEGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        Dish(
                            name = "Dish 4",
                            special = special.NONE
                        ),
                        Dish(
                            name = "Dish 5",
                            special = special.VEGAN
                        ),
                        Dish(
                            name = "Dish 6",
                            special = special.VEGIE
                        ),
                    )
                )
            ),
            Day(
                dag = "Friday",
                message = "",
                menu = mapOf(
                    "Lunch" to listOf(
                        Dish(
                            name = "Dish 1",
                            special = special.NONE
                        ),
                        Dish(
                            name = "Dish 2",
                            special = special.VEGAN
                        ),
                        Dish(
                            name = "Dish 3",
                            special = special.VEGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        Dish(
                            name = "Dish 4",
                            special = special.NONE
                        ),
                        Dish(
                            name = "Dish 5",
                            special = special.VEGAN
                        ),
                        Dish(
                            name = "Dish 6",
                            special = special.VEGIE
                        ),
                    )
                )
            ),
        )
    )
}


