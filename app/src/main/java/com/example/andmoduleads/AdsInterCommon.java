package com.example.andmoduleads;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.ads.control.admob.Admob;
import com.ads.control.admob.AppOpenManager;
import com.ads.control.ads.AperoAdCallback;
import com.ads.control.ads.wrapper.ApAdError;
import com.ads.control.billing.AppPurchase;
import com.ads.control.event.AperoLogEventManager;
import com.ads.control.funtion.AdCallback;
import com.ads.control.funtion.AdType;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.Calendar;

public class AdsInterCommon {
    private static final String TAG = "AdsInterCommon";
    private final static int NUMBER_RELOAD_WHEN_LOAD_FAILED = 3;

    private static AdsInterCommon instance;
    private InterstitialAd interPriority;
    private InterstitialAd interNormal;
    private int numberReloadAdPriorityWhenFail = 0;
    private int numberReloadAdNormalWhenFail = 0;
    private String idAdInterPriority;
    private String idAdInterNormal;
    private String activityName;

    public static AdsInterCommon getInstance() {
        if (instance == null) {
            instance = new AdsInterCommon();
        }
        return instance;
    }

    public void setAdIdsInter(String adIdPriority, String adIdNormal) {
        idAdInterPriority = adIdPriority;
        idAdInterNormal = adIdNormal;
    }

    public InterstitialAd getInterPriority() {
        return interPriority;
    }

    public InterstitialAd getInterNormal() {
        return interNormal;
    }

    public void loadInterSameTime(final Context context, boolean reloadIfFail, AperoAdCallback adListener) {
        if (AppPurchase.getInstance().isPurchased(context)) {
            if (adListener != null) {
                adListener.onNextAction();
            }
            return;
        }
        numberReloadAdPriorityWhenFail = 0;
        numberReloadAdNormalWhenFail = 0;
        if (interPriority == null) {
            getAdsInterPriority(context, reloadIfFail, adListener);
        }
        if (interNormal == null) {
            getAdsInterNormal(context, reloadIfFail, adListener);
        }
    }

    public void showInterSameTime(Context context, AperoAdCallback adCallback) {
        activityName = context.getClass().getSimpleName();
        if (interPriority != null) {
            showAdsInterPriority((AppCompatActivity) context, adCallback);
        } else if (interNormal != null) {
            showAdsInterNormal((AppCompatActivity) context, adCallback);
        } else {
            adCallback.onNextAction();
        }
    }

    private void getAdsInterPriority(final Context context, boolean reloadIfFail, AperoAdCallback adListener) {
        Log.i(TAG, "getAdsInterPriority: ");
        Admob.getInstance().getInterstitialAds(context, idAdInterPriority, new AdCallback() {
            @Override
            public void onInterstitialLoad(InterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                Log.i(TAG, "loadAdInterstitialPriority end time loading success:" + Calendar.getInstance().getTimeInMillis());
                if (interstitialAd != null) {
                    interPriority = interstitialAd;
                    interPriority.setOnPaidEventListener(adValue -> {
                        Log.d(TAG, "OnPaidEvent splash:" + adValue.getValueMicros());
                        AperoLogEventManager.logPaidAdImpression(context,
                                adValue,
                                interPriority.getAdUnitId(),
                                interPriority.getResponseInfo()
                                        .getMediationAdapterClassName(), AdType.INTERSTITIAL);
                    });
                    adListener.onAdSplashPriorityReady();
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError i) {
                super.onAdFailedToLoad(i);
                Log.e(TAG, "loadAdInterstitialPriority end time loading error:" + Calendar.getInstance().getTimeInMillis());
                if (reloadIfFail && numberReloadAdPriorityWhenFail < NUMBER_RELOAD_WHEN_LOAD_FAILED) {
                    numberReloadAdPriorityWhenFail++;
                    getAdsInterPriority(context, true, adListener);
                } else {
                    adListener.onAdPriorityFailedToLoad(new ApAdError((i)));
                }
            }
        });
    }

    private void getAdsInterNormal(final Context context, boolean reloadIfFail, AperoAdCallback adListener) {
        Log.i(TAG, "getAdsInterNormal: ");
        Admob.getInstance().getInterstitialAds(context, idAdInterNormal, new AdCallback() {
            @Override
            public void onInterstitialLoad(InterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                Log.i(TAG, "loadAdInterstitialNormal end time loading success:" + Calendar.getInstance().getTimeInMillis());
                if (interstitialAd != null) {
                    interNormal = interstitialAd;
                    interNormal.setOnPaidEventListener(adValue -> {
                        AperoLogEventManager.logPaidAdImpression(context,
                                adValue,
                                interNormal.getAdUnitId(),
                                interNormal.getResponseInfo()
                                        .getMediationAdapterClassName(), AdType.INTERSTITIAL);
                    });
                    adListener.onAdSplashReady();
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError i) {
                super.onAdFailedToLoad(i);
                Log.e(TAG, "loadAdInterstitialNormal end time loading error:" + Calendar.getInstance().getTimeInMillis());
                if (reloadIfFail && numberReloadAdNormalWhenFail < NUMBER_RELOAD_WHEN_LOAD_FAILED) {
                    numberReloadAdNormalWhenFail++;
                    getAdsInterNormal(context, true, adListener);
                } else {
                    adListener.onAdFailedToLoad(new ApAdError(i));
                }
            }
        });
    }

    private void showAdsInterPriority(AppCompatActivity activity, AperoAdCallback adListener) {
        Log.i(TAG, "onShowAdInterstitial: Priority ");

        if (interPriority == null) {
            adListener.onAdPriorityFailedToShow(new ApAdError("interstitial ads null "));
            return;
        }

        if (adListener != null) {
            adListener.onAdLoaded();
        }
        interPriority.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                Log.d(TAG, "Interstitial:onAdShowedFullScreenContent ");
                AppOpenManager.getInstance().setInterstitialShowing(true);
                AppOpenManager.getInstance().disableAppResume();
                AperoLogEventManager.onTrackEvent("inter_show_" + activityName);
                interPriority = null;
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                Log.d(TAG, "Interstitial:onAdDismissedFullScreenContent ");
                AppOpenManager.getInstance().setInterstitialShowing(false);
                AppOpenManager.getInstance().enableAppResume();
                interPriority = null;
                if (adListener != null) {
                    adListener.onAdClosed();
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                Log.e(TAG, "Interstitial onAdFailedToShowFullScreenContent: " + adError.getMessage());
                interPriority = null;
                if (adListener != null) {
                    adListener.onAdPriorityFailedToShow(new ApAdError(adError));
                }
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                if (adListener != null) {
                    adListener.onAdClicked();
                }
                AppOpenManager.getInstance().disableAdResumeByClickAction();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                if (adListener != null) {
                    adListener.onAdImpression();
                }
            }
        });

        if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            new Handler().postDelayed(() -> {
                if (activity.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (interPriority != null) {
                        Log.i(TAG, "start show InterstitialAdPriority " + activity.getLifecycle().getCurrentState().name() + "/" + ProcessLifecycleOwner.get().getLifecycle().getCurrentState().name());
                        interPriority.show(activity);
                    } else if (adListener != null) {
                        adListener.onAdPriorityFailedToShow(new ApAdError("interstitial ads null "));
                    }
                } else {
                    Log.e(TAG, "onShowInterstitial:   show fail in background after show loading ad");
                    adListener.onAdPriorityFailedToShow(new ApAdError(new AdError(0, " show fail in background after show loading ad", "AperoAd")));
                }
            }, 800);

        } else {
            adListener.onAdPriorityFailedToShow(new ApAdError(new AdError(0, " show fail in background after show loading ad", "AperoAd")));
            Log.e(TAG, "onShowInterstitial: fail on background");
        }
    }

    private void showAdsInterNormal(AppCompatActivity activity, AperoAdCallback adListener) {
        Log.i(TAG, "onShowAdInterstitial: Normal ");

        if (interNormal == null) {
            adListener.onAdFailedToShow(new ApAdError("interstitial ads null "));
            return;
        }

        if (adListener != null) {
            adListener.onAdLoaded();
        }
        interNormal.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                Log.d(TAG, "Interstitial:onAdShowedFullScreenContent ");
                AppOpenManager.getInstance().setInterstitialShowing(true);
                AppOpenManager.getInstance().disableAppResume();
                AperoLogEventManager.onTrackEvent("inter_show_" + activityName);
                interNormal = null;
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                Log.d(TAG, "Interstitial:onAdDismissedFullScreenContent ");
                AppOpenManager.getInstance().setInterstitialShowing(false);
                AppOpenManager.getInstance().enableAppResume();
                interNormal = null;
                if (adListener != null) {
                    adListener.onAdClosed();
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                Log.e(TAG, "Interstitial onAdFailedToShowFullScreenContent: " + adError.getMessage());
                interNormal = null;
                if (adListener != null) {
                    adListener.onAdPriorityFailedToShow(new ApAdError(adError));
                }
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                if (adListener != null) {
                    adListener.onAdClicked();
                }
                AppOpenManager.getInstance().disableAdResumeByClickAction();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                if (adListener != null) {
                    adListener.onAdImpression();
                }
            }
        });

        if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            new Handler().postDelayed(() -> {
                if (activity.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (interNormal != null) {
                        Log.i(TAG, "start show InterstitialAdNormal " + activity.getLifecycle().getCurrentState().name() + "/" + ProcessLifecycleOwner.get().getLifecycle().getCurrentState().name());
                        interNormal.show(activity);
                    } else if (adListener != null) {
                        adListener.onAdFailedToShow(new ApAdError("interstitial ads null "));
                    }
                } else {
                    Log.e(TAG, "onShowInterstitial:   show fail in background after show loading ad");
                    adListener.onAdFailedToShow(new ApAdError(new AdError(0, " show fail in background after show loading ad", "AperoAd")));
                }
            }, 800);

        } else {
            adListener.onAdFailedToShow(new ApAdError(new AdError(0, " show fail in background after show loading ad", "AperoAd")));
            Log.e(TAG, "onShowInterstitial: fail on background");
        }
    }

}
