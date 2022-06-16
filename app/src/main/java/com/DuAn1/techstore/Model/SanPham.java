package com.DuAn1.techstore.Model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private int maLoai;
    private int maSanPham;
    private String tenSanPham;
    private int soLuongNhap;
    private String hinhAnh;
    private int giaTien;
    private int giaCu;
    private String ngayNhap;
    private String thongTinSanPham;

    public SanPham(int maLoai, int maSanPham, String tenSanPham, int soLuongNhap, String hinhAnh, int giaTien, int giaCu, String ngayNhap, String thongTinSanPham) {
        this.maLoai = maLoai;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuongNhap = soLuongNhap;
        this.hinhAnh = hinhAnh;
        this.giaTien = giaTien;
        this.giaCu = giaCu;
        this.ngayNhap = ngayNhap;
        this.thongTinSanPham = thongTinSanPham;
    }

    public SanPham() {
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getSoLuongNhap() {
        return soLuongNhap;
    }

    public void setSoLuongNhap(int soLuongNhap) {
        this.soLuongNhap = soLuongNhap;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(int giaTien) {
        this.giaTien = giaTien;
    }

    public int getGiaCu() {
        return giaCu;
    }

    public void setGiaCu(int giaCu) {
        this.giaCu = giaCu;
    }

    public String getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(String ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getThongTinSanPham() {
        return thongTinSanPham;
    }

    public void setThongTinSanPham(String thongTinSanPham) {
        this.thongTinSanPham = thongTinSanPham;
    }
}
