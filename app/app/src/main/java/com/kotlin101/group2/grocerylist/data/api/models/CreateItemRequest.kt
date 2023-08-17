package com.kotlin101.group2.grocerylist.data.api.models

data class CreateItemRequest(
    val alternativeItemId: Int,
    val description: String,
    val name: String,
    val price: Int
)