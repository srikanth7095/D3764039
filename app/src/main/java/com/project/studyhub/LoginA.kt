package com.project.studyhub

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.project.studyhub.admin.AdminHome
import com.project.studyhub.models.UserModel

class LoginA : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                LoginScreen()
            }
        }
        if (isUserLoggedIn(this)) {
            val sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
            var email = sharedPreferences.getString("email", "") ?: ""
            if (email == "admin@gmail.com"){
               moveToAdmin(this)
            }
            else{
                moveToMain(this)
            }

        }
    }
    private fun login(context: Context, auth: FirebaseAuth, email: String, password: String,setLoading: (Boolean) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            setLoading(false)
            Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        setLoading(false)
                        readUserData(context,email,setLoading)
                    } else {
                        setLoading(false)
                        Toast.makeText(context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    fun moveToAdmin(context: Context){
        val intent = Intent(context, AdminHome::class.java)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
    }

    fun moveToRegister(){
        val intent = Intent(this, RegisterA::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToLogin(context: Context) {
        val intent = Intent(context, LoginA::class.java)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
    }

    fun moveToMain(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
    }

    @Preview
    @Composable
    private fun LoginScreen() {
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val gradientColor = listOf(Color(0xFFFFFFFF), Color(0xFF3291cb))
        val context = LocalContext.current
        val (isLoading, setLoading) = remember { mutableStateOf(false) }
        val auth = Firebase.auth
        val backgroundColor = Color(0xFF2196F3)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ){
            Column(  modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)) {
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
                Spacer(Modifier.height(30.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    text = "Login!",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    text = "",
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
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
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "New user?",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { moveToRegister() },
                    text = "Register",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp),
                    onClick = {
                        setLoading(true)
                        login(context, auth, email.value, password.value,setLoading)
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
                            text = "Login",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }
            }
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
fun saveLoginState(context: Context, isLoggedIn: Boolean) {
    val sharedPreferences = context.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("isLoggedIn", isLoggedIn)
    editor.apply()
}
fun saveUser(context: Context, username: String, email: String,password: String){
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    var editor = sharedPreferences.edit()
    editor.putString("username", username)
    editor.putString("email", email)
    editor.putString("password", password)
    editor.commit()
}
fun isUserLoggedIn(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isLoggedIn", false)
}
fun clearLoginState(context: Context) {
    val sharedPreferences = context.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.remove("isLoggedIn")
    editor.clear()
    editor.commit()
    val loginA = LoginA()
    loginA.moveToLogin(context)
}

fun readUserData(context: Context,email: String, setLoading: (Boolean) -> Unit){
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")
    val query: Query = usersRef.orderByChild("email").equalTo(email)
    query.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            setLoading(false)
            if (snapshot.exists()) {
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    // Process each user object
                    if (user != null) {
                        Log.d("LoginA", "onDataChange: " + user.username)
                        // Access user properties
                        var username = user.username
                        var email = user.email
                        var password = user.password
                        saveUser(context, username ,email ,password)
                        saveLoginState(context, true)

                        val intent = if (email == "admin@gmail.com" && password == "admin@123") {
                            Intent(context, AdminHome::class.java)
                        } else {
                            Intent(context, MainActivity::class.java)
                        }

                        context.startActivity(intent)
                        if (context is Activity) {
                            context.finish()
                        }
                    }

                }
            }
            else {
                // No users found
            }
        }

        override fun onCancelled(error: DatabaseError) {
            setLoading(false)
            Log.e("error: ", "Failed to read user data", error.toException())
        }
    })
}
//fun setLoggedIn(loggedIn: Boolean) {
//    isLoggedIn = loggedIn
//    editor.putBoolean("isLoggedIn", isLoggedIn).commit()
//}