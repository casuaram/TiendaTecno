package com.tiendatecno.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CatalogFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCatalog)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val catalogList = listOf(
            "Audífonos Inalámbricos",
            "Cable Lightning",
            "Soporte para Carro",
            "Adaptador HDMI",
            "Mouse Inalámbrico",
            "Teclado Bluetooth"
        )

        recyclerView.adapter = ProductAdapter(catalogList)
        return view
    }
}