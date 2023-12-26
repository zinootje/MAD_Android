package com.example.mvp.ui.Util

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

//make a semantic

val TabKey = SemanticsPropertyKey<String>("TabKey")
var SemanticsPropertyReceiver.tabKey by TabKey
