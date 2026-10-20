package com.example.trendifydb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartFragment : Fragment() {

    private lateinit var db: TrendifyDatabaseHelper
    private lateinit var rvCart: RecyclerView
    private lateinit var tvCartTotal: TextView
    private lateinit var tvCartEmpty: TextView
    private lateinit var btnCheckout: Button
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        db = TrendifyDatabaseHelper(requireContext())
        rvCart = view.findViewById(R.id.rvCart)
        tvCartTotal = view.findViewById(R.id.tvCartTotal)
        tvCartEmpty = view.findViewById(R.id.tvCartEmpty)
        btnCheckout = view.findViewById(R.id.btnCheckout)

        rvCart.layoutManager = LinearLayoutManager(requireContext())
        adapter = CartAdapter(requireContext(), db.getAllCartItems()) {
            refreshCart()
        }
        rvCart.adapter = adapter

        btnCheckout.setOnClickListener {
            if (db.getAllCartItems().isEmpty()) {
                Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                db.clearCart()
                Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show()
                refreshCart()
            }
        }

        refreshCart()
        return view
    }

    override fun onResume() {
        super.onResume()
        refreshCart()
    }

    private fun refreshCart() {
        val items = db.getAllCartItems()
        adapter.updateData(items)
        tvCartTotal.text = String.format("Total: \u20B9%.2f", db.getCartTotal())

        if (items.isEmpty()) {
            tvCartEmpty.visibility = View.VISIBLE
            rvCart.visibility = View.GONE
        } else {
            tvCartEmpty.visibility = View.GONE
            rvCart.visibility = View.VISIBLE
        }
    }
}