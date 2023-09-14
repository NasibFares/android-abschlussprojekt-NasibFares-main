package com.example.zuhauselernen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.adapter.FavouritesAdapter
import com.example.zuhauselernen.data.remote.GameViewModel
import com.example.zuhauselernen.data.remote.helper.GamesTouchHelper
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentUserFavouritesBinding

class UserFavouritesFragment : Fragment() {
    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: FragmentUserFavouritesBinding
    private var userEmailFavourite: String = ""
    private lateinit var userProfile: UserProfile
    private lateinit var adapter: FavouritesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userEmailFavourite = it.getString("userEmailFavourites").toString()
        }
    }
    /**
    Hier wird das binding initialisiert und das Layout gebaut
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }
    /**
     Hier werden die Elemente eingerichtet und z.B. onClickListener gesetzt
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userProfile = UserProfile(requireContext().applicationContext)
        val currentUser = userProfile.getUserProfileByEmail(userEmailFavourite)
        val favouritesGames = userProfile.getUserFavorites(currentUser!!)

        val adapter = FavouritesAdapter(requireContext(), favouritesGames, viewModel)
        binding.rvFavourites.adapter = adapter
        binding.tvUserEmailFavourites.text = userEmailFavourite
        binding.rvFavourites.setHasFixedSize(true)
        /**
        PagerSnapHelper ist eine Implementierung des SnapHelper-Interfaces in Android, die dazu
        verwendet wird, das Scrollen in einem RecyclerView zu erleichtern, sodass die Elemente beim
        Scrollen automatisch in der Mitte des Bildschirms "snappen" (positioniert werden).
        helper wird erstellt und mit binding.rvFavourites (dem RecyclerView) verbunden. Dies bewirkt,
        dass die Elemente im RecyclerView so gescrollt werden, dass sie perfekt in der Mitte des
        Bildschirms positioniert werden.
         */
        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(binding.rvFavourites)
        /**
        GamesTouchHelper ist eine benutzerdefinierte Implementierung eines ItemTouchHelper.SimpleCallback,
        die für das Verschieben oder Entfernen von Elementen in einem RecyclerView zuständig ist.
        Eine Instanz von gamesTouchHelper wird erstellt und einem Lambda-Ausdruck übergeben, der beim
        Verschieben oder Entfernen eines Elements aufgerufen wird.
        Der übergebene Parameter position gibt die Position des verschobenen oder entfernten Elements
        im RecyclerView an.

         */
        val gamesTouchHelper = GamesTouchHelper { position ->
            /**
            Die Behandlung  der Entfernung von Elementen
             */
            val removedGame = favouritesGames.removeAt(position)
            adapter.notifyItemRemoved(position)
            userProfile.removeGameFromUserProfile(currentUser, removedGame.gameId)
        }
        /**
        gamesTouchHelper wird dann mit binding.rvFavourites (dem RecyclerView) verbunden, um die
        Swipe- und Drag-and-Drop-Funktionalität für die Elemente in diesem RecyclerView zu ermöglichen.
         */
        gamesTouchHelper.attachToRecyclerView(binding.rvFavourites)
        viewModel.games.observe(
            viewLifecycleOwner
        ) { _ ->

            binding.rvFavourites.adapter?.let { adapter ->

                if (adapter is FavouritesAdapter) {
                    binding.rvFavourites.adapter =
                        FavouritesAdapter(requireContext(), favouritesGames, viewModel)
                }
            } ?: run {

            }
        }
        binding.bottomNavViewUserFavourites.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    findNavController().navigate(
                        UserFavouritesFragmentDirections
                            .actionUserFavouritesFragmentToHomeFragment()
                    )
                    true
                }
                R.id.logout -> {
                    requireActivity().finishAffinity()
                    true
                }
                R.id.back -> {
                    findNavController().navigateUp()
                }
                else -> {
                    true
                }
            }

        }
    }
}