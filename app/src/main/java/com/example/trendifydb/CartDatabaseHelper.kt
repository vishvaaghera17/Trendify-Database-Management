package com.example.trendifydb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CartDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "trendify_cart.db", null, 1) {

    companion object {
        private const val TABLE_CART = "cart_items"
        private const val COL_ID = "id"
        private const val COL_NAME = "name"
        private const val COL_PRICE = "price"
        private const val COL_QUANTITY = "quantity"
        private const val COL_IMAGE = "image_res"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_CART (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT,
                $COL_PRICE REAL,
                $COL_QUANTITY INTEGER,
                $COL_IMAGE INTEGER
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    fun addToCart(item: CartItem) {
        val db = writableDatabase
        val existing = getCartItemByName(item.name)

        if (existing != null) {
            updateQuantity(existing.id, existing.quantity + item.quantity)
        } else {
            val values = ContentValues().apply {
                put(COL_NAME, item.name)
                put(COL_PRICE, item.price)
                put(COL_QUANTITY, item.quantity)
                put(COL_IMAGE, item.imageRes)
            }
            db.insert(TABLE_CART, null, values)
        }
        db.close()
    }

    fun getAllCartItems(): List<CartItem> {
        val list = mutableListOf<CartItem>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CART, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                list.add(
                    CartItem(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE)),
                        quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY)),
                        imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE))
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
        val cursor = db.query(TABLE_CART, null, "$COL_NAME = ?", arrayOf(name), null, null, null)
        var item: CartItem? = null
        if (cursor.moveToFirst()) {
            item = CartItem(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE)),
                quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY)),
                imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE))
            )
        }
        cursor.close()
        db.close()
        return item
    }

    fun updateQuantity(cartItemId: Int, newQuantity: Int) {
        val db = writableDatabase
        val values = ContentValues().apply { put(COL_QUANTITY, newQuantity) }
        db.update(TABLE_CART, values, "$COL_ID = ?", arrayOf(cartItemId.toString()))
        db.close()
    }

    fun deleteCartItem(cartItemId: Int) {
        val db = writableDatabase
        db.delete(TABLE_CART, "$COL_ID = ?", arrayOf(cartItemId.toString()))
        db.close()
    }

    fun clearCart() {
        val db = writableDatabase
        db.delete(TABLE_CART, null, null)
        db.close()
    }

    fun getCartTotal(): Double = getAllCartItems().sumOf { it.price * it.quantity }
}