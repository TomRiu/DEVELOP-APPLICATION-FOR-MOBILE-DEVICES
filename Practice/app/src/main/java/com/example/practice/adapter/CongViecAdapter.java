package com.example.practice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice.R;
import com.example.practice.model.CongViec;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CongViecAdapter extends RecyclerView.Adapter<CongViecAdapter.ViewHolder> {

    private List<CongViec> congViecList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CongViec congViec, int position);
        void onItemLongClick(CongViec congViec, int position);
        void onCheckBoxClick(CongViec congViec, int position, boolean isChecked);
    }

    public CongViecAdapter(List<CongViec> congViecList, Context context) {
        this.congViecList = congViecList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cong_viec_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CongViec congViec = congViecList.get(position);

        holder.tvTenCongViec.setText(congViec.getTenCongViec());
        holder.tvNoiDungCongViec.setText(congViec.getNoiDungCongViec());
        holder.tvNgayHoanThanh.setText(formatDate(congViec.getNgayHoanThanh()));

        holder.ivGioiTinh.setImageResource(congViec.isNam() ? R.drawable.ic_male : R.drawable.ic_female);
        holder.cbHoanThanh.setOnCheckedChangeListener(null);
        holder.cbHoanThanh.setChecked(congViec.isHoanThanh());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(congViec, holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onItemLongClick(congViec, holder.getAdapterPosition());
                return true;
            }
            return false;
        });

        holder.cbHoanThanh.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onCheckBoxClick(congViec, holder.getAdapterPosition(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return congViecList.size();
    }

    public void updateList(List<CongViec> newList) {
        congViecList = newList;
        notifyDataSetChanged();
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGioiTinh;
        TextView tvTenCongViec;
        TextView tvNoiDungCongViec;
        TextView tvNgayHoanThanh;
        CheckBox cbHoanThanh;

        ViewHolder(View itemView) {
            super(itemView);
            ivGioiTinh = itemView.findViewById(R.id.ivGioiTinh);
            tvTenCongViec = itemView.findViewById(R.id.tvTenCongViec);
            tvNoiDungCongViec = itemView.findViewById(R.id.tvNoiDungCongViec);
            tvNgayHoanThanh = itemView.findViewById(R.id.tvNgayHoanThanh);
            cbHoanThanh = itemView.findViewById(R.id.cbHoanThanh);
        }
    }
}
