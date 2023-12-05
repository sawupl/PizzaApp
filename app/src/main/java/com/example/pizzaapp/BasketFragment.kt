package com.example.pizzaapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzaapp.databinding.FragmentBasketBinding

class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasketBinding
    private lateinit var viewModel: BasketViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory())[BasketViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)

        viewModel.getListOfUserPizzas()

        viewModel.usersPizzaLiveData.observe(viewLifecycleOwner){
            val adapter = PizzaBasketAdapter(it, viewModel, requireContext())
            binding.recipeView.adapter = adapter
            binding.recipeView.layoutManager = LinearLayoutManager(context)
        }

        binding.order.setOnClickListener {
            findNavController().navigate(R.id.action_basketFragment_to_locationFragment)
        }

        binding.orderPrevious.setOnClickListener {
            viewModel.getPreviousOrder()
        }

        return binding.root
    }
}