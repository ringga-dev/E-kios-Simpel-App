package com.buy.e_kios

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_ditail_produc.*

class DitailProducActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ditail_produc)
        val extras = intent.extras

        if (extras?.getString("promo") != null) {
            val kurang =
                extras.getString("harga")!!.toInt() * extras.getString("promo")!!.toInt() / 100
            Toast.makeText(this, kurang.toString(), Toast.LENGTH_SHORT).show()


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tv_harga.text =
                    Html.fromHtml(
                        "<s>Rp ${extras.getString("harga")}</s>" + " Rp " + (extras?.getString(
                            "harga"
                        )!!.toInt() - kurang).toString(), Html.FROM_HTML_MODE_COMPACT
                    )
            } else {
                tv_harga.text =
                    Html.fromHtml(
                        "<s>Rp ${extras.getString("harga")}</s>" + " Rp " + (extras?.getString(
                            "harga"
                        )!!.toInt() - kurang).toString()
                    )
            }
        } else {
            tv_harga.text = extras?.getString("harga")
        }

        tv_nama.text = extras?.getString("nama_barang")
        tv_dec.text = extras?.getString("deskripsi")

        Picasso.get().load(extras?.getString("image1")).into(iv_1)
        Picasso.get().load(extras?.getString("image2")).into(iv_2)
        Picasso.get().load(extras?.getString("image3")).into(iv_3)
        Picasso.get().load(extras?.getString("image4")).into(iv_4)
        tv_nama.text = extras?.getString("id")


    }
}