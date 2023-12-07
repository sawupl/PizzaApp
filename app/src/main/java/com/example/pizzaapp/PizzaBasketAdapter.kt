package com.example.pizzaapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzaapp.databinding.BasketPizzaItemBinding
import com.squareup.picasso.Picasso


class PizzaBasketAdapter(private val pizzaList: ArrayList<Pizza>, private val viewModel: BasketViewModel, private val context: Context): RecyclerView.Adapter<PizzaBasketAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: BasketPizzaItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BasketPizzaItemBinding.inflate(LayoutInflater.from(parent.context), parent,  false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val id = pizzaList[position].id.toString()
        val name = pizzaList[position].name
        val imageUrl = pizzaList[position].imageUrl
        val ingredients = pizzaList[position].ingredients
        val count = pizzaList[position].count
        val price = pizzaList[position].count?.times(pizzaList[position].price!!)
        holder.binding.apply {
            pizzaName.text = name
            pizzaIngredients.text = ingredients
            sumOfPizza.text = price.toString()
            Picasso.get().load(imageUrl).into(pizzaIcon)
            pizzaCount.setText(count.toString())
        }

        holder.binding.plus.setOnClickListener {
            pizzaList[position].count = pizzaList[position].count?.plus(1)
            holder.binding.pizzaCount.setText(pizzaList[position].count.toString())
            val pizzaFullPrice = (pizzaList[position].count?.times(pizzaList[position].price!!))
            holder.binding.sumOfPizza.text = pizzaFullPrice.toString()
            viewModel.addPizza(id, pizzaFullPrice!!)

        }

        holder.binding.minus.setOnClickListener{
            val currentCount = pizzaList[position].count
            var pizzaFullPrice:Long =0
            if (currentCount!! > 1) {
                pizzaList[position].count = pizzaList[position].count?.minus(1)
                holder.binding.pizzaCount.setText(pizzaList[position].count.toString())
                pizzaFullPrice = (pizzaList[position].count?.times(pizzaList[position].price!!)!!)
                holder.binding.sumOfPizza.text = pizzaFullPrice.toString()
            }
            else {
                removeItem(position)
            }
            viewModel.removePizza(id, pizzaFullPrice)
        }


        holder.binding.pizzaCount.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val countString = v.text.toString()
                if (countString.isNotEmpty()) {
                    try {
                        val count = countString.toLong()
                        val pizzaId = pizzaList[position].id.toString()
                        if (count > 0) {
                            pizzaList[position].count = count
                            var pizzaFullPrice = count * pizzaList[position].price!!
                            viewModel.updatePizzaCount(pizzaId, count, pizzaFullPrice)
                        }
                        else {
                            Toast.makeText(context, "Вводить 0 нельзя.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e: NumberFormatException) {
                        Toast.makeText(context, "Слишком большое число.", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(context, "Заполните поле.", Toast.LENGTH_SHORT).show()
                }
                false
            } else false
        }
    }
    override fun getItemCount(): Int {
        return pizzaList.size
    }
    private fun removeItem(position: Int) {
        pizzaList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, pizzaList.size)
    }
}
