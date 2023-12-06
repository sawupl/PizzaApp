package com.example.pizzaapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzaapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
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

        //binding.toBasket.setOnClickListener {
//            findNavController().navigate(R.id.action_mainFragment_to_basketFragment)
        //}

        binding.findPizzaButton.setOnClickListener {
            viewModel.getPizzaWithLikeIngredients()
        }

        return binding.root
    }
}