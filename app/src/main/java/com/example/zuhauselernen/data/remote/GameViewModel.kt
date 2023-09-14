package com.example.zuhauselernen.data.remote

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.zuhauselernen.data.remote.apiservice.GameApi
import com.example.zuhauselernen.data.remote.model.Game
import kotlinx.coroutines.launch

const val TAG = "ViewModelTAG"

/**
Dies ist der Konstruktor des ViewModels, der eine Instanz der Anwendung erhält und an den
Elternkonstruktor AndroidViewModel weiterleitet.
 */
class GameViewModel(application: Application) : AndroidViewModel(application) {
    /**
    Hier wird eine Instanz der GameDatabase erstellt, um auf die Datenbank zuzugreifen. Die Funktion
    getDatabase(application) wird aufgerufen, um die Datenbankinstanz zu erhalten.
     */
    private val database = GameDatabase.getDatabase(application)
    /**
    hier wird eine AppRepository Instanz erstellt, mit dem Parameter GameApi
    Hier wird eine Instanz des AppRepository erstellt. Es wird die GameApi-Instanz übergeben, um
    auf die Game-Datenquelle zuzugreifen, und die erstellte Datenbankinstanz.
     */
    private val repository = AppRepository(GameApi, database)
    /**
    hier werden die Games aus dem repository in einer eigenen Variablen gespeichert.
    Diese Variable speichert die Games aus dem Repository. Sie kann von der Activity oder dem
    Fragment, das das ViewModel verwendet, beobachtet werden, um auf Änderungen der Games zuzugreifen.
     */
    val games = repository.games
    private val _currentGame = MutableLiveData<Game?>()
    private val _favouritesGames = MutableLiveData<List<Game>>(emptyList())
    val favouritesGames: LiveData<List<Game>>
        get() = _favouritesGames
    private val currentGame: MutableLiveData<Game?>
        get() = _currentGame

    fun onGameSelected(game: Game, isFavourite: Boolean) {
        if (isFavourite) {
            currentGame.value = game

        } else {

            if (currentGame.value == game) {

                currentGame.value = null
            }
        }
    }

    fun getFavouritesGames(
    ) {
        val games = repository.games.value!!
        val filteredGames =
            games.filter { it.isFavourite }
        _favouritesGames.value = filteredGames
    }

    /**
    Diese Funktion ruft die Repository-Funktion zum Laden der Games innerhalb einer Coroutine auf.
    Diese Funktion lädt die Games aus der Datenquelle. Sie wird in einer Coroutine ausgeführt, um
    die Funktion asynchron auszuführen. Zuerst werden alle vorhandenen Games aus der Datenbank
    gelöscht, und dann werden die Games aus der Game-Datenquelle abgerufen und in die Datenbank
    gespeichert.
     */
    fun loadData() {
        viewModelScope.launch {
            repository.deleteAllGames()
            repository.getGames()
        }
    }

    /**
    Diese Funktion löscht ein Game anhand seiner ID. Sie wird ebenfalls in einer Coroutine ausgeführt,
    um die Funktion asynchron auszuführen. Die Funktion ruft die entsprechende Funktion im
    AppRepository auf, um das Game aus der Datenbank zu löschen.
     */
    fun deleteGame(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteGame(id)
                Log.e(TAG, "Game with ID: $id was deleted")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete Game with ID: $id", e)

            }
        }
    }

    /**
    Diese Funktion aktualisiert ein Game mit den übergebenen Daten. Sie wird ebenfalls in einer
    Coroutine ausgeführt und ruft die entsprechende Funktion im AppRepository auf, um das Game in
    der Datenbank zu aktualisieren.
     */
    fun updateGame(game: Game) {
        viewModelScope.launch {
            repository.updateGame(game)
        }
    }

    fun saveCurrentGame(gameId: Long) {
        viewModelScope.launch {
            repository.insertCurrentGame(gameId)
        }
    }

    fun addToFavourites(game: Game) {
        viewModelScope.launch {
            repository.insertToFavourites(game)
        }
        /**
        Zusammenfassung:
        Das GamesViewModel stellt somit eine Schnittstelle zwischen der Datenquelle (Repository) und
        der Benutzeroberfläche (Activity oder Fragment) dar. Es ermöglicht das Laden, Löschen und
        Aktualisieren von Games und stellt die Games für die Anzeige bereit, indem es die Games aus
        dem Repository beobachtet und bei Änderungen aktualisiert.
         */
    }


}