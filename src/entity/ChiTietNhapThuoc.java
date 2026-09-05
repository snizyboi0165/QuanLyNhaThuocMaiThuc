package entity;

import java.util.Date;
import java.util.Objects;

public class ChiTietNhapThuoc {
    private String maPhieuNhap;
    private String maThuoc;
    private String maLo;
    private String soLo;
    private Date ngaySanXuat;
    private Date hanSuDung;
    private int soLuong;
    private double donGia;

    public ChiTietNhapThuoc() {
    }

    public ChiTietNhapThuoc(String maPhieuNhap, String maThuoc, int soLuong, double donGia) {
        this.maPhieuNhap = maPhieuNhap;
        this.maThuoc = maThuoc;
        setSoLuong(soLuong);
        setDonGia(donGia);
    }

    public ChiTietNhapThuoc(String maPhieuNhap, String maLo, String soLo, String maThuoc,
            Date ngaySanXuat, Date hanSuDung, int soLuong, double donGia) {
        this(maPhieuNhap, maThuoc, soLuong, donGia);
        this.maLo = maLo;
        this.soLo = soLo;
        this.ngaySanXuat = ngaySanXuat;
        this.hanSuDung = hanSuDung;
    }

    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public String getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(String maThuoc) {
        this.maThuoc = maThuoc;
    }

    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
    }

    public String getSoLo() {
        return soLo;
    }

    public void setSoLo(String soLo) {
        this.soLo = soLo;
    }

    public Date getNgaySanXuat() {
        return ngaySanXuat;
    }

    public void setNgaySanXuat(Date ngaySanXuat) {
        this.ngaySanXuat = ngaySanXuat;
    }

    public Date getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(Date hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("So luong khong duoc am");
        }
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        if (donGia < 0) {
            throw new IllegalArgumentException("Don gia khong duoc am");
        }
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return soLuong * donGia;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPhieuNhap, maLo, maThuoc, soLo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChiTietNhapThuoc)) {
            return false;
        }
        ChiTietNhapThuoc other = (ChiTietNhapThuoc) obj;
        return Objects.equals(maPhieuNhap, other.maPhieuNhap)
                && Objects.equals(maLo, other.maLo)
                && Objects.equals(maThuoc, other.maThuoc)
                && Objects.equals(soLo, other.soLo);
    }

    @Override
    public String toString() {
        return "ChiTietNhapThuoc [maPhieuNhap=" + maPhieuNhap + ", maLo=" + maLo
                + ", maThuoc=" + maThuoc + ", soLuong=" + soLuong + ", donGia=" + donGia + "]";
    }
}
