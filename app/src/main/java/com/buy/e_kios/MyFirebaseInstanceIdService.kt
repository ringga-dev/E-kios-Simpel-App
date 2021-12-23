package com.buy.e_kios

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.buy.e_kios.db.PreferencesToken
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService


class MyFirebaseInstanceIdService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        PreferencesToken.setToken(baseContext, token)
    }
}