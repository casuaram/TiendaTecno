package com.tiendatecno.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(
    private val products: MutableList<Product>,
    private val isCartView: Boolean = false,
    private val onCartUpdated: () -> Unit = {}
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvProductName)
        val tvPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
        val btnAction: ImageButton = view.findViewById(R.id.btnAddCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvName.text = product.nombre
        holder.tvPrice.text = "$" + product.precio + " COP"

        if (product.imagenUrl.isNotEmpty() && product.imagenUrl.startsWith("http")) {

            com.bumptech.glide.Glide.with(holder.itemView.context)
                .load(product.imagenUrl)
                .into(holder.imgProduct)
        } else {
            holder.imgProduct.setImageResource(R.drawable.baseline_inventory_24)
        }

        if (isCartView) {
            holder.btnAction.setImageResource(android.R.drawable.ic_menu_delete)
            holder.btnAction.setOnClickListener {
                products.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, products.size)
                onCartUpdated()
                Toast.makeText(holder.itemView.context, "Producto eliminado", Toast.LENGTH_SHORT).show()
            }
        } else {
            holder.btnAction.setImageResource(R.drawable.round_shopping_cart_24)
            holder.btnAction.setOnClickListener {
                CartManager.cartItems.add(product)
                Toast.makeText(holder.itemView.context, "${product.nombre} agregado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = products.size
}