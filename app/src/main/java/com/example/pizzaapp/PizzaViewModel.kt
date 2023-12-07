package com.example.pizzaapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PizzaViewModel(private val db: FirebaseFirestore, private val auth: FirebaseAuth): ViewModel(){
    val ingredientLiveData = MutableLiveData<PizzaIngredient>()
    val id = auth.currentUser?.uid.toString()

    fun getPizza(pizza: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val userIngredientList = mutableListOf<String>()
            val userIngredientRef = db.collection("users").document(id).collection("like-ingredient").get().await()
            userIngredientRef.documents.forEach {
                userIngredientList.add(it.id.toLowerCase())
            }
            val pizzaIngredientList = mutableListOf<Ingredient>()
            val pizzaRef = db.collection("pizzas").document(pizza.toString()).get().await()

                val id = pizzaRef.id
                val name = pizzaRef.data?.get("name").toString()
                val picture = pizzaRef.data?.get("picture").toString()
                val ingredientRef = pizzaRef.reference.collection("ingredient").get().await()
                ingredientRef.documents.forEach { ingredientItem ->
                    val ingredient = ingredientItem.data?.get("ingredient").toString().toLowerCase()
                    if (ingredient in userIngredientList){
                        pizzaIngredientList.add(Ingredient(ingredient,true))
                    }
                    else{
                        pizzaIngredientList.add(Ingredient(ingredient,false))
                    }
                }

            ingredientLiveData.postValue(PizzaIngredient(id,name,picture,pizzaIngredientList))
        }
    }
    fun likePizza(ingredientName: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val ingredient = hashMapOf(
                "like" to true,
            )
            val likePizzaRef = db.collection("users").document(id).collection("like-ingredient").document(
                ingredientName.toString().toLowerCase()
            )
            val likePizzaRefValue = likePizzaRef.get().await()
            if (likePizzaRefValue.exists()) {
                likePizzaRef.delete()
            }
            else {
                likePizzaRef.set(ingredient)
            }
        }
    }
}