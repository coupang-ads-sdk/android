package com.coupang.tokenexample;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coupang.ads.token.AdTokenRequester;
import com.coupang.ads.token.AdTokenResponse;

public class ActivityJava extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AdTokenRequester requester = new AdTokenRequester(this);
		requester.asyncGetAdToken(adTokenResponseResult -> {
			AdTokenResponse adTokenResponse = adTokenResponseResult.getOrNull();
			String token = adTokenResponse == null ? null : adTokenResponse.getToken();
			Throwable exception = adTokenResponseResult.exceptionOrNull();
			if (token != null && !token.isEmpty()) {
				// getToken Success
				Log.i("adToken", "get Coupang Ad Token success");
			} else if (exception != null) {
				// getToken failed with exception
				Log.e("adToken", "get Coupang Ad Token failed");
			} else {
				// getToken failed with unknown issue
				Log.e("adToken", "get Coupang Ad Token failed");
			}
			return null;
		});
	}
}
