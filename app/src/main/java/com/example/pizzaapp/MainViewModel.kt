package com.example.pizzaapp

import android.content.ContentValues.TAG
import android.util.Log
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
import kotlinx.coroutines.withContext
import java.util.Date

class MainViewModel(private val db: FirebaseFirestore, private val auth: FirebaseAuth): ViewModel() {

    val pizzaLiveData = MutableLiveData<List<Pizza>>()
    val id = auth.currentUser?.uid.toString()
    init {
//        getListOfPizza()
        getListOfPizzas()
    }

    private fun getListOfPizzas() {
        viewModelScope.launch(Dispatchers.IO) {
            val userPizzaList = mutableListOf<String>()
            val userPizzaRef = db.collection("users").document(id).collection("pizzas").get().await()
            userPizzaRef.documents.forEach {
                userPizzaList.add(it.id)
            }
            userPizzaList.forEach{ println(it) }
            val pizzaList = mutableListOf<Pizza>()
            val pizzaRef = db.collection("pizzas").get().await()
            pizzaRef.documents.forEach { pizzaItem ->
                val id = pizzaItem.id
                val name = pizzaItem.data?.get("name").toString()
                val picture = pizzaItem.data?.get("picture").toString()
                val ingredientRef = pizzaItem.reference.collection("ingredient").get().await()
                var ingregientInString = ""
                ingredientRef.documents.forEach { ingredientItem ->
                    val ingredient = ingredientItem.data?.get("ingredient").toString()
                    ingregientInString += "$ingredient, "
                }
                ingregientInString = ingregientInString.substring(0, ingregientInString.length - 2)
                println("-------------------")
                if (id in userPizzaList) {
                    println(id)
                    pizzaList.add(Pizza(id, name, picture, ingregientInString, true))
                }
                else {
                    pizzaList.add(Pizza(id, name, picture, ingregientInString))
                }
            }
            println("------------------")
            println(pizzaList.forEach { println(it.id + " " + it.name + " " + it.imageUrl + " " + it.ingredients + " " + it.added) })
            pizzaLiveData.postValue(pizzaList)
        }
    }
    private fun getListOfUserPizzas() {
        viewModelScope.launch(Dispatchers.IO) {

//            val pizzaList = mutableListOf<Pizza>()
//            val pizzaRef = db.collection("pizzas").get().await()
//            pizzaRef.documents.forEach { pizzaItem ->
//                val id = pizzaItem.id
//                val name = pizzaItem.data?.get("name").toString()
//                val picture = pizzaItem.data?.get("picture").toString()
//                val ingredientRef = pizzaItem.reference.collection("ingredient").get().await()
//                var ingregientInString = ""
//                ingredientRef.documents.forEach { ingredientItem ->
//                    val ingredient = ingredientItem.data?.get("ingredient").toString()
//                    ingregientInString += "$ingredient, "
//                }
//                ingregientInString = ingregientInString.substring(0, ingregientInString.length - 2)
//                pizzaList.add(Pizza(id, name, picture, ingregientInString))
//            }
//            pizzaLiveData.postValue(pizzaList)
        }
    }

    fun addPizza(pizzaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val inc = FieldValue.increment(1)
            val pizza = hashMapOf(
                "count" to inc
            )
            db.collection("users").document(id).collection("pizzas").document(pizzaId).set(pizza, SetOptions.merge())
        }
    }

    fun deletePizza(pizzaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("users").document(id).collection("pizzas").document(pizzaId).delete().await()
        }
    }

//    suspend fun updatePizza(pizzaId: String): String = withContext(Dispatchers.IO) {
//        val pizzaRef = db.collection("users").document(id).collection("pizzas").document(pizzaId).get().await()
//        return@withContext if (pizzaRef.exists()) pizzaRef.get("count").toString() else "0"
//    }

//    fun deletePizza(pizzaId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val inc = FieldValue.increment(-1)
//            val pizza = hashMapOf(
//                "count" to inc
//            )
//            db.collection("users").document(id).collection("pizzas").document(pizzaId).set(pizza, SetOptions.merge())
//        }
//    }


//    private fun getListOfPizza(){
//        val pizzaRef = db.collection("pizzas")
//
//        val pizzaList = mutableListOf<Pizza>()
//        var i = 1
//        pizzaRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    document.documents.forEach {
//
//                        val pizzaName = it.data?.get("name").toString()
//
//                        val pizzaPicture = it.data?.get("picture").toString()
//
//                        var pizzaIngredientString = ""
//                        it.reference.collection("ingredient").get()
//                            .addOnSuccessListener { ingr ->
//                            ingr.documents.forEach {ingredient ->
//                                pizzaIngredientString = pizzaIngredientString + ingredient.data?.get("ingredient") + ", "
//                            }
//                            pizzaIngredientString = pizzaIngredientString.substring(0, pizzaIngredientString.length - 2)
//                        }
//                            .addOnCompleteListener{
//                                pizzaList.add(Pizza(pizzaName,pizzaPicture,pizzaIngredientString))
//                                Log.d(TAG, "data:$i, $pizzaName, $pizzaPicture, $pizzaIngredientString")
//                                i++
//                                pizzaLiveData.value = pizzaList
//                            }
//
//                    }
//
//                    Log.d(TAG, "DocumentSnapshot data: ${document.documents.toList()}")
//                } else {
//                    Log.d(TAG, "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "get failed with ", exception)
//            }
//    }
}