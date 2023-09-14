package com.example.zuhauselernen.data.remote

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.zuhauselernen.data.remote.model.Game

@Dao
interface GameDao {
    /**
    Games in der Liste hinzufügen.
    Die Funktion nimmt einen Parameter games vom Datentyp List<Game>
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<Game>)

    /**
    Einen Game aktualisieren.
    Die Methode nimmt einen Parameter game vom Datentyp Game
     */
    @Update
    suspend fun update(game: Game)

    /**
    Die Methode gibt eine LiveData verpackte Liste von allen GameData zurück.
     */
    @Query("SELECT * FROM game_table")
    fun getAll(): LiveData<List<Game>>

    /**
    Alle games löschen.
     */
    @Query("DELETE FROM game_table")
    suspend fun deleteAll()

    /**
    Die Methode löscht einen Game anhand seiner id.
    Die Methode nimmt einen Parameter id vom Datentyp Long
     */
    @Query("DELETE  FROM game_table WHERE gameId= :id ")
    suspend fun deleteGame(id: Long)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: Game)
    @Delete
    suspend fun deleteCurrentGame(meal: Game)
    @Query("SELECT * FROM game_table")
    fun getAllGamesLive(): LiveData<List<Game>>



}
