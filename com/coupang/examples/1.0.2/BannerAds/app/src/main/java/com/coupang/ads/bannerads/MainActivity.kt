package com.coupang.ads.bannerads

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.coupang.ads.config.AdSize
import com.coupang.ads.impl.AdListener
import com.coupang.ads.view.banner.AdsBannerView

class MainActivity : AppCompatActivity() {

    val affiliatePage = "Home Page"  // optional，name of the app page.
    val affiliatePlacement = "Bottom Banner"  // optional, location of the ad.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<AdsBannerView>(R.id.ads_banner_view).let {
            it.setAdSize(AdSize.BANNER_320X50)
            it.setAdListener(object : AdListener {
                override fun onAdClicked() {
                    Log.d("ads", "onAdClicked")
                }

                override fun onAdFailedToLoad(errorMessage: String?) {
                    Log.d("ads", "onAdFailedToLoad:$errorMessage")
                }

                override fun onAdLoaded() {
                    Log.d("ads", "onAdLoaded")
                }

            })
            it.loadAdData(514017, affiliatePage, affiliatePlacement)
        }
    }
}