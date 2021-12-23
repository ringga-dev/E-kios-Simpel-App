package com.buy.e_kios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.buy.e_kios.data.api.RetrofitClient
import com.buy.e_kios.data.models.register.Register
import com.buy.e_kios.db.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        tv_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        register.setOnClickListener {
            val email = ed_email.text.toString().trim()
            val password = ed_pass.text.toString().trim()
            val fullname = ed_fullname.text.toString().trim()
            val alamat = ed_alamat.text.toString().trim()
            val phone = ed_nophone.text.toString().trim()

            if (email.isEmpty()) {
                ed_email.error = "Email required"
                ed_email.requestFocus()
                return@setOnClickListener
            }

            if (!cek_email(email)) {
                ed_email.error = "ini bukan format email"
                ed_email.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                ed_pass.error = "Password required"
                ed_pass.requestFocus()
                return@setOnClickListener
            }

            if (fullname.isEmpty()) {
                ed_fullname.error = "Password required"
                ed_fullname.requestFocus()
                return@setOnClickListener
            }

            if (alamat.isEmpty()) {
                ed_alamat.error = "Password required"
                ed_alamat.requestFocus()
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                ed_nophone.error = "Password required"
                ed_nophone.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.register(fullname,email, password,alamat,phone).enqueue(
                object : Callback<Register>{
                    override fun onResponse(call: Call<Register>, response: Response<Register>) {
                        Toast.makeText(applicationContext,response.body()?.msg.toString(), Toast.LENGTH_LONG).show()
                        if (response.body()?.stts ==true){
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Register>, t: Throwable) {
                        Toast.makeText(applicationContext,t.message, Toast.LENGTH_LONG).show()
                    }

                }
            )

        }
    }

    private fun cek_email(myEmail: String): Boolean {
        val emailRegex = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return emailRegex.matcher(myEmail).matches()
    }

    override fun onStart() {
        super.onStart()

        if(SharedPrefManager.getInstance(this)!!.isLoggedIn){
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}