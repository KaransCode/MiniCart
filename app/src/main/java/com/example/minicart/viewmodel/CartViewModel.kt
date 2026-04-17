package com.example.minicart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.minicart.data.CartDatabase
import com.example.minicart.data.CartItem
import com.example.minicart.data.Product
import com.example.minicart.data.UserProfile
import com.example.minicart.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItem>> = repository.allCartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        // Pre-populate with some data only if the database is empty
        viewModelScope.launch {
            val count = withContext(Dispatchers.IO) {
                repository.getProductCount()
            }
            if (count == 0) {
                withContext(Dispatchers.IO) {
                    // Electronics
                    repository.insertProduct(Product(name = "Smartphone", price = 49999.0, category = "Electronics", imageResName = "mobile"))
                    repository.insertProduct(Product(name = "Laptop Pro", price = 125000.0, category = "Electronics", imageResName = "laptop"))
                    repository.insertProduct(Product(name = "Wireless Buds", price = 2999.0, category = "Electronics", imageResName = "buds"))
                    repository.insertProduct(Product(name = "Tablet Air", price = 44999.0, category = "Electronics", imageResName = "tablet"))
                    repository.insertProduct(Product(name = "Smart Watch Ultra", price = 24999.0, category = "Electronics", imageResName = "smartwatch"))
                    repository.insertProduct(Product(name = "Gaming Console", price = 49999.0, category = "Electronics", imageResName = "console"))
                    
                    // Accessories
                    repository.insertProduct(Product(name = "Classic Watch", price = 1499.0, category = "Accessories", imageResName = "watch"))
                    repository.insertProduct(Product(name = "Silicon Case", price = 499.0, category = "Accessories", imageResName = "phone_case"))
                    repository.insertProduct(Product(name = "USB-C Braided Cable", price = 299.0, category = "Accessories", imageResName = "usb_c"))
                    repository.insertProduct(Product(name = "Power Bank 20k", price = 1999.0, category = "Accessories", imageResName = "powerbank"))
                    repository.insertProduct(Product(name = "Bluetooth Mouse", price = 899.0, category = "Accessories", imageResName = "mouse"))
                    repository.insertProduct(Product(name = "Laptop Sleeve", price = 1200.0, category = "Accessories", imageResName = "sleeve"))
                    
                    // Others
                    repository.insertProduct(Product(name = "Travel Backpack", price = 2500.0, category = "Others", imageResName = "backpack"))
                    repository.insertProduct(Product(name = "Hardbound Notebook", price = 450.0, category = "Others", imageResName = "notebook"))
                    repository.insertProduct(Product(name = "LED Desk Lamp", price = 1500.0, category = "Others", imageResName = "desk_lamp"))
                    repository.insertProduct(Product(name = "Metal Water Bottle", price = 799.0, category = "Others", imageResName = "bottle"))
                    repository.insertProduct(Product(name = "Office Chair Mat", price = 1200.0, category = "Others", imageResName = "mat"))
                    repository.insertProduct(Product(name = "Desk Organizer", price = 600.0, category = "Others", imageResName = "desk"))
                }
            }
        }
    }

    fun addToCart(productId: Int, name: String, price: Double) {
        viewModelScope.launch {
            repository.insertCartItem(CartItem(productId = productId, name = name, price = price))
        }
    }

    fun checkout() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun removeFromCart(itemId: Int) {
        viewModelScope.launch {
            repository.removeCartItem(itemId)
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.insertOrUpdateProfile(profile)
        }
    }
}

class CartViewModelFactory(private val database: CartDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            val repository = CartRepository(
                database.productDao(), 
                database.cartDao(),
                database.userProfileDao()
            )
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
