package com.dilip.qrventory.presentation.login

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dilip.qrventory.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

@Composable
fun LoginPage(onLogin: (String) -> Unit, onSignUp: () -> Unit) {
    val context = LocalContext.current
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var phoneNumber by remember { mutableStateOf("+91") }  // Default phone number with +91
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var isOTPSent by remember { mutableStateOf(false) }

    // State to control the visibility of the Column
    var isVisible by remember { mutableStateOf(false) }

    // Trigger the visibility of the login column after a delay (for smoother transition)
    LaunchedEffect(Unit) {
        isVisible = true
    }

    fun signInWithCredential(
        credential: PhoneAuthCredential,
        auth: FirebaseAuth,
        context: Context,
        onLogin: (String) -> Unit
    ) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                onLogin(phoneNumber) // Pass phone number to the main activity
            } else {
                Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val callbacks = remember {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithCredential(credential, auth, context, onLogin)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(context, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                verificationId = id
                isOTPSent = true
                Toast.makeText(context, "OTP sent to $phoneNumber", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun sendOTP(phone: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(context as Activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
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

        // AnimatedVisibility to make the column slide in from bottom to top
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide in from bottom
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
                    text = "Login",
                    fontSize = 32.sp,
                    color = Color(0xFF1976D2),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Number text field with +91 prefix
                TextField(
                    value = phoneNumber,
                    onValueChange = {
                        if (it.startsWith("+91")) {
                            phoneNumber = it
                        }
                    },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color(0xFF1976D2),
                        unfocusedLabelColor = Color.Gray
                    ),
                    shape = MaterialTheme.shapes.large,
                    placeholder = { Text("Enter your phone number") },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                // OTP text field
                if (isOTPSent) {
                    TextField(
                        value = otp,
                        onValueChange = { otp = it },
                        label = { Text("OTP") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedLabelColor = Color(0xFF1976D2),
                            unfocusedLabelColor = Color.Gray
                        ),
                        shape = MaterialTheme.shapes.large,
                        placeholder = { Text("Enter the OTP") },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (verificationId != null) {
                                val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
                                signInWithCredential(credential, auth, context, onLogin)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2), contentColor = Color.White),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(text = "Verify OTP", fontSize = 18.sp)
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (phoneNumber.isNotEmpty()) {
                                sendOTP(phoneNumber)
                            } else {
                                Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2), contentColor = Color.White),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(text = "Send OTP", fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Don't have an account? Sign Up",
                    color = Color(0xFF505D72),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.clickable(onClick = {
                        // Animate out and navigate to RegistrationPage
                        isVisible = false
                        onSignUp()
                    }).padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    LoginPage(onLogin = { phoneNumber ->
        println("Login successful for phone: $phoneNumber")
    }, onSignUp = {
        println("Sign Up clicked")
    })
}
