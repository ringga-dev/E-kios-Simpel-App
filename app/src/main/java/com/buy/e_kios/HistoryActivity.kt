package com.buy.e_kios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_history.*
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.buy.e_kios.data.adapter.HistoryAdapter
import com.buy.e_kios.data.api.RetrofitClient
import com.buy.e_kios.data.models.transaksi.TransaksiItem
import com.buy.e_kios.db.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HistoryActivity : AppCompatActivity() {
    var nama: String? = null
    var data = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setupRecler()

        spener()
        spiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                nama = spiner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spiner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val str = parent.getItemAtPosition(pos)
                val myProfile = SharedPrefManager.getInstance(this@HistoryActivity)?.user
                RetrofitClient.instance.getPesanan(myProfile?.id!!, str.toString())
                    .enqueue(object : Callback<List<TransaksiItem>>{
                        override fun onResponse(
                            call: Call<List<TransaksiItem>>,
                            response: Response<List<TransaksiItem>>
                        ) {
                            rv_history.adapter?.let { adapter ->
                                if (adapter is HistoryAdapter) {
                                    adapter.setWallpapers(response.body()!!)
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<TransaksiItem>>, t: Throwable) {
                            Toast.makeText(this@HistoryActivity, t.message, Toast.LENGTH_SHORT).show()
                        }
                    })

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    private fun spener(){
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spiner.adapter = adapter
        }
    }

    private fun setupRecler() {
        rv_history.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            adapter = HistoryAdapter(mutableListOf(), this@HistoryActivity)

        }
    }

}