package com.coupang.ads.bannerads

import android.app.Application
import com.coupang.ads.CoupangAds

class BannerAdsApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        //Use your own affiliate id, sub id, and widget id.
        CoupangAds.init(context = this, "AFSDKDEMO", "BannarAdsDemo")
    }

}