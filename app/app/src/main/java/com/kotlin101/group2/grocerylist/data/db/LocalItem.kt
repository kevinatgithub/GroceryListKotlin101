package com.kotlin101.group2.grocerylist.data.db

import io.realm.kotlin.types.RealmObject

class LocalItem() : RealmObject {
    var alternativeItemId: Int = 0
    var cartId: Int = 0
    var description: String = ""
    var id: Int = 0
    var img: String? = null
    var imgUrl: String? = null
    var isPrimary: Boolean = false
    var name: String = ""
    var pricePerUnit: Double = 0.0
    var quantity: Int = 0
    var status: Int = 1
    var totalPrice: Double = 0.0
}