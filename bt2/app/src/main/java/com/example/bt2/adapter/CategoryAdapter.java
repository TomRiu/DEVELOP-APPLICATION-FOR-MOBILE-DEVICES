package com.example.bt2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bt2.model.CategoryInOut;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<CategoryInOut> {

    public CategoryAdapter(Context context, List<CategoryInOut> categories) {
        super(context, 0, categories);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(android.R.id.text1);
        CategoryInOut categoryInOut = getItem(position);

        if (categoryInOut != null && categoryInOut.getCategory() != null) {
            textViewName.setText(categoryInOut.getCategory().getName());
        }

        return convertView;
    }
}