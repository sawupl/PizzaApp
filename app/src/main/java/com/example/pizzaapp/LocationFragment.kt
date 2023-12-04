package com.example.pizzaapp

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pizzaapp.databinding.FragmentLocationBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LocationFragment : Fragment() {
    private lateinit var binding: FragmentLocationBinding
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private var streets = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val refDb = db.collection("streets")

        refDb.orderBy("name").get().addOnSuccessListener {documents ->
            for (document in documents) {
                streets.add(document.data.get("name").toString())
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentLocationBinding.inflate(inflater, container, false)


        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line, streets
        )
        binding.streetText.setAdapter(adapter)


        binding.addAddress.setOnClickListener {
            var canBeAdded = true

            val city = "Самара"

            val street = binding.streetText.text.toString()
            var correctStreet = false

            if (street in streets){
                correctStreet = true
            }

            if (!correctStreet){
                canBeAdded = false
                val toast = Toast.makeText(requireContext(), "Введите улицу корректно", Toast.LENGTH_SHORT)
                toast.show()
            }

            val house = binding.houseText.text.toString()
            if (house.isEmpty()){
                canBeAdded = false
                val toast = Toast.makeText(requireContext(), "Введите номер дома", Toast.LENGTH_SHORT)
                toast.show()
            }

            val apartment = binding.apartmentText.text.toString()
            if (apartment.isEmpty()){
                canBeAdded = false
                val toast = Toast.makeText(requireContext(), "Введите номер квартиры", Toast.LENGTH_SHORT)
                toast.show()
            }

            if (canBeAdded){
                val addressString = "г.$city ул.$street д.$house кв.$apartment"
                val address = hashMapOf(
                    "address" to addressString
                )
                db.collection("users")
                    .document(auth.currentUser?.uid.toString())
                    .set(address)
            }

        }

        return binding.root
    }
}