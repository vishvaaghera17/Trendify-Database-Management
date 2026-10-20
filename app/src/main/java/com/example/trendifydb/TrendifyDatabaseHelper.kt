package com.example.trendifydb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TrendifyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "trendify.db"
        private const val DB_VERSION = 2 // bumped: users table gained address/contact/dob

        private const val TABLE_USERS = "users"
        private const val COL_U_ID = "id"
        private const val COL_U_NAME = "name"
        private const val COL_U_EMAIL = "email"
        private const val COL_U_PASSWORD = "password"
        private const val COL_U_ADDRESS = "address"
        private const val COL_U_CONTACT = "contact"
        private const val COL_U_DOB = "dob"

        private const val TABLE_PRODUCTS = "products"
        private const val COL_P_ID = "id"
        private const val COL_P_NAME = "name"
        private const val COL_P_PRICE = "price"
        private const val COL_P_ORIGINAL_PRICE = "original_price"
        private const val COL_P_DISCOUNT = "discount"
        private const val COL_P_CATEGORY = "category"
        private const val COL_P_IMAGE = "image_res"

        private const val TABLE_CART = "cart_items"
        private const val COL_C_ID = "id"
        private const val COL_C_NAME = "name"
        private const val COL_C_PRICE = "price"
        private const val COL_C_QUANTITY = "quantity"
        private const val COL_C_IMAGE = "image_res"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_USERS (
                $COL_U_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_U_NAME TEXT,
                $COL_U_EMAIL TEXT UNIQUE,
                $COL_U_PASSWORD TEXT,
                $COL_U_ADDRESS TEXT,
                $COL_U_CONTACT TEXT,
                $COL_U_DOB TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_PRODUCTS (
                $COL_P_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_P_NAME TEXT,
                $COL_P_PRICE REAL,
                $COL_P_ORIGINAL_PRICE REAL,
                $COL_P_DISCOUNT TEXT,
                $COL_P_CATEGORY TEXT,
                $COL_P_IMAGE INTEGER
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_CART (
                $COL_C_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_C_NAME TEXT,
                $COL_C_PRICE REAL,
                $COL_C_QUANTITY INTEGER,
                $COL_C_IMAGE INTEGER
            )
        """.trimIndent())

        seedProducts(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    private fun seedProducts(db: SQLiteDatabase) {
        val products = listOf(
            Product(name = "Classic Hoodie", price = 1299.0, originalPrice = 1599.0, discount = "-20%", category = "T-Shirts", image = R.drawable.classic_hoodie),
            Product(name = "Denim Jacket", price = 1999.0, originalPrice = 2499.0, discount = "-20%", category = "Jeans", image = R.drawable.demin_jacket),
            Product(name = "Summer Dress", price = 1499.0, originalPrice = 1999.0, discount = "-25%", category = "Dresses", image = R.drawable.summer_dress),
            Product(name = "Formal Blazer", price = 2599.0, originalPrice = 3299.0, discount = "-20%", category = "Formal Wear", image = R.drawable.formal_blazer),
            Product(name = "Printed Kurta", price = 899.0, originalPrice = 1199.0, discount = "-22%", category = "Kurtas", image = R.drawable.printed_kurta),
            Product(name = "Silk Saree", price = 2299.0, originalPrice = 2899.0, discount = "-20%", category = "Sarees", image = R.drawable.silk_saree),
            Product(name = "Slim Fit Jeans", price = 1699.0, originalPrice = 2199.0, discount = "-23%", category = "Jeans", image = R.drawable.slim_fit_jeans),
            Product(name = "Graphic Tee", price = 599.0, originalPrice = 799.0, discount = "-25%", category = "T-Shirts", image = R.drawable.graphic_tee)
        )
        for (p in products) {
            val values = ContentValues().apply {
                put(COL_P_NAME, p.name)
                put(COL_P_PRICE, p.price)
                put(COL_P_ORIGINAL_PRICE, p.originalPrice)
                put(COL_P_DISCOUNT, p.discount)
                put(COL_P_CATEGORY, p.category)
                put(COL_P_IMAGE, p.image)
            }
            db.insert(TABLE_PRODUCTS, null, values)
        }
    }

    // ---------------- USERS ----------------

    fun registerUser(name: String, email: String, password: String): Boolean {
        if (getUserByEmail(email) != null) return false
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_U_NAME, name)
            put(COL_U_EMAIL, email)
            put(COL_U_PASSWORD, password)
            put(COL_U_ADDRESS, "")
            put(COL_U_CONTACT, "")
            put(COL_U_DOB, "")
        }
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    fun loginUser(email: String, password: String): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS, null,
            "$COL_U_EMAIL = ? AND $COL_U_PASSWORD = ?",
            arrayOf(email, password), null, null, null
        )
        val user = cursorToUser(cursor)
        cursor.close()
        db.close()
        return user
    }

    fun getUserByEmail(email: String): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS, null,
            "$COL_U_EMAIL = ?", arrayOf(email),
            null, null, null
        )
        val user = cursorToUser(cursor)
        cursor.close()
        db.close()
        return user
    }

    // Called "Account details" — updates the profile fields for an existing user
    fun updateProfile(email: String, name: String, address: String, contact: String, dob: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_U_NAME, name)
            put(COL_U_ADDRESS, address)
            put(COL_U_CONTACT, contact)
            put(COL_U_DOB, dob)
        }
        val rows = db.update(TABLE_USERS, values, "$COL_U_EMAIL = ?", arrayOf(email))
        db.close()
        return rows > 0
    }

    private fun cursorToUser(cursor: android.database.Cursor): User? {
        if (!cursor.moveToFirst()) return null
        return User(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_U_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_NAME)),
            email = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_EMAIL)),
            password = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_PASSWORD)),
            address = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_ADDRESS)) ?: "",
            contact = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_CONTACT)) ?: "",
            dob = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_DOB)) ?: ""
        )
    }

    // ---------------- PRODUCTS ----------------

    fun getAllProducts(): List<Product> {
        val list = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Product(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_P_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_NAME)),
                        price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_P_PRICE)),
                        originalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_P_ORIGINAL_PRICE)),
                        discount = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_DISCOUNT)),
                        category = cursor.getString(cursor.getColumnIndexOrThrow(COL_P_CATEGORY)),
                        image = cursor.getInt(cursor.getColumnIndexOrThrow(COL_P_IMAGE))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun getProductsByCategory(category: String): List<Product> {
        return getAllProducts().filter { it.category == category }
    }

    // ---------------- CART ----------------

    fun addToCart(item: CartItem) {
        val existing = getCartItemByName(item.name)
        val db = writableDatabase
        if (existing != null) {
            db.close()
            updateQuantity(existing.id, existing.quantity + item.quantity)
        } else {
            val values = ContentValues().apply {
                put(COL_C_NAME, item.name)
                put(COL_C_PRICE, item.price)
                put(COL_C_QUANTITY, item.quantity)
                put(COL_C_IMAGE, item.imageRes)
            }
            db.insert(TABLE_CART, null, values)
            db.close()
        }
    }

    fun getAllCartItems(): List<CartItem> {
        val list = mutableListOf<CartItem>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CART, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    CartItem(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_C_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COL_C_NAME)),
                        price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_C_PRICE)),
                        quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_C_QUANTITY)),
                        imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(COL_C_IMAGE))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    private fun getCartItemByName(name: String): CartItem? {
        val db = readableDatabase
        val cursor = db.query(TABLE_CART, null, "$COL_C_NAME = ?", arrayOf(name), null, null, null)
        var item: CartItem? = null
        if (cursor.moveToFirst()) {
            item = CartItem(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_C_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_C_NAME)),
                price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_C_PRICE)),
                quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_C_QUANTITY)),
                imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(COL_C_IMAGE))
            )
        }
        cursor.close()
        db.close()
        return item
    }

    fun updateQuantity(cartItemId: Int, newQuantity: Int) {
        val db = writableDatabase
        val values = ContentValues().apply { put(COL_C_QUANTITY, newQuantity) }
        db.update(TABLE_CART, values, "$COL_C_ID = ?", arrayOf(cartItemId.toString()))
        db.close()
    }

    fun deleteCartItem(cartItemId: Int) {
        val db = writableDatabase
        db.delete(TABLE_CART, "$COL_C_ID = ?", arrayOf(cartItemId.toString()))
        db.close()
    }

    fun clearCart() {
        val db = writableDatabase
        db.delete(TABLE_CART, null, null)
        db.close()
    }

    fun getCartTotal(): Double = getAllCartItems().sumOf { it.price * it.quantity }
}