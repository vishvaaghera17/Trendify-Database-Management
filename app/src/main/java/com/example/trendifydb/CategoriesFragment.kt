package com.example.trendifydb

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = listOf(
            Category("Formal Wear"),
            Category("Jeans"),
            Category("T-Shirts"),
            Category("Kurtas"),
            Category("Dresses"),
            Category("Sarees")
        )

        val rv = view.findViewById<RecyclerView>(R.id.rvCategories)
        rv.layoutManager = GridLayoutManager(requireContext(), 2)
        rv.adapter = CategoryAdapter(categories) { category ->
            // Open Home fragment filtered to just this category's products
            val filteredHome = HomeFragment.newInstance(category.name)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, filteredHome)
                .addToBackStack(null)
                .commit()
        }
    }
}