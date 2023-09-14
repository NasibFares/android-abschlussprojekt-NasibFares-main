package com.example.zuhauselernen.data.remote.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.remote.GameViewModel
import com.example.zuhauselernen.data.remote.adapter.GameAdapter
import com.example.zuhauselernen.data.remote.adapter.GameItemClickListener
import com.example.zuhauselernen.data.remote.model.Game
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentGameBinding
class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: FragmentGameBinding
    private var userEmailGames: String = ""
   private lateinit var userProfile: UserProfile
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userEmailGames = it.getString("userEmailGames").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
        Die Methode „loadData()“ wird von „viewModel“ aus aufgerufen und löscht alle verwendeten
        Daten (Games),Repository.deleteAllGames()
        Sie lädt die Daten erneut herunter Repository.getGames()
         */
        viewModel.loadData()

        userProfile = UserProfile(requireContext().applicationContext)
        val currentUser = userProfile.getUserProfileByEmail(userEmailGames)

        binding.tvUserEmailGames.text = userEmailGames

        // Initialize the adapter and set the item click listener


        // Bottom navigation click listener
        binding.bottomNavViewGames.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    findNavController().navigate(GameFragmentDirections.actionGameFragmentToHomeFragment())
                }

                R.id.back -> {
                    findNavController().navigate(
                        GameFragmentDirections.actionGameFragmentToUserSubjectsFragment(
                            userEmailUserSubjects = userEmailGames
                        )
                    )
                }

                R.id.download -> {
                    viewModel.loadData()
                }

                R.id.favourites -> {
                    findNavController().navigate(
                        GameFragmentDirections.actionGameFragmentToUserFavouritesFragment(
                            userEmailFavourites = userEmailGames
                        )
                    )
                }
            }
            true
        }

        // Improved performance with a fixed-size RecyclerView
        binding.rvGames.setHasFixedSize(true)

        // SnapHelper for snapping to items
        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(binding.rvGames)


        // Initialize the adapter and set the item click listener
        val adapter = GameAdapter(viewModel, viewModel.games.value ?: emptyList())
        adapter.gameItemClickListener = object : GameItemClickListener {
            override fun onFavoriteClicked(game: Game) {
                val userFavourites = userProfile.getUserFavorites(currentUser!!)
                if(game.isFavourite){
                    if (game in userFavourites) {
                        Toast.makeText(
                            context,
                            "Das Spiel ${game.title} ist schon in Ihrer Spieleliste.",
                            Toast.LENGTH_SHORT
                        ).show()
                }else{
                        userProfile.addNewGameToUserProfile(currentUser, game)
                        println("UserFavourite=${userProfile.getUserFavorites(currentUser)}")
                    }

                } else {
                    userProfile.removeGameFromUserProfile(currentUser, game.gameId)
                    println("UserFavourite=${userProfile.getUserFavorites(currentUser)}")
                }
                adapter.notifyDataSetChanged()

                println("GameId=${game.gameId}")
                println("CurrentUser=$currentUser")
                val updatedUserFavorites = userProfile.getUserFavorites(currentUser)
                println("UserFavorites= $updatedUserFavorites")
            }
        }
        binding.rvGames.adapter = adapter
        viewModel.games.observe(
            viewLifecycleOwner
        ) { games ->
            /**
            Hier wird überprüft, ob bereits ein Adapter für die RecyclerView mit dem Namen rvGames
            festgelegt wurde. Das Fragezeichen (?.) prüft, ob der Adapter existiert. Wenn ja, wird
            der Codeblock im let-Teil ausgeführt, andernfalls wird der Codeblock im run-Teil ausgeführt.
             */
            binding.rvGames.adapter?.let { adapter ->
                /**
                Der Code im let-Teil überprüft, ob der aktuelle Adapter eine Instanz von GameAdapter
                ist. Dadurch wird sichergestellt, dass der Adapter die erforderlichen Methoden enthält.
                 */
                if (adapter is GameAdapter) {
                    /**
                    Wenn der aktuelle Adapter ein GameAdapter ist, wird die Methode setGames
                    aufgerufen und die neuen Games übergeben.
                    Diese Methode aktualisiert den Dataset des Adapters mit den neuen Games
                    und benachrichtigt den Adapter über die Änderung.
                     */
                    adapter.setGames(games)
                }
            } ?: run {
                /**
                Wenn noch kein Adapter für die RecyclerView festgelegt wurde, wird ein neuer
                GameAdapter erstellt und als Adapter für die RecyclerView rvGames zugewiesen.
                Der viewModel und die aktuellen Games werden als Parameter übergeben.
                 */
                binding.rvGames.adapter = GameAdapter(viewModel, games)
                /**
                Zusammenfassung:
                Insgesamt sorgt dieser Code dafür, dass die Games in der RecyclerView aktualisiert
                werden, wenn sich ihre Daten im GamesViewModel ändern. Wenn bereits ein Adapter
                vorhanden ist, wird der Dataset des Adapters aktualisiert. Andernfalls wird ein
                neuer Adapter erstellt und der RecyclerView zugewiesen.
                 */
            }


        }
    }




    }