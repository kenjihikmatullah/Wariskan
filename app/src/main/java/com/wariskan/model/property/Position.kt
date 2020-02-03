package com.kenji.waris.model

import android.content.Context
import com.kenji.waris.model.Gender.*
import com.wariskan.R

/**
 * Position of person related to the deceased.
 */
enum class Position {
    DECEASED,
    DAD,
    MOM,
    HUSBAND,
    WIFE,
    CHILD,
    SIBLING,
    GRANDPA,
    GRANDMA,
    GRANDCHILD,
    UNCLE,
    MALE_COUSIN,
    NEPHEW;

    fun getName(it: Context): String {
        return when (this) {
            DECEASED -> it.getString(R.string.deceased)
            DAD -> it.getString(R.string.dad)
            MOM -> it.getString(R.string.mom)
            HUSBAND -> it.getString(R.string.husband)
            WIFE -> it.getString(R.string.wife)
            CHILD -> it.getString(R.string.child)
            SIBLING -> it.getString(R.string.sibling)
            GRANDPA -> it.getString(R.string.grandpa)
            GRANDMA -> it.getString(R.string.grandma)
            GRANDCHILD -> it.getString(R.string.grandchild)
            UNCLE -> it.getString(R.string.uncle)
            MALE_COUSIN -> it.getString(R.string.male_cousin)
            else -> it.getString(R.string.nephew)
        }
    }

    val isHavingStatus: Boolean
        get() = when (this) {
            DECEASED -> false
            else -> true
        }

    val isHavingFaith: Boolean
        get() = when (this) {
            DECEASED -> false
            else -> true
        }

    val isHavingGender: Boolean
        get() = when (this) {
            in listOf(DECEASED, CHILD, SIBLING, GRANDCHILD) -> true
            else -> false
        }

    companion object {
        fun getDefaultGender(position: Position) : Gender {
            return when (position) {
                DAD, HUSBAND, GRANDPA, UNCLE, MALE_COUSIN, NEPHEW -> MALE
                MOM, WIFE, GRANDMA -> FEMALE
                else -> FEMALE
            }
        }
    }

    val isHavingDelete: Boolean
        get() = when (this) {
            in listOf(DECEASED, DAD, MOM) -> false
            else -> true
        }

}