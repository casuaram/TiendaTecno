package com.tiendatecno.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class CatalogFragment : Fragment() {

    private val productList = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)
        db = FirebaseFirestore.getInstance()

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCatalog)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter

        db.collection("productos").addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            if (snapshot != null) {
                productList.clear()
                for (doc in snapshot.documents) {
                    val product = doc.toObject(Product::class.java)
                    if (product != null) {
                        product.id = doc.id
                        productList.add(product)
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }

        return view
    }
}