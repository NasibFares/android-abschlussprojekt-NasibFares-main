package com.example.zuhauselernen.data.remote

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zuhauselernen.data.remote.model.Game

/**
Die Annotation @Database wird verwendet, um die Datenbankkonfiguration für Room zu definieren. In
diesem Fall wird festgelegt, dass die Datenbank eine einzige Entity namens GameData enthält
und die Version der Datenbank auf 3 festgelegt ist.
 */
@Database(entities = [Game::class], version = 4)
/**
Die Klasse GameDatabase wird als abstract class deklariert, was bedeutet, dass sie nicht direkt
instanziiert werden kann, sondern von einer Unterklasse erweitert werden muss.
 */
abstract class GameDatabase: RoomDatabase() {
    /**
    Innerhalb der GameDatabase-Klasse gibt es eine abstrakte Eigenschaft gameDao vom Typ GameDao.
    GameDao ist ein Interface, die die Datenzugriffsobjekt (DAO)-Methoden definiert, um die
    GameData-Entität in der Datenbank zu manipulieren. Die tatsächliche Implementierung von GameDao
    wird von einer Unterklasse von GameDatabase bereitgestellt.
     */
    abstract val gameDao:GameDao
    /**
    Der Block companion object enthält die Funktion getDatabase, die eine Instanz der Klasse
    GameDatabase liefert. Es handelt sich um eine statische Methode, die einen Context-Parameter
    entgegennimmt, um auf den Anwendungskontext zuzugreifen.
     */
    companion object{
        private lateinit var gameInstance:GameDatabase
        fun getDatabase(context: Context):GameDatabase{
            /**
            Der synchronized(this)-Block stellt sicher, dass nur ein Thread gleichzeitig auf den
            kritischen Codebereich zugreifen kann. Dies ist wichtig, da mehrere Threads gleichzeitig
            auf die Datenbank zugreifen können, was zu Dateninkonsistenzen oder anderen Problemen
            führen könnte.
             */
            synchronized(this) {
                /**
                Innerhalb des synchronized-Blocks wird überprüft, ob GameInstance initialisiert wurde.
                Falls nicht, wird sie initialisiert, indem Room.databaseBuilder() aufgerufen wird.
                Room.databaseBuilder() ist eine Methode, die von Room bereitgestellt wird und eine neue
                Instanz von RoomDatabase unter Verwendung der angegebenen Parameter erstellt.
                Es werden die folgenden Parameter übergeben:
                a- Der Anwendungskontext.
                b- Die Klasse der RoomDatabase-Unterklasse (GameDatabase in diesem Fall).
                c- Der Name der Datenbankdatei ("game_database" in diesem Fall).
                 */
                if(!this::gameInstance.isInitialized){
                    gameInstance= Room.databaseBuilder(
                        context.applicationContext,
                        GameDatabase::class.java,
                        "game_database"
                    )
                        /**
                        -Wichtig-
                        Mit allowMainThreadQueries() wird auf der databaseBuilder-Instanz aufgerufen,
                        wodurch Room Datenbankabfragen auf dem Hauptthread erlaubt. Dies wird im
                        Allgemeinen nicht empfohlen, da dies zu einer schlechten Benutzererfahrung
                        und möglichen ANR (Application Not Responding)-Fehlern führen kann. Es ist
                        besser, asynchrone Abfragen mit Rückrufen oder Coroutines zu verwenden.
                        Schließlich wird build() auf der databaseBuilder-Instanz aufgerufen, um die
                        tatsächliche Datenbankinstanz zu erstellen.
                         */
                        .allowMainThreadQueries().build()
                }
                /**
                Die initialisierte gameInstance wird gespeichert und von der getDatabase-Funktion
                zurückgegeben.
                 */
                return gameInstance
            }
        }
    }
    /**
    Zusammenfassend richtet dieser Code die Datenbankkonfiguration mit den Annotationen von Room ein
    und stellt eine statische Methode bereit, um eine Instanz der GameDatabase-Klasse zu erhalten.
    Die GameDatabase-Klasse selbst ist abstrakt und muss erweitert werden, um die Implementierung der
    Datenzugriffsobjekt (DAO)-Methoden bereitzustellen.
     */
}