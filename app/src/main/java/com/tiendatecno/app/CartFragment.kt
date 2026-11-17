package com.tiendatecno.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment() {

    private lateinit var tvTotal: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCart)
        tvTotal = view.findViewById(R.id.tvTotal)
        val btnCheckout = view.findViewById<MaterialButton>(R.id.btnCheckout)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ProductAdapter(
            products = CartManager.cartItems,
            isCartView = true,
            onCartUpdated = { calcularTotal() }
        )
        recyclerView.adapter = adapter

        calcularTotal()

        btnCheckout.setOnClickListener {
            if (CartManager.cartItems.isEmpty()) {
                Toast.makeText(requireContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val total = calcularTotal()

            val numeroOrden = (10000..99999).random().toString()

            val db = FirebaseFirestore.getInstance()
            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "Invitado"

            val orden = hashMapOf(
                "numeroOrden" to numeroOrden,
                "cliente" to userEmail,
                "total" to total,
                "fecha" to System.currentTimeMillis(),
                "productos" to CartManager.cartItems.map { it.nombre },
                "estado" to "Pendiente"
            )

            db.collection("ordenes").add(orden)
                .addOnSuccessListener {
                    mostrarPopUpExito(numeroOrden)
                    CartManager.cartItems.clear()
                    adapter.notifyDataSetChanged()
                    calcularTotal()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        return view
    }

    private fun calcularTotal(): Double {
        var total = 0.0
        for (product in CartManager.cartItems) {
            val priceString = product.precio.replace(".", "").replace(",", "")
            total += priceString.toDoubleOrNull() ?: 0.0
        }

        val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        tvTotal.text = "Total: ${format.format(total)}"

        return total
    }


    private fun mostrarPopUpExito(numeroOrden: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("¡Compra Exitosa!")
        builder.setMessage("Tu pedido ha sido procesado correctamente.\n\nNúmero de orden: #$numeroOrden\n\n¡Gracias por comprar en TiendaTecno!")
        builder.setIcon(android.R.drawable.checkbox_on_background)


        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }
}