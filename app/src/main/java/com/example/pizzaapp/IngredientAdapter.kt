package com.example.pizzaapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzaapp.databinding.IngredientItemBinding

class IngredientAdapter(private val ingredientList: List<Ingredient>, private val viewModel: PizzaViewModel, private val context: Context): RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: IngredientItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientAdapter.ViewHolder {
        val binding = IngredientItemBinding.inflate(LayoutInflater.from(parent.context), parent,  false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: IngredientAdapter.ViewHolder, position: Int) {
        val name = ingredientList[position].name
        holder.binding.apply {
            ingredientName.text = name
            if (ingredientList[position].added) {
                favouriteIngredient.setColorFilter(ContextCompat.getColor(context, R.color.red))
            }
            else {
                favouriteIngredient.setColorFilter(ContextCompat.getColor(context, R.color.black))
            }
        }
        holder.binding.favouriteIngredient.setOnClickListener{
            if (ingredientList[position].added) {
                holder.binding.favouriteIngredient.setColorFilter(ContextCompat.getColor(context, R.color.black))
                ingredientList[position].added = false
                viewModel.likePizza(name)
            }
            else {
                holder.binding.favouriteIngredient.setColorFilter(ContextCompat.getColor(context, R.color.red))
                ingredientList[position].added = true
                viewModel.likePizza(name)
            }
        }
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
}
