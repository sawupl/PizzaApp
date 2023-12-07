package com.example.pizzaapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pizzaapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val auth = Firebase.auth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentLoginBinding.inflate(inflater, container, false)

        binding.enter.setOnClickListener {
            signIn(
                email = binding.email.text.toString(),
                password = binding.password.text.toString()
            )
        }
        binding.toReg.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_main_navigation)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_loginFragment_to_main_navigation)
                } else {
                    val toast = Toast.makeText(requireContext(), "Неправильная почта или пароль", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
    }
}