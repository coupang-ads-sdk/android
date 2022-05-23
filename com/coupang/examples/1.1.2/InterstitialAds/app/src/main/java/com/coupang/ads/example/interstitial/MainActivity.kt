package com.coupang.ads.example.interstitial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.coupang.ads.AdsException
import com.coupang.ads.config.AdsCreativeSize
import com.coupang.ads.config.AdsMode
import com.coupang.ads.interstitial.AdsInterstitial
import com.coupang.ads.interstitial.AdsInterstitialListener
import com.coupang.ads.tools.adsViewModels
import com.coupang.ads.viewmodels.AdsViewModel

class MainActivity : AppCompatActivity() {
    private val interstitialViewModel: AdsViewModel by adsViewModels(
        "514017", //Use your own widget id.
        AdsCreativeSize.INTERSTITIAL,
        AdsMode.AUTO,
        "Home Page",  // optional，name of the app page.
        "Interstitial"  // optional, location of the ad.
    )

    private val interstitial by lazy {
        AdsInterstitial().also {
            it.listener = object : AdsInterstitialListener {
                override fun onAdDismissed() {
                    Log.i("interstitial", "onAdDismissed")
                    it.viewModel?.loadAdData()
                }

                override fun onAdShowed() {
                    Log.i("interstitial", "onAdShowed")
                }

                override fun onAdFailedToShow(e: AdsException) {
                    Log.e("interstitial", "onAdFailedToShow", e)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        interstitial.bindViewModel(interstitialViewModel)
        interstitialViewModel.loadAdData()
        findViewById<Button>(R.id.interstitial).setOnClickListener {
            if (interstitial.isAvailable()) {
                interstitial.showAds(this)
            } else {
                Toast.makeText(this, "interstitial is not available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}