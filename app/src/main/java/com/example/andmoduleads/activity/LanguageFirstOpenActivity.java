package com.example.andmoduleads.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ads.control.ads.AperoAd;
import com.ads.control.billing.AppPurchase;
import com.example.andmoduleads.MyApplication;
import com.example.andmoduleads.R;
import com.example.andmoduleads.SharePreferenceUtils;
import com.example.andmoduleads.adapter.LanguageFirstOpenAdapter;
import com.example.andmoduleads.databinding.ActivityLanguageFirstOpenBinding;
import com.example.andmoduleads.model.Language;

import java.util.ArrayList;
import java.util.List;

public class LanguageFirstOpenActivity extends AppCompatActivity {

    private LanguageFirstOpenAdapter adapter;
    private List<Language> languages;
    private ActivityLanguageFirstOpenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageFirstOpenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        initLanguage();
        initEvent();
        if (!AppPurchase.getInstance().isPurchased()) {
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
        } else {
            binding.layoutAdNative.setVisibility(View.GONE);
        }
    }

    private void initEvent() {
        binding.imgDone.setOnClickListener(view -> {
            SharePreferenceUtils.setFirstOpenApp(LanguageFirstOpenActivity.this,false);
            startActivity(new Intent(LanguageFirstOpenActivity.this, MainActivity.class));
            finish();
        });
    }

    private void initLanguage() {
        languages = new ArrayList<>();
        Language en = new Language("en", getString(R.string.english), R.drawable.ic_language_en, false);
        Language zh = new Language("zh", getString(R.string.china), R.drawable.ic_language_cn, false);
        Language fr = new Language("fr", getString(R.string.france), R.drawable.ic_language_fr, false);
        languages.add(en);
        languages.add(zh);
        languages.add(fr);

        setupAdapter();
    }

    private void setupAdapter(){
        adapter = new LanguageFirstOpenAdapter(this,languages);
        binding.recyclerView.setAdapter(adapter);
    }
}