package com.pedro.test_1.ui.nav

import kotlinx.serialization.Serializable

@Serializable
object Splash

@Serializable
object Auth

@Serializable
object Main

@Serializable
data class Detail(val itemId: String)
