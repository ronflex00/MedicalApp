package com.example.medicalapp.ui.accueil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.medicalapp.R   // ✅ bien importer le R de ton app
import com.example.medicalapp.databinding.FragmentAccueilBinding

class AccueilFragment : Fragment() {

    private var _binding: FragmentAccueilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccueilBinding.inflate(inflater, container, false)

        binding.btnInscrire.setOnClickListener {
            findNavController().navigate(R.id.action_accueilFragment_to_medicalFormFragment)
        }

        binding.btnDejaUtilisateur.setOnClickListener {
            findNavController().navigate(R.id.action_accueilFragment_to_pinLoginFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
