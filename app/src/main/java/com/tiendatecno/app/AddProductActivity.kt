package com.tiendatecno.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class AddProductActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private lateinit var ivProductImage: ImageView
    private lateinit var db: FirebaseFirestore

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data
            ivProductImage.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        db = FirebaseFirestore.getInstance()

        val etName = findViewById<TextInputEditText>(R.id.etProdName)
        val etPrice = findViewById<TextInputEditText>(R.id.etProdPrice)
        ivProductImage = findViewById(R.id.ivProductImage)
        val btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
        val btnSaveProduct = findViewById<Button>(R.id.btnSaveProduct)

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getContent.launch(intent)
        }

        btnSaveProduct.setOnClickListener {
            val name = etName.text.toString().trim()
            val price = etPrice.text.toString().trim()

            if (name.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "Por favor, complete el nombre y el precio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Guardando producto...", Toast.LENGTH_SHORT).show()

            val imageUrl = "default_image"

            val productMap = hashMapOf(
                "nombre" to name,
                "precio" to price,
                "imagenUrl" to imageUrl
            )

            db.collection("productos").add(productMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "¡Producto guardado exitosamente!", Toast.LENGTH_SHORT).show()
                    finish() // Cierra y vuelve al panel
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}