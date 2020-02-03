package com.kenji.waris.model

/**
 * The deceased's legacy details.
 */
class Legacy {
    var total = 0.0

    var funeralCosts = 0.0

    var debts = mutableListOf<Debt>()
    val debtAmount: Double
        get() {
            var amount = 0.0
            debts.forEach { amount += it.amount }
            return amount
        }

    var wills = mutableListOf<Will>()
    val willAmount: Double
        get() {
            var amount = 0.0
            wills.forEach { amount += it.amount }
            return amount
        }

    val shareable: Double
        get() = total - debtAmount - willAmount - funeralCosts

    var primaryShared = 0.0

    val secondaryShareable: Double
        get() = shareable - primaryShared
}