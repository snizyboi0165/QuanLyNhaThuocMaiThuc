package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class LoThuoc implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maLo;
    private String soLo;
    private Thuoc thuoc;
    private PhieuNhapThuoc phieuNhap;
    private Date ngaySanXuat;
    private Date hanSuDung;
    private int soLuongNhap;
    private int soLuongConLai;
    private double donGiaNhap;
    private String trangThai;

    public LoThuoc() {
    }

    public LoThuoc(String maLo, String soLo, Thuoc thuoc, PhieuNhapThuoc phieuNhap, Date ngaySanXuat,
            Date hanSuDung, int soLuongNhap, int soLuongConLai, double donGiaNhap, String trangThai) {
        setMaLo(maLo);
        setSoLo(soLo);
        setThuoc(thuoc);
        setPhieuNhap(phieuNhap);
        setNgaySanXuat(ngaySanXuat);
        setHanSuDung(hanSuDung);
        setSoLuongNhap(soLuongNhap);
        setSoLuongConLai(soLuongConLai);
        setDonGiaNhap(donGiaNhap);
        setTrangThai(trangThai);
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
        if (soLo == null || soLo.trim().isEmpty()) {
            throw new IllegalArgumentException("So lo khong duoc rong");
        }
        this.soLo = soLo;
    }

    public Thuoc getThuoc() {
        return thuoc;
    }

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public PhieuNhapThuoc getPhieuNhap() {
        return phieuNhap;
    }

    public void setPhieuNhap(PhieuNhapThuoc phieuNhap) {
        this.phieuNhap = phieuNhap;
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
        if (hanSuDung == null) {
            throw new IllegalArgumentException("Han su dung khong duoc rong");
        }
        this.hanSuDung = hanSuDung;
    }

    public int getSoLuongNhap() {
        return soLuongNhap;
    }

    public void setSoLuongNhap(int soLuongNhap) {
        if (soLuongNhap <= 0) {
            throw new IllegalArgumentException("So luong nhap phai lon hon 0");
        }
        this.soLuongNhap = soLuongNhap;
    }

    public int getSoLuongConLai() {
        return soLuongConLai;
    }

    public void setSoLuongConLai(int soLuongConLai) {
        if (soLuongConLai < 0) {
            throw new IllegalArgumentException("So luong con lai khong duoc am");
        }
        this.soLuongConLai = soLuongConLai;
    }

    public double getDonGiaNhap() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(double donGiaNhap) {
        if (donGiaNhap < 0) {
            throw new IllegalArgumentException("Don gia nhap khong duoc am");
        }
        this.donGiaNhap = donGiaNhap;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = (trangThai == null || trangThai.trim().isEmpty()) ? "Còn hàng" : trangThai;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maLo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LoThuoc)) {
            return false;
        }
        LoThuoc other = (LoThuoc) obj;
        return Objects.equals(maLo, other.maLo);
    }
}
