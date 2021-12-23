package com.buy.e_kios.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                ID_BARANG + " INTEGER," +
                NAMA + " TEXT," +
                HARGA + " INTEGER," +
                IMAGE + " TEXT," +
                PROMO + " INTEGER," +
                QTY + " INTEGER" + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addName(id_barang: Int,nama:String, harga: Int, image: String, promo: Int, qty: Int) {
        val values = ContentValues()
        values.put(ID_BARANG, id_barang)
        values.put(NAMA, nama)
        values.put(HARGA, harga)
        values.put(IMAGE, image)
        values.put(PROMO, promo)
        values.put(QTY, qty)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getName(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun cekKeranjang(id_barang: Int):Cursor?{
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $ID_BARANG =$id_barang", null)
    }

    fun delete() {
        val db = this.readableDatabase
        return db.execSQL("DELETE FROM $TABLE_NAME")
    }

    fun update(id : Int,qty:Int): Int {
        val db = this.readableDatabase
        val values = ContentValues()

        values.put(QTY, qty)
        return  db.update(TABLE_NAME, values, "$ID_COL=$id", arrayOf())
    }


    fun deleteById(id:Int){
        val db = this.readableDatabase
        return db.execSQL("DELETE FROM $TABLE_NAME WHERE $ID_COL = $id")
    }


    companion object {
        private val DATABASE_NAME = "GEEKS_FOR_GEEKS"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "gfg_keranjang"
        val ID_COL = "id"
        val ID_BARANG = "id_barang"
        val NAMA = "nama"
        val HARGA = "harga"
        val IMAGE = "image"
        val PROMO = "promo"
        val QTY = "qty"
    }
}