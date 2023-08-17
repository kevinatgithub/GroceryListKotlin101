package com.kotlin101.group2.grocerylist.data.api.models

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