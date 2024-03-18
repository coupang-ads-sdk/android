package com.coupang.tokenexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.coupang.ads.token.AdTokenRequester

class ActivityKt : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AdTokenRequester(this).getAdTokenInCallback {
            val token = it.getOrNull()?.token
            val exception = it.exceptionOrNull()
            if (!token.isNullOrBlank()) {
                // getToken Success
                Log.i("adToken", "get Coupang Ad Token success")
            } else if (exception != null) {
                // getToken failed with exception
                Log.e("adToken", "get Coupang Ad Token failed")
            } else {
                // getToken failed with unknown problem
                Log.e("adToken", "get Coupang Ad Token issue")
            }
        }
    }
}