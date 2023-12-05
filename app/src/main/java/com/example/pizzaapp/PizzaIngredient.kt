package com.example.pizzaapp

data class PizzaIngredient(
    val id: String?,
    val name: String?,
    val imageUrl: String?,
    val ingredients: List<Ingredient>
)
