package com.example.pizzaapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzaapp.databinding.FragmentPizzaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class PizzaFragment : Fragment() {
    private lateinit var binding: FragmentPizzaBinding
    private var pizzaId: String? = null
    private lateinit var viewModel: PizzaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentPizzaBinding.inflate(inflater, container, false)
        pizzaId = arguments?.getString("pizzaId")


        binding.backToMain.setOnClickListener {
            findNavController().popBackStack()
        }


        viewModel = ViewModelProvider(this, ViewModelFactory())[PizzaViewModel::class.java]

        viewModel.getPizza(pizzaId)

        viewModel.ingredientLiveData.observe(viewLifecycleOwner){
            val adapter = IngredientAdapter(it.ingredients, viewModel, requireContext())
            binding.recyclerIngredient.adapter = adapter
            binding.recyclerIngredient.layoutManager = LinearLayoutManager(context)
            binding.textView.text = it.name
            Picasso.get().load(it.imageUrl).into(binding.imageView)
        }
        return binding.root
    }
}