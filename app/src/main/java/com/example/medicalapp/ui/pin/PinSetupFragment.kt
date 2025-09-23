package com.example.medicalapp.ui.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.medicalapp.R
import com.example.medicalapp.data.UserPreferences

class PinSetupFragment : Fragment() {

    private lateinit var userPrefs: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pin_setup, container, false)

        userPrefs = UserPreferences(requireContext())

        val pinInput = view.findViewById<EditText>(R.id.pinInput)
        val saveBtn = view.findViewById<Button>(R.id.savePinBtn)

        saveBtn.setOnClickListener {
            val pin = pinInput.text.toString()
            if (pin.length == 4) {
                userPrefs.savePin(pin)
                Toast.makeText(requireContext(), "PIN sauvegardé !", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_pinSetupFragment_to_dashboardFragment)
            } else {
                Toast.makeText(requireContext(), "Le PIN doit contenir 4 chiffres", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
