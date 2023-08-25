package com.kotlin101.group2.grocerylist.data.db

import io.realm.kotlin.types.RealmObject

class PendingItemUpdate(): RealmObject{
    var itemId: Int = 0
    var status: Int = 1
}
