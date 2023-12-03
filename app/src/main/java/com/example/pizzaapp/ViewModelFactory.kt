package com.example.pizzaapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception


class ViewModelFactory() :
    ViewModelProvider.NewInstanceFactory() {

    private val viewModelHashMap = HashMap<String, ViewModel>()
    private val db = Firebase.firestore


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val key = MainViewModel::class.java.name
            return if (viewModelHashMap.containsKey(key)) {
                getViewModel(key) as T
            } else {
                val viewModel: ViewModel = MainViewModel(db)
                addViewModel(key, viewModel)
                getViewModel(key) as T
            }
        }
        else{
            throw ClassNotFoundException("нет такой ViewModel")
        }
    }

    private fun addViewModel(key: String, viewModel: ViewModel) {
        viewModelHashMap[key] = viewModel
    }

    private fun getViewModel(key: String): ViewModel? {
        return viewModelHashMap[key]
    }
}