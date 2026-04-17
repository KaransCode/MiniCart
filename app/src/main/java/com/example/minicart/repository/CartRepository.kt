package com.example.minicart.repository

import com.example.minicart.data.CartDao
import com.example.minicart.data.CartItem
import com.example.minicart.data.Product
import com.example.minicart.data.ProductDao
import com.example.minicart.data.UserProfile
import com.example.minicart.data.UserProfileDao
import kotlinx.coroutines.flow.Flow

class CartRepository(
    private val productDao: ProductDao, 
    private val cartDao: CartDao,
    private val userProfileDao: UserProfileDao
) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()
    val allCartItems: Flow<List<CartItem>> = cartDao.getAllCartItems()
    val userProfile: Flow<UserProfile?> = userProfileDao.getUserProfile()

    suspend fun insertOrUpdateProfile(profile: UserProfile) {
        userProfileDao.insertOrUpdateProfile(profile)
    }

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun getProductCount(): Int {
        return productDao.getProductCount()
    }

    suspend fun insertCartItem(cartItem: CartItem) {
        cartDao.insertCartItem(cartItem)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    suspend fun removeCartItem(itemId: Int) {
        cartDao.removeCartItem(itemId)
    }
}
