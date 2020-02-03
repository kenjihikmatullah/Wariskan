package com.kenji.waris.util

import com.kenji.waris.model.Heir


/**
 * Get a heir.
 */
//fun MutableList<Heir>.getById(id: Int): Heir {
//    var heir = Heir()
//
//    forEach {
//        if (it.id == id) {
//            heir = it
//            return@forEach
//        }
//    }
//
//    return heir
//}
//
///**
// * Set a heir
// */
//fun MutableList<Heir>.setById(id: Int, heir: Heir) {
//    for (i in 0 until size) {
//        if (this[i].id == id) {
//            this[i] = heir
//            return
//        }
//    }
//}
//
///**
// * Delete a heir
// */
//fun MutableList<Heir>.deleteById(id: Int) {
//    forEach { heir ->
//        if (heir.id == id)
//            remove(heir)
//    }
//}

/**
 * Get eligible heirs
 */
fun List<Heir>.thatEligible() = filter {
    it.eligible
}

/**
 * Returns whether
 * there is eligible heir in heir list
 */
fun List<Heir>.thatEligibleIsExist() = thatEligible().isNotEmpty()

/**
 * Return wheter
 * there are two or more eligible heir in heir list
 */
fun List<Heir>.thatEligibleIsMany() = thatEligible().size > 1


