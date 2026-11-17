package com.tiendatecno.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnBiometric = findViewById<Button>(R.id.btnBiometric)
        val btnGoToRegister = findViewById<Button>(R.id.btnGoToRegister)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)

        btnGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Ingrese email y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            verificarRolYRedirigir(userId)
                        }
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        btnBiometric.setOnClickListener {
            Toast.makeText(this, "Use el login con contraseña para probar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verificarRolYRedirigir(userId: String) {
        db.collection("usuarios").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val rol = document.getString("rol")

                    if (rol == "Administrador" || rol == "Vendedor") {

                        startActivity(Intent(this, AdminActivity::class.java))
                    } else {

                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    finish()
                } else {
                    Toast.makeText(this, "No se encontró el perfil del usuario", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al obtener rol: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}