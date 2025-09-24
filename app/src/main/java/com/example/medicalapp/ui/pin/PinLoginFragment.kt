package com.example.medicalapp.ui.pin

import android.content.Context
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

class PinLoginFragment : Fragment() {

    private lateinit var userEmail: String
    private lateinit var inputPin: EditText
    private lateinit var btnLoginPin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Récupération de l'email via Safe Args / Bundle
        userEmail = requireArguments().getString("userEmail") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pin_login, container, false)

        inputPin = view.findViewById(R.id.inputPin)
        btnLoginPin = view.findViewById(R.id.btnLoginPin)

        btnLoginPin.setOnClickListener {
            val enteredPin = inputPin.text.toString()
            val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val storedPin = sharedPref.getString("pin_$userEmail", null)

            when {
                enteredPin.isEmpty() ->
                    Toast.makeText(requireContext(), "Veuillez entrer votre code PIN", Toast.LENGTH_SHORT).show()
                enteredPin == storedPin -> {
                    Toast.makeText(requireContext(), "Connexion réussie ✅", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_pinLoginFragment_to_dashboardFragment)
                }
                else ->
                    Toast.makeText(requireContext(), "PIN incorrect ❌", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
