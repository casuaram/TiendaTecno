package com.tiendatecno.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.cloudinary.android.MediaManager
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    @SuppressLint("UseKtx")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val config = HashMap<String, String>()
            config["cloud_name"] = "aefisxhm" // Tu Cloud Name
            MediaManager.init(this, config)
        } catch (e: Exception) { }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        loadFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_catalog -> loadFragment(CatalogFragment())
                R.id.nav_cart -> loadFragment(CartFragment())
                R.id.nav_profile -> loadFragment(ProfileFragment())
            }
            true
        }

        solicitarUbicacionParaCompra()

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    private fun solicitarUbicacionParaCompra() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    mostrarPopUpUbicacion(lat, lon)
                } else {
                    mostrarPopUpUbicacion(4.60971, -74.08175) // Coordenadas de Bogotá por defecto
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }
    }

    private fun mostrarPopUpUbicacion(lat: Double, lon: Double) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("📍 Ubicación Detectada")
        builder.setMessage("Hemos detectado que te encuentras en:\n\nLatitud: $lat\nLongitud: $lon\n\n¿Deseas continuar con esta ubicación para tu entrega?")
        builder.setIcon(android.R.drawable.ic_menu_mylocation)

        builder.setPositiveButton("Sí, continuar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setNegativeButton("Cambiar") { dialog, _ ->
            dialog.dismiss()
            android.widget.Toast.makeText(this, "Función de mapa próximamente", android.widget.Toast.LENGTH_SHORT).show()
        }

        builder.setCancelable(false)
        builder.create().show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                solicitarUbicacionParaCompra()
            } else {
                android.widget.Toast.makeText(this, "Se necesita el permiso de ubicación para entregas", android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }
}