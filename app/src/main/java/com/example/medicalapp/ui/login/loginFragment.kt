package com.example.medicalapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.medicalapp.R
import com.example.medicalapp.data.UserPreferences

class loginFragment : Fragment() {

    private lateinit var userPrefs: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        userPrefs = UserPreferences(requireContext())

        val usernameInput = view.findViewById<EditText>(R.id.usernameInput)
        val loginBtn = view.findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener {
            val username = usernameInput.text.toString()
            if (username.isNotEmpty()) {
                if (userPrefs.hasPin()) {
                    findNavController().navigate(R.id.action_loginFragment_to_pinLoginFragment)
                } else {
                    findNavController().navigate(R.id.action_loginFragment_to_pinSetupFragment)
                }
            }
        }

        return view
    }
}
