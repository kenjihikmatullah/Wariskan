package com.kenji.waris.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kenji.waris.model.Gender
import com.kenji.waris.model.Gender.MALE
import com.wariskan.R.drawable.*
import com.wariskan.R.string.*
import com.wariskan.R.string.alive
import com.wariskan.R.string.dead
import com.wariskan.util.getNumber
import kotlin.math.floor

@BindingAdapter("alive_adapter")
fun TextView.aliveAdapter(isAlive: Boolean = true) {
    text = if (isAlive) context.getString(alive)
    else context.getString(dead)
}

@BindingAdapter("faith")
fun ImageView.faithAdapter(isMuslim: Boolean = true) {
    if (isMuslim) setImageResource(ic_masjid)
    else setImageResource(ic_pray_color)
}

@BindingAdapter("grayscale_by_faith")
fun ImageView.faithGrayscale(isMuslim: Boolean = true) {
    if (isMuslim) setImageResource(ic_masjid_grayscale)
    else setImageResource(ic_pray_grayscale)
}

@BindingAdapter("muslim_adapter")
fun TextView.muslimAdapter(isMuslim: Boolean = true) {
    text = if (isMuslim) context.getString(islam)
    else context.getString(kafir)
}

@BindingAdapter("gender")
fun ImageView.fromGender(gender: Gender?) {
    if (gender == MALE) setImageResource(ic_muslim)
    else setImageResource(ic_muslimah)
}

@BindingAdapter("grayscale_by_gender")
fun ImageView.genderGrayscale(gender: Gender?) {
    if (gender == MALE) setImageResource(ic_male_grayscale)
    else setImageResource(ic_female_grayscale)
}

@BindingAdapter("gender_adapter")
fun TextView.genderAdapter(gender: Gender?) {
    text = if (gender == MALE) context.getString(male)
    else context.getString(female)
}

@BindingAdapter("double_adapter")
fun TextView.doubleAdapter(num: Double = 0.0) {

    val aThousand = 1000
    val aMillion = aThousand * 1000
    val aBillion = aMillion * 1000
    val aTrillion: Long = 1000 * aBillion.toLong()

    var shortened = num
    var unit = ""

    if (num < 1000) {
        0.inc()

    } else if (num >= 1000 && num < aMillion) {
        shortened = num / aThousand
        unit += context.getString(thousand)

    } else if (num < aBillion) {
        shortened = num / aMillion
        unit += context.getString(million)

    } else if (num < aTrillion) {
        shortened = num / aBillion
        unit += context.getString(billion)
    }

//    val text = "${floor(shortened)} $unit"
    val text = String.format("%.2f %s", shortened, unit)
    setText(text)
}

@BindingAdapter("number_format")
fun TextView.numberFormat(double: Double) {
    text = getNumber(resources, double)
}
//
//@BindingAdapter("icon_expanded")
//fun ImageButton.iconExpaned(isExpanded: Boolean) {
//    if (!isExpanded) setImageResource(baseline_expand_more_white_24)
//    else setImageResource(baseline_expand_less_white_24)
//}