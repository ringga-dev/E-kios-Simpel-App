package com.buy.e_kios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.buy.e_kios.data.adapter.WallpaperAdapter
import com.buy.e_kios.data.api.RetrofitClient
import com.buy.e_kios.data.models.produc.Produc
import com.buy.e_kios.db.SharedPrefManager
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupRecler()


        ic_keranjang.setOnClickListener {
            startActivity(Intent(this, KeranjangActivity::class.java))
        }


        ic_history.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        ic_news.setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
        }

        ic_profile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        RetrofitClient.instance.getProduc().enqueue(
            object : Callback<List<Produc>> {
                override fun onResponse(
                    call: Call<List<Produc>>,
                    response: Response<List<Produc>>
                ) {
                    rv_produc.adapter?.let { adapter ->
                        if (adapter is WallpaperAdapter) {
                            adapter.setWallpapers(response.body()!!)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Produc>>, t: Throwable) {
                    Toast.makeText(this@HomeActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }


    private fun setupRecler() {
        rv_produc.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = WallpaperAdapter(mutableListOf(), this@HomeActivity)

        }
    }


    override fun onStart() {
        super.onStart()

        if (!SharedPrefManager.getInstance(this)!!.isLoggedIn) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}