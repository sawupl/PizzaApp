package com.example.pizzaapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BasketViewModel(private val db: FirebaseFirestore, private val auth: FirebaseAuth) : ViewModel() {

    val usersPizzaLiveData = MutableLiveData<ArrayList<Pizza>>()
    val id = auth.currentUser?.uid.toString()

    fun getListOfUserPizzas() {
        viewModelScope.launch(Dispatchers.IO) {
            val pizzaList = ArrayList<Pizza>()
            val userPizzaRef = db.collection("users").document(id).collection("pizzas").get().await()
            userPizzaRef.documents.forEach { pizza ->
                val pizzaRef = db.collection("pizzas").document(pizza.id).get().await()
                val count = pizza.get("count") as Long
                val id = pizzaRef.id
                val name = pizzaRef.data?.get("name").toString()
                val picture = pizzaRef.data?.get("picture").toString()
                val price = pizzaRef.data?.get("price") as Long
                val ingredientRef = pizzaRef.reference.collection("ingredient").get().await()
                var ingregientInString = ""
                ingredientRef.documents.forEach { ingredientItem ->
                    val ingredient = ingredientItem.data?.get("ingredient").toString()
                    ingregientInString += "$ingredient, "
                }
                ingregientInString = ingregientInString.substring(0, ingregientInString.length - 2)
                pizzaList.add(Pizza(id = id, name = name,price = price ,imageUrl = picture, ingredients = ingregientInString, count = count))
            }
            usersPizzaLiveData.postValue(pizzaList)
        }
    }

    fun addPizza(pizzaId: String, price:Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val inc = FieldValue.increment(1)
            val pizza = hashMapOf(
                "count" to inc,
                "price" to price
            )
            db.collection("users").document(id).collection("pizzas").document(pizzaId).set(pizza, SetOptions.merge())
        }
    }

    fun removePizza(pizzaId: String, price:Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val pizzaItem = db.collection("users").document(id).collection("pizzas").document(pizzaId).get().await()
            val count = pizzaItem.get("count") as Long
            if (count > 1) {
                val inc = FieldValue.increment(-1)
                val pizza = hashMapOf(
                    "count" to inc,
                    "price" to price
                )
                db.collection("users").document(id).collection("pizzas").document(pizzaId).set(pizza, SetOptions.merge())
            }
            else {
                db.collection("users").document(id).collection("pizzas").document(pizzaId).delete().await()
            }
        }
    }

    fun updatePizzaCount(pizzaId: String, count: Long, price:Long) {
        val pizza = hashMapOf(
            "count" to count,
            "price" to price
        )
        db.collection("users").document(id).collection("pizzas").document(pizzaId).set(pizza)
    }

    fun getPreviousOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            val pizzaList = ArrayList<Pizza>()
            val userPizzaRef = db.collection("users").document(id).collection("history").get().await()
            userPizzaRef.documents.forEach { pizza ->
                val pizzaRef = db.collection("pizzas").document(pizza.id).get().await()
                val count = pizza.get("count") as Long
                val pizzaId = pizzaRef.id
                val name = pizzaRef.data?.get("name").toString()
                val picture = pizzaRef.data?.get("picture").toString()
                val price = pizzaRef.data?.get("price") as Long
                val fullPrice = count * price
                val ingredientRef = pizzaRef.reference.collection("ingredient").get().await()
                var ingregientInString = ""
                ingredientRef.documents.forEach { ingredientItem ->
                    val ingredient = ingredientItem.data?.get("ingredient").toString()
                    ingregientInString += "$ingredient, "
                }
                ingregientInString = ingregientInString.substring(0, ingregientInString.length - 2)
                updatePizzaCount(pizzaId, count, fullPrice)
                pizzaList.add(Pizza(id = pizzaId, name = name, price = price, imageUrl = picture, ingredients = ingregientInString, count = count))
            }
            usersPizzaLiveData.postValue(pizzaList)
        }
    }

    fun clear(){
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("users").document(id).collection("pizzas").get().await().forEach {
                it.reference.delete().await()
            }
        }
    }
}