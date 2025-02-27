package com.example.shoppingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.*

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppingapp.ui.theme.ShoppingAppTheme

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.*

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration

//Returns true if the screen is in landscape mode
@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingAppTheme {

                //remember and mutableStateOf to track the selected product and update the UI accordingly
                var selectedProduct by remember { mutableStateOf<Product?>(null) }


//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    ProductList(
//                        products = products,
//                        onProductClick = { product  ->
//                            selectedProduct = product
//                        },
//                        modifier = Modifier.padding(innerPadding)
//                    )
//
//                    ProductDetails(product = selectedProduct)
//
//                }

                val landscape = isLandscape()

                val navController = rememberNavController()

                if (landscape) {
                    // Landscape mode
                    // Using Row layout to show both the product list and the product details side by side.
                    Row(modifier = Modifier.fillMaxSize()) {
                        ProductList(
                            products = products,
                            onProductClick = { product -> selectedProduct = product },
                            modifier = Modifier.weight(1f) // Assign half of screen space
                        )

                        ProductDetails(
                            product = selectedProduct,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                else {
                    // Portrait mode
                    // Using navigation to navigate from list screen to product details screen
                    NavHost(navController = navController, startDestination = "productList") {
                        composable("productList") {
                            ProductList(
                                products = products,
                                onProductClick = { product ->
                                    selectedProduct = product // Save selected product
                                    navController.navigate("productDetails")
                                },
                                modifier = Modifier.padding(top = 30.dp) // Add top padding
                            )
                        }

                        composable("productDetails") {
                            ProductDetailsScreen(
                                product = selectedProduct, // Retrieve from state

                                // go back to list when back button is clicked
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }

            }
        }
    }
}


data class Product(val name: String, val price: String, val description: String)

val products = listOf(
    Product("Product A", "$100", "This is a great product A."),
    Product("Product B", "$150", "This is product B with more features."),
    Product("Product C", "$200", "Premium product C."),
    Product("Product D", "$300", "Product D"),
    Product("Product E", "$400", "Product E")
)

@Composable
fun ProductList(products: List<Product>, onProductClick: (Product) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier
        .fillMaxSize()
        .background(Color(0xFFA8F7F5))
        .padding(start = 20.dp, end = 20.dp)
    ) {
        //mapping each product
        items(products) { product ->
            Text(
                text = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .clickable { onProductClick(product) }
            )
        }
    }
}

@Composable
fun ProductDetails(product: Product?, modifier: Modifier = Modifier) {
    //if none selected show a message //only landscape mode
    if (product == null) {
        Text(
            text = "Select a product to view details.",
            modifier = modifier.padding(16.dp).fillMaxSize().background(Color(0xFFFFFFF0))
        )
    } else {
        Text(
            text = "Name: ${product.name}\nPrice: ${product.price}\n\n${product.description}",
            modifier = modifier.padding(16.dp)
        )
    }
}

//Portrait mode only
@Composable
fun ProductDetailsScreen(product: Product?, onBackClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFFFFF0))
        .padding(top = 30.dp, start = 20.dp, end = 20.dp)
    ) {
        Button(onClick = onBackClick) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (product != null) {
            Text(text = "Name: ${product.name}\nPrice: ${product.price}\n\n${product.description}")
        }
        else {//not going to be used because when product is not selected it will show the other screen
            Text(text = "No product selected.")
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShoppingAppTheme {
        Greeting("Android")
    }
}