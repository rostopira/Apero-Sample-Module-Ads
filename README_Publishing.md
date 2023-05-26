
# Native Language
Create a variable in the MyApplication to store the value of ad native
~~~
public MutableLiveData<ApNativeAd> nativeAdsLanguage = new MutableLiveData<>();
~~~
In case of loading 2 id ( high, normal )
~~~
AperoAd.getInstance().loadNativePrioritySameTime(
    this,
    BuildConfig.ad_native_priority,
    BuildConfig.ad_native,
    R.layout.view_native_ads_language_first,
    new AperoAdCallback() {
        @Override
        public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
            super.onNativeAdLoaded(nativeAd);
            MyApplication.getApplication().getStorageCommon()
                    .nativeAdsLanguage.postValue(nativeAd);
        }

        @Override
        public void onAdFailedToLoad(@Nullable ApAdError adError) {
            super.onAdFailedToLoad(adError);
            MyApplication.getApplication().getStorageCommon()
                    .nativeAdsLanguage.postValue(null);
        }
    }
);
~~~

In case of loading 3 id ( high, medium, normal )
~~~
AperoAd.getInstance().loadNative3SameTime(
    this,
    BuildConfig.ad_native_priority,
    BuildConfig.ad_native_medium,
    BuildConfig.ad_native,
    R.layout.custom_native_ads_language_first,
    new AperoAdCallback() {
        @Override
        public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
            super.onNativeAdLoaded(nativeAd);
            MyApplication.getApplication().getStorageCommon()
                    .nativeAdsLanguage.postValue(nativeAd);
        }

        @Override
        public void onAdFailedToLoad(@Nullable ApAdError adError) {
            super.onAdFailedToLoad(adError);
            MyApplication.getApplication().getStorageCommon()
                    .nativeAdsLanguage.postValue(null);
        }
    }
);
~~~

Show ads native
~~~
MyApplication.getApplication().getStorageCommon().nativeAdsLanguage.observe(this, apNativeAd -> {
    if (apNativeAd != null) {
        AperoAd.getInstance().populateNativeAdView(
                LanguageFirstOpenActivity.this,
                apNativeAd,
                binding.layoutAdNative,
                binding.layoutShimmer.shimmerContainerNative
        );
    } else {
        binding.layoutAdNative.setVisibility(View.GONE);
    }
});
~~~

Layout native custom - [Click Here](https://github.com/AperoVN/Apero-Sample-Module-Ads/blob/develop/app/src/main/res/layout/custom_native_ads_language_first.xml)

Layout native loading - [Click Here](https://github.com/AperoVN/Apero-Sample-Module-Ads/blob/develop/app/src/main/res/layout/layout_loading_ads_native.xml)

SplashActivity ( load ad ) - [Click Here](https://github.com/AperoVN/Apero-Sample-Module-Ads/blob/develop/app/src/main/java/com/example/andmoduleads/activity/SplashActivity.java)

LanguageFirstOpenActivity ( show ad ) - [Click Here](https://github.com/AperoVN/Apero-Sample-Module-Ads/blob/develop/app/src/main/java/com/example/andmoduleads/activity/LanguageFirstOpenActivity.java)


# Inter Splash
_* Case 2 id ( high, normal )_
~~~
// Load ad

AperoAd.getInstance().loadSplashInterPrioritySameTime(
    this,
    BuildConfig.inter_splash_high_floor,
    interId,
    ADS_LOADING_TIMEOUT,
    TIME_DELAY,
    false,
    object : AperoAdCallback() {
        override fun onNextAction() {
            super.onNextAction()
            if (isFinished || isOnStop) {
                return
            }
            typeShowAds = INTER_HIGH_FLOOR
            startMain()
        }

        override fun onAdSplashReady() {
            super.onAdSplashReady()
            if (isFinished) {
                return
            }
            typeShowAds = INTER_HIGH_FLOOR
            showAdsSplash()
        }

        override fun onNormalInterSplashLoaded() {
            super.onNormalInterSplashLoaded()
            if (isFinished) {
                return
            }
            typeShowAds = INTER_NORMAL
            showAdsSplash()
        }
    }
)

// Show ad

* TYPE = INTER_HIGH_FLOOR
AperoAd.getInstance().onShowSplashPriority(
    this,
    object : AperoAdCallback() {
        override fun onNextAction() {
            super.onNextAction()
            if (isFinished || isOnStop) {
                return
            }
            startMain()
        }

        override fun onAdFailedToShow(adError: ApAdError?) {
            super.onAdFailedToShow(adError)
            if (isFinished || isOnStop) {
                return
            }
            startMain()
        }
    })

* TYPE = INTER_NORMAL
AperoAd.getInstance().onShowSplash(this@SplashActivity, AperoAdCallback(){
    override fun onNextAction() {
            super.onNextAction()
            if (!isDestroyed) {
                startMain()
            }
        }
})

// when hide app -> reopen app will be loaded forever, we will use this function in onResume - First time opening the app ignore this function

* TYPE = INTER_HIGH_FLOOR
AperoAd.getInstance().onCheckShowSplashPriorityWhenFail(
    this,
    object : AperoAdCallback() {
        override fun onNextAction() {
            super.onNextAction()
            if (isFinished) {
                return
            }
            startMain()
        }

        override fun onAdFailedToShow(adError: ApAdError?) {
            super.onAdFailedToShow(adError)
            if (isFinished) {
                return
            }
            startMain()
        }
    },
    1000
)

* TYPE = INTER_NORMAL
AperoAd.getInstance().onCheckShowSplashWhenFail(this, AperoAdCallback(){
    override fun onNextAction() {
            super.onNextAction()
            if (!isDestroyed) {
                startMain()
            }
        }
}, 1000)
~~~

_* Case 3 id ( high, medium, normal )_

~~~
// Load ad
AperoAd.getInstance().loadSplashInterPriority3SameTime(this,
    BuildConfig.ads_inter_priority,
    BuildConfig.ads_inter_medium,
    BuildConfig.ad_interstitial_splash,
    30000,
    3000,
    false,
    new AperoAdCallback() {
        @Override
        public void onAdSplashPriorityReady() {
            super.onAdSplashPriorityReady();
            showInterSplash3();
        }

        @Override
        public void onAdSplashPriorityMediumReady() {
            super.onAdSplashPriorityMediumReady();
            showInterSplash3();
        }

        @Override
        public void onAdSplashReady() {
            super.onAdSplashReady();
            showInterSplash3();
        }

        @Override
        public void onNextAction() {
            super.onNextAction();
            startMain();
        }
    });

// Show ad
AperoAd.getInstance().onShowSplashPriority3(SplashActivity.this, new AperoAdCallback() {
    @Override
    public void onAdClosed() {
        super.onAdClosed();
        startMain();
    }

    @Override
    public void onAdPriorityFailedToShow(@Nullable ApAdError adError) {
        super.onAdPriorityFailedToShow(adError);
    }

    @Override
    public void onAdPriorityMediumFailedToShow(@Nullable ApAdError adError) {
        super.onAdPriorityMediumFailedToShow(adError);
    }

    @Override
    public void onAdFailedToShow(@Nullable ApAdError adError) {
        super.onAdFailedToShow(adError);
    }

    @Override
    public void onNextAction() {
        super.onNextAction();
    }
});

// when hide app -> reopen app will be loaded forever, we will use this function in onResume - First time opening the app ignore this function
AperoAd.getInstance().onCheckShowSplashPriority3WhenFail(this, new AperoAdCallback() {
    @Override
    public void onNextAction() {
        super.onNextAction();
        if (isFinishing()) {
            return;
        }
        startMain();

    }
}, 1000);
~~~

SplashActivity - [Click Here](https://github.com/AperoVN/Apero-Sample-Module-Ads/blob/develop/app/src/main/java/com/example/andmoduleads/activity/SplashActivity.java)

# NOTE
* This is just an example of how to load and display the ad, the handling of the callback from AperoAdCallback will depend on the case
* In the sample application we have specifically implemented, you can get the project and refer to it





