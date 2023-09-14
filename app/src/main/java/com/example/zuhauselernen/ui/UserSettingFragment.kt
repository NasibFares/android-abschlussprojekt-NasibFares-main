package com.example.zuhauselernen.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.zuhauselernen.FunctionUtils
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.model.UserData
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentUserSettingBinding


class UserSettingFragment : Fragment() {
    private lateinit var binding: FragmentUserSettingBinding
    private lateinit var currentUser: UserData
    private lateinit var userProfile: UserProfile
    private var userSettingEmail = ""
    private var isImageVisible = true

    /**
    Eine Variable spinnerRepository definieren, um auf die SpinnerRepository zuzugreifen.
     */
    private val spinnerRepository = FunctionUtils.SpinnerDataRepository.instance
    private val landOptions = spinnerRepository.landOptions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userSettingEmail = it.getString("emailUserSetting")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_setting, container, false)
        userProfile = UserProfile(requireContext().applicationContext)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
        Setz die Eigenschaft 'isEnabled'  für was auf dem Bild angezeigt wird auf false.
         */
        binding.editTextUserFirstName.isEnabled = false
        binding.editTextUserLastName.isEnabled = false
        binding.editTextUserPassword.isEnabled = false
        binding.editTextConfirmUserPassword.visibility=View.GONE
        binding.tvConfirmPassword.visibility=View.GONE
        binding.spinnerLand.isEnabled = false
        binding.spinnerCity.isEnabled = false
        /**
        Die e-mail des Nutzers im TextBox 'tvEmailUserSetting' anzeigen lassen.
         */
        userSettingEmail.also { binding.tvEmailUserSetting.text = it }
        /**
        Abruf das Profil des aktuellen Nutzers.
         */
        currentUser = userProfile.getUserProfileByEmail(userSettingEmail) ?: UserData(
            "", "", userSettingEmail, "", "", "", "",
            "", "", mutableListOf(), mutableListOf(),"",mutableListOf()
        )
        val userProfilePhoto=binding.ivUserProfileImageSetting
        if (currentUser?.userPhoto != null) {
            val userPhotoUri = Uri.parse(currentUser.userPhoto)

            val requestOptions = RequestOptions().transform(CircleCrop()).error(R.drawable.baseline_person_24)

            Glide.with(requireContext())
                .load(userPhotoUri)
                .apply(requestOptions)
                .into(userProfilePhoto)
        } else {
            userProfilePhoto.setImageResource(R.drawable.baseline_person_24)
        }
        binding.ivBackSetting.setOnClickListener {
            findNavController().navigateUp()
        }

        if (currentUser != null) {
            /**
            Spinners implementieren
             */
        val landSpinner = binding.spinnerLand
        val landAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            landOptions
        )
        landSpinner.adapter = landAdapter
            val userCurrentLand = userProfile.getUserLandByEmail(userSettingEmail)
            val userCurrentCity = userProfile.getUserCityByEmail(userSettingEmail)
            val userCurrentLandPosition = landOptions.indexOf(userCurrentLand)
            landSpinner.setSelection(userCurrentLandPosition)


        val citySpinner = binding.spinnerCity
        val cityOptions = FunctionUtils.SpinnerDataRepository.instance.getCityOptions(userProfile.getUserLandByEmail(userSettingEmail)!!)
        val cityAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            cityOptions
        )
        citySpinner.adapter = cityAdapter
            val userCurrentCityPosition = cityOptions.indexOf(userCurrentCity)
            citySpinner.setSelection(userCurrentCityPosition)


            landSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedLand = parent?.getItemAtPosition(position).toString()
                    val citiesForLand = FunctionUtils.SpinnerDataRepository.instance.getCityOptions(selectedLand)


                    val cityAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        citiesForLand
                    )


                    citySpinner.adapter = cityAdapter
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            binding.editTextUserFirstName.setText(currentUser.firstName)
            binding.editTextUserLastName.setText(currentUser.lastName)
            binding.editTextUserPassword.setText(currentUser.password)
        } else {

        }






        binding.btnSave.setOnClickListener {
            currentUser.let {
                isImageVisible = !isImageVisible

                if (isImageVisible) {
                    // User clicked to save
                    val newFirstName = binding.editTextUserFirstName?.text?.toString() ?: ""
                    val newLastName = binding.editTextUserLastName?.text?.toString() ?: ""
                    val newPassword = binding.editTextUserPassword?.text?.toString() ?: ""
                    val newLand = binding.spinnerLand?.selectedItem?.toString() ?: ""
                    val newCity = binding.spinnerCity?.selectedItem?.toString() ?: ""
                    val repeatedPassword = binding.editTextConfirmUserPassword?.text?.toString() ?: ""
                    if(binding.editTextConfirmUserPassword?.isVisible==false){
                        if (newFirstName.isNotEmpty() && newLastName.isNotEmpty()
                            && newPassword.isNotEmpty() && newLand.isNotEmpty() && newCity.isNotEmpty()
                            && newLand!=null && newCity!=null

                        ) {
                            // Condition met, perform save
                            it.firstName = newFirstName
                            it.lastName = newLastName
                            it.password = newPassword
                            it.land = newLand
                            it.city = newCity
                            isImageVisible=true
                            userProfile.updateUserPersonalData(currentUser)
                            findNavController().navigateUp()
                        }
                    }else{
                        if (newFirstName.isNotEmpty() && newLastName.isNotEmpty()
                            && newPassword.isNotEmpty() && newLand.isNotEmpty() && newCity.isNotEmpty()
                            && newLand!=null && newCity!=null && newPassword==repeatedPassword){
                            it.firstName = newFirstName
                            it.lastName = newLastName
                            it.password = newPassword
                            it.land = newLand
                            it.city = newCity
                            isImageVisible=true
                            userProfile.updateUserPersonalData(currentUser)
                            findNavController().navigateUp()
                        }
                        else {
                            // Condition not met, show a message
                            Toast.makeText(context, "Fill in all fields!!", Toast.LENGTH_SHORT).show()
                            isImageVisible=false
                        }
                    }


                } else {
                    binding.btnSave.setImageResource(R.drawable.baseline_save_24)
                    binding.editTextUserFirstName.isEnabled = true
                    binding.editTextUserLastName.isEnabled = true
                    binding.editTextUserPassword.isEnabled = true
                    binding.spinnerLand.isEnabled = true
                    binding.spinnerCity.isEnabled = true
                    binding.editTextUserPassword.setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            val newPassword=binding.editTextUserPassword.text.toString()
                            /**
                             Wenn das neue Kennwort nicht das gleiche wie das vorherige Kennwort.
                             (Änderung ist aufgefallen)
                             */
                            if (newPassword!= currentUser.password) {
                                /**
                                Wenn das neu eingegebene Passwort weder leer ist noch den
                                Passwortregeln entspricht.
                                 */
                                if (newPassword.isNotEmpty() && !FunctionUtils.checkPassword(newPassword)) {

                                    /**
                                     Der Nutzer bekommt eine Nachricht.
                                     */
                                    binding.editTextUserPassword.error = getString(R.string.passwordTipp)
                                    binding.editTextUserPassword.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.red
                                        )
                                    )
                                    /**
                                     Wenn das Kennwort leer ist
                                     */
                                } else {
                                    binding.editTextUserPassword.error = null
                                    binding.editTextUserPassword.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.yellow
                                        )
                                    )
                                    binding.tvConfirmPassword.visibility = View.VISIBLE
                                    binding.editTextConfirmUserPassword.visibility = View.VISIBLE
                                    //binding.editTextConfirmUserPassword.requestFocus()
                                    binding.tvConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
                                        if (!hasFocus) {

                                            val repeatedPassword = binding.editTextConfirmUserPassword.text.toString()
                                            if (repeatedPassword.isNotEmpty() && !FunctionUtils.checkPasswordMatch(
                                                    newPassword,
                                                    repeatedPassword
                                                )
                                            ) {
                                                binding.editTextConfirmUserPassword.error = getString(R.string.passwordMatching)
                                                binding.editTextConfirmUserPassword.setTextColor(
                                                    ContextCompat.getColor(
                                                        requireContext(),
                                                        R.color.red
                                                    )
                                                )
                                               
                                            } else {
                                                binding.editTextConfirmUserPassword.error = null
                                                binding.editTextConfirmUserPassword.setTextColor(
                                                    ContextCompat.getColor(
                                                        requireContext(),
                                                        R.color.yellow
                                                    )
                                                )

                                            }
                                        }
                                    }
                                }
                                /**
                                 Wenn das neue eingegebene Kennwort ist das gleiche wie das
                                 vorherige Kennwort.
                                 */
                            } else {
                                binding.tvConfirmPassword.visibility = View.GONE
                                binding.editTextConfirmUserPassword.visibility = View.GONE
                            }
                        }
                    }

                }
            }
        }
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(UserSettingFragmentDirections.actionUserSettingFragmentToUserSubjectsFragment(userEmailUserSubjects = userSettingEmail))
        }
    }

}