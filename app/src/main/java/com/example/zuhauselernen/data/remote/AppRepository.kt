package com.example.zuhauselernen.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.zuhauselernen.data.remote.apiservice.GameApi
import com.example.zuhauselernen.data.remote.model.Game

const val TAG1 = "GameRepositoryTAG"
/**
 * Diese Klasse holt die Informationen und stellt sie mithilfe von Live Data dem Rest
der App zur Verfügung.
- Die Klasse hat ein Konstruktorargument api, das eine Instanz der GameApi-Klasse ist.
Dies ermöglicht den Zugriff auf den API-Service.

- Die Klasse hat auch ein Konstruktorargument database, das eine Instanz der GameDatabase-Klasse ist.
Dies ermöglicht den Zugriff auf die lokale Datenbank.
 */
class AppRepository(private var api: GameApi, private var database: GameDatabase) {
    /** Die LiveData Variable games enthält die Liste aus dem API call
    Die GameRepository-Klasse enthält eine private MutableLiveData-Variable _games, die eine Liste von
    games enthält. Durch die Verwendung von MutableLiveData kann die Liste von games aktualisiert
    werden und die Änderungen werden automatisch an alle Observer weitergegeben. Die öffentliche
    LiveData-Eigenschaft games gibt die _games-Variable nur lesbar zurück.
     */
    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>>
        get() = _games
    private var _game = MutableLiveData<Game>()
    val game: LiveData<Game>
        get() = _game
    /**
     * Diese Funktion ruft die Daten aus dem API Service ab und speichert die Antwort in der
     * Variable games. Falls der Call nicht funktioniert, wird die Fehlermeldung geloggt
     */
    suspend fun getGames() {
        Log.e(TAG1, "get Games")
        try {
            /**
            Die getGames()-Funktion ruft die Daten aus dem API-Service ab und speichert die Antwort
            in der _games-Variable.
            Dabei wird die Funktion getGames() des API-Service aufgerufen, um die games vom Server
            abzurufen. Die erhaltenen games werden anschließend zufällig sortiert und in der
            _games-Variable gespeichert.
             */
            val gameData = api.retrofitService.getGames()
            _games.value = gameData.shuffled()
            /**
            Schließlich wird die insertAll()-Funktion des gameDao-Objekts aufgerufen, um die games
            in die lokale Datenbank einzufügen. Der Parameter games.value!! gibt die aktuelle Liste
            der games aus der _games-Variablen zurück. Das !!-Operator stellt sicher, dass der Wert
            nicht null ist, da _games eine MutableLiveData-Variable ist und nicht null sein kann.
             */
            database.gameDao.insertAll(games.value!!)
            /**
            Falls ein Fehler beim Abrufen der Daten auftritt, wird die Fehlermeldung geloggt.
             */
        } catch (e: Exception) {
            Log.e(TAG1, "Error loading Games from API: $e")
        }
    }
    /**
    Die deleteAllGames()-Funktion löscht alle vorhandenen games aus der lokalen Datenbank. Dabei wird
    die deleteAll()-Funktion des GameDao-Objekts aufgerufen, das den Zugriff auf die Datenbank ermöglicht.
     */
    suspend fun deleteAllGames() {
        try {
            database.gameDao.deleteAll()
        } catch (e: Exception) {
            Log.e(TAG1, "Failed to delete existing GAMES from the database: $e")
        }
    }
    /**
    Die deleteGame(id:Long)-Funktion löscht ein spezifisches Game aus der lokalen Datenbank.
    Dabei wird die deleteGame()-Funktion des GameDao-Objekts aufgerufen und das entsprechende Game
    anhand seiner ID gelöscht.
     */
    suspend fun deleteGame(id:Long){
        try{
            database.gameDao.deleteGame(id)
        }catch(e: Exception){
            Log.e(TAG1,"Failed to delete GAME!!")
        }
    }
    /**
    Die updateGame(game:Game)-Funktion aktualisiert ein spezifisches Game in der lokalen Datenbank.
    Dabei wird die update()-Funktion des GameDao-Objekts aufgerufen und das Game aktualisiert.
     */
    suspend fun updateGame(game: Game){
        try{
            database.gameDao.update(game)
        }catch(e: Exception){
            Log.e(TAG1,"Failed to update GAME!!")
        }
    }
    suspend fun insertCurrentGame(id:Long) {
        try {
            database.gameDao.insertGame(_game.value!!)
        } catch (e: java.lang.Exception) {
            Log.e("ERROR", "${e.message}")
        }
    }
    /*suspend fun getFavouriteGames(): LiveData<List<Game>> {
        return database.gameDao.getFavouriteGames()
    }*/
    suspend fun insertToFavourites(game: Game) {
        try {

        } catch (e: Exception) {
            Log.e(TAG1, "Failed to add game to favourites: $e")
        }
    }


}