package com.example.test

import android.graphics.drawable.Drawable

class Product {
    var image: Drawable? = null
    var name: String? = ""
    var price: Int? = 0

    constructor(image: Drawable?, name: String?, price: Int?) {
        this.image = image
        this.name = name
        this.price = price
    }
}