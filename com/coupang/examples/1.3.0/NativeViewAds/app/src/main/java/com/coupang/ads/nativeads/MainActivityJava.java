package com.coupang.ads.nativeads;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coupang.ads.AdsContext;
import com.coupang.ads.config.AdsCreativeSize;
import com.coupang.ads.config.AdsMode;
import com.coupang.ads.custom.widget.AdsProductStarRating;
import com.coupang.ads.nativeViewAds.R;
import com.coupang.ads.tools.ViewModelExtensionsKt;
import com.coupang.ads.custom.AdsNativeView;
import com.coupang.ads.custom.AdsPlacementLayout;
import com.coupang.ads.custom.AdsProductLayout;
import com.coupang.ads.viewmodels.AdsViewModel;

public class MainActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // create AdsViewModel instance
        AdsViewModel nativeViewModel = ViewModelExtensionsKt.createAdsViewModelJava(
                this,
                AdsViewModel.class,
                "514017", //Use your own widget id.
                AdsCreativeSize.NATIVE,
                AdsMode.AUTO,
                "Home Page",  // optional，name of the app page.
                "Native Ads",  // optional, location of the ad.
                AdsContext.Companion.generateAnonId() // optional, user's puid, e.g. "puid@mail.com".
        );

        // Create an observer for the AdsViewModel to monitor the download of AD data.
        nativeViewModel.observeJava(this, r -> {
            if (r.isSuccess()) {
                Log.i("nativeObserver", "native ads download success");
                Toast.makeText(this, "native ads download success", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("nativeObserver", "native ads download failed", r.exceptionOrNull());
                Toast.makeText(this, "native ads download failed", Toast.LENGTH_SHORT).show();
            }
        });
        // Call the loadAdData function to download the AD data.
        nativeViewModel.loadAdData();

        // init native view
        AdsNativeView nativeView = findViewById(R.id.ads_native_view);
        ImageView logo = findViewById(R.id.logo);
        ImageView adsInfo = findViewById(R.id.opt_out);
        ImageView mainImageView = findViewById(R.id.main_image);
        TextView title = findViewById(R.id.title);
        ImageView rocketBadge = findViewById(R.id.deliver);
        AdsProductStarRating rating = findViewById(R.id.rating);
        View callToAction = findViewById(R.id.call_to_action);
        TextView price = findViewById(R.id.price);
        nativeView.setAdsPlacementLayout(new AdsPlacementLayout.Builder()
                .setLogoView(logo)
                .setAdsInfoView(adsInfo)
                .setProductLayout(new AdsProductLayout.Builder()
                        .setTitleView(title)
                        .setMainImageView(mainImageView)
                        .setRocketBadgeView(rocketBadge)
                        .setPriceView(price)
                        .setRatingView(rating)
                        .setCallToActionView(callToAction)
                        .build())
                .build());

        // Bind AdsViewModel to Native View.
        nativeView.bindViewModel(this, nativeViewModel);
    }
}
