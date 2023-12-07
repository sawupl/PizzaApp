package com.example.pizzaapp.models

import com.example.pizzaapp.models.Ingredient

data class PizzaIngredient(
    val id: String?,
    val name: String?,
    val imageUrl: String?,
    val ingredients: List<Ingredient>
)
