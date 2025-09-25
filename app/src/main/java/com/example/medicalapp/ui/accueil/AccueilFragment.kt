package com.example.medicalapp.ui.accueil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.medicalapp.R
import com.example.medicalapp.databinding.FragmentAccueilBinding

class AccueilFragment : Fragment() {

    private var _binding: FragmentAccueilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccueilBinding.inflate(inflater, container, false)

        // Bouton S'inscrire
        binding.btnInscrire.setOnClickListener {
            findNavController().navigate(R.id.action_accueilFragment_to_medicalFormFragment)
        }

        // Bouton Déjà utilisateur
        binding.btnDejaUtilisateur.setOnClickListener {
            findNavController().navigate(R.id.action_accueilFragment_to_pinLoginFragment)
        }

        // Bouton Connexion Médecin
        binding.btnLoginMedecin.setOnClickListener {
            findNavController().navigate(R.id.action_accueilFragment_to_soignantLoginFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
