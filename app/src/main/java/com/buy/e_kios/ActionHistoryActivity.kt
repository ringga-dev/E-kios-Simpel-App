package com.buy.e_kios

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.buy.e_kios.data.api.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_action_history.*

class ActionHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_history)
        val extras = intent.extras

        Picasso.get().load(RetrofitClient.BASE_URL + "assets/barang/" + extras?.getString("image"))
            .into(iv_barang)

        tv_nama.text = extras?.getString("nama_barang")
        val kurang =
            (extras?.getInt("harga")!! * extras.getInt("promo") / 100) * extras.getInt("qty")
        tv_harga.text =
            "Pembayaran: " + "Rp " + (extras?.getInt("harga")!! * extras.getInt("qty") - kurang)

        tv_promo.text = extras?.getString("promo")
        tv_promo.text = extras?.getString("date")
        tv_stts.text = extras?.getString("stts")

        if (extras?.getString("stts")=="DI KIRIM"){
            btn_like.visibility =View.VISIBLE
        }

        if (extras?.getString("stts")=="DI PESAN"){
            btn_tolak.visibility =View.VISIBLE
        }


        btn_tolak.setOnClickListener {
            getAlert()
        }
    }

    private fun getAlert() {
        val inflate = layoutInflater
        val infla_view = inflate.inflate(R.layout.alert_terima, null)
        val yes = infla_view.findViewById<ImageView>(R.id.btn_yes)
        val keluar = infla_view.findViewById<ImageView>(R.id.btn_tolak)
        yes.setOnClickListener {
//            startActivity(Intent(this, PesanActivity::class.java))
        }

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setView(infla_view)
        alertDialog.setCancelable(false)

        val dialog = alertDialog.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        keluar.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this, "close alert", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.red))
    }
}