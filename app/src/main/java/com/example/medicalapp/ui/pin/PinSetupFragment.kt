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

class PinSetupFragment : Fragment() {

    private lateinit var userEmail: String
    private lateinit var inputPin: EditText
    private lateinit var inputConfirmPin: EditText
    private lateinit var btnSavePin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Récupération de l'email via Safe Args / Bundle
        userEmail = requireArguments().getString("userEmail") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pin_setup, container, false)

        inputPin = view.findViewById(R.id.inputPin)
        inputConfirmPin = view.findViewById(R.id.inputConfirmPin)
        btnSavePin = view.findViewById(R.id.btnSavePin)

        btnSavePin.setOnClickListener {
            val pin = inputPin.text.toString()
            val confirmPin = inputConfirmPin.text.toString()

            when {
                pin.isEmpty() || confirmPin.isEmpty() ->
                    Toast.makeText(requireContext(), "Veuillez entrer un code PIN", Toast.LENGTH_SHORT).show()
                pin != confirmPin ->
                    Toast.makeText(requireContext(), "Les codes PIN ne correspondent pas", Toast.LENGTH_SHORT).show()
                else -> {
                    // Sauvegarde du PIN dans SharedPreferences, clé unique par email
                    val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("pin_$userEmail", pin).apply()

                    Toast.makeText(requireContext(), "PIN enregistré avec succès ✅", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_pinSetupFragment_to_dashboardFragment)
                }
            }
        }

        return view
    }
}
