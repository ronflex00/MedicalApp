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

class PinLoginFragment : Fragment() {

    private lateinit var userPrefs: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pin_login, container, false)

        userPrefs = UserPreferences(requireContext())

        val pinInput = view.findViewById<EditText>(R.id.pinInput)
        val loginBtn = view.findViewById<Button>(R.id.loginPinBtn)

        loginBtn.setOnClickListener {
            val pin = pinInput.text.toString()
            val savedPin = userPrefs.getPin()

            if (pin == savedPin) {
                Toast.makeText(requireContext(), "Accès accordé ✅", Toast.LENGTH_SHORT).show()
                // ✅ Aller au Dashboard si le PIN est correct
                findNavController().navigate(R.id.action_pinLoginFragment_to_dashboardFragment)
            } else {
                Toast.makeText(requireContext(), "PIN incorrect ❌", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
