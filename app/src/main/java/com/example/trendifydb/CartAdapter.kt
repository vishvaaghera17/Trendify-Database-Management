package com.example.trendifydb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val context: Context,
    private var cartItems: List<CartItem>,
    private val onCartChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val db = TrendifyDatabaseHelper(context)

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCartProductName)
        val tvUnitPrice: TextView = itemView.findViewById(R.id.tvCartUnitPrice)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val tvLineTotal: TextView = itemView.findViewById(R.id.tvLineTotal)
        val btnMinus: Button = itemView.findViewById(R.id.btnMinus)
        val btnPlus: Button = itemView.findViewById(R.id.btnPlus)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
    }

    fun updateData(newItems: List<CartItem>) {
        cartItems = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]

        holder.tvName.text = item.name
        holder.tvUnitPrice.text = String.format("\u20B9%.2f each", item.price)
        holder.tvQuantity.text = item.quantity.toString()
        holder.tvLineTotal.text = String.format("\u20B9%.2f", item.price * item.quantity)

        holder.btnPlus.setOnClickListener {
            db.updateQuantity(item.id, item.quantity + 1)
            onCartChanged()
        }

        holder.btnMinus.setOnClickListener {
            if (item.quantity > 1) {
                db.updateQuantity(item.id, item.quantity - 1)
            } else {
                db.deleteCartItem(item.id)
            }
            onCartChanged()
        }

        holder.btnRemove.setOnClickListener {
            db.deleteCartItem(item.id)
            onCartChanged()
        }
    }
}