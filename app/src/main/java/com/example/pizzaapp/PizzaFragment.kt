package com.example.pizzaapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pizzaapp.databinding.FragmentLocationBinding
import com.example.pizzaapp.databinding.FragmentPizzaBinding


class PizzaFragment : Fragment() {
    private lateinit var binding: FragmentPizzaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentPizzaBinding.inflate(inflater, container, false)


        return binding.root
    }

}