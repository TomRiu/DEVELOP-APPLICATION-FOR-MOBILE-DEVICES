package com.example.practice.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice.R;
import com.example.practice.adapter.CongViecAdapter;
import com.example.practice.model.CongViec;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvCongViec;
    private CongViecAdapter adapter;
    private List<CongViec> congViecList;
    private EditText etSearch;
    private Button btnThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        rvCongViec = findViewById(R.id.rvCongViec);
        etSearch = findViewById(R.id.etSearch);
        btnThem = findViewById(R.id.btnThem);
    }

    private void setupRecyclerView() {
        congViecList = new ArrayList<>();
        adapter = new CongViecAdapter(congViecList, this);
        rvCongViec.setLayoutManager(new LinearLayoutManager(this));
        rvCongViec.setAdapter(adapter);

        adapter.setOnItemClickListener(new CongViecAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CongViec congViec, int position) {
                showAddEditDialog(congViec, position);
            }

            @Override
            public void onItemLongClick(CongViec congViec, int position) {
                showDeleteConfirmDialog(position);
            }

            @Override
            public void onCheckBoxClick(CongViec congViec, int position, boolean isChecked) {
                congViec.setHoanThanh(isChecked);
                updateCongViec(congViec, position);
            }
        });
    }

    private void setupListeners() {
        btnThem.setOnClickListener(v -> showAddEditDialog(null, -1));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchCongViec(s.toString());
            }
        });
    }

    private void showAddEditDialog(final CongViec congViec, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_edit_cong_viec, null);
        builder.setView(view);

        final EditText etTenCongViec = view.findViewById(R.id.etTenCongViec);
        final EditText etNoiDungCongViec = view.findViewById(R.id.etNoiDungCongViec);
        final RadioGroup rgGioiTinh = view.findViewById(R.id.rgGioiTinh);
        final TextView tvNgayHoanThanh = view.findViewById(R.id.tvNgayHoanThanh);

        if (congViec != null) {
            etTenCongViec.setText(congViec.getTenCongViec());
            etNoiDungCongViec.setText(congViec.getNoiDungCongViec());
            rgGioiTinh.check(congViec.isNam() ? R.id.rbNam : R.id.rbNu);
            tvNgayHoanThanh.setText(formatDate(congViec.getNgayHoanThanh()));
        }

        tvNgayHoanThanh.setOnClickListener(v -> showDatePickerDialog(tvNgayHoanThanh));

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String tenCongViec = etTenCongViec.getText().toString();
            String noiDungCongViec = etNoiDungCongViec.getText().toString();
            boolean isNam = rgGioiTinh.getCheckedRadioButtonId() == R.id.rbNam;
            Date ngayHoanThanh = parseDate(tvNgayHoanThanh.getText().toString());

            if (congViec == null) {
                CongViec newCongViec = new CongViec(tenCongViec, noiDungCongViec, isNam, ngayHoanThanh);
                addCongViec(newCongViec);
            } else {
                congViec.setTenCongViec(tenCongViec);
                congViec.setNoiDungCongViec(noiDungCongViec);
                congViec.setNam(isNam);
                congViec.setNgayHoanThanh(ngayHoanThanh);
                updateCongViec(congViec, position);
            }
        });

        builder.setNegativeButton("Hủy", null);

        builder.create().show();
    }

    private void showDatePickerDialog(final TextView tvDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    tvDate.setText(formatDate(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showDeleteConfirmDialog(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa công việc")
                .setMessage("Bạn có chắc chắn muốn xóa công việc này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteCongViec(position))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void addCongViec(CongViec congViec) {
        congViecList.add(congViec);
        adapter.notifyItemInserted(congViecList.size() - 1);
    }

    private void updateCongViec(CongViec congViec, int position) {
        congViecList.set(position, congViec);
        adapter.notifyItemChanged(position);
    }

    private void deleteCongViec(int position) {
        congViecList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    private void searchCongViec(String query) {
        List<CongViec> filteredList = new ArrayList<>();
        for (CongViec congViec : congViecList) {
            if (congViec.getTenCongViec().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(congViec);
            }
        }
        adapter.updateList(filteredList);
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    private Date parseDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
