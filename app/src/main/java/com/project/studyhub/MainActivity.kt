package com.project.studyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.studyhub.ui.theme.StudyHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyHubTheme {
                MainScreen()
            }
        }
    }
}

@Preview
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(containerColor = Color(0xFF2196F3), modifier = Modifier.height(55.dp)) {
                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Home
                        navController.navigate(FragmentScreens.Home.screens) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Home) Color.White else Color(
                            0xFFACABAB
                        ),
                    )
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.DateRange
                        navController.navigate(FragmentScreens.Bookings.screens) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.DateRange) Color.White else Color(
                            0xFFACABAB
                        ),
                    )
                }
                IconButton(
                    onClick = {
                        selected.value = Icons.Default.List
                        navController.navigate(FragmentScreens.Quotes.screens) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.List) Color.White else Color(
                            0xFFACABAB
                        ),
                    )
                }
                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Person
                        navController.navigate(FragmentScreens.Profile.screens) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Person) Color.White else Color(
                            0xFFACABAB
                        ),
                    )
                }

            }
        },
    ){paddingValues ->
        NavHost(
            navController = navController,
            startDestination = FragmentScreens.Home.screens,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(FragmentScreens.Home.screens){ Home()}
            composable(FragmentScreens.Bookings.screens){ Bookings() }
            composable(FragmentScreens.Quotes.screens){ MyQuotes() }
            composable(FragmentScreens.Profile.screens){ Profile()}

        }

    }
}
