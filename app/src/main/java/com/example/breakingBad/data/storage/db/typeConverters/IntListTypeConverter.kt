package com.example.breakingBad.data.storage.db.typeConverters

import androidx.room.TypeConverter

class IntListTypeConverter {

    @TypeConverter
    fun stringToIntList(string: String): List<Int> {
        val stringList = string.split("|")
        val convertedList = mutableListOf<Int>()
        stringList.forEach {
            convertedList.add(it.toInt())
        }
        return convertedList.toList()
    }

    @TypeConverter
    fun intListToString(int: List<Int>): String {
        return int.joinToString("|")
    }

}