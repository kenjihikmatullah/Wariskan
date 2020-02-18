package com.wariskan.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(

    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "email")
    val email: String

//    @Json(name = "email_verified_at")
//    val email_verified_at: Timestamp,

//    @Json(name = "password")
//    val password: String,

//    @Json(name = "remember_token")
//    val remember_token: String

//    @Json(name = "created_at")
//    val created_at: Timestamp,
//
//    @Json(name = "updated_at")
//    val updated_at: Timestamp
)