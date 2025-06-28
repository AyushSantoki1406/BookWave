package com.example.bookwave

import java.io.Serializable

class BookDataClass(
    val bookName: String,
    val author: String,
    val img: String,
    val category: String,
    val description: String,
    val language: String,
    val rating: Number
) : Serializable