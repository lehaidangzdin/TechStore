package com.DuAn1.techstore.Model;

import java.io.Serializable;

public class HoaDon implements Serializable {

    private int maHoaDon;
    private int maKhachHang;
    private String ngayBan;
    private int tongTien;
    private int isPay;

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getNgayBan() {
        return ngayBan;
    }

    public void setNgayBan(String ngayBan) {
        this.ngayBan = ngayBan;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public HoaDon(int maHoaDon, int maKhachHang, String ngayBan, int tongTien, int isPay) {
        this.maHoaDon = maHoaDon;
        this.maKhachHang = maKhachHang;
        this.ngayBan = ngayBan;
        this.tongTien = tongTien;
        this.isPay = isPay;
    }

    public HoaDon() {
    }
}
