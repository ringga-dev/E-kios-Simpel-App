package com.buy.e_kios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.buy.e_kios.data.api.RetrofitClient
import com.buy.e_kios.db.SharedPrefManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val myProfile = SharedPrefManager.getInstance(this)?.user
        Picasso.get().load(RetrofitClient.BASE_URL + "assets/profile/" + myProfile?.image)
            .into(image_user)
        tv_nama.text = myProfile?.fullname
        tv_no_pengenal.text = myProfile?.email
        no_phone.text = myProfile?.no_phone
        tv_alamat.text = myProfile?.alamat

        btn_logout.setOnClickListener {
            SharedPrefManager.getInstance(this)!!.clear()
            startActivity(Intent(baseContext, LoginActivity::class.java))
            finish()
        }
    }
}