package com.buy.e_kios

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.buy.e_kios.data.api.RetrofitClient
import com.buy.e_kios.data.models.login.Login
import com.buy.e_kios.data.models.register.Register
import com.buy.e_kios.db.PreferencesToken
import com.buy.e_kios.db.SharedPrefManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern.compile

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tv_register.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        tv_login.setOnClickListener {
            startActivity(Intent(this, LoginSMSActivity::class.java))
            finish()
        }

        btn_login.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_pass.text.toString().trim()

            if (email.isEmpty()) {
                et_email.error = "Email required"
                et_email.requestFocus()
                return@setOnClickListener
            }
            if (!cek_email(email)) {
                et_email.error = "ini bukan format email"
                et_email.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                et_pass.error = "Password required"
                et_pass.requestFocus()
                return@setOnClickListener
            }
            RetrofitClient.instance.login(email, password)
                .enqueue(object : Callback<Login> {
                    override fun onFailure(call: Call<Login>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<Login>,
                        response: Response<Login>
                    ) {
                        if (response.body()?.stts == true) {
                            response.body()?.data?.let { it1 ->
                                SharedPrefManager.getInstance(applicationContext)!!
                                    .saveUser(response.body()?.data!!)

                                saveToken(response.body()?.data!!.id)

                                val intent = Intent(applicationContext, HomeActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                response.body()?.msg.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                })


        }
    }

    private fun tokenCek(): String? {
        var tokencek: String? = null
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            tokencek = task.result
        })

        return tokencek
    }

    private fun saveToken(id: Int) {
        val token = PreferencesToken.getToken(this).toString()

        RetrofitClient.instance.saveToken(id, token).enqueue(
            object : Callback<Register> {
                override fun onResponse(call: Call<Register>, response: Response<Register>) {
                    Toast.makeText(
                        applicationContext,
                        response.code().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onFailure(call: Call<Register>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

            }
        )

    }

    private fun cek_email(myEmail: String): Boolean {
        val emailRegex = compile(
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

        if (SharedPrefManager.getInstance(this)!!.isLoggedIn) {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}