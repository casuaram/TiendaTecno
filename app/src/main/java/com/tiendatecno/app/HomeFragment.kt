package com.tiendatecno.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvProducts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val productList = listOf(
            "Audífonos Bluetooth Pro",
            "Cargador Rápido 20W",
            "Cable Tipo C 2 metros",
            "Funda Protectora",
            "Smartwatch Deportivo",
            "PowerBank 10000mAh"
        )

        recyclerView.adapter = ProductAdapter(productList)
        return view
    }
}