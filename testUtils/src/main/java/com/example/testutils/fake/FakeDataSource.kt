package com.example.testutils.fake

import com.example.core.model.Resto
import com.example.network.ApiRestoMenu
import com.example.network.Day
import com.example.network.Dish
import com.example.network.Special

object FakeDataSource {
    val restoList = listOf("Resto 1", "Resto 2", "Resto 3")
    var restoObjectList = listOf(
        Resto(
        restoList[0],true), Resto(
        restoList[1],false), Resto(
            restoList[2], false
        )
    )
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
                //keep this message for testing
                message = "Test message day2",
                menu = mapOf(
                    "Lunch" to listOf(
                        Dish(
                            name = "Dish 1",
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 2",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 3",
                            special = Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        Dish(
                            name = "Dish 4",
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 5",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 6",
                            special = Special.VEGGIE
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
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 2",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 3",
                            special = Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        Dish(
                            name = "Dish 4",
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 5",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 6",
                            special = Special.VEGGIE
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
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 2",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 3",
                            special = Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        Dish(
                            name = "Dish 4",
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 5",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 6",
                            special = Special.VEGGIE
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
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 2",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 3",
                            special = Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        Dish(
                            name = "Dish 4",
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 5",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 6",
                            special = Special.VEGGIE
                        ),
                    )
                )
            ),
        )
    )
    val restoMenu2 = ApiRestoMenu(
        location = "Resto 2",
        days = listOf(
            Day(
                dag = "Monday",
                message = "Open",
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
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 2",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 3",
                            special = Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        Dish(
                            name = "Dish 4",
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 5",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 6",
                            special = Special.VEGGIE
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
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 2",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 3",
                            special = Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        Dish(
                            name = "Dish 4",
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 5",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 6",
                            special = Special.VEGGIE
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
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 2",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 3",
                            special = Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        Dish(
                            name = "Dish 4",
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 5",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 6",
                            special = Special.VEGGIE
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
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 2",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 3",
                            special = Special.VEGGIE
                        ),
                    ),
                    "Dinner" to listOf(
                        Dish(
                            name = "Dish 4",
                            special = Special.NONE
                        ),
                        Dish(
                            name = "Dish 5",
                            special = Special.VEGAN
                        ),
                        Dish(
                            name = "Dish 6",
                            special = Special.VEGGIE
                        ),
                    )
                )
            ),
        )
    )
}


