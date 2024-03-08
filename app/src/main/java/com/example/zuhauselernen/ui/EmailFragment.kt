package com.example.zuhauselernen.ui

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.zuhauselernen.FunctionUtils
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_CITY
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_CLASS
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_EMAIL
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_FIRSTNAME
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_LAND
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_LASTNAME
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PASSWORD
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PAYMENT_ACCOUNT_OWNER
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PAYMENT_ADRESS
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PAYMENT_BIRTHDATE
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PAYMENT_CARD_EXPIRY_DATE
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PAYMENT_CARD_NUMBER
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PAYMENT_ID
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PAYMENT_IMAGE
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PAYMENT_NAME
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PAYMENT_PLZ
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PAYMENT_SECURITY_CODE
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PAYMENT_SELECTED
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_REASON
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_SCHOOL
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_SUBJECT_CHECKED
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_SUBJECT_ID
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_SUBJECT_IMAGE
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_SUBJECT_NAME
import com.example.zuhauselernen.data.local.model.UserData
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentEmailBinding


class EmailFragment : Fragment() {
    private lateinit var binding: FragmentEmailBinding
    private lateinit var context: Context
    private lateinit var userProfile: UserProfile
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_email, container, false)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> findNavController().navigate(EmailFragmentDirections.actionEmailFragmentToHomeFragment())
                R.id.logout -> {

                    requireActivity().finishAffinity()
                }
            }
            true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appLogoEmail.setImageResource(R.drawable.homelogo)
        /**
        Die Gültigkeit und die Existenz der eingegeben E-Mail Adresse überprüfen.
         */
        var isEmailValid = false
        var isEmailRegistered = false
        binding.eMailAdresse.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val email = binding.eMailAdresse.text.toString()
                if (email.isNotEmpty() && (!FunctionUtils.isValidEmail(email) || FunctionUtils.isEmailExists(
                        requireContext(),
                        email
                    ))
                ) {
                    if (!FunctionUtils.isValidEmail(email)) {
                        binding.eMailAdresse.error = "Ungültige E-Mail Adresse!"
                        binding.eMailAdresse.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        isEmailValid = false
                    }
                    if (FunctionUtils.isEmailExists(requireContext(), email)) {
                        binding.eMailAdresse.error = "Die E-Mail Adresse existiert schon!"
                        binding.eMailAdresse.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        isEmailRegistered = false
                    }
                    binding.confirmEmail.isEnabled = false
                } else {
                    binding.eMailAdresse.error = null
                    binding.eMailAdresse.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.yellow
                        )
                    )
                    isEmailValid = true
                    isEmailRegistered = true
                    binding.confirmEmail.isEnabled = true

                }
            }
        }
        /**
        Die Gültigkeit des Kennwort überprüfen.
         */
        binding.password.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val password = binding.password.text.toString()
                if (password.isNotEmpty() && !FunctionUtils.checkPassword(password)) {
                    binding.password.error = getString(R.string.passwordTipp)
                    binding.password.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    binding.confirmEmail.isEnabled = false
                } else {
                    binding.password.error = null
                    binding.password.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.yellow
                        )
                    )
                    binding.confirmEmail.isEnabled = true
                }
            }
        }
        /**
        Die entsprechung  der Kennwörter überprüfen.
         */
        binding.passwordRepeat.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val password = binding.password.text.toString()
                val repeatedPassword = binding.passwordRepeat.text.toString()
                if (repeatedPassword.isNotEmpty() && !FunctionUtils.checkPasswordMatch(
                        password,
                        repeatedPassword
                    )
                ) {
                    binding.passwordRepeat.error = getString(R.string.passwordMatching)
                    binding.passwordRepeat.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    binding.confirmEmail.isEnabled = false
                } else {
                    binding.passwordRepeat.error = null
                    binding.passwordRepeat.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.yellow
                        )
                    )
                    binding.confirmEmail.isEnabled = true
                }
            }
        }
        /**
        Die Gültigkeit des Vornamen überprüfen.
         */
        binding.firstName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val firstName = binding.firstName.text.toString()
                if (firstName.isNotEmpty() && !FunctionUtils.checkFirstName(firstName)) {
                    binding.firstName.error = "Der Vorname ist ungültig!!"
                    binding.firstName.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    binding.confirmEmail.isEnabled = false
                } else {
                    binding.firstName.error = null
                    binding.firstName.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.yellow
                        )
                    )
                    binding.confirmEmail.isEnabled = true
                }
            }
        }
        /**
        Die Gültigkeit des Nachnamen überprüfen.
         */
        binding.lastName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val lastName = binding.lastName.text.toString()
                if (lastName.isNotEmpty() && !FunctionUtils.checkLastName(lastName)) {
                    binding.lastName.error = "Der Nachname ist ungültig!!"
                    binding.lastName.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    binding.confirmEmail.isEnabled = false
                } else {
                    binding.lastName.error = null
                    binding.lastName.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.yellow
                        )
                    )
                    binding.confirmEmail.isEnabled = true
                }
            }
        }

        /**
        Eine Aktion beim Klicken 'BackArrow' ausführen.
         */
        binding.ivBackEmail.setOnClickListener {
            findNavController().navigate(EmailFragmentDirections.actionEmailFragmentToRegisterFragment())
        }
        /**
        Eine Aktion beim Klicken 'Bestätigen' ausführen.
         */
        binding.confirmEmail.setOnClickListener {
            /**
            Neue Variable user wird definiert
             */
            val user = UserData(
                "", "", "", "", "", "", "",
                "", "", mutableListOf(), mutableListOf(), "", mutableListOf()
            )
            /**
            Die vom Nutzer eingegebenen (first name, last name, e-mail address, password)
            werden im neuen erstellten Nutzer hinzugefügt.
             */
            user.firstName = binding.firstName.text.toString()
            user.lastName = binding.lastName.text.toString()
            user.emailAdress = binding.eMailAdresse.text.toString()
            user.password = binding.password.text.toString()

            val dbHelper = UserProfile(context)
            val db = dbHelper.writableDatabase
            if (db.version == 0) {
                dbHelper.onCreate(db)
            }

            val values = ContentValues().apply {
                put(COLUMN_FIRSTNAME, user.firstName)
                put(COLUMN_LASTNAME, user.lastName)
                put(COLUMN_EMAIL, user.emailAdress)
                put(COLUMN_PASSWORD, user.password)
                put(COLUMN_LAND, "")
                put(COLUMN_CITY, "")
                put(COLUMN_REASON, "")
                put(COLUMN_SCHOOL, "")
                put(COLUMN_CLASS, "")
                put(COLUMN_SUBJECT_ID, "")
                put(COLUMN_SUBJECT_NAME, "")
                put(COLUMN_SUBJECT_IMAGE, "")
                put(COLUMN_SUBJECT_CHECKED, "")
                put(COLUMN_PAYMENT_ID, "")
                put(COLUMN_PAYMENT_NAME, "")
                put(COLUMN_PAYMENT_IMAGE, "")
                put(COLUMN_PAYMENT_SELECTED, "")
                put(COLUMN_PAYMENT_ACCOUNT_OWNER, "")
                put(COLUMN_PAYMENT_CARD_NUMBER, "")
                put(COLUMN_PAYMENT_CARD_EXPIRY_DATE, "")
                put(COLUMN_PAYMENT_SECURITY_CODE, "")
                put(COLUMN_PAYMENT_BIRTHDATE, "")
                put(COLUMN_PAYMENT_ADRESS, "")
                put(COLUMN_PAYMENT_PLZ, "")
            }
            if (isEmailValid && isEmailRegistered &&
                binding.confirmEmail.isEnabled &&
                user.firstName.isNotEmpty() && user.lastName.isNotEmpty() &&
                user.password.isNotEmpty() && FunctionUtils.checkPassword(user.password) &&
                user.password == binding.passwordRepeat.text.toString()
            ) {
                val values = ContentValues().apply {
                    put(COLUMN_FIRSTNAME, user.firstName)
                    put(COLUMN_LASTNAME, user.lastName)
                    put(COLUMN_EMAIL, user.emailAdress)
                    put(COLUMN_PASSWORD, user.password)
                }
                db.insert(UserProfile.TABLE_NAME_USERS, null, values)

                Toast.makeText(
                    requireContext(),
                    "Ihre Daten wurden erfolgreich gespeichert",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(
                    EmailFragmentDirections.actionEmailFragmentToSettingFragment(
                        userEmail = binding.eMailAdresse.text.toString()
                    )
                )

            } else {
                Toast.makeText(
                    requireContext(),
                    "Please fill in all fields correctly.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }
}