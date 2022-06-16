package com.DuAn1.techstore.Model;

import java.io.Serializable;

public class GioHang implements Serializable {
    private int maKH;
    private int maSanPham;
    private int soLuongMua;

    public GioHang(int maKH, int maSanPham, int soLuongMua) {
        this.maKH = maKH;
        this.maSanPham = maSanPham;
        this.soLuongMua = soLuongMua;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public GioHang() {
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getSoLuongMua() {
        return soLuongMua;
    }

    public void setSoLuongMua(int soLuongMua) {
        this.soLuongMua = soLuongMua;
    }
}
