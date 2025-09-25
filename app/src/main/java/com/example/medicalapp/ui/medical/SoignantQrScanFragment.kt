package com.example.medicalapp.ui.medical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.medicalapp.databinding.FragmentSoignantQrScanBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class SoignantQrScanFragment : Fragment() {

    private var _binding: FragmentSoignantQrScanBinding? = null
    private val binding get() = _binding!!

    private val qrScanner = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            // ✅ Affiche le JSON scanné
            binding.tvScanResult.text = "QR Code lu : \n${result.contents}"
        } else {
            binding.tvScanResult.text = "Aucun QR Code détecté."
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSoignantQrScanBinding.inflate(inflater, container, false)

        // Bouton lancer scanner
        binding.btnStartScan.setOnClickListener {
            val options = ScanOptions().apply {
                setPrompt("Scannez le QR Code du patient")
                setBeepEnabled(true)
                setOrientationLocked(true)
                setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            }
            qrScanner.launch(options)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
