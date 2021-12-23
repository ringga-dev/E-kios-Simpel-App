package com.buy.e_kios.data.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.buy.e_kios.DitailProducActivity
import com.buy.e_kios.HomeActivity
import com.buy.e_kios.KeranjangActivity
import com.buy.e_kios.R
import com.buy.e_kios.data.api.RetrofitClient
import com.buy.e_kios.data.models.keranjang.Keranjang
import com.buy.e_kios.db.DBHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_keranjang.view.*

class KeranjangAdapter(
    private var wallpaper: MutableList<Keranjang>,
    private var context: Context
) : RecyclerView.Adapter<KeranjangAdapter.ViewHolder>() {

    fun setWallpapers(r: List<Keranjang>) {
        wallpaper.clear()
        wallpaper.addAll(r)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(wallpaper[position], context)

    override fun getItemCount() = wallpaper.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(wallpaper: Keranjang, context: Context) {
            Picasso.get().load(RetrofitClient.BASE_URL + "assets/barang/" + wallpaper.image)
                .into(itemView.imageProduk)
            val db = DBHelper(context, null)
            val kurang = wallpaper.harga * wallpaper.promo / 100
            itemView.harga_barang.text = "Rp " + (wallpaper.harga - kurang)

            itemView.ed_qty.setText(wallpaper.qty.toString())
            itemView.nama_barang.text = wallpaper.nama
            itemView.btn_min.setOnClickListener {
                if (itemView.ed_qty.text.toString().trim().toInt() == 1) {
                    db.deleteById(wallpaper.id)
                    var i = Intent(context, KeranjangActivity::class.java)
                    context.startActivity(i)
                }else if (itemView.ed_qty.text.toString().trim().toInt() >= 1){
                    var jumlah = itemView.ed_qty.text.toString().trim().toInt() - 1
                    itemView.ed_qty.setText(jumlah.toString())
                    db.update(wallpaper.id, jumlah)
                }
            }
            itemView.btn_add.setOnClickListener {
                var jumlah = itemView.ed_qty.text.toString().trim().toInt() + 1
                itemView.ed_qty.setText(jumlah.toString())
                db.update(wallpaper.id, jumlah)
            }
        }


    }


}