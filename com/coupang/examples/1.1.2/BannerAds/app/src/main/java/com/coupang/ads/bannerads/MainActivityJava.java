package com.coupang.ads.bannerads;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.coupang.ads.config.AdsCreativeSize;
import com.coupang.ads.config.AdsMode;
import com.coupang.ads.java.Result;
import com.coupang.ads.tools.ViewModelExtensionsKt;
import com.coupang.ads.view.banner.AdsBannerView;
import com.coupang.ads.viewmodels.AdsViewModel;

import kotlin.Lazy;

public class MainActivityJava extends AppCompatActivity {

	/**
	 * Generate AdsViewModel in lazy way, you can also use createAdsViewModel directly to generate AdsViewModel immediately
	 * like:
	 *
	 *  @Override
	 *	protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
	 *			super.onCreate(savedInstanceState);
	 * 			AdsViewModel adsViewModel = ViewModelExtensionsKt.createAdsViewModelJava(
	 * 				this,
	 * 				AdsViewModel.class,
	 * 				"514017", //Use your own widget id.
	 * 				AdsCreativeSize._320x50,
	 * 				AdsMode.AUTO,
	 * 				"Home Page",  // optional，name of the app page.
	 * 				"Bottom Banner"  // optional, location of the ad.
	 * 		);
	 */
	final private Lazy<AdsViewModel> bannerViewModelLazy = ViewModelExtensionsKt.adsViewModelsLazyJava(
			this,
			AdsViewModel.class,
			"514017", //Use your own widget id.
			AdsCreativeSize._320x50,
			AdsMode.AUTO,
			"Home Page",  // optional，name of the app page.
			"Bottom Banner"  // optional, location of the ad.
	);


	@Override
	protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// Create an observer for the AdsViewModel to monitor the download of AD data.
		bannerViewModelLazy.getValue().observe(this, result -> {
			// Use Kotlin code KtUtilsKt.getResult() instead of `result` object
			// This because there is an issue in kotlin inline class. We will support no kotlin code API in future.
			Result<?> r = KtUtilsKt.getResult(bannerViewModelLazy.getValue().getDataResult());
			if (r.isSuccess()) {
				Log.i("bannerObserver", "banner ads download success");
				Toast.makeText(MainActivityJava.this, "banner ads download success", Toast.LENGTH_SHORT).show();
			} else {
				Log.i("bannerObserver", "banner ads download failed", r.exceptionOrNull());
				Toast.makeText(MainActivityJava.this, "banner ads download failed", Toast.LENGTH_SHORT).show();
			}
		});
		// Call the loadAdData function to download the AD data.
		bannerViewModelLazy.getValue().loadAdData();


		// Bind AdsViewModel to Banner View.
		AdsBannerView bannerView = findViewById(R.id.ads_banner_view);
		bannerView.bindViewModel(this, bannerViewModelLazy.getValue());
	}
}
