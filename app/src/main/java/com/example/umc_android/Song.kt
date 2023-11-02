package com.example.umc_android

data class Song(
    val title : String = "",
    val singer : String = "",
    val second : Int = 0,
    var playTime : Int = 0,
    var isPlaying : Boolean = true
)
