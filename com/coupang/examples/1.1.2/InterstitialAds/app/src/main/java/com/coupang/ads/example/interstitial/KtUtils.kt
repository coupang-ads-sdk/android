package com.coupang.ads.example.interstitial

import androidx.lifecycle.LiveData

fun <T> LiveData<Result<T>>.getResult(): com.coupang.ads.java.Result<T> {
    return com.coupang.ads.java.Result(this.value!!)
}