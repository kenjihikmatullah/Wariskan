package com.wariskan.util

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wariskan.network.User
import java.text.NumberFormat
import java.text.NumberFormat.getNumberInstance
import java.util.*
import kotlin.math.floor

const val ID = "ID"
const val POSITION = "POSITION"
const val ORDER = "ORDER"
const val TO = "TO"
const val ADMOB_ID = "ca-app-pub-3178233257268861~5817631177"

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

fun getWatcher(et: EditText, res: Resources) : TextWatcher {
    return object : TextWatcher {

        var lenBefore = 0
        var lenAfter = 0
        var lenBlocked = 0
        var selectionBefore = 0
        val selectionAfter: Int
            get() {
                val diffs = lenAfter - lenBefore
                return if (diffs >= 0) {
                    selectionBefore + diffs

                } else {
                    selectionBefore + diffs + lenBlocked
                }
            }

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
            s?.let {
                lenBefore = it.length
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            selectionBefore = start
            lenBlocked = before
        }

        override fun afterTextChanged(s: Editable?) {
            et.removeTextChangedListener(this)
            val text = et.text.toString().getStringNoComma()
            if (!text.isBlank() && text.toDouble() <= Double.MAX_VALUE) {
                val double = getNumber(res, text.toDouble())
                lenAfter = double.length
                et.setText(double)
                et.setSelection(selectionAfter)
            }
            et.addTextChangedListener(this)
        }
    }
}

// Temporary
var isLoggedIn = false
var user: User? = null