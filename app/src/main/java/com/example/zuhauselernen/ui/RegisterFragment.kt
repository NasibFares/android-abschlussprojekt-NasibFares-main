package com.example.zuhauselernen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        /**
        wird der bottomNavigationView (untenstehende Navigationsleiste) über das Binding-Objekt referenziert.
        Der setOnItemSelectedListener-Listener wird für den bottomNavigationView festgelegt.
        Dieser Listener wird aufgerufen, wenn ein Element in der Navigationsleiste ausgewählt
        wird.
         */
        binding.bottomNavViewRegister.setOnItemSelectedListener { item ->
            when (item.itemId) {
                /**
                Es wird überprüft, welches Element ausgewählt wurde, indem die itemId überprüft wird.
                 */
                /**
                enn das Element mit der ID R.id.home ausgewählt wurde, wird die Navigation zum
                HomeFragment mit Hilfe von
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment()) durchgeführt.
                 */
                R.id.home -> findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment())
                /**
                enn das Element mit der ID R.id.dark_mode ausgewählt wurde, wird der Dunkelmodus
                aktiviert, indem AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                aufgerufen wird. Dann wird recreate(requireActivity()) verwendet, um die Aktivität
                neu zu erstellen und den Modus sofort anzuwenden.
                 */
                R.id.dark_mode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    ActivityCompat.recreate(requireActivity())
                }
                /**
                Wenn das Element mit der ID R.id.light_mode ausgewählt wurde, wird der Helligkeitsmodus
                aktiviert, indem AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                aufgerufen wird. Auch hier wird recreate(requireActivity()) verwendet, um die Aktivität
                neu zu erstellen und den Modus sofort anzuwenden.
                 */
                R.id.light_mode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    ActivityCompat.recreate(requireActivity())
                }
                R.id.logout->{
                    requireActivity().finishAffinity()
                }
            }
            true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appLogoRegister.setImageResource(R.drawable.homelogo)
        binding.tvRegister.setText(R.string.registrierenMenu)
       binding.btnNewUser.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToEmailFragment())
        }
    }
}