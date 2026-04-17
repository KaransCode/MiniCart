package com.example.minicart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.minicart.data.CartDatabase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.minicart.ui.theme.LoginScreen
import com.example.minicart.ui.theme.MainScreen
import com.example.minicart.viewmodel.CartViewModel
import com.example.minicart.viewmodel.CartViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: CartViewModel by viewModels {
        CartViewModelFactory(CartDatabase.getDatabase(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    var isLoggedIn by remember { mutableStateOf(false) }

                    if (isLoggedIn) {
                        MainScreen(viewModel)
                    } else {
                        LoginScreen(onLoginSuccess = { isLoggedIn = true })
                    }
                }
            }
        }
    }
}