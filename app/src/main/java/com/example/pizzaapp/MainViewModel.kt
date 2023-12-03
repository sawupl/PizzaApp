package com.example.pizzaapp

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel(private val db: FirebaseFirestore): ViewModel() {

    val pizzaLiveData = MutableLiveData<List<Pizza>>()
    init {
        getListOfPizza()
    }


    private fun getListOfPizza(){
        val pizzaRef = db.collection("pizzas")

        val pizzaList = mutableListOf<Pizza>()

        pizzaRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    document.documents.forEach {


                        /*
                          ЯРИК, Я ПЕРЕДЕЛАЛ, ДОЛЖНО РАБОТАТЬ
                         */

                        val pizzaName = it.data?.get("name").toString()
                        /*
                          ЯРИК, Я ПЕРЕДЕЛАЛ, ДОЛЖНО РАБОТАТЬ
                         */
                        val pizzaPicture = it.data?.get("picture").toString()
                        /*
                          ЯРИК, Я ПЕРЕДЕЛАЛ, ДОЛЖНО РАБОТАТЬ
                         */
                        var pizzaIngredientString = ""
                        it.reference.collection("ingredient").get().addOnSuccessListener { ingr ->
                            ingr.documents.forEach {ingredient ->
                                pizzaIngredientString = pizzaIngredientString + ingredient.data?.get("ingredient") + ", "
                            }
                            pizzaIngredientString = pizzaIngredientString.substring(0, pizzaIngredientString.length - 2)
                        }
                            /*
                          ЯРИК, Я ПЕРЕДЕЛАЛ, ДОЛЖНО РАБОТАТЬ
                         */
                            .addOnCompleteListener{
                                pizzaList.add(Pizza(pizzaName,pizzaPicture,pizzaIngredientString))
                                Log.d(TAG, "data: $pizzaName, $pizzaPicture, $pizzaIngredientString")
                                pizzaLiveData.value = pizzaList
                            }
                        /*
                          ЯРИК, Я ПЕРЕДЕЛАЛ, ДОЛЖНО РАБОТАТЬ
                         */
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