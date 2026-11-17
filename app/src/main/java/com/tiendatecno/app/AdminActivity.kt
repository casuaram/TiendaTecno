package com.tiendatecno.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: AdminProductAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        db = FirebaseFirestore.getInstance()

        val rvProducts = findViewById<RecyclerView>(R.id.rvAdminProducts)
        val btnAdd = findViewById<FloatingActionButton>(R.id.btnAddProduct)

        rvProducts.layoutManager = LinearLayoutManager(this)
        adapter = AdminProductAdapter(productList) { product ->

            db.collection("productos").document(product.id).delete()
                .addOnSuccessListener { Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show() }
        }
        rvProducts.adapter = adapter

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

        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        val btnLogoutAdmin = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogoutAdmin)
        btnLogoutAdmin.setOnClickListener {
            com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
            startActivity(android.content.Intent(this, LoginActivity::class.java))
            finish()
        }
        obtenerUbicacion()

    }

    private fun obtenerUbicacion() {
        val fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this)

        if (androidx.core.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    android.widget.Toast.makeText(this, "Ubicación capturada:\nLat: $lat\nLon: $lon", android.widget.Toast.LENGTH_LONG).show()

                } else {
                    android.widget.Toast.makeText(this, "No se pudo obtener la ubicación. Activa el GPS del emulador.", android.widget.Toast.LENGTH_LONG).show()
                }
            }
        } else {

            androidx.core.app.ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }
    }

}