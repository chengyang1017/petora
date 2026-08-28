package com.chengyang.petora.feature.chat.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val message: String,
    val petId: String,
    val previousResponseId: String? = null,
    val place: PlaceRequest? = null,
)

@Serializable
data class PlaceRequest(
    val name: String,
    val latitude: Double,
    val longitude: Double,
)