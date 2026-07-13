package com.kiro.poc.data.dto


import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class Origin(
    @SerialName("name")
    val name: String? = null,
    @SerialName("url")
    val url: String? = null
)