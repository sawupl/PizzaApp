package com.example.pizzaapp

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

class MainViewModel(private val db: FirebaseFirestore): ViewModel() {

    val pizzaLiveData = MutableLiveData<List<Pizza>>()
    init {
//        getListOfPizza()
        getListOfPizzas()
    }

    private fun getListOfPizzas() {
        viewModelScope.launch(Dispatchers.IO) {
            val pizzaList = mutableListOf<Pizza>()
            val pizzaRef = db.collection("pizzas").get().await()
            pizzaRef.documents.forEach { pizzaItem ->
                val name = pizzaItem.data?.get("name").toString()
                val picture = pizzaItem.data?.get("picture").toString()
                val ingredientRef = pizzaItem.reference.collection("ingredient").get().await()
                var ingregientInString = ""
                ingredientRef.documents.forEach { ingredientItem ->
                    val ingredient = ingredientItem.data?.get("ingredient").toString()
                    ingregientInString += "$ingredient, "
                }
                ingregientInString = ingregientInString.substring(0, ingregientInString.length - 2)
                pizzaList.add(Pizza(name, picture, ingregientInString))
            }
            pizzaLiveData.postValue(pizzaList)
        }
    }


    private fun getListOfPizza(){
        val pizzaRef = db.collection("pizzas")

        val pizzaList = mutableListOf<Pizza>()
        var i = 1
        pizzaRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    document.documents.forEach {

                        val pizzaName = it.data?.get("name").toString()

                        val pizzaPicture = it.data?.get("picture").toString()

                        var pizzaIngredientString = ""
                        it.reference.collection("ingredient").get()
                            .addOnSuccessListener { ingr ->
                            ingr.documents.forEach {ingredient ->
                                pizzaIngredientString = pizzaIngredientString + ingredient.data?.get("ingredient") + ", "
                            }
                            pizzaIngredientString = pizzaIngredientString.substring(0, pizzaIngredientString.length - 2)
                        }
                            .addOnCompleteListener{
                                pizzaList.add(Pizza(pizzaName,pizzaPicture,pizzaIngredientString))
                                Log.d(TAG, "data:$i, $pizzaName, $pizzaPicture, $pizzaIngredientString")
                                i++
                                pizzaLiveData.value = pizzaList
                            }

                    }

                    Log.d(TAG, "DocumentSnapshot data: ${document.documents.toList()}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }
}