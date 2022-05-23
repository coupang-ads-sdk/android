package com.coupang.ads.bannerads

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.coupang.ads.config.AdsCreativeSize
import com.coupang.ads.config.AdsMode
import com.coupang.ads.tools.adsViewModels
import com.coupang.ads.view.banner.AdsBannerView
import com.coupang.ads.viewmodels.AdsViewModel

class MainActivity : AppCompatActivity() {
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
        findViewById<AdsBannerView>(R.id.ads_banner_view).let {
            it.bindViewModel(
                this,
                bannerViewModel
            )
            bannerViewModel.loadAdData()
        }
    }
}