package com.example.zuhauselernen.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.zuhauselernen.FunctionUtils
import com.example.zuhauselernen.data.local.UserProfile
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentLoginBinding


class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentLoginBinding
    private val currentMode = AppCompatDelegate.getDefaultNightMode()
    private lateinit var emailInputs: LinearLayout
    private lateinit var gmailInputs: LinearLayout
    private lateinit var appleInputs: LinearLayout
    private lateinit var facebookInputs: LinearLayout
    private var activeInputs: LinearLayout? = null
    private var isEmailValid: Boolean = false
    private var isEmailRegistered: Boolean = false
    private lateinit var userProfile:UserProfile
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)


        emailInputs = binding.logInEmail
        gmailInputs = binding.logInGmail
        appleInputs = binding.logInApple
        facebookInputs = binding.logInFacebook
        val email = binding.accountLogin
        val gmail = binding.gMailLogin
        val apple = binding.appleLogin
        val facebook = binding.facebookLogin


        email.setOnClickListener(this)
        gmail.setOnClickListener(this)
        apple.setOnClickListener(this)
        facebook.setOnClickListener(this)

        /**
        Funktionalität des BottomNavigationBar
         */
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                R.id.dark_mode -> {
                    if (currentMode != AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        recreate(requireActivity())
                    }
                }
                R.id.light_mode -> {
                    if (currentMode != AppCompatDelegate.MODE_NIGHT_NO) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        recreate(requireActivity())
                    }
                }
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
        userProfile = UserProfile(requireContext().applicationContext)

        binding.confirmLogIn.isEnabled = false
        emailInputs = binding.logInEmail
        val currentUser=userProfile.getUserProfileByEmail(binding.logInEmail.toString())
        gmailInputs = binding.logInGmail
        appleInputs = binding.logInApple
        facebookInputs = binding.logInFacebook
        val email = binding.accountLogin
        val gmail = binding.gMailLogin
        val apple = binding.appleLogin
        val facebook = binding.facebookLogin


        email.setOnClickListener(this)
        gmail.setOnClickListener(this)
        apple.setOnClickListener(this)
        facebook.setOnClickListener(this)


    }

    /**
     Die Methode 'onClick' wird überschrieben ,sie definiert die Aktionen, die beim Klicken die
     MaterialButtons ausgeführt werden mussen.
     */
    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {
                R.id.accountLogin -> {
                    setActiveInputs(emailInputs)
                    if (emailInputs.visibility == View.VISIBLE) {
                        val emailAdress = emailInputs.findViewById<EditText>(R.id.eMailAdress)
                        val emailPassword = emailInputs.findViewById<EditText>(R.id.emailPassword)
                        emailAdress.setOnFocusChangeListener { _, hasFocus ->
                            if (!hasFocus) {
                                val email = emailAdress.text.toString()
                                /**
                                Es wird überprüft, ob die e-mail Adresse nicht leer ist und ob
                                (sie nicht gültig oder nicht registriert) ist
                                 */
                                if (email.isNotEmpty() && (!FunctionUtils.isValidEmail(email) || !FunctionUtils.isEmailExists(
                                        requireContext(),
                                        email
                                    ))
                                ) {
                                    /**
                                    Ob die e-mail Adresse nicht gültig ist:
                                     */
                                    if (!FunctionUtils.isValidEmail(email)) {
                                        emailAdress.error = "Ungültige E-Mail Adresse!"
                                        emailAdress.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.red
                                            )
                                        )
                                        isEmailValid = false
                                        emailPassword.isEnabled=false
                                        /**
                                        Falls die e-mail Adress gültig ist, wird es überprüft
                                        ob sie registriert ist:
                                         */
                                    } else
                                    /**
                                    Falls die e-mail Adresse nicht registriert ist:
                                     */
                                        if (!FunctionUtils.isEmailExists(requireContext(), email)) {
                                            emailAdress.error =
                                                "Die E-Mail Adresse ist nicht registriert!"
                                            emailAdress.setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.red
                                                )
                                            )
                                            isEmailRegistered = false
                                            emailPassword.isEnabled=false
                                        }
                                    binding.confirmLogIn.isEnabled = false
                                    /**
                                    Falls die e-mail Adresse gültig und registriert ist:
                                     */
                                } else {
                                    emailAdress.error = null
                                    emailAdress.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.yellow
                                        )
                                    )
                                    isEmailValid = true
                                    isEmailRegistered = true
                                    emailPassword.isEnabled=true
                                    emailPassword.hasFocus()
                                }
                            }
                        }
                        emailPassword.setOnFocusChangeListener { _, hasFocus ->
                            if (!hasFocus) {
                                val password = emailPassword.text.toString()
                                val passwordInDb = FunctionUtils.getUserPassword(
                                    requireContext(),
                                    emailAdress.text.toString()
                                )
                                if (password.isNotEmpty() && passwordInDb != null) {
                                    /**
                                     Falls das Kennwort falsch ist.
                                     */
                                    if (password != passwordInDb) {
                                        emailPassword.error = "Falsches Kennwort"
                                        emailPassword.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.red
                                            )
                                        )
                                        isEmailValid = false
                                        /**
                                         Falls das Kennwort richtig ist
                                         */
                                    } else {
                                        emailPassword.error = null
                                        emailPassword.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.yellow
                                            )
                                        )
                                        isEmailValid = true
                                        binding.confirmLogIn.isEnabled = true


                                    }
                                }

                            }
                        }
                    }
                }
                /**
                Weiter mit Gmail einloggen
                 */
                R.id.gMailLogin -> {
                    setActiveInputs(gmailInputs)

                    if (gmailInputs.visibility == View.VISIBLE) {
                        val gmailAdress = gmailInputs.findViewById<EditText>(R.id.gMailAdress)
                        val gmailPassword = gmailInputs.findViewById<EditText>(R.id.gMailPassword)
                        val gmail = gmailAdress.text.toString()
                        val gmailPass = gmailPassword.text.toString()
                    }
                }
                /**
                Weiter mit AppleID einloggen
                 */
                R.id.appleLogin -> {
                    setActiveInputs(appleInputs)

                    if (appleInputs.visibility == View.VISIBLE) {
                        val appleId = appleInputs.findViewById<EditText>(R.id.appleId)
                        val applePassword = appleInputs.findViewById<EditText>(R.id.applePassword)
                        val appleID = appleId.text.toString()
                        val applePass = applePassword.text.toString()

                    }
                }
                /**
                Weiter mit Facebook einloggen
                 */
                R.id.facebookLogin -> {
                    setActiveInputs(facebookInputs)

                    if (facebookInputs.visibility == View.VISIBLE) {
                        val facebookId = facebookInputs.findViewById<EditText>(R.id.facebookId)
                        val facebookPassword =
                            facebookInputs.findViewById<EditText>(R.id.facebookPassword)


                        val facebookUserName = facebookId.text.toString()
                        val facebookPass = facebookPassword.text.toString()


                    }
                }

            }
        }
        binding.confirmLogIn.setOnClickListener {
            val userSubjects=userProfile.getUserSubjectsByEmail(binding.eMailAdress.text.toString())
            if(userSubjects.size!=0){
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToUserSubjectsFragment(
                        userEmailUserSubjects = binding.eMailAdress.text.toString()
                    )
                )
            }else{
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSubjectFragment(userEmailSubject=binding.eMailAdress.text.toString()))
            }

        }

    }

    private fun setActiveInputs(inputsLayout: LinearLayout) {
        if (activeInputs == inputsLayout) {

            toggleFields(inputsLayout)
        } else {

            activeInputs?.visibility = View.GONE
            toggleFields(inputsLayout)
            activeInputs = inputsLayout
        }
    }

    private fun toggleFields(fieldsLayout: LinearLayout) {
        if (fieldsLayout.visibility == View.VISIBLE) {
            fieldsLayout.visibility = View.GONE
        } else {
            fieldsLayout.visibility = View.VISIBLE
        }
    }

}