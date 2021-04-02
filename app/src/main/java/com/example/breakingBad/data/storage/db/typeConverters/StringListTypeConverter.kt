package com.example.breakingBad.data.storage.db.typeConverters

import androidx.room.TypeConverter

class StringListTypeConverter {
    @TypeConverter
    fun toStringList(string : String):List<String>{
        return string.split("|").toList()
    }

    @TypeConverter
    fun listToString(string: List<String>): String {
        return string.joinToString("|")
    }
}