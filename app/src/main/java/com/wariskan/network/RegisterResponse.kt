package com.wariskan.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponse(

    @Json(name = "name")
    val name: String
)