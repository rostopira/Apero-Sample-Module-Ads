package com.example.andmoduleads;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ads.control.ads.AperoAd;
import com.ads.control.ads.AperoAdCallback;
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

    public void loadInterSameTime(final Context context, String idAdInterPriority, String idAdInterNormal, AperoAdCallback adListener) {
        if (AppPurchase.getInstance().isPurchased(context)) {
            return;
        }
        if (MyApplication.getApplication().getStorageCommon().interPriority == null) {
            Log.i(TAG, "getAdsInterPriority: ");
            AperoAd.getInstance().getInterstitialAds(context, idAdInterPriority, new AperoAdCallback() {
                @Override
                public void onInterstitialLoad(@Nullable ApInterstitialAd interstitialAd) {
                    super.onInterstitialLoad(interstitialAd);
                    adListener.onInterPriorityLoaded(interstitialAd);
                }
            });
        }
        if (MyApplication.getApplication().getStorageCommon().interNormal == null) {
            Log.i(TAG, "getAdsInterNormal: ");
            AperoAd.getInstance().getInterstitialAds(context, idAdInterNormal, new AperoAdCallback() {
                @Override
                public void onInterstitialLoad(@Nullable ApInterstitialAd interstitialAd) {
                    super.onInterstitialLoad(interstitialAd);
                    adListener.onInterstitialLoad(interstitialAd);
                }
            });
        }
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
            adCallback.onInterstitialPriorityShowed();
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
                    },
                    reload);
        } else if (interNormal != null) {
            Log.e(TAG, "showInterSameTime: Ad normal");
            adCallback.onInterstitialNormalShowed();
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
                    },
                    reload);
        } else {
            adCallback.onNextAction();
        }
    }

}
