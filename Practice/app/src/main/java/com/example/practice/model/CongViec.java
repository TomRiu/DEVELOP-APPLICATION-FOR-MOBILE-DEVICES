package com.example.practice.model;

import java.util.Date;

public class CongViec {
    private String tenCongViec;
    private String noiDungCongViec;
    private boolean isNam;
    private Date ngayHoanThanh;
    private boolean hoanThanh;

    public CongViec(String tenCongViec, String noiDungCongViec, boolean isNam, Date ngayHoanThanh) {
        this.tenCongViec = tenCongViec;
        this.noiDungCongViec = noiDungCongViec;
        this.isNam = isNam;
        this.ngayHoanThanh = ngayHoanThanh;
        this.hoanThanh = false;
    }

    public String getTenCongViec() {
        return tenCongViec;
    }

    public void setTenCongViec(String tenCongViec) {
        this.tenCongViec = tenCongViec;
    }

    public String getNoiDungCongViec() {
        return noiDungCongViec;
    }

    public void setNoiDungCongViec(String noiDungCongViec) {
        this.noiDungCongViec = noiDungCongViec;
    }

    public boolean isNam() {
        return isNam;
    }

    public void setNam(boolean nam) {
        isNam = nam;
    }

    public Date getNgayHoanThanh() {
        return ngayHoanThanh;
    }

    public void setNgayHoanThanh(Date ngayHoanThanh) {
        this.ngayHoanThanh = ngayHoanThanh;
    }

    public boolean isHoanThanh() {
        return hoanThanh;
    }

    public void setHoanThanh(boolean hoanThanh) {
        this.hoanThanh = hoanThanh;
    }
}
