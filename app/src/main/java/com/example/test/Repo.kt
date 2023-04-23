package com.example.test

import androidx.lifecycle.MutableLiveData

object Repo {
    private val cartItems: MutableLiveData<MutableList<Product>> = MutableLiveData<MutableList<Product>>()
    fun getCartItems(): MutableLiveData<MutableList<Product>> {
        return cartItems
    }

    fun addToCart(product: Product) {
        val mutableList: MutableList<Product> = ArrayList()
        if (cartItems.value?.isNotEmpty() == true)
            mutableList.addAll(cartItems.value!!)
        if (!mutableList.contains(product)) {
            mutableList.add(product)
            cartItems?.postValue(mutableList)
        }
    }

    fun removeFromCart(product: Product) {
        val mutableList: MutableList<Product> = ArrayList()
        if (cartItems.value?.isNotEmpty() == true)
            mutableList.addAll(cartItems.value!!)
        mutableList.remove(product)
        cartItems.postValue(mutableList)
    }

    fun clearCart() {
        val mutableList: MutableList<Product> = ArrayList()
        cartItems.postValue(mutableList)
    }
}