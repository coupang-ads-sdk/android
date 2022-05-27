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

class MainActivityKotlin : AppCompatActivity() {

    // Generate AdsViewModel in lazy way
    private val interstitialViewModel: AdsViewModel by adsViewModels(
        "514017", //Use your own widget id.
        AdsCreativeSize.INTERSTITIAL,
        AdsMode.AUTO,
        "Home Page",  // optional，name of the app page.
        "Interstitial"  // optional, location of the ad.
    )

    /**
     * Create Interstitial object.
     */
    private val interstitial by lazy {
        AdsInterstitial()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create an observer for the AdsViewModel to monitor the download of AD data.
        interstitialViewModel.observe(this) {
            if (it.isSuccess) {
                Log.i("interstitialObserver", "interstitial ads download success")
                Toast.makeText(this, "interstitial ads download success", Toast.LENGTH_SHORT).show()
            } else {
                Log.i("interstitialObserver", "interstitial ads download failed", it.exceptionOrNull())
                Toast.makeText(this, "interstitial ads download failed", Toast.LENGTH_SHORT).show()
            }
        }

        // Configure a listener for interstitial
        interstitial.listener = object : AdsInterstitialListener {
            override fun onAdDismissed() {
                Log.i("interstitial", "onAdDismissed")
                /*
                 * Call loadAdData() to pre-load the ads to be displayed next time. Without this, the same ad will be displayed when showAds() is called again with the same interstitial instance.
                 * Note: If your app won’t call showAds() again with the same instance, you should not call loadAdData() here though.
                 */
                interstitialViewModel.loadAdData()
            }

            override fun onAdShowed() {
                Log.i("interstitial", "onAdShowed")
            }

            override fun onAdFailedToShow(e: AdsException) {
                Log.e("interstitial", "onAdFailedToShow", e)
            }
        }

        // Bind AdsViewModel to Interstitial.
        interstitial.bindViewModel(interstitialViewModel)
        interstitialViewModel.loadAdData()


        // Show Interstitial when needed.
        val showInterstitialButton = findViewById<Button>(R.id.interstitial)
        showInterstitialButton.setOnClickListener {
            // Need to check if Interstitial is ready before displaying.
            if (interstitial.isAvailable()) {
                // Interstitial is ready to show.
                interstitial.showAds(this)
            } else {
                // Interstitial is not ready.
                Toast.makeText(this, "interstitial is not available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}