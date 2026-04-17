package com.example.minicart.ui.theme

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.minicart.R
import com.example.minicart.data.CartItem
import com.example.minicart.data.Product
import com.example.minicart.data.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: com.example.minicart.viewmodel.CartViewModel) {
    val context = LocalContext.current
    val products by viewModel.products.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val categories = listOf("Electronics", "Accessories", "Others")
    
    var showMenu by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showCartScreen by remember { mutableStateOf(false) }
    var showProfileScreen by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    if (showCartScreen) {
        CartScreen(
            cartItems = cartItems,
            onRemoveItem = { viewModel.removeFromCart(it.id) },
            onBack = { showCartScreen = false },
            onCheckout = {
                viewModel.checkout()
                showCartScreen = false
                Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_LONG).show()
            }
        )
        return
    }

    if (showProfileScreen) {
        ProfileScreen(
            userProfile = userProfile,
            onSave = { 
                viewModel.updateProfile(it)
                showProfileScreen = false
                Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
            },
            onBack = { showProfileScreen = false }
        )
        return
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About MiniCart") },
            text = { Text("MiniCart is a demo shopping application built with Jetpack Compose and Room Database.\n\nVersion: 1.0.0") },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) { Text("OK") }
            }
        )
    }

    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Settings") },
            text = { Text("Settings are coming soon! You will be able to manage your profile and preferences here.") },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) { Text("OK") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    if (isSearching) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search products...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                                unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent
                            )
                        )
                    } else {
                        Text("MiniCart Store") 
                    }
                },
                navigationIcon = {
                    if (isSearching) {
                        IconButton(onClick = { 
                            isSearching = false
                            searchQuery = ""
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (!isSearching) {
                        IconButton(onClick = { isSearching = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                    IconButton(onClick = { showCartScreen = true }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Profile") },
                            onClick = { 
                                showMenu = false
                                showProfileScreen = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = { 
                                showMenu = false
                                showSettingsDialog = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("About") },
                            onClick = { 
                                showMenu = false
                                showAboutDialog = true
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                categories.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            val filteredProducts = products.filter { 
                it.category == categories[selectedTabIndex] && 
                (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true))
            }

            MainContent(
                products = filteredProducts,
                cartItems = cartItems,
                onAddToCart = { product -> 
                    viewModel.addToCart(product.id, product.name, product.price)
                    Toast.makeText(context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
                },
                onCheckout = { 
                    if (cartItems.isNotEmpty()) {
                        viewModel.checkout() 
                        Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Cart is empty!", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

@Composable
fun MainContent(
    products: List<Product>,
    cartItems: List<CartItem>,
    onAddToCart: (Product) -> Unit,
    onCheckout: () -> Unit
) {
    val totalItems = cartItems.size
    val totalPrice = cartItems.sumOf { it.price }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(products) { product ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Product Image
                        Surface(
                            modifier = Modifier.size(64.dp),
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            val resId = if (product.imageResName != null) {
                                LocalContext.current.resources.getIdentifier(
                                    product.imageResName, "drawable", LocalContext.current.packageName
                                )
                            } else 0

                            if (resId != 0) {
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    modifier = Modifier.padding(16.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(product.name, style = MaterialTheme.typography.titleMedium)
                            Text("₹${product.price}", style = MaterialTheme.typography.bodyLarge)
                        }
                        
                        Button(onClick = { onAddToCart(product) }) {
                            Text("Add")
                        }
                    }
                }
            }
        }

        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))
        Text("Cart Items: $totalItems")
        Text("Total: ₹$totalPrice")

        Button(
            onClick = onCheckout,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Checkout")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    onRemoveItem: (CartItem) -> Unit,
    onBack: () -> Unit,
    onCheckout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize().padding(16.dp)) {
            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Your cart is empty", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { item ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                                    Text("₹${item.price}", style = MaterialTheme.typography.bodyMedium)
                                }
                                IconButton(onClick = { onRemoveItem(item) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Price:", style = MaterialTheme.typography.titleLarge)
                    Text("₹${cartItems.sumOf { it.price }}", style = MaterialTheme.typography.titleLarge)
                }

                Button(
                    onClick = onCheckout,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Text("Checkout")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val sampleProducts = listOf(
        Product(1, "Smartphone", 49999.0, "Electronics", "mobile"),
        Product(2, "Phone Case", 299.0, "Accessories", "phone_case")
    )
    val sampleCart = listOf(
        CartItem(1, 1, "Smartphone", 49999.0)
    )
    MaterialTheme {
        Surface {
            MainContent(
                products = sampleProducts,
                cartItems = sampleCart,
                onAddToCart = {},
                onCheckout = {}
            )
        }
    }
}
