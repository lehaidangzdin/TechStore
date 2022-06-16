package com.DuAn1.techstore.Model;

public class ChiTietHoaDon {

    private int maHoaDon;
    private int maSanPham;
    private int donGia;
    private int soLuongMua;

    public ChiTietHoaDon() {
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public int getSoLuongMua() {
        return soLuongMua;
    }

    public void setSoLuongMua(int soLuongMua) {
        this.soLuongMua = soLuongMua;
    }

    public ChiTietHoaDon(int maHoaDon, int maSanPham, int donGia, int soLuongMua) {
        this.maHoaDon = maHoaDon;
        this.maSanPham = maSanPham;
        this.donGia = donGia;
        this.soLuongMua = soLuongMua;
    }
}
