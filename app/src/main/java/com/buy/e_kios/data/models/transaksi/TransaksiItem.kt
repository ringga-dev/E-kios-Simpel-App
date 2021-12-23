package com.buy.e_kios.data.models.transaksi

data class TransaksiItem(
    val alamat: String,
    val date: String,
    val email: String,
    val fullname: String,
    val harga: Int,
    val id: String,
    val id_barang: String,
    val id_pengguna: String,
    val lat: String,
    val long: String,
    val message: String,
    val nama_barang: String,
    val no_phone: String,
    val qty: Int,
    val send: String,
    val stts: String,
    val image_b1:String,
    val promo:Int
)