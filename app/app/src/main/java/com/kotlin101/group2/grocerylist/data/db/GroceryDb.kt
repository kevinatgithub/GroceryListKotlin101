package com.kotlin101.group2.grocerylist.data.db

import android.content.Context
import com.kotlin101.group2.grocerylist.data.api.models.Item
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.query.RealmResults

class GroceryDb(context: Context) {
    private lateinit var realm: Realm

    companion object{
        fun apiToDb(item: Item) : LocalItem{
            var _item = LocalItem()
            var _img : String? = null
            if (item.img != null){
                _img = item.img.toString()
            }
            var _imgUrl : String? = null
            if (item.imgUrl != null){
                _imgUrl = item.imgUrl.toString()
            }
            with(_item){
                alternativeItemId = item.alternativeItemId
                cartId = item.cartId
                description = item.description
                id = item.id
                img = _img
                imgUrl = _imgUrl
                isPrimary = item.isPrimary
                name = item.name
                pricePerUnit = item.pricePerUnit
                quantity = item.quantity
                status = item.status
                totalPrice = item.totalPrice
            }
            return _item
        }
        fun dbToApi(item: LocalItem) : Item{
            var _img : Any? = null
            if (item.img != null){
                _img = item.img.toString()
            }
            var _imgUrl : Any? = null
            if (item.imgUrl != null){
                _imgUrl = item.imgUrl.toString()
            }
            return Item(item.alternativeItemId, item.cartId, item.description, item.id, _img, _imgUrl, item.isPrimary, item.name, item.pricePerUnit, item.quantity, item.status, item.totalPrice)
        }
    }

    init {
        val config = RealmConfiguration.create(schema = setOf(LocalItem::class))
        realm = Realm.open(config)
    }

    fun add(item: LocalItem){
        realm.writeBlocking {
            copyToRealm(item)
        }
    }

    fun all(condition: String? = null): RealmResults<LocalItem>{
        if (condition == null){
            return realm.query(LocalItem::class).find()
        }
        return realm.query(LocalItem::class, condition!!).find()
    }

    fun update(item: LocalItem){
        realm.writeBlocking {
            findLatest(all("id == $item.id")[0]).apply {
                if (this != null){
                    alternativeItemId = item.alternativeItemId
                    cartId = item.cartId
                    description = item.description
                    id = item.id
                    img = item.img
                    imgUrl = item.imgUrl
                    isPrimary = item.isPrimary
                    name = item.name
                    pricePerUnit = item.pricePerUnit
                    quantity = item.quantity
                    status = item.status
                    totalPrice = item.totalPrice
                }
            }
        }
    }

    fun delete(item: LocalItem){
        realm.writeBlocking {
            delete(item)
        }
    }
}