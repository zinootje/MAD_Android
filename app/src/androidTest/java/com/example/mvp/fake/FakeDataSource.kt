package com.example.mvp.fake

import com.example.core.model.Resto

object FakeDataSource {
    val restoList = listOf("Resto 1", "Resto 2", "Resto 3")
    var restoObjectList = listOf(
        Resto(
        restoList[0],true), Resto(
        restoList[1],false), Resto(
            restoList[2], false
        )
    )
    val restoMenu = com.example.network.ApiRestoMenu(
        location = "Resto 1",
        days = listOf(
            com.example.network.Day(
                dag = "Monday",
                message = "CLOSED",
                menu = mapOf(
                )
            ),
            com.example.network.Day(
                dag = "Tuesday",
                //keep this message for testing
                message = "Test message day2",
                menu = mapOf(
                    "Lunch" to listOf(
                        com.example.network.Dish(
                            name = "Dish 1",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 2",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 3",
                            special = com.example.network.Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        com.example.network.Dish(
                            name = "Dish 4",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 5",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 6",
                            special = com.example.network.Special.VEGGIE
                        ),
                    )
                )
            ),
            com.example.network.Day(
                dag = "Wednesday",
                message = "",
                menu = mapOf(
                    "Lunch" to listOf(
                        com.example.network.Dish(
                            name = "Dish 1",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 2",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 3",
                            special = com.example.network.Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        com.example.network.Dish(
                            name = "Dish 4",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 5",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 6",
                            special = com.example.network.Special.VEGGIE
                        ),
                    )
                )
            ),
            com.example.network.Day(
                dag = "Thursday",
                message = "",
                menu = mapOf(
                    "Lunch" to listOf(
                        com.example.network.Dish(
                            name = "Dish 1",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 2",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 3",
                            special = com.example.network.Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        com.example.network.Dish(
                            name = "Dish 4",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 5",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 6",
                            special = com.example.network.Special.VEGGIE
                        ),
                    )
                )
            ),
            com.example.network.Day(
                dag = "Friday",
                message = "",
                menu = mapOf(
                    "Lunch" to listOf(
                        com.example.network.Dish(
                            name = "Dish 1",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 2",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 3",
                            special = com.example.network.Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        com.example.network.Dish(
                            name = "Dish 4",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 5",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 6",
                            special = com.example.network.Special.VEGGIE
                        ),
                    )
                )
            ),
        )
    )
    val restoMenu2 = com.example.network.ApiRestoMenu(
        location = "Resto 2",
        days = listOf(
            com.example.network.Day(
                dag = "Monday",
                message = "Open",
                menu = mapOf(
                )
            ),
            com.example.network.Day(
                dag = "Tuesday",
                message = "",
                menu = mapOf(
                    "Lunch" to listOf(
                        com.example.network.Dish(
                            name = "Dish 1",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 2",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 3",
                            special = com.example.network.Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        com.example.network.Dish(
                            name = "Dish 4",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 5",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 6",
                            special = com.example.network.Special.VEGGIE
                        ),
                    )
                )
            ),
            com.example.network.Day(
                dag = "Wednesday",
                message = "",
                menu = mapOf(
                    "Lunch" to listOf(
                        com.example.network.Dish(
                            name = "Dish 1",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 2",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 3",
                            special = com.example.network.Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        com.example.network.Dish(
                            name = "Dish 4",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 5",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 6",
                            special = com.example.network.Special.VEGGIE
                        ),
                    )
                )
            ),
            com.example.network.Day(
                dag = "Thursday",
                message = "",
                menu = mapOf(
                    "Lunch" to listOf(
                        com.example.network.Dish(
                            name = "Dish 1",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 2",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 3",
                            special = com.example.network.Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        com.example.network.Dish(
                            name = "Dish 4",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 5",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 6",
                            special = com.example.network.Special.VEGGIE
                        ),
                    )
                )
            ),
            com.example.network.Day(
                dag = "Friday",
                message = "",
                menu = mapOf(
                    "Lunch" to listOf(
                        com.example.network.Dish(
                            name = "Dish 1",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 2",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 3",
                            special = com.example.network.Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        com.example.network.Dish(
                            name = "Dish 4",
                            special = com.example.network.Special.NONE
                        ),
                        com.example.network.Dish(
                            name = "Dish 5",
                            special = com.example.network.Special.VEGAN
                        ),
                        com.example.network.Dish(
                            name = "Dish 6",
                            special = com.example.network.Special.VEGGIE
                        ),
                    )
                )
            ),
        )
    )
}


