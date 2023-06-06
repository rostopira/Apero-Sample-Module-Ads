package com.example.andmoduleads.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andmoduleads.MyApplication;
import com.example.andmoduleads.PreloadAdsCallback;
import com.example.andmoduleads.databinding.ActivityTestNativeAdBinding;
import com.example.andmoduleads.utils.PreloadAdsUtils;

public class TestNativeAdActivity extends AppCompatActivity {

    private ActivityTestNativeAdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestNativeAdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showPreNative();
    }

    private void showPreNative() {
        PreloadAdsUtils.getInstance().showPreNativeSametime(
                TestNativeAdActivity.this,
                MyApplication.getApplication().getStorageCommon().nativeAdHigh,
                MyApplication.getApplication().getStorageCommon().nativeAdMedium,
                MyApplication.getApplication().getStorageCommon().nativeAdNormal,
                binding.frAds,
                binding.includeNative.shimmerContainerNative,
                new PreloadAdsCallback() {
                    @Override
                    public void onNativeAdShow() {
                        MyApplication.getApplication().getStorageCommon().nativeAdNormal = null;
                    }

                    @Override
                    public void onNativeHighAdShow() {
                        MyApplication.getApplication().getStorageCommon().nativeAdHigh = null;
                    }

                    @Override
                    public void onNativeMediumAdShow() {
                        MyApplication.getApplication().getStorageCommon().nativeAdMedium = null;
                    }
                }
        );
    }
}