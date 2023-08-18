package com.kotlin101.group2.grocerylist.data.api.models

data class UpdateItemRequest(
    val description: String,
    val img: Any,
    val imgURL: Any,
    val name: String,
    val pricePerUnit: Int,
    val quantity: Int
)