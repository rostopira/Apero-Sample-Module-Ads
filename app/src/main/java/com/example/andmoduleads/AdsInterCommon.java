package com.example.andmoduleads;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ads.control.ads.AperoAd;
import com.ads.control.ads.AperoAdCallback;
import com.ads.control.ads.wrapper.ApAdError;
import com.ads.control.ads.wrapper.ApInterstitialAd;
import com.ads.control.billing.AppPurchase;

public class AdsInterCommon {
    private static final String TAG = "AdsInterCommon";
    private static AdsInterCommon instance;

    public static AdsInterCommon getInstance() {
        if (instance == null) {
            instance = new AdsInterCommon();
        }
        return instance;
    }

    private int loadTimesFailPriority = 0;
    private int loadTimesFailNormal = 0;
    private final int limitLoad = 2;

    public void loadInterSameTime(final Context context, String idAdInterPriority, String idAdInterNormal, AperoAdCallback adListener) {
        if (AppPurchase.getInstance().isPurchased(context)) {
            return;
        }
        loadTimesFailPriority = 0;
        loadTimesFailNormal = 0;
        if (MyApplication.getApplication().getStorageCommon().interPriority == null) {
            loadInterPriority(context, idAdInterPriority, adListener);
        }
        if (MyApplication.getApplication().getStorageCommon().interNormal == null) {
            loadInterNormal(context, idAdInterNormal, adListener);
        }
    }

    private void loadInterNormal(Context context, String idAdInterNormal, AperoAdCallback adListener) {
        Log.e(TAG, "loadInterNormal: ");
        AperoAd.getInstance().getInterstitialAds(context, idAdInterNormal, new AperoAdCallback() {
            @Override
            public void onInterstitialLoad(@Nullable ApInterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                adListener.onInterstitialLoad(interstitialAd);
            }

            @Override
            public void onAdFailedToLoad(@Nullable ApAdError adError) {
                super.onAdFailedToLoad(adError);
                Log.e(TAG, "onAdFailedToLoad: Normal");
                if (loadTimesFailNormal < limitLoad) {
                    loadTimesFailNormal++;
                    loadInterNormal(context, idAdInterNormal, adListener);
                }
            }
        });
    }

    private void loadInterPriority(Context context, String idAdInterPriority, AperoAdCallback adListener) {
        Log.e(TAG, "loadInterPriority: ");
        AperoAd.getInstance().getInterstitialAds(context, idAdInterPriority, new AperoAdCallback() {
            @Override
            public void onInterstitialLoad(@Nullable ApInterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                adListener.onInterstitialLoad(interstitialAd);
            }

            @Override
            public void onAdFailedToLoad(@Nullable ApAdError adError) {
                super.onAdFailedToLoad(adError);
                Log.e(TAG, "onAdFailedToLoad: Priority");
                if (loadTimesFailPriority < limitLoad) {
                    loadTimesFailPriority++;
                    loadInterPriority(context, idAdInterPriority, adListener);
                }
            }
        });
    }

    public void showInterSameTime(Context context, ApInterstitialAd interPriority, ApInterstitialAd interNormal, Boolean reload, AdsInterCallBack adCallback) {
        if (AppPurchase.getInstance().isPurchased(context)) {
            if (adCallback != null) {
                adCallback.onNextAction();
            }
            return;
        }
        if (interPriority != null) {
            Log.e(TAG, "showInterSameTime: Ad priority");
            AperoAd.getInstance().forceShowInterstitial(
                    context,
                    interPriority,
                    new AperoAdCallback() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            adCallback.onAdClosed();
                        }

                        @Override
                        public void onNextAction() {
                            super.onNextAction();
                            adCallback.onNextAction();
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                            adCallback.onAdClicked();
                        }

                        @Override
                        public void onInterstitialShow() {
                            super.onInterstitialShow();
                            adCallback.onInterstitialPriorityShowed();
                        }
                    },
                    reload);
        } else if (interNormal != null) {
            Log.e(TAG, "showInterSameTime: Ad normal");
            AperoAd.getInstance().forceShowInterstitial(
                    context,
                    interNormal,
                    new AperoAdCallback() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            adCallback.onAdClosed();
                        }

                        @Override
                        public void onNextAction() {
                            super.onNextAction();

                            adCallback.onNextAction();
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                            adCallback.onAdClicked();
                        }

                        public void onInterstitialShow() {
                            super.onInterstitialShow();
                            adCallback.onInterstitialNormalShowed();
                        }
                    },
                    reload);
        } else {
            adCallback.onNextAction();
        }
    }

}
