package com.example.medicalapp.ui.medical

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
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import org.json.JSONObject
import java.io.File

class SoignantLoginFragment : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private val fileName = "carnet_medical.json"

    // Scanner QR
    private val qrScanLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            try {
                val json = JSONObject(result.contents)
                File(requireContext().filesDir, fileName).writeText(json.toString())
                Toast.makeText(requireContext(), "Données importées depuis QR ✅", Toast.LENGTH_SHORT).show()

                // Naviguer vers le fragment d’édition du formulaire médical
                findNavController().navigate(R.id.medicalFormEditFragment)

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erreur import QR: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_soignant_login, container, false)

        usernameEditText = view.findViewById(R.id.editTextUsername)
        passwordEditText = view.findViewById(R.id.editTextPassword)
        loginButton = view.findViewById(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Login dur pour l’instant
                if (username == "medecin" && password == "1234") {
                    Toast.makeText(requireContext(), "Connexion réussie", Toast.LENGTH_SHORT).show()
                    startQRScanner()
                } else {
                    Toast.makeText(requireContext(), "Identifiants invalides", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Veuillez remplir les champs", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun startQRScanner() {
        val options = ScanOptions().apply {
            setPrompt("Scanne le QR Code contenant le JSON")
            setBeepEnabled(true)
            setOrientationLocked(true)
        }
        qrScanLauncher.launch(options)
    }
}
