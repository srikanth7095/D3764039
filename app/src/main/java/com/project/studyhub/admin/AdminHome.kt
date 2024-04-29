package com.project.studyhub.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.studyhub.R
import com.project.studyhub.RoomCard
import com.project.studyhub.RoomDetailPage
import com.project.studyhub.RoomModel
import com.project.studyhub.clearLoginState
import com.project.studyhub.ui.theme.StudyHubTheme

class AdminHome : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyHubTheme {
                App()
            }
        }
    }
    @Composable
    fun App() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "mainScreen") {
            composable("mainScreen") {
                AdminScreen(navController, viewModel = RoomViewModel())
            }
            composable("addRooms") {
                // Assume you have a ViewModel here, you might need to pass or retrieve it correctly
                AddRooms(viewModel = RoomViewModel())
            }
        }
    }

    @Composable
    fun AdminScreen(navController: NavController,viewModel: RoomViewModel) {
        val context = LocalContext.current
        val rooms by viewModel.rooms.observeAsState(initial = emptyList())
        val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        var username = remember { mutableStateOf("") }
        var email = remember { mutableStateOf("") }

        // Read user data from SharedPreferences
        username.value = sharedPreferences.getString("username", "") ?: ""
        email.value = sharedPreferences.getString("email", "") ?: ""

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("addRooms")
                    },

                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Room")
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(it)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Welcome to Admin Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White, // Card background color
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(10.dp),
                        ) {
                            Image(
                                // Example image, replace with your own logic
                                painter = painterResource(id = R.drawable.user),
                                contentDescription = "User Image",
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
                                Spacer(modifier = Modifier.height(8.dp))
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Study Rooms",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(rooms) { room ->
                            RoomCard(room = room) { roomId ->
                                // Handle item click by creating an intent to navigate to the room detail page
                                val intent = Intent(context, RoomDetailPage::class.java).apply {
                                    putExtra("name", room.roomName)
                                    putExtra("desc", room.roomDescription)
                                    putExtra("image", room.roomImageUri)
                                    putExtra("price", room.price)
                                    putExtra("totalSeats", room.totalSeats)
                                    putExtra("availableSeats", room.availableSeats)
                                }
//                                Toast.makeText(context,room.roomName, Toast.LENGTH_SHORT).show()
                                context.startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }
}



