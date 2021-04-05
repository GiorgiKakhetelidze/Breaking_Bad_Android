package com.example.breakingBad.data.storage.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedCharacterIdEntity(
    @PrimaryKey
    val charId: Int,
)
