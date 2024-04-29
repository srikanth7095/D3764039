package com.project.studyhub

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Preview
@Composable
fun Profile(){
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)


    var username = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }

    // Read user data from SharedPreferences
    username.value = sharedPreferences.getString("username", "") ?: ""
    email.value = sharedPreferences.getString("email", "") ?: ""
    Box(modifier = Modifier.fillMaxSize()){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Image on the left corner
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.CenterVertically)
                    ) {

                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "My Profile",
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.CenterVertically)
                    ) {

                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White, // Card background color
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp)) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Image(
                            //painter = rememberAsyncImagePainter(room.imageUrl),
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "Room Image",
                            modifier = Modifier
                                .height(50.dp)
                                .width(50.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Column(modifier = Modifier.padding(5.dp)) {
                            Text(
                                text = "Username: ${username.value}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Email: ${email.value}",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { clearLoginState(context) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text(text = "Sign Out")
                            }
                        }
                    }
                }

            }
        }
    }

}



