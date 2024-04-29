package com.project.studyhub

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun Bookings(){
    val context = LocalContext.current
    val bookingEntries = remember { getBookingEntriesFromSharedPreferences(context) }
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bookings",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(Modifier.height(16.dp))
            LazyColumn {
                items(bookingEntries) { bookingEntry ->
                    BookingEntryItem(bookingEntry)
                }
            }
        }
    }

}
// Function to retrieve booking entries from SharedPreferences
fun getBookingEntriesFromSharedPreferences(context: Context): List<BookingEntry> {
    val sharedPreferences = context.getSharedPreferences("BookingHistory", Context.MODE_PRIVATE)
    val gson = Gson()
    val json = sharedPreferences.getString("bookingList", null)
    val type = object : TypeToken<List<BookingEntry>>() {}.type
    return gson.fromJson(json, type) ?: emptyList()
}

@Composable
fun BookingList(context: Context) {
    val bookingEntries = remember { getBookingEntriesFromSharedPreferences(context) }

    LazyColumn {
        items(bookingEntries) { bookingEntry ->
            BookingEntryItem(bookingEntry)
        }
    }
}



@Composable
fun BookingEntryItem(bookingEntry: BookingEntry) {
    // Your UI for displaying a single booking entry
    Column {
        Row {
            Text(text = "Room name:")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "${bookingEntry.roomName}", fontWeight = FontWeight.Medium, color =  Color(
                0xFF3D3D3D
            )
            )
        }
        Row {
            Text(text = "From:")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "${bookingEntry.startDate}", fontWeight = FontWeight.Medium, color =  Color(
                0xFF0B756B)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "To:")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "${bookingEntry.endDate}", fontWeight = FontWeight.Medium, color =  Color(
                0xFFC44339
            )
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Divider(color = Color(0xFFC4C4C4), thickness = 1.dp)
    }
}
