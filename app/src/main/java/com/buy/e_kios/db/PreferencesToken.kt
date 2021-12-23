package com.buy.e_kios.db

import android.content.Context
import android.content.SharedPreferences

import android.preference.PreferenceManager


class PreferencesToken {

    companion object {
        val token = "user"

        private fun getSharedPreference(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

        fun setToken(context: Context, username: String?) {
            val editor = getSharedPreference(context).edit()
            editor.putString(token, username)
            editor.apply()
        }

        fun getToken(context: Context): String? {
            return getSharedPreference(context).getString(token, "")
        }


        fun ClearToken(context: Context) {
            val editor = getSharedPreference(context).edit()
            editor.remove(token)
            editor.apply()
        }

    }
}