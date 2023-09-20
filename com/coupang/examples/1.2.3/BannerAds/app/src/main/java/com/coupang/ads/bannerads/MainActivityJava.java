package com.coupang.ads.bannerads;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coupang.ads.AdsContext;
import com.coupang.ads.config.AdsCreativeSize;
import com.coupang.ads.config.AdsMode;
import com.coupang.ads.tools.ViewModelExtensionsKt;
import com.coupang.ads.view.banner.AdsBannerView;
import com.coupang.ads.viewmodels.AdsViewModel;

public class MainActivityJava extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		// create AdsViewModel instance
		AdsViewModel bannerViewModel = ViewModelExtensionsKt.createAdsViewModelJava(
				this,
				AdsViewModel.class,
				"514017", //Use your own widget id.
				AdsCreativeSize._320x50,
				AdsMode.AUTO,
				"Home Page",  // optional，name of the app page.
				"Bottom Banner",  // optional, location of the ad.
				AdsContext.Companion.generateAnonId() // optional, user's puid, e.g. "puid@mail.com".
		);

		// Create an observer for the AdsViewModel to monitor the download of AD data.
		bannerViewModel.observeJava(this, r -> {
			if (r.isSuccess()) {
				Log.i("bannerObserver", "banner ads download success");
				Toast.makeText(this, "banner ads download success", Toast.LENGTH_SHORT).show();
			} else {
				Log.i("bannerObserver", "banner ads download failed", r.exceptionOrNull());
				Toast.makeText(this, "banner ads download failed", Toast.LENGTH_SHORT).show();
			}
		});
		// Call the loadAdData function to download the AD data.
		bannerViewModel.loadAdData();


		// Bind AdsViewModel to Banner View.
		AdsBannerView bannerView = findViewById(R.id.ads_banner_view);
		bannerView.bindViewModel(this, bannerViewModel);
	}
}
