package com.example.servidor

import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.servidor.ui.theme.ServidorTheme
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import androidx.compose.material.Text as Text1

class Usuario(i: Int, s: String, s1: String) {

}

// Interface for your API service (replace with your actual endpoint)
interface ApiService {
    @GET("/users")
    suspend fun getUsers(): List<User> // Replace User with your data model class
    val usuarios
        get() = listOf(
            Usuario(1, "Juan Pérez", "juan@example.com"),
            Usuario(2, "María López", "maria@example.com")
        )

}

class MainActivity : ComponentActivity() {

    // Create a lazy instance of ApiService using Retrofit
    private val apiService: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://tinyurl.com/") // Replace with your server URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServidorTheme {

            }
        }

        // Call fetchData here to load data on app launch
        fetchData()
    }

     private fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user: List<User>

                // Update UI with the users data (implementation needed)
            } catch (e: Exception) {
                // Handle errors (show error message, etc.)
            }
        }
    }
}

@Composable
fun GreetingScreen(isLoading: Boolean = false, users: List<User> = emptyList()) {
    Surface(){
        if (isLoading) {
            // Show loading indicator while data is being fetched
            CircularProgressIndicator(modifier = Modifier.fillMaxSize(0.2F))
        /*(modifier = Modifier.align(Alignment.Center))*/
        } else {
            // Display user list if data is available
            if (users.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize(0.2F)) {
                    items { user ->
                        var user = "Hello, ${user}!"
                    }
                }
            } else {
                // Show a message if there are no users
                Text1(text = "No users found.", modifier = Modifier)
            }
        }
    }
}

fun items(itemContent: LazyItemScope.(index: Int) -> Unit) {

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ServidorTheme {
        GreetingScreen(isLoading = true) // Simulate loading state
    }
}