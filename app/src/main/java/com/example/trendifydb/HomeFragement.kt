package com.example.trendifydb

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment(R.layout.fragment_home) {

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = TrendifyDatabaseHelper(requireContext())
        val selectedCategory = arguments?.getString(ARG_CATEGORY)
        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcome)

        val productsToShow: List<Product>
        if (selectedCategory != null) {
            productsToShow = db.getProductsByCategory(selectedCategory)
            tvWelcome.text = selectedCategory
        } else {
            val userName = activity?.intent?.getStringExtra("USER_NAME")
            tvWelcome.text = if (!userName.isNullOrBlank()) "Welcome, $userName!" else "Welcome!"
            productsToShow = db.getAllProducts()
        }

        val rv = view.findViewById<RecyclerView>(R.id.rvProduct)
        rv.layoutManager = GridLayoutManager(requireContext(), 2)
        rv.adapter = ProductAdapter(productsToShow) { product ->
            db.addToCart(
                CartItem(
                    name = product.name,
                    price = product.price,
                    quantity = 1,
                    imageRes = product.image
                )
            )
            Toast.makeText(requireContext(), "${product.name} added to cart", Toast.LENGTH_SHORT).show()
        }
    }
}