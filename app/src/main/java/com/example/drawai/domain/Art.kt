package com.example.drawai.domain

data class Art(
    val id: Int = 0,
    val prompt: String,
    val resolution : String,
    val imageUrl: String,
    val createdAt: Long
)