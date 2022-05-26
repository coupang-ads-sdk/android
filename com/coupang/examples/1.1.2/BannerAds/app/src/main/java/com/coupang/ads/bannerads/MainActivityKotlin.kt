package com.coupang.ads.bannerads

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.coupang.ads.config.AdsCreativeSize
import com.coupang.ads.config.AdsMode
import com.coupang.ads.tools.adsViewModels
import com.coupang.ads.tools.createAdsViewModel
import com.coupang.ads.view.banner.AdsBannerView
import com.coupang.ads.viewmodels.AdsViewModel

class MainActivityKotlin : AppCompatActivity() {

    /**
     * Generate AdsViewModel in lazy way, you can also use createAdsViewModel directly to generate AdsViewModel immediately
     * like:
     * override fun onCreate(savedInstanceState: Bundle?) {
     *      super.onCreate(savedInstanceState)
     *      val bannerViewModel = createAdsViewModel<AdsViewModel>(
     *          "514017", //Use your own widget id.
     *          AdsCreativeSize._320x50,
     *          AdsMode.AUTO,
     *          "Home Page",  // optional，name of the app page.
     *          "Bottom Banner"  // optional, location of the ad.
     *      )
     *  }
     */
    private val bannerViewModel: AdsViewModel by adsViewModels(
        "514017", //Use your own widget id.
        AdsCreativeSize._320x50,
        AdsMode.AUTO,
        "Home Page",  // optional，name of the app page.
        "Bottom Banner"  // optional, location of the ad.
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create an observer for the AdsViewModel to monitor the download of AD data.
        bannerViewModel.observe(this) {
            if (it.isSuccess) {
                Log.i("bannerObserver", "banner ads download success")
                Toast.makeText(this, "banner ads download success", Toast.LENGTH_SHORT).show()
            } else {
                Log.i("bannerObserver", "banner ads download failed", it.exceptionOrNull())
                Toast.makeText(this, "banner ads download failed", Toast.LENGTH_SHORT).show()
            }
        }
        // Call the loadAdData function to download the AD data.
        bannerViewModel.loadAdData()

        // Bind AdsViewModel to Banner View.
        val bannerView = findViewById<AdsBannerView>(R.id.ads_banner_view)
        bannerView.bindViewModel(this, bannerViewModel)
    }
}