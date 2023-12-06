package com.example.pizzaapp

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
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
        val id = pizzaList[position].id.toString()
        val name = pizzaList[position].name
        val price = pizzaList[position].price
        val imageUrl = pizzaList[position].imageUrl
        val ingredients = pizzaList[position].ingredients
        holder.binding.apply {
            pizzaName.text = name
            pizzaIngredients.text = ingredients
            pizzaIngredients.text = Html.fromHtml(ingredients)
            textView2.text = price.toString()
            Picasso.get().load(imageUrl).into(pizzaIcon)
            if (pizzaList[position].added) {
//                println("$position added position")
                basket.setColorFilter(ContextCompat.getColor(context, R.color.blue))
            }
            else {
                basket.setColorFilter(ContextCompat.getColor(context, R.color.black))
            }
            if (pizzaList[position].like) {
//                println("$position like position")
                like.setColorFilter(ContextCompat.getColor(context, R.color.red))
            }
            else {
//                println("here $position")
                like.setColorFilter(ContextCompat.getColor(context, R.color.black))
            }
        }
        holder.binding.basket.setOnClickListener {
            println("click added")
            if (pizzaList[position].added) {
                holder.binding.basket.setColorFilter(ContextCompat.getColor(context, R.color.black))
                pizzaList[position].added = false
                viewModel.deletePizza(id)
            }
            else {
                holder.binding.basket.setColorFilter(ContextCompat.getColor(context, R.color.blue))
                pizzaList[position].added = true
                viewModel.addPizza(id)
            }
        }

        holder.binding.like.setOnClickListener{
            println("click like")
            if (pizzaList[position].like) {
                holder.binding.like.setColorFilter(ContextCompat.getColor(context, R.color.black))
                pizzaList[position].like = false
                viewModel.likePizza(id)
            }
            else {
                holder.binding.like.setColorFilter(ContextCompat.getColor(context, R.color.red))
                pizzaList[position].like = true
                viewModel.likePizza(id)
            }
        }
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("pizzaId", pizzaList[position].id)
            findNavController(it).navigate(R.id.action_mainFragment_to_pizzaFragment, bundle)
        }
    }



    override fun getItemCount(): Int {
        return pizzaList.size
    }
}
