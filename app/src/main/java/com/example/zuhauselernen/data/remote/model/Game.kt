package com.example.zuhauselernen.data.remote.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "game_table")
data class Game(
    @PrimaryKey(autoGenerate = true)
    @Json(name = "id")
    val gameId: Long,

    var title: String,
    val thumbnail: String,
    @Json(name="short_description")
    val description: String,
    @Json(name="game_url")
    val gameUrl: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    var developer: String,
    @Json(name="release_date")
    val releaseDate: String,
    @Json(name="freetogame_profile_url")
    val profileUrl: String,

    var isFavourite: Boolean= false
)
