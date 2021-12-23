package com.buy.e_kios.db

import android.content.Context
import android.content.SharedPreferences
import com.buy.e_kios.data.models.login.Data


class SharedPrefManager private constructor(mCtx: Context) {
    private val mCtx: Context
    fun saveUser(user: Data) {
        val sharedPreferences: SharedPreferences = mCtx.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putInt("id", user.id)
        editor.putString("fullname", user.fullname)
        editor.putString("email", user.email)
        editor.putString("alamat", user.alamat)
        editor.putString("image", user.image)
        editor.putString("no_phone", user.no_phone)
        editor.putString("stts", user.stts)
        editor.apply()
    }

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences: SharedPreferences = mCtx.getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
            return sharedPreferences.getInt("id", -1) != -1
        }

    val user: Data
        get() {
            val sharedPreferences: SharedPreferences = mCtx.getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
            return Data(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("email", null)!!,
                sharedPreferences.getString("fullname", null)!!,
                sharedPreferences.getString("alamat", null)!!,
                sharedPreferences.getString("image", null)!!,
                sharedPreferences.getString("no_phone", null)!!,
                sharedPreferences.getString("stts", null)!!,
            )
        }

    fun clear() {
        val sharedPreferences: SharedPreferences = mCtx.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager? {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

    init {
        this.mCtx = mCtx
    }
}