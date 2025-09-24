package com.example.medicalapp.ui.medical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.medicalapp.R
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

    private val fileName = "carnet_medical.json"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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

        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnExport = view.findViewById<Button>(R.id.btnExport)
        val btnImport = view.findViewById<Button>(R.id.btnImport)

        btnSave.setOnClickListener { saveData() }
        btnExport.setOnClickListener { exportData() }
        btnImport.setOnClickListener { importData() }

        return view
    }

    private fun saveData() {
        val json = JSONObject()
        json.put("nom", inputName.text.toString())
        json.put("prenom", inputFirstName.text.toString())
        json.put("dateNaissance", inputBirthDate.text.toString())
        json.put("groupeSanguin", inputBloodGroup.text.toString())
        json.put("medecin", JSONObject().apply {
            put("nomMedecin", inputDoctor.text.toString())
            put("numeroMedecin", inputDoctorPhone.text.toString())
        })
        json.put("allergies", inputAllergies.text.toString().split(","))
        json.put("contacts", inputContacts.text.toString().split(";"))

        val file = File(requireContext().filesDir, fileName)
        file.writeText(json.toString())

        Toast.makeText(requireContext(), "Données sauvegardées", Toast.LENGTH_SHORT).show()
    }

    private fun exportData() {
        val file = File(requireContext().filesDir, fileName)
        if (file.exists()) {
            Toast.makeText(requireContext(), "Export JSON: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Aucune donnée à exporter", Toast.LENGTH_SHORT).show()
        }
    }

    private fun importData() {
        val file = File(requireContext().filesDir, fileName)
        if (file.exists()) {
            val content = file.readText()
            val json = JSONObject(content)

            inputName.setText(json.getString("nom"))
            inputFirstName.setText(json.getString("prenom"))
            inputBirthDate.setText(json.getString("dateNaissance"))
            inputBloodGroup.setText(json.getString("groupeSanguin"))

            val medecin = json.getJSONObject("medecin")
            inputDoctor.setText(medecin.getString("nomMedecin"))
            inputDoctorPhone.setText(medecin.getString("numeroMedecin"))

            inputAllergies.setText(json.getJSONArray("allergies").join(", "))
            inputContacts.setText(json.getJSONArray("contacts").join("; "))

            Toast.makeText(requireContext(), "Données importées", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Aucun fichier trouvé", Toast.LENGTH_SHORT).show()
        }
    }
}
