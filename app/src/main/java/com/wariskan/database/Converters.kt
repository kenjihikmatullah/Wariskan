package com.kenji.waris.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kenji.waris.model.*
import com.wariskan.model.person.*
import com.wariskan.model.property.Type

class Converters {

    private val gson = Gson()

    /*
     * Position
     */
    @TypeConverter fun stringToPosition(value: String): Position {
        val type = object : TypeToken<Position>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun positionToString(value: Position): String {
        return gson.toJson(value)
    }

    /*
     * Gender
     */
    @TypeConverter fun stringToGender(value: String) : Gender {
        val type = object : TypeToken<Gender>(){}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun genderToString(value: Gender) : String {
        return gson.toJson(value)
    }

    /*
     * Type
     */
    @TypeConverter fun stringToType(value: String) : Type {
        val type = object : TypeToken<Type>(){}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun typeToString(value: Type) : String {
        return gson.toJson(value)
    }

    /*
     * Heir list
     */
    @TypeConverter fun stringToHeirList(value: String): MutableList<Heir> {
        val type = object : TypeToken<MutableList<Heir>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun HeirListToString(value: MutableList<Heir>): String {
        return gson.toJson(value)
    }

    /*
     * Debt list
     */
    @TypeConverter fun stringToDebtList(value: String): MutableList<Debt> {
        val type = object : TypeToken<MutableList<Debt>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun debtListToString(value: MutableList<Debt>): String {
        return gson.toJson(value)
    }

    /*
     * Will list
     */
    @TypeConverter fun stringToWillList(value: String): MutableList<Will> {
        val type = object : TypeToken<MutableList<Will>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun willListToString(value: MutableList<Will>): String {
        return gson.toJson(value)
    }

    /*
     * Sibling list
     */
    @TypeConverter fun stringToSiblingList(value: String): MutableList<Sibling> {
        val type = object : TypeToken<MutableList<Sibling>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun siblingListToString(value: MutableList<Sibling>): String {
        return gson.toJson(value)
    }

    /*
     * Grandpa list
     */
    @TypeConverter fun stringToGrandpaList(value: String): MutableList<Grandpa> {
        val type = object : TypeToken<MutableList<Grandpa>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun grandpaListToString(value: MutableList<Grandpa>): String {
        return gson.toJson(value)
    }

    /*
     * Grandma list
     */
    @TypeConverter fun stringToGrandmaList(value: String): MutableList<Grandma> {
        val type = object : TypeToken<MutableList<Grandma>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun grandmaListToString(value: MutableList<Grandma>): String {
        return gson.toJson(value)
    }

    /*
     * Grandchild list
     */
    @TypeConverter fun stringToGrandchildList(value: String): MutableList<Grandchild> {
        val type = object : TypeToken<MutableList<Grandchild>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun grandchildListToString(value: MutableList<Grandchild>): String {
        return gson.toJson(value)
    }

    /*
     * Uncle list
     */
    @TypeConverter fun stringToUncleList(value: String): MutableList<Uncle> {
        val type = object : TypeToken<MutableList<Uncle>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun uncleListToString(value: MutableList<Uncle>): String {
        return gson.toJson(value)
    }

    /*
     * Male cousin list
     */
    @TypeConverter fun stringToMaleCousinList(value: String): MutableList<MaleCousin> {
        val type = object : TypeToken<MutableList<MaleCousin>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun maleCousinListToString(value: MutableList<MaleCousin>): String {
        return gson.toJson(value)
    }

    /*
     * Nephew list
     */
    @TypeConverter fun stringToNephewList(value: String): MutableList<Nephew> {
        val type = object : TypeToken<MutableList<Nephew>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter fun nephewListToString(value: MutableList<Nephew>): String {
        return gson.toJson(value)
    }
}