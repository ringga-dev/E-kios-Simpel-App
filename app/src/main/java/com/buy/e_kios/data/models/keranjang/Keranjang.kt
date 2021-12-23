package com.buy.e_kios.data.models.keranjang

data class Keranjang(
    val id :Int,
    val id_barang: Int,
    val nama: String,
    val harga: Int,
    val image: String,
    val promo: Int,
    val qty: Int
)