package com.DuAn1.techstore.Model;

import java.io.Serializable;

public class KhachHang implements Serializable {
    private int maKhachHang;
    private  String username;
    private  String password;
    private  String tenKhachHang;
    private String namSinh;
    private String soDienThoai;
    private String diaChi;

    public KhachHang(int maKhachHang, String username, String password, String tenKhachHang, String namSinh, String soDienThoai, String diaChi) {
        this.maKhachHang = maKhachHang;
        this.username = username;
        this.password = password;
        this.tenKhachHang = tenKhachHang;
        this.namSinh = namSinh;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
    }

    public KhachHang() {
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(String namSinh) {
        this.namSinh = namSinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}
