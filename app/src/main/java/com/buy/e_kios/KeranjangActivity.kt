package com.buy.e_kios

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.red
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.buy.e_kios.data.adapter.KeranjangAdapter
import com.buy.e_kios.data.models.keranjang.Keranjang
import com.buy.e_kios.db.DBHelper
import kotlinx.android.synthetic.main.activity_keranjang.*

class KeranjangActivity : AppCompatActivity() {
    private var keranjang: MutableList<Keranjang> = ArrayList()
    private var total: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keranjang)
        getData()
        setupRecler()

        btn_bayar.setOnClickListener {

            getData()
            if (keranjang.size < 1) {
                Toast.makeText(this, "Anda belum belanja satupun", Toast.LENGTH_SHORT).show()
            } else {
                tv_total.text = "RP $total"

                getAlert()
                setupRecler()
            }
        }
    }

    private fun setupRecler() {
        rv_keranjang.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            adapter = KeranjangAdapter(mutableListOf(), this@KeranjangActivity)

        }

        rv_keranjang.adapter?.let { adapter ->
            if (adapter is KeranjangAdapter) {
                adapter.setWallpapers(keranjang)
            }
        }
    }

    @SuppressLint("Range")
    private fun getData() {
        keranjang?.clear()
        val db = DBHelper(this, null)
        var hargatotal = 0
        val cursor = db.getName()
        cursor!!.moveToFirst()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
                val barang = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_BARANG))
                val name = cursor.getString(cursor.getColumnIndex(DBHelper.NAMA))
                val harga = cursor.getInt(cursor.getColumnIndex(DBHelper.HARGA))
                val image = cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE))
                val promo = cursor.getInt(cursor.getColumnIndex(DBHelper.PROMO))
                val qty = cursor.getInt(cursor.getColumnIndex(DBHelper.QTY))
                keranjang.add(Keranjang(id, barang, name, harga, image, promo, qty))

                val kurang = (harga * promo / 100) * qty
                hargatotal += (harga * qty) - kurang
            } while (cursor.moveToNext())
        }
        // at last we close our cursor
        cursor.close()
        total = hargatotal
    }

    private fun getAlert() {
        val inflate = layoutInflater
        val infla_view = inflate.inflate(R.layout.alert_bayar, null)
        val bayar = infla_view.findViewById<Button>(R.id.btn_bayar)
        val keluar = infla_view.findViewById<ImageView>(R.id.close)
        val totalharga = infla_view.findViewById<TextView>(R.id.tv_harga)
        totalharga.text ="Rp " + total.toString()
        bayar.setOnClickListener {
            startActivity(Intent(this, PesanActivity::class.java))
        }

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setView(infla_view)
        alertDialog.setCancelable(false)
        val dialog = alertDialog.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        keluar.setOnClickListener {
            //dismiss dialog
            dialog.dismiss()
            Toast.makeText(this, "close alert", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.red))
    }
}