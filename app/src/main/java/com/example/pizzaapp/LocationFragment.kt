package com.example.pizzaapp

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.pizzaapp.databinding.FragmentLocationBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LocationFragment : Fragment() {
    private lateinit var binding: FragmentLocationBinding
    private val db = Firebase.firestore
    private var cities = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val refDb = db.collection("streets")

        refDb.orderBy("name").get().addOnSuccessListener {documents ->
            for (document in documents) {
                cities.add(document.data.get("name").toString())
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentLocationBinding.inflate(inflater, container, false)

        binding.db.setOnClickListener {

        }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line, cities
        )
        binding.streetText.setAdapter(adapter)
        return binding.root
    }
}