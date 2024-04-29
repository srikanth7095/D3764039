package com.project.studyhub

sealed class FragmentScreens (val screens: String){
    data object Home: FragmentScreens ("Home")
    data object Bookings: FragmentScreens ("Bookings")
    data object Quotes: FragmentScreens ("Quotes")
    data object Profile: FragmentScreens ("Profile")
    data object RoomDetailPage: FragmentScreens ("RoomDetailPage")
}