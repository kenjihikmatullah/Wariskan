package com.wariskan.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InheritanceNetwork(

    @Json(name = "id")
    val id: Int,

    @Json(name = "id_room")
    val id_room: Int,

    @Json(name = "user_id")
    val user_id: Int
)