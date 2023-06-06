package com.example.andmoduleads;

import androidx.lifecycle.MutableLiveData;

import com.ads.control.ads.wrapper.ApInterstitialAd;
import com.ads.control.ads.wrapper.ApNativeAd;

public class StorageCommon {
    public MutableLiveData<ApNativeAd> nativeAdsLanguage = new MutableLiveData<>();
    public ApInterstitialAd interPriority;
    public ApInterstitialAd interNormal;

    public ApNativeAd nativeAdHigh;
    public ApNativeAd nativeAdMedium;
    public ApNativeAd nativeAdNormal;
}
