package com.kotlin101.group2.grocerylist.data.api.models

 data class Item(
    val alternativeItemId: Int,
    val cartId: Int,
    val description: String,
    val id: Int,
    val img: Any,
    val imgUrl: Any,
    val isPrimary: Boolean,
    val name: String,
    val pricePerUnit: Int,
    val quantity: Int,
    val status: Int,
    val totalPrice: Int
)