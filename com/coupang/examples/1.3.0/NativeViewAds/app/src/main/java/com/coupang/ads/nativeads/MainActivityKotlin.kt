package com.coupang.ads.nativeads

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.coupang.ads.AdsContext
import com.coupang.ads.config.AdsCreativeSize
import com.coupang.ads.config.AdsMode
import com.coupang.ads.tools.adsViewModels
import com.coupang.ads.tools.createAdsViewModel
import com.coupang.ads.custom.AdsNativeView
import com.coupang.ads.custom.AdsPlacementLayout
import com.coupang.ads.custom.AdsProductLayout
import com.coupang.ads.viewmodels.AdsViewModel

class MainActivityKotlin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create AdsViewModel instance
        val nativeViewModel = createAdsViewModel<AdsViewModel>(
            "514017", //Use your own widget id.
            AdsCreativeSize._320x50,
            AdsMode.AUTO,
            "Home Page",  // optional，name of the app page.
            "Native Ads",  // optional, location of the ad.
            AdsContext.generateAnonId() // optional, user's puid, e.g. "puid@mail.com".
        )

        // Create an observer for the AdsViewModel to monitor the download of AD data.
        nativeViewModel.observe(this) {
            if (it.isSuccess) {
                Log.i("nativeObserver", "native ads download success")
                Toast.makeText(this, "native ads download success", Toast.LENGTH_SHORT).show()
            } else {
                Log.i("nativeObserver", "native ads download failed", it.exceptionOrNull())
                Toast.makeText(this, "native ads download failed", Toast.LENGTH_SHORT).show()
            }
        }
        // Call the loadAdData function to download the AD data.
        nativeViewModel.loadAdData()

        // init native view
        val nativeView = findViewById<AdsNativeView>(R.id.ads_native_view)
        val logo = findViewById<ImageView>(R.id.logo)
        val adsInfo = findViewById<ImageView>(R.id.opt_out)
        val mainImageView = findViewById<ImageView>(R.id.main_image)
        val title = findViewById<TextView>(R.id.title)
        val rocketBadge = findViewById<ImageView>(R.id.deliver)
        val rating = findViewById<AdsProductStarRating>(R.id.rating)
        val callToAction = findViewById<View>(R.id.call_to_action)
        val price = findViewById<TextView>(R.id.price)

        adsNativeView.adsPlacementLayout = AdsPlacementLayout.Builder()
            .setLogoView(logo)
            .setAdsInfoView(adsInfo)
            .setProductLayout(AdsProductLayout.Builder()
                .setTitleView(title)
                .setMainImageView(mainImageView)
                .setRocketBadgeView(rocketBadge)
                .setPriceView(price)
                .setRatingView(rating)
                .setCallToActionView(callToAction)
                .build())
            .build()

        // Bind AdsViewModel to Native View.
        nativeView.bindViewModel(this, nativeViewModel)
    }
}