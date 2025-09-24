package com.example.medicalapp.ui.medical

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.medicalapp.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class MedicalFormEditFragment : Fragment() {

    private lateinit var inputName: EditText
    private lateinit var inputFirstName: EditText
    private lateinit var inputBirthDate: EditText
    private lateinit var inputBloodGroup: EditText
    private lateinit var inputDoctor: EditText
    private lateinit var inputDoctorPhone: EditText
    private lateinit var inputAllergies: EditText
    private lateinit var inputContacts: EditText

    private lateinit var btnSave: Button
    private lateinit var btnExport: Button
    private lateinit var btnImport: Button

    private val fileName = "carnet_medical.json"

    private val qrScanLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            try {
                File(requireContext().filesDir, fileName).writeText(result.contents)
                loadDataFromFile()
                Toast.makeText(requireContext(), "Données importées depuis QR ✅", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erreur import QR: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_medical_form_edit, container, false)

        inputName = view.findViewById(R.id.inputName)
        inputFirstName = view.findViewById(R.id.inputFirstName)
        inputBirthDate = view.findViewById(R.id.inputBirthDate)
        inputBloodGroup = view.findViewById(R.id.inputBloodGroup)
        inputDoctor = view.findViewById(R.id.inputDoctor)
        inputDoctorPhone = view.findViewById(R.id.inputDoctorPhone)
        inputAllergies = view.findViewById(R.id.inputAllergies)
        inputContacts = view.findViewById(R.id.inputContacts)

        btnSave = view.findViewById(R.id.btnSave)
        btnExport = view.findViewById(R.id.btnExport)
        btnImport = view.findViewById(R.id.btnImport)

        loadDataFromFile()

        btnSave.setOnClickListener { saveData() }
        btnExport.setOnClickListener { generateQRCode() }
        btnImport.setOnClickListener { startQRScanner() }

        return view
    }

    private fun loadDataFromFile() {
        val file = File(requireContext().filesDir, fileName)
        if (!file.exists()) return

        val content = file.readText()
        val json = JSONObject(content)
        val utilisateur = json.getJSONObject("urgence").getJSONObject("utilisateur")

        inputName.setText(utilisateur.getString("nom"))
        inputFirstName.setText(utilisateur.getString("prenom"))
        inputBirthDate.setText(utilisateur.getString("date"))
        inputBloodGroup.setText(utilisateur.getString("groupe"))

        val medecin = utilisateur.getJSONObject("medecin")
        inputDoctor.setText(medecin.getString("nomMedecin"))
        inputDoctorPhone.setText(medecin.getString("numeroMedecin"))

        inputAllergies.setText(utilisateur.getJSONArray("allergenes").join(", "))

        val contactsArray = utilisateur.getJSONArray("contact")
        val contactsString = (0 until contactsArray.length()).joinToString(";") { i ->
            val c = contactsArray.getJSONObject(i)
            "${c.getString("prenomContact")},${c.getString("nomContact")},${c.getString("numeroContact")}"
        }
        inputContacts.setText(contactsString)
    }

    private fun saveData() {
        val fields = listOf(inputName, inputFirstName, inputBirthDate, inputBloodGroup,
            inputDoctor, inputDoctorPhone, inputAllergies, inputContacts)
        if (fields.any { it.text.toString().isBlank() }) {
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

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

        val json = JSONObject().apply {
            put("urgence", JSONObject().apply { put("utilisateur", utilisateur) })
            put("confidentiel", JSONObject().apply {
                put("antecedents", JSONArray())
                put("vaccins", JSONArray())
                put("handicap", JSONArray())
            })
        }

        File(requireContext().filesDir, fileName).writeText(json.toString())
        Toast.makeText(requireContext(), "Données mises à jour ✅", Toast.LENGTH_SHORT).show()
    }

    private fun generateQRCode() {
        val file = File(requireContext().filesDir, fileName)
        if (!file.exists()) {
            Toast.makeText(requireContext(), "Aucun fichier à exporter", Toast.LENGTH_SHORT).show()
            return
        }

        val content = file.readText()
        try {
            val width = 500
            val height = 500
            val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }

            val dialogView = layoutInflater.inflate(R.layout.dialog_qr_code, null)
            val qrImage = dialogView.findViewById<ImageView>(R.id.qrImageViewPopup)
            qrImage.setImageBitmap(bmp)

            val dialog = android.app.AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Fermer") { d, _ -> d.dismiss() }
                .create()
            dialog.show()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Erreur génération QR: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startQRScanner() {
        val options = ScanOptions()
        options.setPrompt("Scanne le QR Code contenant le JSON")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        qrScanLauncher.launch(options)
    }
}
