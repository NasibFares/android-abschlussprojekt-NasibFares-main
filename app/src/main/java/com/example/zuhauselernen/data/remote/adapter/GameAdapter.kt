package com.example.zuhauselernen.data.remote.adapter


import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.zuhauselernen.data.remote.GameViewModel
import com.example.zuhauselernen.data.remote.model.Game
import com.examples.zuhauselernen.R
interface GameItemClickListener {
    fun onFavoriteClicked(game: Game)
}

/**
Der Adapter erweitert die RecyclerView.Adapter Klasse und verwendet den ItemViewHolder für die
Verwaltung der einzelnen Listenelemente.
 */
class GameAdapter(
    private val viewModel: GameViewModel,
    private var dataset: List<Game>,
) : RecyclerView.Adapter<GameAdapter.ItemViewHolder>() {

    var gameItemClickListener: GameItemClickListener? = null

    /**
    Die Klasse ItemViewHolder stellt einen einzelnen Listeneintrag dar und enthält Referenzen auf
    die Views innerhalb des Eintrags, wie das (ImageView) für das [Game-Bild], den (TextView) für den
    [Titel des Spiels]  sowie (Buttons) zum Speichern und Löschen.
     */
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivGame: ImageView = itemView.findViewById(R.id.ivGame)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitleGame)
        val btnSave: ImageButton = itemView.findViewById(R.id.btnSaveGame)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteGame)
        val tvGameUrl: TextView =itemView.findViewById(R.id.tv_gameUrl)
        val tvGamePlatform: TextView =itemView.findViewById(R.id.tv_Platform)
        val tvPublisher: TextView =itemView.findViewById(R.id.tv_publisher)
        val tvReleaseDate: TextView =itemView.findViewById(R.id.tv_releaseDate)
        val tvGameDatabaseId: TextView =itemView.findViewById(R.id.tv_gameDBid)
        val tvGerne: TextView =itemView.findViewById(R.id.tv_category)
        val tvDescription: TextView =itemView.findViewById(R.id.tv_gameDescription)
        val markGameAsFavourite:ImageButton=itemView.findViewById(R.id.btnMarkGameAsFavourite)

    }
    /**
    Die Methode onCreateViewHolder wird aufgerufen, wenn neue ViewHolder erstellt werden müssen.
    Sie erstellt einen neuen ItemViewHolder, indem sie das Layout des Listeneintrags (game_list)
    aufbläst und als Parameter den parent ViewGroup und viewType verwendet.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_list, parent, false)
        return ItemViewHolder(itemLayout)
    }
    /**
    Die Methode onBindViewHolder wird aufgerufen, um den Inhalt eines ViewHolder an einer bestimmten
    Position in der Liste zu binden. Hier werden die entsprechenden Daten aus dem Dataset abgerufen
    und in die entsprechenden Views des ViewHolders gesetzt.
    - Das Spiel-Bild wird mit Hilfe der Coil-Bibliothek geladen, wobei auch eine abgerundete
    Ecken-Transformation angewendet wird.
    - Der Titel des Spiels wird in den TextView gesetzt. Es werden auch Click Listener für die Speichern-
    und Löschen-Buttons festgelegt, um die entsprechenden Aktionen auszuführen.
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        /**
        hole das gameItem aus dem dataset.
         */
        val game = dataset[position]

        /**
        baue eine URI aus der Bild URL.
         */
        val imgUri = game.thumbnail.toUri().buildUpon().scheme("https").build()
        /**
        lade das Bild mithilfe der URI in die ImageView und runde die Ecken ab.
         */
        holder.ivGame.load(imgUri) {
            error(R.drawable.ic_broken_image)
            transformations(RoundedCornersTransformation(10f))
        }
        /**
        Lade den Titel aus dem gameItem in das XML Element.
         */
        holder.tvTitle.text = game.title
        holder.tvGameUrl.text=game.gameUrl
        holder.tvGamePlatform.text=game.platform
        holder.tvPublisher.text=game.publisher
        holder.tvReleaseDate.text=game.releaseDate
        holder.tvGameDatabaseId.text=game.gameId.toString()
        holder.tvGerne.text=game.genre
        holder.tvDescription.text=game.description
        holder.tvGameUrl.text = game.gameUrl
        Linkify.addLinks(holder.tvGameUrl, Linkify.WEB_URLS)
        /**
        Setze einen Click Listener auf btnSave,der den aktuellen Titel in das game Objekt speichert.
         */
        holder.btnSave.setOnClickListener {
            game.title = holder.tvTitle.text.toString()
            viewModel.updateGame(game)
        }
        // Update the markGameAsFavourite view based on isFavourite
        val drawableId = if (game.isFavourite) R.drawable.clicked_heart_button else R.drawable.normal_heart_button
        holder.markGameAsFavourite.setBackgroundResource(drawableId)

        // Set a click listener for markGameAsFavourite
        holder.markGameAsFavourite.setOnClickListener {
            game.isFavourite = !game.isFavourite
            val newDrawableId = if (game.isFavourite) R.drawable.clicked_heart_button else R.drawable.normal_heart_button
            holder.markGameAsFavourite.setBackgroundResource(newDrawableId)
            gameItemClickListener?.onFavoriteClicked(game)
        }
        /**
         Wenn auf dem Hertz symbole geklickt , wird die Methode updateIsFavourite von viewModel
         aufgerufen um die Eigenschaft isFavourite zu aktualisieren.
         */
        holder.markGameAsFavourite.setOnClickListener {
            game.isFavourite = !game.isFavourite
            val drawableId = if (game.isFavourite) R.drawable.clicked_heart_button else R.drawable.normal_heart_button
            holder.markGameAsFavourite.setBackgroundResource(drawableId)
             gameItemClickListener?.onFavoriteClicked(game)
        }

        /**
        Setze einen Click Listener auf btnDelete,der die aktuelle Game löscht.
        1- viewModel.deleteGame() ruft die Methode deleteGame() vom GameViewModel
        1-1-deleteGame() im GameViewModel ruft innerhalb einer Coroutine die Methode deleteGame()
        von Repository.
        1-2-deleteGame() in Repository ruft die Methode deleteGame() innerhalb try-catch Block
        von gameDao.
        2- deleteGame(gameId) (Hilfsmethode) wird verwendet um game aus dem Dataset zu löschen.
         */
        holder.btnDelete.setOnClickListener {
            val gameId = game.gameId
            viewModel.deleteGame(gameId)
            deleteGame(gameId)
        }

        holder.markGameAsFavourite.setOnClickListener {
            game.isFavourite = !game.isFavourite
            val drawableId = if (game.isFavourite) R.drawable.clicked_heart_button else R.drawable.normal_heart_button
            holder.markGameAsFavourite.setBackgroundResource(drawableId)
            gameItemClickListener?.onFavoriteClicked(game) // Notify the click listener
        }




    }
    /**
    Die Methode getItemCount gibt die Anzahl der Elemente im Dataset zurück.
     */
    override fun getItemCount(): Int {
        return dataset.size
    }
    /**
    Diese Funktion wird verwendet, um den Dataset des Adapters mit einer neuen Liste von Games zu
    aktualisieren. Sie aktualisiert den dataset und benachrichtigt den Adapter über die Änderung,
    damit die Liste neu gerendert wird.
     */
    fun setGames(games: List<Game>) {
        dataset = games
        notifyDataSetChanged()
    }
    /**
    Diese private Hilfsfunktion wird aufgerufen, um ein Game aus dem Dataset zu löschen. Sie sucht
    das Game anhand der übergebenen gameId im dataset und entfernt es aus der Liste. Anschließend
    wird der Adapter über die Änderung benachrichtigt, indem notifyItemRemoved aufgerufen wird.
     */
    private fun deleteGame(gameId: Long) {
        val updatedList = dataset.toMutableList()
        val index = updatedList.indexOfFirst { it.gameId == gameId }
        if (index != -1) {
            updatedList.removeAt(index)
            dataset = updatedList
            notifyItemRemoved(index)
        }
    }

    /**
    Zusammenfassung:
    Der GameAdapter fungiert als Vermittler zwischen dem Dataset von Games und der RecyclerView.
    Er ist verantwortlich für das Erstellen der ViewHolder, das Binden der Daten an die Views und
    das Aktualisieren der Liste bei Änderungen.
     */
}