package com.project.studyhub.admin

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.studyhub.RoomModel
import android.content.Context
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDialog
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Calendar
import androidx.compose.ui.text.font.FontWeight
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream


class RoomViewModel : ViewModel() {

    var roomName by mutableStateOf("")
    var roomImageUri by mutableStateOf<Uri?>(null)
    var totalSeats by mutableStateOf("")
    var availableSeats by mutableStateOf("")
    var description by mutableStateOf("")
    var price by mutableStateOf("")
    var startDate by mutableStateOf("")
    var endDate by mutableStateOf("")

    fun addRoomToDatabase(context: Context,room: RoomModel) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Adding room...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val storageRef = FirebaseStorage.getInstance().getReference("room_images")
        roomImageUri?.let { uri ->
            val fileRef = storageRef.child("${System.currentTimeMillis()}.jpg")
            val uploadTask = fileRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                    val roomsRef: DatabaseReference = database.getReference("rooms")
                    val newRoomRef = roomsRef.push()
                    val updatedRoom = room.copy(roomImageUri = downloadUri.toString())
                    newRoomRef.setValue(updatedRoom)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(context, "Room added successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            progressDialog.dismiss()
                            Log.e("RoomViewModel", "Error adding room to database", e)
                        }
                }
            }.addOnFailureListener { e ->
                progressDialog.dismiss()
                Log.e("RoomViewModel", "Error uploading image", e)
            }
        } ?: kotlin.run {
            progressDialog.dismiss()
            Toast.makeText(context, "Image must be selected", Toast.LENGTH_SHORT).show()
        }

    }

    private val roomsRef = FirebaseDatabase.getInstance().getReference("rooms")

    private val _rooms = MutableLiveData<List<RoomModel>>()
    val rooms: LiveData<List<RoomModel>> = _rooms

    init {
        fetchRooms()
    }

    private fun fetchRooms() {
        roomsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val roomsList = mutableListOf<RoomModel>()
                for (roomSnapshot in snapshot.children) {
                    val room = roomSnapshot.getValue(RoomModel::class.java)
                    room?.let { roomsList.add(it) }
                }
                _rooms.value = roomsList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e(TAG, "Error fetching rooms", error.toException())
            }
        })
    }

    companion object {
        private const val TAG = "RoomViewModel"
    }

}

@Composable
fun AddRooms(viewModel: RoomViewModel) {
    var context = LocalContext.current
    // ActivityResultLauncher for image picker
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            // Process the bitmap, save it to a local file, and then set the URI
            val uri = saveImageToInternalStorage(context, bitmap)
            viewModel.roomImageUri = uri
        }
    }

    // ActivityResultLauncher for picking image from the gallery
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.roomImageUri = uri
        }
    }
    // Initialize room with values from the RoomViewModel
    val rooms = RoomModel(
        roomName = viewModel.roomName,
        roomImageUri = viewModel.roomImageUri?.toString() ?: "",
        totalSeats = viewModel.totalSeats,
        availableSeats = viewModel.availableSeats,
        roomDescription = viewModel.description,
        startDate = viewModel.startDate,
        endDate = viewModel.endDate,
        price = viewModel.price
    )
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                viewModel.roomImageUri = uri
            }
        }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        contentAlignment = Alignment.Center){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()))

        {
            Text("Add New Room", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.roomName,
                onValueChange = { viewModel.roomName = it },
                label = { Text("Room Name") }
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Placeholder for image picker logic
//            Button(onClick = {
//                takePictureLauncher.launch(null)
//            }) {
//                Text("Pick Room Image")
//            }

            Row {
                Button(onClick = {
                    takePictureLauncher.launch(null)
                }) {
                    Text("Take Picture")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    pickImageLauncher.launch("image/*")
                }) {
                    Text("Pick from Gallery")
                }
            }

            // Image preview
            Spacer(modifier = Modifier.height(16.dp))
            viewModel.roomImageUri?.let { uri ->
                Image(painter = rememberAsyncImagePainter(uri), contentDescription = "Room Image",
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp)))
            }


            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.totalSeats,
                onValueChange = { viewModel.totalSeats = it },
                label = { Text("Total Seats") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.availableSeats,
                onValueChange = { viewModel.availableSeats = it },
                label = { Text("Available Seats") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.description,
                onValueChange = { viewModel.description = it },
                label = { Text("Description") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.price,
                onValueChange = { viewModel.price = it },
                label = { Text("Price") }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.addRoomToDatabase(context, rooms)
            }
            , modifier = Modifier.fillMaxWidth()) {
                Text("Add Room")
            }
        }
    }

}
// Function to save the captured image to internal storage
fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): Uri {
    val filename = "temp_image_${System.currentTimeMillis()}.jpg"
    val fos: FileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
    fos.close()
    return File(context.filesDir, filename).toUri()
}