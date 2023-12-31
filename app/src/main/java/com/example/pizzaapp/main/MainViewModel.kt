package com.example.pizzaapp.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzaapp.models.Pizza
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel(private val db: FirebaseFirestore, private val auth: FirebaseAuth): ViewModel() {

    val pizzaLiveData = MutableLiveData<List<Pizza>>()
    val id = auth.currentUser?.uid.toString()

    fun getListOfPizzas() {
        viewModelScope.launch(Dispatchers.IO) {
            val userPizzaList = mutableListOf<String>()
            val userPizzaRef =
                db.collection("users").document(id).collection("pizzas").get().await()
            userPizzaRef.documents.forEach {
                userPizzaList.add(it.id)
            }
            val userPizzaLikeList = mutableListOf<String>()
            val userPizzaLikeRef =
                db.collection("users").document(id).collection("like-pizza").get().await()
            userPizzaLikeRef.documents.forEach {
                userPizzaLikeList.add(it.id)
            }
            val pizzaList = mutableListOf<Pizza>()
            val pizzaRef = db.collection("pizzas").get().await()
            pizzaRef.documents.forEach { pizzaItem ->
                val id = pizzaItem.id
                val name = pizzaItem.data?.get("name").toString()
                val picture = pizzaItem.data?.get("picture").toString()
                val price = pizzaItem.data?.get("price") as Long
                val ingredientRef = pizzaItem.reference.collection("ingredient").get().await()
                var ingregientInString = ""
                ingredientRef.documents.forEach { ingredientItem ->
                    val ingredient = ingredientItem.data?.get("ingredient").toString()
                    ingregientInString += "$ingredient, "
                }
                ingregientInString = ingregientInString.substring(0, ingregientInString.length - 2)
                var added = false
                var like = false
                if (id in userPizzaList) {
                    added = true
                }
                if (id in userPizzaLikeList) {
                    like = true
                }
                pizzaList.add(
                    Pizza(
                        id = id,
                        name = name,
                        price = price,
                        imageUrl = picture,
                        ingredients = ingregientInString,
                        added = added,
                        like = like
                    )
                )
            }
            pizzaLiveData.postValue(pizzaList)
        }
    }

    fun addPizza(pizzaId: String, price: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val inc = FieldValue.increment(1)
            val pizza = hashMapOf(
                "count" to inc,
                "price" to price
            )
            db.collection("users").document(id).collection("pizzas").document(pizzaId)
                .set(pizza, SetOptions.merge())
        }
    }

    fun deletePizza(pizzaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("users").document(id).collection("pizzas").document(pizzaId).delete()
                .await()
        }
    }

    fun likePizza(pizzaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val pizza = hashMapOf(
                "like" to true
            )
            val likePizzaRef =
                db.collection("users").document(id).collection("like-pizza").document(pizzaId)
            val likePizzaRefValue = likePizzaRef.get().await()
            if (likePizzaRefValue.exists()) {
                likePizzaRef.delete()
            } else {
                likePizzaRef.set(pizza)
            }
        }
    }

    fun getPizzaWithLikeIngredients() {
        viewModelScope.launch(Dispatchers.IO) {
            // list of basket
            val userPizzaList = mutableListOf<String>()
            val userPizzaRef =
                db.collection("users").document(id).collection("pizzas").get().await()
            userPizzaRef.documents.forEach {
                userPizzaList.add(it.id)
            }
            // list of like pizza
            val userPizzaLikeList = mutableListOf<String>()
            val userPizzaLikeRef =
                db.collection("users").document(id).collection("like-pizza").get().await()
            userPizzaLikeRef.documents.forEach {
                userPizzaLikeList.add(it.id)
            }

            // list of like ingredients
            val userIngredientList = mutableListOf<String>()
            val userIngredientRef =
                db.collection("users").document(id).collection("like-ingredient").get().await()
            userIngredientRef.documents.forEach {
                userIngredientList.add(it.id)
            }

            // list of pizza
            val pizzaList = mutableListOf<Pizza>()
            val pizzaRef = db.collection("pizzas").get().await()
            pizzaRef.documents.forEach { pizzaItem ->
                val id = pizzaItem.id
                val name = pizzaItem.data?.get("name").toString()
                val picture = pizzaItem.data?.get("picture").toString()
                val price = pizzaItem.data?.get("price") as Long
                val ingredientRef = pizzaItem.reference.collection("ingredient").get().await()
                var ingregientInString = ""
                var isLikeIngredient = false
                ingredientRef.documents.forEach { ingredientItem ->
                    var ingredient = ingredientItem.data?.get("ingredient").toString()
                    if (ingredient in userIngredientList) {
                        isLikeIngredient = true
                        ingredient = "<font color='#C9007A'>$ingredient</font>"
                    }
                    ingregientInString += "$ingredient, "
                }
                ingregientInString = ingregientInString.substring(0, ingregientInString.length - 2)
                if (isLikeIngredient) {
                    var added = false
                    var like = false
                    if (id in userPizzaList) {
                        added = true
                    }
                    if (id in userPizzaLikeList) {
                        like = true
                    }
                    pizzaList.add(
                        Pizza(
                            id = id,
                            name = name,
                            price = price,
                            imageUrl = picture,
                            ingredients = ingregientInString,
                            added = added,
                            like = like
                        )
                    )
                }
            }
            pizzaLiveData.postValue(pizzaList)
            }
        }
    }