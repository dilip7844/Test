package com.example.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VModel : ViewModel() {
    val repo = Repo
    fun getCartItems(): MutableLiveData<MutableList<Product>>? {
        return repo.getCartItems()
    }
}