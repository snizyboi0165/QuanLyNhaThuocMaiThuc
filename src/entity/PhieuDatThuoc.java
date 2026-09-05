package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class PhieuDatThuoc implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String maPhieuDat;
    private Date ngayDat;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private Thue thue;
    private KhuyenMai khuyenMai;
    private String diaChi;
    private String hinhThucThanhToan;
    private String trangThai;
    
    public static final String REGEX_MA_PHIEU_DAT = "^PDT\\d{5}$";
    
    public PhieuDatThuoc() {
        this("", new Date(), null, "", "Tại chỗ", "Chưa hoàn thành");
    }
    
    public PhieuDatThuoc(String maPhieuDat, Date ngayDat, KhachHang khachHang, 
                         String diaChi, String hinhThucThanhToan, String trangThai) {
        this(maPhieuDat, ngayDat, khachHang, null, diaChi, hinhThucThanhToan, trangThai);
    }

    public PhieuDatThuoc(String maPhieuDat, Date ngayDat, KhachHang khachHang, NhanVien nhanVien,
                         String diaChi, String hinhThucThanhToan, String trangThai) {
        this(maPhieuDat, ngayDat, khachHang, nhanVien, null, null, diaChi, hinhThucThanhToan, trangThai);
    }

    public PhieuDatThuoc(String maPhieuDat, Date ngayDat, KhachHang khachHang, NhanVien nhanVien,
                         Thue thue, KhuyenMai khuyenMai, String diaChi, String hinhThucThanhToan,
                         String trangThai) {
        setMaPhieuDat(maPhieuDat);
        setNgayDat(ngayDat);
        setKhachHang(khachHang);
        setNhanVien(nhanVien);
        setThue(thue);
        setKhuyenMai(khuyenMai);
        setDiaChi(diaChi);
        setHinhThucThanhToan(hinhThucThanhToan);
        setTrangThai(trangThai);
    }

    
    public PhieuDatThuoc(String maPDT) {
		setMaPhieuDat(maPDT);
	}

	public void setMaPhieuDat(String maPhieuDat) {
        if (maPhieuDat == null || maPhieuDat.trim().isEmpty()) {
            this.maPhieuDat = "";
        } else if (!maPhieuDat.matches(REGEX_MA_PHIEU_DAT)) {
            throw new IllegalArgumentException("Mã phiếu đặt thuốc phải có dạng PDT + 5 chữ số (VD: PDT00001)");
        } else {
            this.maPhieuDat = maPhieuDat;
        }
    }
    
    public void setNgayDat(Date ngayDat) {
        this.ngayDat = ngayDat;
    }
    
    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public void setThue(Thue thue) {
        this.thue = thue;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }
    
    public void setDiaChi(String diaChi) {
        if (diaChi == null || diaChi.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống");
        }
        this.diaChi = diaChi;
    }
    
    public void setHinhThucThanhToan(String hinhThucThanhToan) {
        if (hinhThucThanhToan == null || 
            (!hinhThucThanhToan.equals("Thanh toán online") && 
             !hinhThucThanhToan.equals("Tại chỗ"))) {
            throw new IllegalArgumentException("Hình thức thanh toán phải là Thanh toán online hoặc Tại chỗ");
        }
        this.hinhThucThanhToan = hinhThucThanhToan;
    }
    
    public void setTrangThai(String trangThai) {
        if (trangThai == null || 
            (!trangThai.equals("Đã hoàn thành") && 
             !trangThai.equals("Chưa hoàn thành"))) {
            throw new IllegalArgumentException("Trạng thái phải là Đã hoàn thành hoặc Chưa hoàn thành");
        }
        this.trangThai = trangThai;
    }
    
    public String getMaPhieuDat() {
        return maPhieuDat;
    }
    
    public Date getNgayDat() {
        return ngayDat;
    }
    
    public KhachHang getKhachHang() {
        return khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public Thue getThue() {
        return thue;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }
    
    public String getDiaChi() {
        return diaChi;
    }
    
    public String getHinhThucThanhToan() {
        return hinhThucThanhToan;
    }
    
    public String getTrangThai() {
        return trangThai;
    }
    

    @Override
    public int hashCode() {
        return Objects.hash(maPhieuDat);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PhieuDatThuoc other = (PhieuDatThuoc) obj;
        return Objects.equals(maPhieuDat, other.maPhieuDat);
    }
}
