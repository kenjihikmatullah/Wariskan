package com.kenji.waris.model

import androidx.room.Embedded

class Deceased : Person() {
    @Embedded(prefix = "inheritanceOut_")
    var legacy = Legacy()

    fun resetShared() {
        legacy.primaryShared = 0.0
    }
}