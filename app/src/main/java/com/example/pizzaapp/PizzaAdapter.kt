package com.example.pizzaapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzaapp.databinding.PizzaItemBinding


class PizzaAdapter(private val pizzaList: List<Pizza>): RecyclerView.Adapter<PizzaAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: PizzaItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PizzaItemBinding.inflate(LayoutInflater.from(parent.context), parent,  false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            recipeName.text = pizzaList[position].name
            recipeIngredients.text = pizzaList[position].ingredients
            //Picasso.get().load(pizzaList[position].imageUrl).into(recipeIcon)
        }
    }
    override fun getItemCount(): Int {
        return pizzaList.size
    }
}
