package com.martabak.ecommerce.main.store.data

data class ProductQuery(
    var search: String = "",
    var brand: String? = null,
    var lowest: Int? = null,
    var highest : Int? = null,
    var sort: String? = null,
    var limit : Int = 6
)