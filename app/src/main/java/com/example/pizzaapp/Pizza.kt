package com.example.pizzaapp

data class Pizza(
    val id: String?,
    val name: String?,
    val price: Long?,
    val imageUrl: String?,
    val ingredients: String?,
    var added: Boolean = false,
    var like: Boolean = false,
    var count: Long? = null
)
