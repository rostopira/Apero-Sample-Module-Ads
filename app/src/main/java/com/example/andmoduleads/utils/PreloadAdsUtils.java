package com.example.andmoduleads.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.control.ads.AperoAd;
import com.ads.control.ads.AperoAdCallback;
import com.ads.control.ads.wrapper.ApAdError;
import com.ads.control.ads.wrapper.ApInterstitialAd;
import com.ads.control.ads.wrapper.ApNativeAd;
import com.ads.control.billing.AppPurchase;
import com.example.andmoduleads.PreloadAdsCallback;
import com.example.andmoduleads.MyApplication;
import com.facebook.shimmer.ShimmerFrameLayout;

public class PreloadAdsUtils {
    private static final String TAG = "PreloadAdsUtils";
    private static PreloadAdsUtils instance;

    public static PreloadAdsUtils getInstance() {
        if (instance == null) {
            instance = new PreloadAdsUtils();
        }
        return instance;
    }

    private int loadTimesFailHigh = 0;
    private int loadTimesFailMedium = 0;
    private int loadTimesFailNormal = 0;
    private final int limitLoad = 2;

    public void loadInterSameTime(final Context context, String idAdInterPriority, String idAdInterNormal, AperoAdCallback adListener) {
        if (AppPurchase.getInstance().isPurchased(context)) {
            return;
        }
        loadTimesFailHigh = 0;
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
                adListener.onInterPriorityLoaded(interstitialAd);
            }

            @Override
            public void onAdFailedToLoad(@Nullable ApAdError adError) {
                super.onAdFailedToLoad(adError);
                Log.e(TAG, "onAdFailedToLoad: Priority");
                if (loadTimesFailHigh < limitLoad) {
                    loadTimesFailHigh++;
                    loadInterPriority(context, idAdInterPriority, adListener);
                }
            }
        });
    }

    public void showInterSameTime(
            Context context,
            ApInterstitialAd interPriority,
            ApInterstitialAd interNormal,
            Boolean reload,
            PreloadAdsCallback adCallback) {
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

    public void preLoadNativeSameTime(
            final Activity activity,
            String idAdNativeHigh,
            String idAdNativeMedium,
            String idAdNativeNormal,
            int layoutNativeCustom,
            PreloadAdsCallback callBack) {
        if (AppPurchase.getInstance().isPurchased(activity)) {
            return;
        }
        loadTimesFailHigh = 0;
        loadTimesFailMedium = 0;
        loadTimesFailNormal = 0;
        if (MyApplication.getApplication().getStorageCommon().nativeAdHigh == null) {
            loadNativeHigh(activity, idAdNativeHigh, layoutNativeCustom, callBack);
        }
        if (MyApplication.getApplication().getStorageCommon().nativeAdMedium == null) {
            loadNativeMedium(activity, idAdNativeMedium, layoutNativeCustom, callBack);
        }
        if (MyApplication.getApplication().getStorageCommon().nativeAdNormal == null) {
            loadNativeNormal(activity, idAdNativeNormal, layoutNativeCustom, callBack);
        }
    }

    private void loadNativeHigh(
            Activity activity,
            String idNativeHigh,
            int layoutNativeCustom,
            PreloadAdsCallback callBack) {
        Log.d(TAG, "loadNativeHigh");
        AperoAd.getInstance().loadNativeAdResultCallback(
                activity,
                idNativeHigh,
                layoutNativeCustom,
                new AperoAdCallback() {
                    @Override
                    public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                        super.onNativeAdLoaded(nativeAd);
                        callBack.onNativeHighAdLoaded(nativeAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable ApAdError adError) {
                        super.onAdFailedToLoad(adError);
                        if (loadTimesFailHigh < limitLoad) {
                            loadTimesFailHigh++;
                            loadNativeHigh(activity, idNativeHigh, layoutNativeCustom, callBack);
                        }
                    }
                });
    }

    private void loadNativeMedium(
            Activity activity,
            String idNativeMedium,
            int layoutNativeCustom,
            PreloadAdsCallback adListener) {
        Log.d(TAG, "loadNativeMedium");
        AperoAd.getInstance().loadNativeAdResultCallback(
                activity,
                idNativeMedium,
                layoutNativeCustom,
                new AperoAdCallback() {
                    @Override
                    public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                        super.onNativeAdLoaded(nativeAd);
                        adListener.onNativeMediumAdLoaded(nativeAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable ApAdError adError) {
                        super.onAdFailedToLoad(adError);
                        if (loadTimesFailMedium < limitLoad) {
                            loadTimesFailMedium++;
                            loadNativeMedium(activity, idNativeMedium, layoutNativeCustom, adListener);
                        }
                    }
                });
    }

    private void loadNativeNormal(
            Activity activity,
            String idNativeNormal,
            int layoutNativeCustom,
            PreloadAdsCallback callBack) {
        Log.d(TAG, "loadNativeNormal");
        AperoAd.getInstance().loadNativeAdResultCallback(
                activity,
                idNativeNormal,
                layoutNativeCustom,
                new AperoAdCallback() {
                    @Override
                    public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                        super.onNativeAdLoaded(nativeAd);
                        callBack.onNativeAdLoaded(nativeAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable ApAdError adError) {
                        super.onAdFailedToLoad(adError);
                        if (loadTimesFailNormal < limitLoad) {
                            loadTimesFailNormal++;
                            loadNativeNormal(activity, idNativeNormal, layoutNativeCustom, callBack);
                        }
                    }
                });
    }

    public void showPreNativeSametime(
            Activity activity,
            ApNativeAd nativeAdHigh,
            ApNativeAd nativeAdMedium,
            ApNativeAd nativeAdNormal,
            FrameLayout adPlaceHolder,
            ShimmerFrameLayout containerShimmerLoading,
            PreloadAdsCallback callBack) {
        if (nativeAdHigh != null) {
            Log.d(TAG, "showPreNativeSametime: nativeAdHigh");
            AperoAd.getInstance().populateNativeAdView(
                    activity,
                    nativeAdHigh,
                    adPlaceHolder,
                    containerShimmerLoading
            );
            callBack.onNativeHighAdShow();
        } else if (nativeAdMedium != null) {
            Log.d(TAG, "showPreNativeSametime: nativeAdMedium");
            AperoAd.getInstance().populateNativeAdView(
                    activity,
                    nativeAdMedium,
                    adPlaceHolder,
                    containerShimmerLoading
            );
            callBack.onNativeMediumAdShow();
        } else if (nativeAdNormal != null) {
            Log.d(TAG, "showPreNativeSametime: nativeAdNormal");
            AperoAd.getInstance().populateNativeAdView(
                    activity,
                    nativeAdNormal,
                    adPlaceHolder,
                    containerShimmerLoading
            );
            callBack.onNativeAdShow();
        } else {
            adPlaceHolder.setVisibility(View.GONE);
        }
    }

}
