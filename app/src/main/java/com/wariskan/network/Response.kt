package com.wariskan.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response(

    @Json(name = "inheritances")
    val inheritances: List<InheritanceNetwork>
)