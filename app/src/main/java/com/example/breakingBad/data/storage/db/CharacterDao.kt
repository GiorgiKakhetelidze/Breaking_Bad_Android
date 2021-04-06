package com.example.breakingBad.data.storage.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.breakingBad.data.models.character.Character

@Dao
interface CharacterDao {

    @Query("select* from character")
    suspend fun getAll(): List<Character>

    @Query("select * from character where charId=:id")
    suspend fun getById(id: Int): Character?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(character: Character)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(character: List<Character>)

}