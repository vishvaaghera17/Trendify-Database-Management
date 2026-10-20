package com.example.trendifydb

data class CartItem(
    val id: Int = 0,
    val name: String,
    val price: Double,
    var quantity: Int,
    val imageRes: Int
)