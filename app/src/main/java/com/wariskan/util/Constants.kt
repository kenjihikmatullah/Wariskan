package com.wariskan.util

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.text.NumberFormat
import java.text.NumberFormat.getNumberInstance
import java.util.*
import kotlin.math.floor

const val ID = "ID"
const val POSITION = "POSITION"
const val ORDER = "ORDER"

const val TO = "TO"

const val ADMOB_ID = "ca-app-pub-3178233257268861~5817631177"

val MIGRATION_to_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

    }
}

fun getLocale(conf: Configuration): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        conf.locales[0]
    } else {
        conf.locale;
    }
}

/**
 * Get number
 * with comma/point every 3 digits,
 * floored, and converted to string
 */
fun getNumber(res: Resources, double: Double) : String {
    return getNumberInstance(getLocale(res.configuration))
        .format(floor(double))
}

fun String.getStringNoComma() : String {
    return if (contains(",")) {
        replace(",", "")

    } else if (contains(".")) {
        replace(".", "")

    } else {
        this
    }
}