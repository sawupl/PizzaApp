package com.example.pizzaapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzaapp.databinding.FragmentPizzaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.tasks.await


class PizzaFragment : Fragment() {
    private lateinit var binding: FragmentPizzaBinding
    private var pizzaId: String? = null
    private val db = Firebase.firestore
    private lateinit var viewModel: PizzaViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentPizzaBinding.inflate(inflater, container, false)
        pizzaId = arguments?.getString("pizzaId")

        db.collection("pizzas").document(pizzaId.toString()).get().addOnSuccessListener {
            val name = it.data?.get("name").toString()
            val picture = it.data?.get("picture").toString()
            binding.textView.text = name
            Picasso.get().load(picture).into(binding.imageView)
        }



        viewModel = ViewModelProvider(this, ViewModelFactory())[PizzaViewModel::class.java]

        viewModel.getPizza(pizzaId)

        viewModel.ingredientLiveData.observe(viewLifecycleOwner){
            val adapter = IngredientAdapter(it.ingredients, viewModel, requireContext())
            binding.recyclerIngredient.adapter = adapter
            binding.recyclerIngredient.layoutManager = LinearLayoutManager(context)

        }
        return binding.root
    }
}