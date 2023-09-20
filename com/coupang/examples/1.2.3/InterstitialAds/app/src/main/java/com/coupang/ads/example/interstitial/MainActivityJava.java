package com.coupang.ads.example.interstitial;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coupang.ads.AdsContext;
import com.coupang.ads.AdsException;
import com.coupang.ads.config.AdsCreativeSize;
import com.coupang.ads.config.AdsMode;
import com.coupang.ads.config.AdsViewType;
import com.coupang.ads.interstitial.AdsInterstitial;
import com.coupang.ads.interstitial.AdsInterstitialListener;
import com.coupang.ads.viewmodels.AdsRequest;
import com.coupang.ads.viewmodels.AdsViewModel;

import org.jetbrains.annotations.NotNull;

public class MainActivityJava extends AppCompatActivity {

	// crete AdsViewModel
	private final AdsViewModel interstitialViewModel = new AdsViewModel(
			new AdsRequest(
					"514017", //Use your own widget id.
					AdsCreativeSize.INTERSTITIAL,
					AdsMode.AUTO,
					"Home Page",  // optional，name of the app page.
					"Interstitial",  // optional, location of the ad.
					AdsContext.Companion.generateAnonId() // optional, user's puid, e.g. "puid@mail.com".
			)
	);

	// Create Interstitial object.
	private final AdsInterstitial interstitial = new AdsInterstitial();

	@Override
	protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create an observer for the AdsViewModel to monitor the download of AD data.
		interstitialViewModel.observeJava(this, r -> {
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
				interstitialViewModel.loadAdData();
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
		interstitial.bindViewModel(interstitialViewModel);
		interstitialViewModel.loadAdData();

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
