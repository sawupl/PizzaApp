package com.example.pizzaapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LocationViewModel(private val db: FirebaseFirestore, private val auth: FirebaseAuth) : ViewModel() {

    val id = auth.currentUser?.uid.toString()

    fun saveToHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val order = db.collection("users").document(id).collection("pizzas").get().await()
            order.documents.forEach { pizza ->
                val count = pizza.get("count") as Long
                val pizzaId = pizza.id
                val price = pizza.get("price") as Long
                val historyPizza = hashMapOf(
                    "count" to count,
                    "price" to price
                )
                db.collection("users").document(id).collection("history").document(pizzaId).set(historyPizza).await()
            }
        }
    }

    fun clearUserCurrentOrder(){
        viewModelScope.launch(Dispatchers.IO) {
            val order = db.collection("users").document(id).collection("pizzas").get().await()
            order.documents.forEach {
                it.reference.delete()
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val history = db.collection("users").document(id).collection("history").get().await()
            history.documents.forEach {
                it.reference.delete()
            }
        }
    }

}