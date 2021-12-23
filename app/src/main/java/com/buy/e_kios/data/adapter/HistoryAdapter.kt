package com.buy.e_kios.data.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buy.e_kios.ActionHistoryActivity
import com.buy.e_kios.DitailProducActivity
import com.buy.e_kios.R
import com.buy.e_kios.data.api.RetrofitClient
import com.buy.e_kios.data.models.transaksi.TransaksiItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter(
    private var wallpaper: MutableList<TransaksiItem>,
    private var context: Context
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    fun setWallpapers(r: List<TransaksiItem>) {
        wallpaper.clear()
        wallpaper.addAll(r)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(wallpaper[position], context)

    override fun getItemCount() = wallpaper.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(wallpaper: TransaksiItem, context: Context) {
            Picasso.get().load(RetrofitClient.BASE_URL + "assets/barang/" + wallpaper.image_b1)
                .into(itemView.im_history)

            itemView.tv_stts.text = "Stts: " + wallpaper.stts
            itemView.tv_date.text = "Date: " + wallpaper.date
            itemView.tv_nama.text = "Produc: " + wallpaper.nama_barang
            itemView.tv_qty.text = "jumlah: " + wallpaper.qty.toString()
            val kurang = (wallpaper.harga * wallpaper.promo / 100) * wallpaper.qty

            itemView.tv_harga.text =
                "Pembayaran: " + "Rp " + (wallpaper.harga * wallpaper.qty - kurang)

            itemView.btn_action.setOnClickListener {
                var i = Intent(context, ActionHistoryActivity::class.java)
                i.putExtra("image", wallpaper.image_b1)
                i.putExtra("nama_barang", wallpaper.nama_barang)
                i.putExtra("harga", wallpaper.harga)
                i.putExtra("promo", wallpaper.promo)
                i.putExtra("date", wallpaper.date)
                i.putExtra("stts", wallpaper.stts)
                i.putExtra("id", wallpaper.id)
                i.putExtra("qty", wallpaper.qty)
                context.startActivity(i)
            }
        }

    }
}