package com.example.pizzaapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LocationViewModel(private val db: FirebaseFirestore, private val auth: FirebaseAuth) : ViewModel() {

    val id = auth.currentUser?.uid.toString()
    val addressLiveData = MutableLiveData<List<String>>()
    val streetLiveData = MutableLiveData<List<String>>()

    init {
        getAddress()
        getCityStreets()
    }

    fun saveToHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val order = db.collection("users").document(id).collection("pizzas").get().await()
            order.documents.forEach { pizza ->
                val count = pizza.get("count") as Long
                val pizzaI = pizza.id
                val price = pizza.get("price") as Long
                val historyPizza = hashMapOf(
                    "count" to count,
                    "price" to price
                )
                db.collection("users").document(id).collection("history").document(pizzaI).set(historyPizza).await()
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

    fun getAddress(){
        var street: String
        var house: String
        var appartment: String
        val living = mutableListOf<String>()
        viewModelScope.launch(Dispatchers.IO) {
            val history = db.collection("users").document(id).get().await()
            street = history.data?.get("street").toString()
            house = history.data?.get("house").toString()
            appartment = history.data?.get("apartment").toString()
            if (street.equals("null")){
                living.add("")
            }
            else{
                living.add(street)
            }
            if (house.equals("null")){
                living.add("")
            }
            else{
                living.add(house)
            }
            if (appartment.equals("null")){
                living.add("")
            }
            else{
                living.add(appartment)
            }
            addressLiveData.postValue(living)
        }
    }
    fun getCityStreets(){
        val streetsList = mutableListOf<String>()
        viewModelScope.launch(Dispatchers.IO) {
            val streets = db.collection("streets").get().await()
            streets.documents.forEach {
                streetsList.add(it.data?.get("name").toString())
            }
            streetLiveData.postValue(streetsList)
        }
    }
}