package com.example.zuhauselernen.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.zuhauselernen.SharedViewModel
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.adapter.CategoryAdapter
import com.example.zuhauselernen.data.local.adapter.UserSubjectsAdapter
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentUserSubjectsBinding
import com.google.android.material.navigation.NavigationView


class UserSubjectsFragment : Fragment() {
    private lateinit var binding: FragmentUserSubjectsBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private var userEmailUserSubjects=""
    private lateinit var userProfile: UserProfile
    private val PHOTO_PICKER_REQUEST_CODE = 1
    private lateinit var userProfilePhoto: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userEmailUserSubjects = it.getString("userEmailUserSubjects")!!

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_subjects, container, false)
        val view = binding.root
        binding.fabMenu.setOnClickListener {
            binding.drawer.open()
        }
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navigationView.setupWithNavController(navController)
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {

                    findNavController().navigate(UserSubjectsFragmentDirections.actionUserSubjectsFragmentToHomeFragment())
                    true
                }
                R.id.nav_logout -> {
                    activity?.finish()

                    true
                }
                R.id.nav_setting->{
                    findNavController().navigate(UserSubjectsFragmentDirections
                        .actionUserSubjectsFragmentToUserSettingFragment(emailUserSetting=userEmailUserSubjects))
                    true
                }
                R.id.nav_subscribe->{
                    findNavController().navigate(UserSubjectsFragmentDirections
                        .actionUserSubjectsFragmentToUserSubscriptionFragment(userEmailUserSubscription=userEmailUserSubjects))

                    true
                }
                R.id.nav_invoice->{
                    findNavController().navigate(UserSubjectsFragmentDirections
                        .actionUserSubjectsFragmentToInvoiceFragment(userEmailUserInvoices=userEmailUserSubjects))

                    true
                }
                R.id.nav_favourites->{
                    findNavController().navigate(UserSubjectsFragmentDirections
                        .actionUserSubjectsFragmentToUserFavouritesFragment(userEmailFavourites=userEmailUserSubjects))
                    true
                }


                else -> false
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userProfile = UserProfile(requireContext())
        val currentUser = userProfile.getUserProfileByEmail(userEmailUserSubjects)

        val navigationView: NavigationView = binding.navigationView
        val headerView: View = navigationView.getHeaderView(0)

        val userNameNavDrawer: TextView = headerView.findViewById(R.id.tv_userName)
        val emailNavDrawer: TextView = headerView.findViewById(R.id.tv_emailAdress)
        userProfilePhoto= headerView.findViewById(R.id.iv_UserProfilePhoto)

        userNameNavDrawer.text = currentUser?.firstName
        emailNavDrawer.text = userEmailUserSubjects

       if (currentUser?.userPhoto != null) {
            val userPhotoUri = Uri.parse(currentUser.userPhoto)
            Glide.with(this)
                .load(userPhotoUri)
                .transform(CircleCrop())
                .error(R.drawable.baseline_person_24)
                .into(userProfilePhoto)
        } else {

            userProfilePhoto.setImageResource(R.drawable.baseline_person_24)
        }

        userProfilePhoto.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(photoPickerIntent, PHOTO_PICKER_REQUEST_CODE)
        }

        binding.tvUserEmailUserSubjects.text = userEmailUserSubjects

        val userSubjects = userProfile.getUserSubjectsByEmail(userEmailUserSubjects)
        println("currentUserSubjects: $userSubjects")

        val userSubjectsAdapter = UserSubjectsAdapter(requireContext(), userSubjects) { clickedSubject ->
            val id = clickedSubject.subjectId
            binding.tvSubjectName.text = context?.getText(clickedSubject.subjectName.toInt())
            viewModel.getCategoriesBySubjectId(id)

            viewModel.categories.observe(viewLifecycleOwner) { categories ->
                val filteredList = categories.filter { it.categorySubject == id }
                binding.rvUserSubjectCategories.adapter = CategoryAdapter(requireContext(), filteredList)
            }
        }
        binding.rvUserSubjects.adapter = userSubjectsAdapter
        /**
        WERBUNG INNERHALB USERSUBJECTS-SCREEN ANPASSEN:
        Diese Anpassungen stellen sicher, dass die Animation erst ausgeführt wird, nachdem die
        Layout-Berechnung abgeschlossen ist und die korrekten Breitenwerte für die Animation
        verwendet werden.
         */
        /**
        Hier fügen wir einen OnGlobalLayoutListener zum viewTreeObserver des linearLayout hinzu.
        Der Listener reagiert auf das Abschlussereignis der Layout-Berechnung.
         */
        binding.constraintLayout.viewTreeObserver.addOnGlobalLayoutListener {
            /**
            Hier wird die Breite des linearLayout in Float umgewandelt und in der Variable
            parentWidth gespeichert.
             */
            val parentWidth = binding.constraintLayout.width.toFloat()

            /**
            Hier wird die Breite des tvGamesAdv in Float umgewandelt und negiert, um den
            anfänglichen X-Übersetzungswert zu bestimmen.
             */
            val initialX = -binding.tvGamesAdv.width.toFloat()

            /**
            Hier wird ein ObjectAnimator erstellt, der eine X-Übersetzung für tvGamesAdv von
            initialX zu parentWidth animiert.
             */
            val slideAnimation = ObjectAnimator.ofFloat(
                binding.tvGamesAdv,
                "translationX",
                initialX,
                parentWidth
            )
            /**
            Hier wird die Dauer der Animation auf 13000 Millisekunden (13 Sekunden) festgelegt.
             */
            slideAnimation.duration = 13000
            /**
            Hier wird eine Startverzögerung von 500 Millisekunden festgelegt.
             */
            slideAnimation.startDelay = 500
            /**
            Hier wird die Anzahl der Wiederholungen auf unendlich festgelegt.
             */
            slideAnimation.repeatCount = ValueAnimator.INFINITE
            /**
            Hier wird der Wiederholungsmodus auf RESTART festgelegt, sodass die Animation am Ende
            zurückspringt und erneut abgespielt wird.
             */
            slideAnimation.repeatMode = ValueAnimator.RESTART
            /**
            Hier wird der Interpolator auf einen linearen Interpolator festgelegt, um eine
            gleichmäßige Bewegung zu erzielen.
             */
            slideAnimation.interpolator = LinearInterpolator()
            /**
            Hier wird die Animation gestartet.
             */
            slideAnimation.start()
        }


        binding.tvGamesAdv.setOnClickListener {
            findNavController().navigate(UserSubjectsFragmentDirections.actionUserSubjectsFragmentToGameFragment(userEmailGames=userEmailUserSubjects))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHOTO_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {

                val currentUserEmail = userEmailUserSubjects
                userProfile.updateUserProfilePhoto(currentUserEmail, selectedImageUri.toString())

                Glide.with(this)
                    .load(selectedImageUri)
                    .transform(CircleCrop())
                    .into(userProfilePhoto)


            }
        }
    }


}