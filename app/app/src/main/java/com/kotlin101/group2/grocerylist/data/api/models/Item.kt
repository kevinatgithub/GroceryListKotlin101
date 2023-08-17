package com.group2.kotlinlab.data.models

 data class Item(
    val alternativeItemId: Int,
    val cartId: Int,
    val description: String,
    val id: Int,
    val isPrimary: Boolean,
    val name: String,
    val price: Int,
    val status: Int
)