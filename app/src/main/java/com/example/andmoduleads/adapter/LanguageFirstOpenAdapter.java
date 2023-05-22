package com.example.andmoduleads.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andmoduleads.R;
import com.example.andmoduleads.model.Language;

import java.util.List;

public class LanguageFirstOpenAdapter extends
        RecyclerView.Adapter<LanguageFirstOpenAdapter.LanguageFirstOpenViewHolder> {
    private Context context;
    private List<Language> languages;

    public LanguageFirstOpenAdapter(Context context, List<Language> languages) {
        this.context = context;
        this.languages = languages;
    }

    @NonNull
    @Override
    public LanguageFirstOpenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LanguageFirstOpenViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_language_first_open_app, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageFirstOpenViewHolder holder, int position) {
        Language language = languages.get(position);

        holder.tvNameLanguage.setText(language.getName());
    }

    @Override
    public int getItemCount() {
        return Math.min(languages.size(), 5);
    }

    class LanguageFirstOpenViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameLanguage;

        public LanguageFirstOpenViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameLanguage = itemView.findViewById(R.id.tvNameLanguage);
        }
    }
}
