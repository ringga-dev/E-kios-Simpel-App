package com.buy.e_kios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.buy.e_kios.data.adapter.WallpaperAdapter
import com.buy.e_kios.data.api.RetrofitClient
import com.buy.e_kios.data.models.produc.Produc
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        setupRecler()


        RetrofitClient.instance.getNews().enqueue(object : Callback<List<Produc>>{
            override fun onResponse(call: Call<List<Produc>>, response: Response<List<Produc>>) {
                rv_produc.adapter?.let { adapter ->
                    if (adapter is WallpaperAdapter) {
                        adapter.setWallpapers(response.body()!!)
                    }
                }
            }

            override fun onFailure(call: Call<List<Produc>>, t: Throwable) {
                Toast.makeText(this@NewsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecler() {
        rv_produc.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = WallpaperAdapter(mutableListOf(), this@NewsActivity)

        }
    }
}