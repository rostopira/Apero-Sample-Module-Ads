package com.example.andmoduleads.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ads.control.admob.AppOpenManager;
import com.ads.control.ads.AperoAd;
import com.ads.control.ads.AperoAdCallback;
import com.ads.control.ads.AperoInitCallback;
import com.ads.control.ads.wrapper.ApAdError;
import com.ads.control.ads.wrapper.ApInterstitialAd;
import com.ads.control.ads.wrapper.ApNativeAd;
import com.ads.control.billing.AppPurchase;
import com.ads.control.config.AperoAdConfig;
import com.ads.control.funtion.AdCallback;
import com.ads.control.funtion.BillingListener;
import com.example.andmoduleads.BuildConfig;
import com.example.andmoduleads.MyApplication;
import com.example.andmoduleads.R;
import com.example.andmoduleads.SharePreferenceUtils;
import com.example.andmoduleads.utils.Constant;
import com.example.andmoduleads.utils.NetworkUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.ltl.apero.languageopen.Language;
import com.ltl.apero.languageopen.LanguageFirstOpen;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private List<String> list = new ArrayList<>();
    private String idAdSplash;
    private boolean isFirstRunApp = true;
    private final int TYPE_INTER_HIGH = 0;
    private final int TYPE_INTER_MEDIUM = 1;
    private final int TYPE_INTER_NORMAL = 2;
    private int typeShowAd = TYPE_INTER_HIGH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        reproduceCrash();
    }

    private void loadNativeAdsFirstLanguageOpen() {
        if (MyApplication.getApplication().getStorageCommon().nativeAdsLanguage.getValue() == null
                && !AppPurchase.getInstance().isPurchased()) {
            loadNativeLanguageHigh();
            /*use native medium then use this function*/
            /*loadNativeLanguage3();*/
        }
    }

    private void reproduceCrash() {
        AppOpenManager.getInstance().setSplashAdId("ca-app-pub-3940256099942544/3419835294");
        AppOpenManager.getInstance().setSplashAdId("ca-app-pub-3940256099942544/3419835294");
        AperoAd.getInstance().loadAppOpenSplash3SameTime(
            this,
    "ca-app-pub-3940256099942544/1033173712",
            3000L,
            0L,
            true,
            adCallback
        );
    }

    private void loadNativeLanguageHigh(){
        AperoAd.getInstance().loadNativePrioritySameTime(
                this,
                BuildConfig.ad_native_priority,
                BuildConfig.ad_native,
                com.ltl.apero.languageopen.R.layout.view_native_ads_language_first,
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
    }

    private void loadNativeLanguage3() {
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
    }

    private void testCase() {
        findViewById(R.id.txtLoading).setOnClickListener(v -> startMain());
    }

    private void loadAndShowOpenAppSplash() {
        AppOpenManager.getInstance().setSplashAdId(BuildConfig.ads_open_app);
        AperoAd.getInstance().loadAppOpenSplashSameTime(this, BuildConfig.ad_interstitial_splash, 30000, 3000, false, new AperoAdCallback() {
            @Override
            public void onAdSplashReady() {
                super.onAdSplashReady();
                //App open ads ready
                AppOpenManager.getInstance().showAppOpenSplash(SplashActivity.this, new AdCallback() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        startMain();
                    }

                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        startMain();
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable AdError adError) {
                        super.onAdFailedToShow(adError);
                        startMain();
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@Nullable ApAdError adError) {
                super.onAdFailedToLoad(adError);
                startMain();
            }

            @Override
            public void onNextAction() {
                super.onNextAction();
                startMain();
            }

            @Override
            public void onInterstitialLoad(@Nullable ApInterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                //App open ads failed, inter ads loaded
                AperoAd.getInstance().onShowSplash(SplashActivity.this, new AperoAdCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        startMain();
                    }
                });
            }
        });
    }

    private void loadInterSplash3(){
        String remote = Constant.SAMETIME; /*sample*/
        switch (remote){
            case (Constant.SAMETIME):
                loadInterSplash3Sametime();
                break;
            case (Constant.ALTERNATE):
                loadInterSplash3Alternate();
                break;
            default:
                /*if request is only inter splash high then just call this function*/
                loadAndShowInterPriority();
                break;
        }
    }

    private void loadInterSplash3Sametime(){
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
                        Log.i(TAG, "onAdSplashHighFloorReady: ");
                        typeShowAd = TYPE_INTER_MEDIUM;
                        showInterSplash3();
                    }

                    @Override
                    public void onAdPriorityFailedToLoad(@Nullable ApAdError adError) {
                        super.onAdPriorityFailedToLoad(adError);
                        Log.e(TAG, "onAdHighFloorFailedToLoad: " + adError);
                    }

                    @Override
                    public void onAdSplashPriorityMediumReady() {
                        super.onAdSplashPriorityMediumReady();
                        Log.i(TAG, "onAdSplashHighMediumReady: ");
                        typeShowAd = TYPE_INTER_MEDIUM;
                        showInterSplash3();
                    }

                    @Override
                    public void onAdPriorityMediumFailedToLoad(@Nullable ApAdError adError) {
                        super.onAdPriorityMediumFailedToLoad(adError);
                        Log.i(TAG, "onAdMediumFailedToLoad: " + adError);
                    }

                    @Override
                    public void onAdSplashReady() {
                        super.onAdSplashReady();
                        Log.i(TAG, "onAdSplashReady: ");
                        typeShowAd = TYPE_INTER_MEDIUM;
                        showInterSplash3();
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable ApAdError adError) {
                        super.onAdFailedToLoad(adError);
                        Log.e(TAG, "onAdFailedToLoad: ");
                    }

                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        startMain();
                        Log.i(TAG, "onNextAction:1 ");
                    }
                });
    }

    private void loadInterSplash3Alternate(){
        AperoAd.getInstance().loadSplashInterPriority3Alternate(this,
                BuildConfig.ads_inter_priority,
                BuildConfig.ads_inter_medium,
                BuildConfig.ad_interstitial_splash,
                30000,
                3000,
                new AperoAdCallback() {
                    @Override
                    public void onAdSplashReady() {
                        super.onAdSplashReady();
                        typeShowAd = TYPE_INTER_MEDIUM;
                        showInterSplash3();
                    }

                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        startMain();
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable ApAdError adError) {
                        super.onAdFailedToShow(adError);
                        Log.e(TAG, "onAdFailedToShow: "+ adError );
                    }

                    @Override
                    public void onAdPriorityFailedToLoad(@Nullable ApAdError adError) {
                        super.onAdPriorityFailedToLoad(adError);
                        Log.e(TAG, "onAdPriorityFailedToLoad: " );
                    }

                    @Override
                    public void onAdPriorityMediumFailedToLoad(@Nullable ApAdError adError) {
                        super.onAdPriorityMediumFailedToLoad(adError);
                        Log.e(TAG, "onAdPriorityMediumFailedToLoad: " );
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable ApAdError adError) {
                        super.onAdFailedToLoad(adError);
                        Log.e(TAG, "onAdFailedToLoad: " );
                    }
                });
    }

    private void showInterSplash3(){
        AperoAd.getInstance().onShowSplashPriority3(SplashActivity.this, new AperoAdCallback() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startMain();
                Log.e(TAG, "onAdClosed: ");
            }

            @Override
            public void onAdPriorityFailedToShow(@Nullable ApAdError adError) {
                super.onAdPriorityFailedToShow(adError);
                Log.e(TAG, "onAdPriorityFailedToShow: ");
            }

            @Override
            public void onAdPriorityMediumFailedToShow(@Nullable ApAdError adError) {
                super.onAdPriorityMediumFailedToShow(adError);
                Log.e(TAG, "onAdPriorityMediumFailedToShow: ");
            }

            @Override
            public void onAdFailedToShow(@Nullable ApAdError adError) {
                super.onAdFailedToShow(adError);
                Log.e(TAG, "onAdFailedToShow: ");
            }

            @Override
            public void onNextAction() {
                super.onNextAction();
                //startMain();
                Log.e(TAG, "onNextAction: ");
            }
        });
    }

    private void loadAndShowInterPriority() {
        AperoAd.getInstance().loadSplashInterPrioritySameTime(
                this,
                BuildConfig.ads_inter_priority,
                BuildConfig.ad_interstitial_splash,
                30000,
                3000,
                false,
                new AperoAdCallback() {
                    @Override
                    public void onAdSplashReady() {
                        super.onAdSplashReady();
                        typeShowAd = TYPE_INTER_HIGH;
                        //Ads Inter priority ready
                        AperoAd.getInstance().onShowSplashPriority(SplashActivity.this, new AperoAdCallback() {
                            @Override
                            public void onNextAction() {
                                super.onNextAction();
                                startMain();
                            }
                        });
                    }

                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        startMain();
                    }

                    @Override
                    public void onNormalInterSplashLoaded() {
                        super.onNormalInterSplashLoaded();
                        typeShowAd = TYPE_INTER_NORMAL;
                        //ads inter priority failed, ads inter normal loaded
                        AperoAd.getInstance().onShowSplash(SplashActivity.this, new AperoAdCallback() {
                            @Override
                            public void onNextAction() {
                                super.onNextAction();
                                startMain();
                            }
                        });
                    }
                });
    }


    AperoAdCallback adCallback = new AperoAdCallback() {
        @Override
        public void onAdFailedToLoad(@Nullable ApAdError i) {
            super.onAdFailedToLoad(i);
            Log.d(TAG, "onAdLoaded");
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            Log.d(TAG, "onAdLoaded");
        }

        @Override
        public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
            super.onNativeAdLoaded(nativeAd);
        }

        @Override
        public void onNextAction() {
            super.onNextAction();
            Log.d(TAG, "onNextAction");
            startMain();
        }

        @Override
        public void onAdSplashReady() {
            super.onAdSplashReady();

        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            Log.d(TAG, "onAdClosed");
        }

    };

    private void loadSplash() {
        Log.d(TAG, "onCreate: show splash ads");
        AperoAd.getInstance().setInitCallback(new AperoInitCallback() {
            @Override
            public void initAdSuccess() {
                AperoAd.getInstance().loadSplashInterstitialAds(SplashActivity.this, idAdSplash, 30000, 5000, true, adCallback);
            }
        });

    }


    private void loadAdmobAd() {
        AppOpenManager.getInstance().setSplashActivity(SplashActivity.class, getString(R.string.admod_app_open_ad_id), 30000);
        AppOpenManager.getInstance().setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                startMain();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                startMain();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }
        });
        AppOpenManager.getInstance().loadAndShowSplashAds(getString(R.string.admod_app_open_ad_id));
    }

    private void startMain() {
        if (!SharePreferenceUtils.isFirstOpenApp(this)) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            loadNativeAdsFirstLanguageOpen();
            startActivity(new Intent(SplashActivity.this, LanguageFirstOpenActivity.class));
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "Splash onPause: ");
        if (isFirstRunApp) {
            isFirstRunApp = false;
            return;
        }
        switch (typeShowAd) {
            case TYPE_INTER_HIGH:
                AperoAd.getInstance().onCheckShowSplashPriorityWhenFail(this, new AperoAdCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        if (isFinishing()) {
                            return;
                        }
                        startMain();

                    }

                    @Override
                    public void onAdFailedToShow(@Nullable ApAdError adError) {
                        super.onAdFailedToShow(adError);
                        if (isFinishing()) {
                            return;
                        }
                        startMain();
                    }
                }, 1000);
                break;
            case TYPE_INTER_MEDIUM:
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
                break;
            default:
                AperoAd.getInstance().onCheckShowSplashWhenFail(this, new AperoAdCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        if (isFinishing()) {
                            return;
                        }
                        startMain();
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable ApAdError adError) {
                        super.onAdFailedToShow(adError);
                        if (isFinishing()) {
                            return;
                        }
                        startMain();
                    }
                }, 1000);
                break;

        }
        AperoAd.getInstance().onCheckShowSplashWhenFail(this, adCallback, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "Splash onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "Splash onStop: ");
    }

    @Override
    protected void onDestroy() {
//        AppOpenManager.getInstance().removeFullScreenContentCallback();
        super.onDestroy();
    }
}