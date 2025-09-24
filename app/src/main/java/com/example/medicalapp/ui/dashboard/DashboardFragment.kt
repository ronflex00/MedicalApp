package com.example.medicalapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.medicalapp.R
import com.example.medicalapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // Message de bienvenue
        binding.textDashboard.text = "Bienvenue dans ton tableau de bord ! 🚀"

        // Bouton Mon carnet médical
        binding.btnMedicalForm.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_medicalFormEditFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}