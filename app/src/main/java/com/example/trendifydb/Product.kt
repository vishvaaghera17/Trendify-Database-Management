package com.example.trendifydb

data class Product(
    val id: Int = 0,
    val name: String,
    val price: Double,
    val originalPrice: Double,
    val discount: String,
    val category: String,
    val image: Int
)