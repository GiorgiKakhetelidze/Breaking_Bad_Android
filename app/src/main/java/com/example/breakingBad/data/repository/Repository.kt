package com.example.breakingBad.data.repository

import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.data.storage.DataStore
import com.example.breakingBad.data.storage.db.entities.SavedCharacterIdEntity

object Repository {

    suspend fun checkSavedIdsValidity(): Boolean =
        System.currentTimeMillis() - DataStore.lastTimeSavedCardsFetched < 60 * 60 * 1000

    suspend fun invalidateSavedIds() {
        DataStore.lastTimeSavedCardsFetched = 0
    }

    suspend fun getLocalCharacterById(id: Int): Character? {
        return DataStore.db.getCharacterDao().getById(id)
    }

    suspend fun getLocalSavedCharacterIds() =
        DataStore.db.getSavedCharactersDao().getSavedCharacters().map { it.charId }

    suspend fun updateRemoteSavedCharacters(): List<Int> {
        val ids = NetworkClient.userService.getUserCharacters().map { it }
        DataStore.db.getSavedCharactersDao().insert(ids = ids.map { SavedCharacterIdEntity(it) })
        DataStore.lastTimeSavedCardsFetched = System.currentTimeMillis()
        return ids
    }

    suspend fun getRemoteCharacterById(id: Int): Character {
        return NetworkClient.characterService.getCharacterBydId(id).first()
            .also {
                DataStore.db.getCharacterDao().insert(it)
            }
    }
}