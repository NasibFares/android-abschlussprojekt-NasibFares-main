package com.example.zuhauselernen.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var imageView: ImageView
    private lateinit var switchCompat: SwitchCompat
    private var sharedPreferences: SharedPreferences? = null
    /**
    Diese Methode wird aufgerufen, um das Layout des Fragments zu erstellen. Es verwendet die Data
    Binding Library, um das Layout fragment_home.xml aufzubauen. Außerdem wird die SharedPreferences-
    Instanz initialisiert.
     */
        @SuppressLint("CommitPrefEdits")
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        /**
        Hier wird eine Instanz von SharedPreferences erstellt und mit dem Namen "night" initialisiert.
        Dies ermöglicht das Speichern und Abrufen von Schlüssel-Wert-Paaren, die den Nachtmodus der
        App steuern.
         */
            sharedPreferences = requireContext().getSharedPreferences("night", Context.MODE_PRIVATE)
           val editor=sharedPreferences!!.edit()
            imageView = binding.imageView
            switchCompat = binding.switchCompat

        /**
        Hier wird der Wert des Schlüssels "night_mode" aus den SharedPreferences abgerufen. Wenn der
        Schlüssel nicht existiert, wird der Standardwert true verwendet. Dieser Wert gibt an, ob der
        Nachtmodus aktiviert ist oder nicht.
         */
            val nightModeStatus: Boolean = sharedPreferences!!.getBoolean("night_mode", true)
            if (nightModeStatus) {
                /**
                Wenn der boolean-Wert true ist, wird der Nachtmodus in der App aktiviert. Dies
                geschieht mit der Methode setDefaultNightMode der AppCompatDelegate-Klasse.
                 */
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("boolean",true)
                editor.apply()
                /**
                Die switchCompat-Schaltfläche wird auf den Wert true gesetzt, um den Nachtmodus zu
                reflektieren.
                 */
                switchCompat.isChecked = true
                /**
                Das Bild des imageView-Elements wird auf das entsprechende Nachtmodus-Bild gesetzt.
                */
                imageView.setImageResource(R.drawable.baseline_nightlight_24)
            }
        /**
        Hier wird ein setOnCheckedChangeListener für die switchCompat-Schaltfläche festgelegt. Diese
        Funktion wird aufgerufen, wenn sich der Status der Schaltfläche ändert.
         */
        switchCompat.setOnCheckedChangeListener { _, isChecked ->
            /**
            Hier wird überprüft, ob die Schaltfläche aktiviert (isChecked == true) oder deaktiviert
            (isChecked == false) ist.
            Je nach Status wird der Nachtmodus aktiviert oder deaktiviert. Das entsprechende Bild
            und der boolean-Wert werden in den SharedPreferences gespeichert.
             */
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchCompat.isChecked = true
                    imageView.setImageResource(R.drawable.baseline_nightlight_24)
                    val editor = sharedPreferences!!.edit()
                    editor.putBoolean("night_mode", true)
                    /**
                    Nachdem die Änderungen an den SharedPreferences vorgenommen wurden, werden sie
                    mit dieser Methode angewendet, um die Änderungen dauerhaft zu speichern.
                     */
                    editor.apply()
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchCompat.isChecked = false
                    imageView.setImageResource(R.drawable.baseline_light_mode_24)
                    val editor = sharedPreferences!!.edit()
                    editor.putBoolean("night_mode", false)
                    editor.apply()
                }
            /**
            Zusammenfassung:
            Der Code ermöglicht es dem Benutzer, den Nachtmodus der App über eine SwitchCompat-
            Schaltfläche zu steuern und speichert den Zustand des Nachtmodus in den SharedPreferences,
            um ihn zwischen den App-Sitzungen beizubehalten.
             */
            }
            return binding.root
        }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)


            /**
            Aktion beim Klicken login
             */
            binding.logIn.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
            }
            /**
            Aktion beim Klicken register
             */
            binding.register.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRegisterFragment())
            }
            /**
            Aktion beim Klicken logout
             */
            binding.logOut.setOnClickListener {
                requireActivity().finishAffinity()
            }
            binding.appLogoHome.setImageResource(R.drawable.homelogo)
            binding.ivHomeImage.setImageResource(R.drawable.home_screen_photo)

            val appName = getString(R.string.app_name)
            val spannableString = SpannableString(appName)
            /**
            Die Bestimmung der Farben für bestimmte Buchstaben
             */
            val redColor = ContextCompat.getColor(requireContext(), R.color.red)
            val yellowColor = ContextCompat.getColor(requireContext(), R.color.buttons)
            /**
            Wenden die Farben auf bestimmte Buchstaben an
             */
            /**
             Z einstellen
             */
            spannableString.setSpan(
                ForegroundColorSpan(redColor),
                0, 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            /**
            u einstellen
             */
            spannableString.setSpan(
                ForegroundColorSpan(yellowColor),
                1, 2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            /**
            H einstellen
             */
            spannableString.setSpan(
                ForegroundColorSpan(redColor),
                2, 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            /**
            a, u, s, e einstellen
             */
            spannableString.setSpan(
                ForegroundColorSpan(yellowColor),
                3, 7,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            /**
            L einstellen
             */
            spannableString.setSpan(
                ForegroundColorSpan(redColor),
                7, 8,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            /**
            e, r, n, e, n einstellen
             */
            spannableString.setSpan(
                ForegroundColorSpan(yellowColor),
                8, 13,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            /**
            Legen  den geänderten SpannableString auf die TextView (tvAppName:ZuHauseLernen) fest
             */
            binding.tvAppName.text = spannableString

        }

    override fun onResume() {
        super.onResume()
    }
}