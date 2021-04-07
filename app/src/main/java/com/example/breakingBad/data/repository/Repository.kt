package com.example.breakingBad.data.repository

import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.models.user.UserRegistrationRequest
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.data.storage.DataStore
import com.example.breakingBad.data.storage.db.entities.SavedCharacterIdEntity
import kotlinx.coroutines.flow.map

object Repository {

    fun checkSavedIdsValidity(): Boolean =
        System.currentTimeMillis() - DataStore.lastTimeSavedCardsFetched < 60 * 60 * 1000

    fun invalidateSavedIds() {
        DataStore.lastTimeSavedCardsFetched = 0
    }

    suspend fun getRemoteCharacterByName(name : String) : List<Character>{
       return NetworkClient.characterService.getCharacterByName(name)
    }

    suspend fun getLocalCharacterById(id: Int): Character? {
        return DataStore.db.getCharacterDao().getById(id)
    }

    suspend fun saveCard(character: Character) {
        NetworkClient.userService.saveUserCharacter(character.charId)
        DataStore.db.getSavedCharactersDao().insert(SavedCharacterIdEntity(character.charId))
    }

    suspend fun deleteCard(character: Character) {
        NetworkClient.userService.deleteUserCharacter(character.charId)
        DataStore.db.getSavedCharactersDao().delete(SavedCharacterIdEntity(character.charId))
    }

    fun getLocalSavedCharactersFlow() =
        DataStore.db.getSavedCharactersDao().getSavedCharacterFlow()
            .map { list -> list.map { it.charId } }

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

    suspend fun getRemoteAndSaveProfile() {
        DataStore.db.getUserProfileDao().insert(
            NetworkClient.userService.getUser()
        )
    }

    suspend fun getRemoteCharactersAndStore(
        limit: Int,
        offset: Int
    ): List<Character> {
        val characters = NetworkClient.characterService.getLimitedCharacters(
            limit = limit,
            offset = offset
        )
        DataStore.db.getCharacterDao().insert(characters)
        return characters
    }

    suspend fun loginAndSetToken(username: String, password: String) {
        NetworkClient.userService.login(
            username = username,
            password = password
        ).apply {
            DataStore.authToken = accessToken
        }
    }

    fun clearProfile() {
        DataStore.db.getUserProfileDao().delete()
    }

    fun clearSavedCards() =
        DataStore.db.getSavedCharactersDao().deleteAll()

    fun getLocalSavedCharacterIds() =
        DataStore.db.getSavedCharactersDao().getSavedCharacters().map { it.charId }

    suspend fun registerAndLogin(
        name: String,
        userName: String,
        password: String
    ) {
        NetworkClient.userService.register(
            UserRegistrationRequest(
                name = name,
                userName = userName,
                password = password
            )
        )
        NetworkClient.userService.login(
            username = userName,
            password = password
        ).accessToken.apply {
            DataStore.authToken = this
        }
    }
}