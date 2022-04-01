package com.coupang.ads.bannerads

import android.app.Application
import com.coupang.ads.CoupangAds

class BannerAdsApplication: Application() {

    companion object {
        //Use your own affiliate id, sub id.
        const val AFFILIATE_ID = "AFSDKDEMO"
        const val SUB_ID = "BannarAdsDemo"
    }


    override fun onCreate() {
        super.onCreate()

        CoupangAds.init(context = this, AFFILIATE_ID, SUB_ID)
    }

}