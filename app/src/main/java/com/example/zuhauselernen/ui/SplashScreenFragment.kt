package com.example.zuhauselernen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.delay


class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstance: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash_screen, container, false)
        /**
        Innerhalb von viewLifecycleOwner.lifecycleScope wird eine Coroutine mit launchWhenStarted
        gestartet. Dies wird verwendet, um asynchrone Vorgänge auszuführen, ohne den Hauptthread zu
        blockieren.
         */
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            /**
            Verzögerung(2000) wird verwendet, um eine Verzögerung von 2000 Millisekunden (2 Sekunden)
            einzuführen.
             */
            delay(2000)
            /**
            Dadurch wird sichergestellt, dass die Navigation erfolgt, wenn sich die Ansicht des
            Fragments im Status „Gestartet“ befindet, was der richtige Zeitpunkt für die Navigation ist.
             */
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
        }

        return binding.root
    }
}
