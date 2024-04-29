package com.project.studyhub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.project.studyhub.ui.theme.StudyHubTheme

class RegisterA : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyHubTheme {
                RegisterScreen()
            }
        }
    }

    private fun moveToLogin(){
        val intent = Intent(this, LoginA::class.java)
        startActivity(intent)
        finish()
    }
    private fun registerUser(context: Context, auth: FirebaseAuth, username: String, email: String, password: String, setLoading: (Boolean) -> Unit) {
        if (email.isBlank() || password.isBlank() || username.isBlank()) {
            setLoading(false)
            Toast.makeText(context, "Field should not be empty", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        setLoading(false)
                        val firebaseUser = auth.currentUser
                        val uid = firebaseUser?.uid

                        // Save user data to Realtime Database
                        if (uid != null) {
                            val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(uid)
                            val userData = hashMapOf(
                                "username" to username,
                                "email" to email,
                                "password" to password
                            )
                            databaseReference.setValue(userData)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        Toast.makeText(this,"Registration Successful",Toast.LENGTH_SHORT).show()
                                        readUserData(context,email,setLoading)
                                    } else {
                                        Toast.makeText(this,"Database Error",Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        setLoading(false)
                        Toast.makeText(this,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    @Preview
    @Composable
    private fun RegisterScreen() {
        val auth = FirebaseAuth.getInstance()
        val username = remember { mutableStateOf("") }
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val context = LocalContext.current
        val (isLoading, setLoading) = remember { mutableStateOf(false) }
        val gradientColor = listOf(Color(0xFFFFFFFF), Color(0xFF3291cb))
        val backgroundColor = Color(0xFF3291cb)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ){
            Column(  modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)) {

                Text(
                    text = "",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(colors = gradientColor),
                            shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.study),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                }
                Spacer(Modifier.height(15.dp))
                Text(
                    text = "Create new account",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(Modifier.height(30.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    value = username.value,
                    onValueChange = {
                        username.value = it
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = "person")
                    },
                    label = {
                        Text(text = "Username")
                    },
                    placeholder = {
                        Text(text = "Enter your name")
                    }
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "email")
                    },
                    label = {
                        Text(text = "Email")
                    },
                    placeholder = {
                        Text(text = "Enter your email")
                    }
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    value = password.value,
                    onValueChange = {
                        password.value = it
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "email")
                    },
                    label = {
                        Text(text = "Password")
                    },
                    placeholder = {
                        Text(text = "Enter your password")
                    }
                )
                Spacer(modifier = Modifier.height(25.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp),
                    onClick = {
                        setLoading(true)
                        registerUser(context ,auth, username.value, email.value, password.value,setLoading)
                    },
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp)
                ){

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(colors = gradientColor),
                                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Register",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Already have an account?",
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = { moveToLogin() },
                        colors = ButtonDefaults.buttonColors(backgroundColor)
                    ) {
                        Text(text = "Login")
                    }
                }
            }
//            if (isLoading) {
//
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//            }
            if (isLoading) {
                Card(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(5.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(100.dp)
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}