package com.example.bt2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bt2.model.CategoryInOut;

import java.util.List;

import com.example.bt2.R;

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
                    R.layout.item_category, parent, false);
        }

        ImageView imageViewIcon = convertView.findViewById(R.id.icon);
        TextView textViewName = convertView.findViewById(R.id.text1);
        CategoryInOut categoryInOut = getItem(position);

        if (categoryInOut != null && categoryInOut.getCategory() != null) {
            textViewName.setText(categoryInOut.getCategory().getName());
            // Thiết lập icon từ drawable
            String iconName = categoryInOut.getCategory().getIcon(); // Lấy tên icon
            int iconResId = getContext().getResources().getIdentifier(iconName, "drawable", getContext().getPackageName());
            if (iconResId != 0) { // Kiểm tra xem ID có hợp lệ không
                imageViewIcon.setImageResource(iconResId);
            } else {
                // Nếu không tìm thấy, có thể đặt icon mặc định hoặc để trống
                imageViewIcon.setImageResource(R.drawable.empty_folder); // Thay 'default_icon' bằng icon mặc định của bạn
            }
        }

        return convertView;
    }
}
