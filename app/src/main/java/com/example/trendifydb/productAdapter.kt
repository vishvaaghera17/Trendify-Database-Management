package com.example.trendifydb

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val products: List<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProduct: ImageView = itemView.findViewById(R.id.ivProduct)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvOriginalPrice: TextView = itemView.findViewById(R.id.tvOriginalPrice)
        val tvDiscount: TextView = itemView.findViewById(R.id.tvDiscount)
        val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.ivProduct.setImageResource(product.image)
        holder.ivProduct.contentDescription = product.name
        holder.tvName.text = product.name
        holder.tvPrice.text = String.format("₹%.2f", product.price)
        holder.tvOriginalPrice.text = String.format("₹%.2f", product.originalPrice)
        holder.tvDiscount.text = product.discount
        holder.ivProduct.apply {
            setImageResource(product.image)
            scaleType = ImageView.ScaleType.CENTER_CROP
            adjustViewBounds = false
            setPadding(0, 0, 0, 0)
            contentDescription = product.name
        }
        holder.tvOriginalPrice.paintFlags =
            holder.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        holder.btnAddToCart.setOnClickListener { onAddToCart(product) }
    }

    override fun getItemCount(): Int = products.size
}