package com.example.pizzaapp

data class Pizza(
    val id: String?,
    val name: String?,
    val imageUrl: String?,
    val ingredients: String?,
    var added: Boolean = false
)
