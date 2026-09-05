package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Thuoc implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maThuoc;
    private String tenThuoc;
    private String donViTinh;
    private double giaBan;
    private int soLuongTon;
    private Date hanSuDung;
    private String moTa;
    private DanhMucThuoc danhMucThuoc;
    private String hinhAnh;
    private String thanhPhan;
    private Date ngaySanXuat;
    private String xuatXu;

    public static final String REGEX_MA_THUOC = "^TH\\d{5}$";

    public Thuoc() {
    }

    public Thuoc(String maThuoc, String tenThuoc, String donViTinh, double giaBan, int soLuongTon, Date hanSuDung,
            String moTa, DanhMucThuoc danhMucThuoc, String hinhAnh, String thanhPhan, Date ngaySanXuat,
            String xuatXu) {
        setMaThuoc(maThuoc);
        setTenThuoc(tenThuoc);
        setDonViTinh(donViTinh);
        setGiaBan(giaBan);
        setSoLuongTon(soLuongTon);
        setHanSuDung(hanSuDung);
        setMoTa(moTa);
        setDanhMucThuoc(danhMucThuoc);
        setHinhAnh(hinhAnh);
        setThanhPhan(thanhPhan);
        setNgaySanXuat(ngaySanXuat);
        setXuatXu(xuatXu);
    }

    public Thuoc(String maThuoc) {
        setMaThuoc(maThuoc);
    }

    public void setMaThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) {
            this.maThuoc = "";
        } else if (!maThuoc.matches(REGEX_MA_THUOC)) {
            throw new IllegalArgumentException("Ma thuoc phai co dang TH + 5 chu so (VD: TH00001)");
        } else {
            this.maThuoc = maThuoc;
        }
    }

    public void setTenThuoc(String tenThuoc) {
        if (tenThuoc == null || tenThuoc.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten thuoc khong duoc rong");
        }
        this.tenThuoc = tenThuoc;
    }

    public void setDonViTinh(String donViTinh) {
        if (donViTinh == null || donViTinh.trim().isEmpty()) {
            throw new IllegalArgumentException("Don vi tinh khong duoc rong");
        }
        this.donViTinh = donViTinh;
    }

    public void setGiaBan(double giaBan) {
        if (giaBan < 0) {
            throw new IllegalArgumentException("Gia ban phai lon hon hoac bang 0");
        }
        this.giaBan = giaBan;
    }

    public void setSoLuongTon(int soLuongTon) {
        if (soLuongTon < 0) {
            throw new IllegalArgumentException("So luong ton kho phai lon hon hoac bang 0");
        }
        this.soLuongTon = soLuongTon;
    }

    public void setHanSuDung(Date hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public void setMoTa(String moTa) {
        if (moTa == null || moTa.trim().isEmpty() || moTa.trim().length() < 10) {
            throw new IllegalArgumentException("Mo ta phai co it nhat 10 ky tu");
        }
        this.moTa = moTa;
    }

    public void setDanhMucThuoc(DanhMucThuoc danhMucThuoc) {
        this.danhMucThuoc = danhMucThuoc;
    }

    public void setHinhAnh(String hinhAnh) {
        if (hinhAnh == null || hinhAnh.trim().isEmpty()) {
            throw new IllegalArgumentException("Hinh anh thuoc khong duoc rong");
        }
        this.hinhAnh = hinhAnh;
    }

    public void setThanhPhan(String thanhPhan) {
        if (thanhPhan == null || thanhPhan.trim().isEmpty()) {
            throw new IllegalArgumentException("Thanh phan thuoc khong duoc rong");
        }
        this.thanhPhan = thanhPhan;
    }

    public void setNgaySanXuat(Date ngaySanXuat) {
        if (ngaySanXuat != null && ngaySanXuat.after(new Date())) {
            throw new IllegalArgumentException("Ngay san xuat khong duoc sau ngay hien tai");
        }
        this.ngaySanXuat = ngaySanXuat;
    }

    public void setXuatXu(String xuatXu) {
        if (xuatXu == null || xuatXu.trim().isEmpty()) {
            throw new IllegalArgumentException("Xuat xu khong duoc rong");
        }
        this.xuatXu = xuatXu;
    }

    public String getMaThuoc() {
        return maThuoc;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public Date getHanSuDung() {
        return hanSuDung;
    }

    public String getMoTa() {
        return moTa;
    }

    public DanhMucThuoc getDanhMucThuoc() {
        return danhMucThuoc;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public String getThanhPhan() {
        return thanhPhan;
    }

    public Date getNgaySanXuat() {
        return ngaySanXuat;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    @Override
    public String toString() {
        return "Thuoc{" +
                "maThuoc='" + maThuoc + '\'' +
                ", tenThuoc='" + tenThuoc + '\'' +
                ", donViTinh='" + donViTinh + '\'' +
                ", giaBan=" + giaBan +
                ", soLuongTon=" + soLuongTon +
                ", hanSuDung=" + hanSuDung +
                ", moTa='" + moTa + '\'' +
                ", danhMucThuoc=" + (danhMucThuoc != null ? danhMucThuoc.getMaDanhMuc() : "null") +
                ", hinhAnh='" + hinhAnh + '\'' +
                ", thanhPhan='" + thanhPhan + '\'' +
                ", ngaySanXuat=" + ngaySanXuat +
                ", xuatXu='" + xuatXu + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(maThuoc);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Thuoc)) {
            return false;
        }
        Thuoc other = (Thuoc) obj;
        return Objects.equals(maThuoc, other.maThuoc);
    }
}
