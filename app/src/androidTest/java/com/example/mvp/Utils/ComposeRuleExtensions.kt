package com.example.mvp.Utils

//https://github.com/google-developer-training/basic-android-kotlin-compose-training-cupcake/blob/main/app/src/androidTest/java/com/example/cupcake/test/ComposeRuleExtensions.kt

/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.mvp.MvpApplication

/**
 * Retrieves the [SemanticsNodeInteraction] with the given string resource id as text.
 * This is a wrapper around [onNodeWithText] that takes a string resource id instead
 *
 * @param id The resource ID of the text string.
 * @return The [SemanticsNodeInteraction] for the specified text string.
 */
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.onNodeWithStringId(
    @StringRes id: Int
): SemanticsNodeInteraction = onNodeWithText(activity.getString(id))



/**
 * Retrieves the [SemanticsNodeInteraction] with the given string resource id as content description.
 * This is a wrapper around [onNodeWithContentDescription] that takes a string resource id instead
 *
 * @param id The resource ID of the content description string.
 * @return The [SemanticsNodeInteraction] for the specified content description string.
 */
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.onNodeWithContentDescriptionStringId(
    @StringRes id: Int
): SemanticsNodeInteraction = onNodeWithContentDescription(activity.getString(id))



/**
 * Sets the [FakeAppContainer] as the application container in the [MvpApplication].
 * This is useful for testing purposes when you want to replace the default implementation
 * with a fake implementation of the [AppContainer].
 */
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.setFakeAppContainer() {
    activityRule.scenario.onActivity {
        val app = it.application as MvpApplication
        app.container = FakeAppContainer()
    }
}