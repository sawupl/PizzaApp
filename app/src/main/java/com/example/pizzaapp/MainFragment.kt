package com.example.pizzaapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzaapp.databinding.FragmentMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val db = Firebase.firestore
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory())[MainViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentMainBinding.inflate(inflater, container, false)
        viewModel.pizzaLiveData.observe(viewLifecycleOwner){

            val adapter = PizzaAdapter(it, viewModel, requireContext())
            binding.recipeView.adapter = adapter
            binding.recipeView.layoutManager = LinearLayoutManager(context)

        }




        return binding.root
    }
}