package com.wariskan.util

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

const val ID = "ID"
const val POSITION = "POSITION"
const val ORDER = "ORDER"

const val TO = "TO"

const val ADMOB_ID = "ca-app-pub-3178233257268861~5817631177"

val MIGRATION_to_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
    }
}