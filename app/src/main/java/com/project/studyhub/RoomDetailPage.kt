package com.project.studyhub

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.project.studyhub.ui.theme.StudyHubTheme
import java.util.Calendar
import java.util.Date

class RoomDetailPage : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyHubTheme {
                val intent = intent
                var name = intent.getStringExtra("name").toString()
                var desc = intent.getStringExtra("desc").toString()
                var image = intent.getStringExtra("image").toString()
                var price = intent.getStringExtra("price").toString()
                var totalSeats = intent.getStringExtra("totalSeats").toString()
                var availableSeats = intent.getStringExtra("availableSeats").toString()

                DetailPage(name,desc,image,price,totalSeats,availableSeats)
            }
        }
    }
}
@Composable
fun DetailPage(
    name: String,
    desc: String,
    image: String,
    price: String,
    totalSeats: String,
    availableSeats: String
) {
    var context = LocalContext.current
    var startDate = remember { mutableStateOf("") }
    var endDate = remember { mutableStateOf("") }
    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    val mDate = remember { mutableStateOf("") }

    mCalendar.time = Date()
    val startDatePickerDialog = android.app.DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            startDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )
    val endDatePickerDialog = android.app.DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            endDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                // Call your function to handle the booking and show notification
                handleBookingAndNotify(context, name, startDate.value, endDate.value)
            } else {
                // Handle the case where the user denies the permission
                Toast.makeText(context, "Permission denied, cannot show notifications.", Toast.LENGTH_LONG).show()
            }
        }
    )

    // Request permission and handle booking
    fun checkPermissionAndBook() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) -> {
                // Permission is already granted
                handleBookingAndNotify(context, name, startDate.value, endDate.value)
            }
            else -> {
                // Request permission
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val gradientColor = listOf(Color(0xFFFFFFFF), Color(0xFF3291cb))
//    var ss:String = getIntent().putExtra("complexObject", clickedTitle);
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(image),
                        contentDescription = "Room Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    Spacer(Modifier.height(8.dp))
                    Row {
                        Text("$name", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row {
                        Text("Available Seats:", fontSize = 18.sp)
                        Spacer(Modifier.width(5.dp))
                        Text("$availableSeats", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color.Blue)
                    }

                    Spacer(Modifier.height(8.dp))

                    Row {
                        Text("Total Seats:", fontSize = 18.sp)
                        Spacer(Modifier.width(5.dp))
                        Text("$totalSeats", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    }

                    Spacer(Modifier.height(8.dp))
                    Row {
                        Text("Price:", fontSize = 18.sp)
                        Spacer(Modifier.width(5.dp))
                        Text("$price", fontWeight = FontWeight.Normal, fontSize = 18.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row {
                        Text("Description:", fontSize = 18.sp)
                        Spacer(Modifier.width(5.dp))
                        Text("$desc", fontWeight = FontWeight.Medium, fontSize = 18.sp, color = Color.DarkGray)
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)
                                .weight(1f)
                                .clickable { startDatePickerDialog.show() },
                            value = startDate.value,
                            onValueChange = {
                                startDate.value = it
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { startDatePickerDialog.show() },
                                ) {
                                    Icon(Icons.Default.DateRange, contentDescription = "startDate")
                                }
                            },
                            readOnly = true,
                            label = {
                                Text(text = "Start Date")
                            },
                            placeholder = {
                                Text(text = "Select Start Date")
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)
                                .weight(1f)
                                .clickable {

                                },
                            value = endDate.value,
                            onValueChange = {
                                endDate.value = it
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { endDatePickerDialog.show() },
                                ) {
                                    Icon(Icons.Default.DateRange, contentDescription = "endDate")
                                }
                            },
                            readOnly = true,
                            label = {
                                Text(text = "End Date")
                            },
                            placeholder = {
                                Text(text = "Select End Date")
                            }
                        )
                    }

                    // Add EditText for picking a date here
                    Spacer(Modifier.height(16.dp))

                    // Button for booking
                    Button(
                        onClick = {
                            checkPermissionAndBook()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF2ba3ed)
                        )
                    ) {
                        Text(
                            text = "Book Now",
                            color = Color.White
                        )
                    }
                }
            }
        }
    )

}
// Define a data class for booking entry
data class BookingEntry(
    val roomName: String,
    val startDate: String,
    val endDate: String
)

// Function to add booking entry to SharedPreferences
fun addBookingEntryToSharedPreferences(context: Context, bookingEntry: BookingEntry) {
    val sharedPreferences = context.getSharedPreferences("BookingHistory", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()
    val bookingListJson = sharedPreferences.getString("bookingList", null)
    val bookingList = if (bookingListJson != null) {
        gson.fromJson(bookingListJson, Array<BookingEntry>::class.java).toMutableList()
    } else {
        mutableListOf()
    }
    bookingList.add(bookingEntry)
    editor.putString("bookingList", gson.toJson(bookingList))
    editor.commit()
}
fun showBookingConfirmationNotification(context: Context, roomName: String, startDate: String, endDate: String) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Creating a notification channel for Android O and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "booking_notification_channel",
            "Room Booking Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notification channel for room booking"
        }
        notificationManager.createNotificationChannel(channel)
    }

    // Building the notification
    val notification = NotificationCompat.Builder(context, "booking_notification_channel")
        .setSmallIcon(R.drawable.study)  // Replace with your app's icon
        .setContentTitle("Booking Confirmed")
        .setContentText("Your booking for $roomName from $startDate to $endDate is confirmed.")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    // Showing the notification
    notificationManager.notify(1001, notification)
    Toast.makeText(context,"Booked Successfully!", Toast.LENGTH_SHORT).show()

}
fun handleBookingAndNotify(context: Context, roomName: String, startDate: String, endDate: String) {
    // Assuming you handle booking here
    val bookingEntry = BookingEntry(roomName = roomName, startDate = startDate, endDate = endDate)
    addBookingEntryToSharedPreferences(context, bookingEntry)
    // Optionally update Firebase here for room availability
    // Update available seats in Firebase
    // Show success message or navigate to confirmation screen

    showBookingConfirmationNotification(context, roomName, startDate, endDate)
}

// Function to update available seats count in room data
fun updateAvailableSeatsInFirebase(roomId: String, newAvailableSeats: Int) {
    val database = FirebaseDatabase.getInstance()
    val roomRef = database.getReference("rooms").child(roomId)

    // Update the available seats count
    roomRef.child("availableSeats").setValue(newAvailableSeats)
        .addOnSuccessListener {
            Log.d("RoomDetails", "Available seats count updated successfully for room: $roomId")
        }
        .addOnFailureListener { e ->
            Log.e("RoomDetails : error", "Error updating available seats count for room: $roomId", e)
        }
}

