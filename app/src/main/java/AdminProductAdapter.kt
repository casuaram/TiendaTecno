package com.tiendatecno.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminProductAdapter(
    private val productList: List<Product>,
    private val onDeleteClick: (Product) -> Unit
) : RecyclerView.Adapter<AdminProductAdapter.AdminViewHolder>() {

    class AdminViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvProductName)
        val tvPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val btnAdd: ImageButton = view.findViewById(R.id.btnAddCart) // Lo reusamos como botón de borrar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return AdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val product = productList[position]
        holder.tvName.text = product.nombre
        holder.tvPrice.text = "$" + product.precio + " COP"

        holder.btnAdd.setImageResource(android.R.drawable.ic_delete)

        holder.btnAdd.setOnClickListener {
            onDeleteClick(product)
        }
    }

    override fun getItemCount() = productList.size
}