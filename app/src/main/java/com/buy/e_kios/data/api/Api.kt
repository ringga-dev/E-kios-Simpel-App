package com.buy.e_kios.data.api

import com.buy.e_kios.data.models.login.Login
import com.buy.e_kios.data.models.produc.Produc
import com.buy.e_kios.data.models.register.Register
import com.buy.e_kios.data.models.transaksi.TransaksiItem
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    //login
    @FormUrlEncoded
    @POST("apimobile/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Login>

    //register
    @FormUrlEncoded
    @POST("apimobile/register")
    fun register(
        @Field("fullname") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("alamat") alamat: String,
        @Field("no_phone") no_phone: String
    ): Call<Register>

    //pesan
    @FormUrlEncoded
    @POST("apimobile/pesan")
    fun pesan(
        @Field("id_barang") id_barang: Int,
        @Field("id_pengguna") id_pengguna: Int,
        @Field("qty") qty: Int,
        @Field("alamat") alamat: String,
        @Field("lat") lat: Double?,
        @Field("long") long: Double?,
        @Field("message") message: String
    ): Call<Register>

    //pesan
    @FormUrlEncoded
    @POST("apimobile/pembatalan")
    fun batal(
        @Field("id") id: Int,
        @Field("message") message: String
    ): Call<Register>

    //pesan
    @FormUrlEncoded
    @POST("apimobile/diterima")
    fun diterima(
        @Field("id") id: Int,
        @Field("message") message: String
    ): Call<Register>

    //menampilkan story pesanan
    @FormUrlEncoded
    @POST("apimobile/get_transaksi")
    fun getPesanan(
        @Field("id_user") id_user: Int,
        @Field("stts") stts: String
    ): Call<List<TransaksiItem>>



    //menampilkan story pesanan
    @FormUrlEncoded
    @POST("apimobile/save_token")
    fun saveToken(
        @Field("id") id: Int,
        @Field("token") token: String
    ): Call<Register>

    //menampilkan data di keranjang
    @FormUrlEncoded
    @POST("apimobile/keranjang")
    fun insertKeranjang(
        @Field("id_barang") id_barang: Int,
        @Field("id_pengguna") id_pengguna: Int
    ): Call<Register>


    //menampilkan data di keranjang
    @FormUrlEncoded
    @POST("apimobile/get_keranjang")
    fun getKeranjang(
        @Field("id_pengguna") id_pengguna: Int
    ): Call<Register>

    @GET("apimobile/getProduc")
    fun getProduc(): Call<List<Produc>>

    @GET("apimobile/getNews")
    fun getNews(): Call<List<Produc>>

}


