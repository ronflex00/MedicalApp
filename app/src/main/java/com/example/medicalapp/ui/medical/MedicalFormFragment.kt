package com.example.medicalapp.ui.medical

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
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class MedicalFormFragment : Fragment() {

    private lateinit var inputName: EditText
    private lateinit var inputFirstName: EditText
    private lateinit var inputBirthDate: EditText
    private lateinit var inputBloodGroup: EditText
    private lateinit var inputDoctor: EditText
    private lateinit var inputDoctorPhone: EditText
    private lateinit var inputAllergies: EditText
    private lateinit var inputContacts: EditText

    private lateinit var btnSave: Button
    private val fileName = "carnet_medical.json"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_medical_form, container, false)

        inputName = view.findViewById(R.id.inputName)
        inputFirstName = view.findViewById(R.id.inputFirstName)
        inputBirthDate = view.findViewById(R.id.inputBirthDate)
        inputBloodGroup = view.findViewById(R.id.inputBloodGroup)
        inputDoctor = view.findViewById(R.id.inputDoctor)
        inputDoctorPhone = view.findViewById(R.id.inputDoctorPhone)
        inputAllergies = view.findViewById(R.id.inputAllergies)
        inputContacts = view.findViewById(R.id.inputContacts)

        btnSave = view.findViewById(R.id.btnSave)

        btnSave.setOnClickListener { saveDataAndGoToPinSetup() }

        return view
    }

    private fun saveDataAndGoToPinSetup() {
        val fields = listOf(
            inputName, inputFirstName, inputBirthDate, inputBloodGroup,
            inputDoctor, inputDoctorPhone, inputAllergies, inputContacts
        )

        if (fields.any { it.text.toString().isBlank() }) {
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        val json = JSONObject()
        val utilisateur = JSONObject().apply {
            put("nom", inputName.text.toString())
            put("prenom", inputFirstName.text.toString())
            put("date", inputBirthDate.text.toString())
            put("groupe", inputBloodGroup.text.toString())
            put("medecin", JSONObject().apply {
                put("nomMedecin", inputDoctor.text.toString())
                put("numeroMedecin", inputDoctorPhone.text.toString())
            })
            put("allergenes", JSONArray(inputAllergies.text.toString().split(",")))

            val contactsArray = JSONArray()
            inputContacts.text.toString().split(";").forEach { contact ->
                val parts = contact.split(",")
                if (parts.size == 3) {
                    contactsArray.put(JSONObject().apply {
                        put("prenomContact", parts[0].trim())
                        put("nomContact", parts[1].trim())
                        put("numeroContact", parts[2].trim())
                    })
                }
            }
            put("contact", contactsArray)
        }

        json.put("urgence", JSONObject().apply { put("utilisateur", utilisateur) })
        json.put("confidentiel", JSONObject().apply {
            put("antecedents", JSONArray())
            put("vaccins", JSONArray())
            put("handicap", JSONArray())
        })

        val file = File(requireContext().filesDir, fileName)
        file.writeText(json.toString())

        Toast.makeText(requireContext(), "Inscription réussie ✅", Toast.LENGTH_SHORT).show()

        // Navigation vers le setup PIN
        findNavController().navigate(R.id.action_medicalFormFragment_to_pinSetupFragment)
    }
}
