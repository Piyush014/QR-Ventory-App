package com.dilip.qrventory.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.dilip.qrventory.R
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

// Data class to represent the user
data class User(
    val phoneNumber: String = "",
    val name: String = "",
    val email: String = ""
)

@Composable
fun RegistrationPage(onRegister: (String) -> Unit, onLogin: () -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    // State to control the visibility of the registration column
    var isVisible by remember { mutableStateOf(false) }

    // Trigger the visibility of the registration column after a delay (for smoother transition)
    LaunchedEffect(Unit) {
        isVisible = true
    }

    fun saveUserToDatabase(user: User) {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users").child(user.phoneNumber)

        usersRef.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                onRegister(user.phoneNumber) // Pass phone number to the main activity
            } else {
                Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // AnimatedVisibility to make the column slide in from right
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(
                initialOffsetX = { it }, // Slide in from right
                animationSpec = tween(durationMillis = 1000) // Animation duration
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it }, // Slide out to right
                animationSpec = tween(durationMillis = 1000) // Animation duration
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White.copy(alpha = 0.85f), MaterialTheme.shapes.large)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mahi),
                    contentDescription = "Logo",
                    modifier = Modifier.size(200.dp).padding(bottom = 16.dp)
                )

                Text(
                    text = "Registration",
                    fontSize = 32.sp,
                    color = Color(0xFF1976D2),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color(0xFF1976D2),
                        unfocusedLabelColor = Color.Gray
                    ),
                    shape = MaterialTheme.shapes.large,
                    placeholder = { Text("Enter your name") }
                )

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color(0xFF1976D2),
                        unfocusedLabelColor = Color.Gray
                    ),
                    shape = MaterialTheme.shapes.large,
                    placeholder = { Text("Enter your email") }
                )

                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color(0xFF1976D2),
                        unfocusedLabelColor = Color.Gray
                    ),
                    shape = MaterialTheme.shapes.large,
                    placeholder = { Text("Enter your phone number") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isNotEmpty() && email.isNotEmpty() && phoneNumber.isNotEmpty()) {
                            saveUserToDatabase(User(phoneNumber, name, email))
                        } else {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2), contentColor = Color.White),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(text = "Register", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Already have an account? Log In",
                    color = Color(0xFF505D72),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.clickable(onClick = {
                        // Animate out and navigate to LoginPage
                        isVisible = false
                        onLogin()
                    }).padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationPagePreview() {
    RegistrationPage(onRegister = { phoneNumber ->
        println("Registration successful for phone: $phoneNumber")
    }, onLogin = {
        println("Log In clicked")
    })
}
