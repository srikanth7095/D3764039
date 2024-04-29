package com.project.studyhub

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.project.studyhub.admin.RoomViewModel

@Composable
fun Home(viewModel: RoomViewModel){
    //val rooms = remember { Details.RoomDetailsList }
    val context = LocalContext.current
    val rooms by viewModel.rooms.observeAsState(initial = emptyList())
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)){
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
                    Image(
                        painterResource(id = R.drawable.study),
                        contentDescription = "Setting icon",
                        modifier = Modifier.size(32.dp),
//                        colorFilter = ColorFilter.tint(Color(0xFFAE0431))
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "Home",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "Profile icon",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn (
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp)
            ){
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
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}