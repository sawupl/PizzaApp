package com.example.pizzaapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pizzaapp.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentRegistrationBinding.inflate(inflater, container, false)
        binding.register.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val login = binding.login.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && login.isNotEmpty()){
                registration(
                    email = email,
                    password = password,
                    login = login
                )
            }
            else{
                val toast = Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
        binding.toLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
    private fun registration(email: String, password: String, login: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    addUserInDatabase(login, auth.currentUser?.uid)
                    findNavController().navigate(R.id.action_registrationFragment_to_main_navigation)
                } else {
                    val toast = Toast.makeText(requireContext(), "Неправильный ввод данных", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
    }

    private fun addUserInDatabase(login: String, uid: String?) {
        val user = hashMapOf(
            "login" to login
        )
        db.collection("users")
            .document(uid.toString())
            .set(user)
    }
}