package com.project.studyhub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter

@Composable
fun RoomCard(room: RoomModel) {
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
            .fillMaxSize()
            .padding(10.dp)) {
            Image(
                //painter = rememberAsyncImagePainter(room.imageUrl),
                painter = painterResource(id = R.drawable.download),
                contentDescription = "Room Image",
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = "Room Name: ${room.roomName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Available Seats: ${room.availableSeat}",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total Seats: ${room.totalSeat}",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Description: ${room.roomDescription}",
                    fontSize = 16.sp,
                    maxLines = 3
                )
            }
        }
    }
}