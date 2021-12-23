package com.buy.e_kios.data.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.buy.e_kios.DitailProducActivity
import com.buy.e_kios.R
import com.buy.e_kios.data.api.RetrofitClient
import com.buy.e_kios.data.models.produc.Produc
import com.buy.e_kios.db.DBHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_produc.view.*

class WallpaperAdapter(
    private var wallpaper: MutableList<Produc>,
    private var context: Context
) : RecyclerView.Adapter<WallpaperAdapter.ViewHolder>() {

    fun setWallpapers(r: List<Produc>) {
        wallpaper.clear()
        wallpaper.addAll(r)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_produc, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(wallpaper[position], context)

    override fun getItemCount() = wallpaper.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(wallpaper: Produc, context: Context) {
            Picasso.get().load(RetrofitClient.BASE_URL + "assets/barang/" + wallpaper.image_b1)
                .into(itemView.imageView)

            if (wallpaper.promo != null) {
                itemView.tv_harga.text = "kurang dari Rp ${wallpaper.harga}"
            } else {
                itemView.tv_harga.text = "Rp ${wallpaper.harga}"
            }

            itemView.tv_nama.text = wallpaper.nama_barang
            itemView.btn_add.setOnClickListener {
                val db = DBHelper(context, null)
                val cek =db.cekKeranjang(wallpaper.id.toInt())
                if (cek?.count!! > 0){
                    Toast.makeText(context, "Sudah di keranjang", Toast.LENGTH_SHORT).show()
                }else{
                    if (wallpaper.promo == null) {
                        db.addName(
                            wallpaper.id.toInt(),
                            wallpaper.nama_barang,
                            wallpaper.harga.toInt(),
                            wallpaper.image_b1,
                            0,
                            1
                        )
                    } else {
                        db.addName(
                            wallpaper.id.toInt(),
                            wallpaper.nama_barang,
                            wallpaper.harga.toInt(),
                            wallpaper.image_b1,
                            wallpaper.promo.toInt(),
                            1
                        )
                    }
                    Toast.makeText(context, "Di tambahkan", Toast.LENGTH_SHORT).show()
                }
            }



            itemView.card.setOnClickListener {
                var i = Intent(context, DitailProducActivity::class.java)
                i.putExtra(
                    "image1",
                    RetrofitClient.BASE_URL + "assets/barang/" + wallpaper.image_b1
                )
                i.putExtra(
                    "image2",
                    RetrofitClient.BASE_URL + "assets/barang/" + wallpaper.image_b2
                )
                i.putExtra(
                    "image3",
                    RetrofitClient.BASE_URL + "assets/barang/" + wallpaper.image_b3
                )
                i.putExtra(
                    "image4",
                    RetrofitClient.BASE_URL + "assets/barang/" + wallpaper.image_b3
                )
                i.putExtra("nama_barang", wallpaper.nama_barang)
                i.putExtra("harga", wallpaper.harga)
                i.putExtra("promo", wallpaper.promo)
                i.putExtra("deskripsi", wallpaper.deskripsi)
                i.putExtra("id", wallpaper.id)
                i.putExtra("kategory", wallpaper.kategory)
                context.startActivity(i)
            }
        }
    }
}