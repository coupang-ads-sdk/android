package com.coupang.ads.example.interstitial;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coupang.ads.AdsException;
import com.coupang.ads.config.AdsCreativeSize;
import com.coupang.ads.config.AdsMode;
import com.coupang.ads.interstitial.AdsInterstitial;
import com.coupang.ads.interstitial.AdsInterstitialListener;
import com.coupang.ads.interstitial.AdsViewType;
import com.coupang.ads.java.Result;
import com.coupang.ads.tools.ViewModelExtensionsKt;
import com.coupang.ads.viewmodels.AdsViewModel;

import org.jetbrains.annotations.NotNull;

import kotlin.Lazy;

public class MainActivityJava extends AppCompatActivity {


	/**
	 * Generate AdsViewModel in lazy way, you can also use createAdsViewModel directly to generate AdsViewModel immediately
	 * like:
	 * 	@Override
	 * 	protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
	 *		super.onCreate(savedInstanceState);
	 * 		AdsViewModel interstitialViewModel = ViewModelExtensionsKt.createAdsViewModelJava(
	 *      	this,
	 *     		AdsViewModel.class,
	 *     		"514017", //Use your own widget id.
	 *     		AdsCreativeSize.INTERSTITIAL,
	 *     		AdsMode.AUTO,
	 *     		"Home Page",  // optional，name of the app page.
	 *     		"Interstitial"  // optional, location of the ad.
	 * 		);
	 * 	}
	 */
	private final Lazy<AdsViewModel> interstitialViewModelLazy = ViewModelExtensionsKt.adsViewModelsLazyJava(
			this,
			AdsViewModel.class,
			"514017", //Use your own widget id.
			AdsCreativeSize.INTERSTITIAL,
			AdsMode.AUTO,
			"Home Page",  // optional，name of the app page.
			"Interstitial"  // optional, location of the ad.
	);

	/**
	 * Create Interstitial object.
	 */
	private final AdsInterstitial interstitial = new AdsInterstitial();

	@Override
	protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// Create an observer for the AdsViewModel to monitor the download of AD data.
		interstitialViewModelLazy.getValue().observe(this, result -> {
			// Use Kotlin code KtUtilsKt.getResult() instead of `result` object
			// This because there is an issue with kotlin inline class. We will support no kotlin code API in future.
			Result<?> r = KtUtilsKt.getResult(interstitialViewModelLazy.getValue().getDataResult());
			if (r.isSuccess()) {
				Log.i("interstitialObserver", "interstitial ads download success");
				Toast.makeText(this, "interstitial ads download success", Toast.LENGTH_SHORT).show();
			} else {
				Log.i("interstitialObserver", "interstitial ads download failed", r.exceptionOrNull());
				Toast.makeText(this, "interstitial ads download failed", Toast.LENGTH_SHORT).show();
			}
		});

		// Configure a listener for interstitial
		interstitial.setListener(new AdsInterstitialListener() {
			@Override
			public void onAdDismissed() {
				Log.i("interstitial", "onAdDismissed");
				/*
				 * Call loadAdData() to pre-load the ads to be displayed next time. Without this, the same ad will be displayed when showAds() is called again with the same interstitial instance.
				 * Note: If your app won’t call showAds() again with the same instance, you should not call loadAdData() here though.
				 */
				interstitialViewModelLazy.getValue().loadAdData();
			}

			@Override
			public void onAdShowed() {
				Log.i("interstitial", "onAdShowed");
			}

			@Override
			public void onAdFailedToShow(@NotNull AdsException e) {
				Log.e("interstitial", "onAdFailedToShow", e);
			}

			@Override
			public boolean onClick(@NotNull AdsViewType adsViewType) {
				// it should always return false.
				return false;
			}
		});

		// Bind AdsViewModel to Interstitial.
		interstitial.bindViewModel(interstitialViewModelLazy.getValue());
		interstitialViewModelLazy.getValue().loadAdData();

		// Show Interstitial when needed.
		Button showInterstitialButton = findViewById(R.id.interstitial);
		showInterstitialButton.setOnClickListener(v -> {
			// Need to check if Interstitial is ready before displaying.
			if (interstitial.isAvailable()) {
				// Interstitial is ready to show.
				interstitial.showAds(this);
			} else {
				// Interstitial is not ready.
				Toast.makeText(this, "interstitial is not available", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
