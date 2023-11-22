package com.example.umc_android

data class Album (
    val title : String? = "",
    val singer : String? = "",
    val coverImage: Int? = null,
    val song: ArrayList<Song>? = null
)