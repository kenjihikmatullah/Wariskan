package com.wariskan.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.wariskan.network.InheritanceNetwork
import com.wariskan.network.User

@JsonClass(generateAdapter = true)
data class LoginResponse(

    @Json(name = "token")
    val token: String,

    @Json(name = "user")
    val user: User,

    @Json(name = "inheritances")
    val inheritances: List<InheritanceNetwork>
)