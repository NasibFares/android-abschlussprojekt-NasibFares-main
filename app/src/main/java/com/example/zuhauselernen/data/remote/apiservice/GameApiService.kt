package com.example.zuhauselernen.data.remote.apiservice

import com.example.zuhauselernen.data.remote.model.Game
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/**
Die Konstante BASE_URL enthält die Basis-URL der API, mit der die Verbindung hergestellt wird.
 */
const val BASE_URL = "https://www.freetogame.com/"
/**
Die Moshi-Instanz wird erstellt, um die JSON-Antworten des Servers in Kotlin-Objekte zu konvertieren.
Das KotlinJsonAdapterFactory wird hinzugefügt, um die Konvertierung für Kotlin-Klassen zu unterstützen.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
/**
Retrofit übernimmt die Kommunikation und übersetzt die Antwort
Die Retrofit-Instanz wird erstellt, um die Kommunikation mit dem Server zu handhaben.
Der MoshiConverterFactory wird als Konverter hinzugefügt, um die JSON-Antworten in Kotlin-Objekte
zu konvertieren. Die BASE_URL wird als Basis-URL für die API angegeben.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()
/**
Das Interface bestimmt, wie mit dem Server kommuniziert wird
Das Interface GameApiService definiert die Methoden, um mit dem Server zu kommunizieren. In diesem
Fall gibt es eine Methode getGames(), die die URL "get_games" spezifiziert und eine Liste von games
zurückgibt. Die Annotation @GET wird verwendet, um den HTTP-GET-Request zu definieren.
 */
interface GameApiService {
    /**
     * Diese Funktion spezifiziert die URL und holt die Liste an Informationen
     */
    @GET("api/games")
    suspend fun getGames(): List<Game>
}
/**Das Objekt dient als Zugangspunkt für den Rest der App und stellt den API Service zur Verfügung
Das Objekt GameApi dient als Zugangspunkt für den Rest der App. Es stellt den Retrofit-Service
GameApiService über die retrofitService-Eigenschaft bereit. Dabei wird die create()-Methode verwendet,
um eine Implementierung des GameApiService-Interfaces zu erstellen, die die Retrofit-Instanz verwendet.
 */
object GameApi {
    val retrofitService: GameApiService by lazy { retrofit.create(GameApiService::class.java) }
}