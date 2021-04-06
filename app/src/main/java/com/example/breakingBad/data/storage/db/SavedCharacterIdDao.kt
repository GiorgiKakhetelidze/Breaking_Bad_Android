package com.example.breakingBad.data.storage.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.breakingBad.data.storage.db.entities.SavedCharacterIdEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedCharacterIdDao {

    @Query("select* from SavedCharacterIdEntity")
    fun getSavedCharacters(): List<SavedCharacterIdEntity>

    @Query("select * from SavedCharacterIdEntity")
    fun getSavedCharacterFlow(): Flow<List<SavedCharacterIdEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(ids: List<SavedCharacterIdEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(id: SavedCharacterIdEntity)

    @Delete
    fun delete(vararg id : SavedCharacterIdEntity)

    @Query("delete from SavedCharacterIdEntity")
    fun deleteAll()

}