package com.example.breakingBad.data.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.storage.db.typeConverters.IntListTypeConverter
import com.example.breakingBad.data.storage.db.typeConverters.StringListTypeConverter

@Database(entities = [Character::class], version = 1)
@TypeConverters(StringListTypeConverter::class, IntListTypeConverter::class)
abstract class CharacterDb : RoomDatabase() {
    abstract fun getCharacterDao(): CharacterDao
}