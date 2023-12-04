package com.example.pizzaapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzaapp.databinding.PizzaItemBinding
import com.squareup.picasso.Picasso


class PizzaAdapter(private val pizzaList: List<Pizza>, private val viewModel: MainViewModel, private val context: Context): RecyclerView.Adapter<PizzaAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: PizzaItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PizzaItemBinding.inflate(LayoutInflater.from(parent.context), parent,  false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            pizzaName.text = pizzaList[position].name
            pizzaIngredients.text = pizzaList[position].ingredients
            Picasso.get().load(pizzaList[position].imageUrl).into(pizzaIcon)
            println(position)
            if (pizzaList[position].added) {
                println("$position position")
                basket.setColorFilter(ContextCompat.getColor(context, R.color.blue));
            }
            else {
                basket.setColorFilter(ContextCompat.getColor(context, R.color.black));
            }
        }
        holder.binding.basket.setOnClickListener {
            if (pizzaList[position].added) {
                viewModel.deletePizza(pizzaList[position].id.toString())
                holder.binding.basket.setColorFilter(ContextCompat.getColor(context, R.color.black));
                pizzaList[position].added = false
            }
            else {
                viewModel.addPizza(pizzaList[position].id.toString())
                holder.binding.basket.setColorFilter(ContextCompat.getColor(context, R.color.blue));
                pizzaList[position].added = true
            }
        }
    }
    override fun getItemCount(): Int {
        return pizzaList.size
    }
}
