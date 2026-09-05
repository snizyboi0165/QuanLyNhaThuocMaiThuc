package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class KhuyenMai implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String maKM;
    private String tenKM;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private double phanTramGiamGia;
    private boolean lapHangNam;
    
    public static final String REGEX_MA_KM = "^KM\\d{5}$";
    
    public KhuyenMai() {
        this("", "", new Date(), new Date(), 10.0, true);
    }
    
    public KhuyenMai(String maKM, String tenKM, Date ngayBatDau, Date ngayKetThuc, double phanTramGiamGia) {
        this(maKM, tenKM, ngayBatDau, ngayKetThuc, phanTramGiamGia, true);
    }

    public KhuyenMai(String maKM, String tenKM, Date ngayBatDau, Date ngayKetThuc, double phanTramGiamGia,
            boolean lapHangNam) {
        setMaKM(maKM);
        setTenKM(tenKM);
        setNgayBatDau(ngayBatDau);
        setNgayKetThuc(ngayKetThuc);
        setPhanTramGiamGia(phanTramGiamGia);
        setLapHangNam(lapHangNam);
    }
    
    public KhuyenMai(String maKM2) {
		setMaKM(maKM2);
	}

	public void setMaKM(String maKM) {
        if (maKM == null || maKM.trim().isEmpty()) {
            this.maKM = "";
        } else if (!maKM.matches(REGEX_MA_KM)) {
            throw new IllegalArgumentException("Mã khuyến mãi phải có dạng KM + 5 chữ số (VD: KM00001)");
        } else {
            this.maKM = maKM;
        }
    }
    
    public void setTenKM(String tenKM) {
        if (tenKM == null || tenKM.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khuyến mãi không được rỗng");
        }
        this.tenKM = tenKM;
    }
    
    public void setNgayBatDau(Date ngayBatDau) {
//        Date currentDate = new Date();
//        if (ngayBatDau != null && ngayBatDau.before(currentDate)) {
//            throw new IllegalArgumentException("Ngày bắt đầu phải sau ngày hiện tại");
//        }
        this.ngayBatDau = ngayBatDau;
    }
    
    public void setNgayKetThuc(Date ngayKetThuc) {
        if (ngayKetThuc != null && this.ngayBatDau != null && ngayKetThuc.before(this.ngayBatDau)) {
            throw new IllegalArgumentException("Ngày kết thúc phải >= ngày bắt đầu");
        }
        this.ngayKetThuc = ngayKetThuc;
    }
    
    public void setPhanTramGiamGia(double phanTramGiamGia) {
        if (phanTramGiamGia <= 0 || phanTramGiamGia > 100) {
            throw new IllegalArgumentException("Phần trăm giảm giá phải nằm trong khoảng từ 0 tới 100");
        }
        this.phanTramGiamGia = phanTramGiamGia;
    }
    
    public String getMaKM() {
        return maKM;
    }
    
    public String getTenKM() {
        return tenKM;
    }
    
    public Date getNgayBatDau() {
        return ngayBatDau;
    }
    
    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }
    
    public double getPhanTramGiamGia() {
        return phanTramGiamGia;
    }

    public boolean isLapHangNam() {
        return lapHangNam;
    }

    public void setLapHangNam(boolean lapHangNam) {
        this.lapHangNam = lapHangNam;
    }
    
    @Override
    public String toString() {
        return "KhuyenMai{" +
                "maKM='" + maKM + '\'' +
                ", tenKM='" + tenKM + '\'' +
                ", ngayBatDau=" + ngayBatDau +
                ", ngayKetThuc=" + ngayKetThuc +
                ", phanTramGiamGia=" + phanTramGiamGia + "%" +
                ", lapHangNam=" + lapHangNam +
                '}';
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(maKM);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KhuyenMai other = (KhuyenMai) obj;
        return Objects.equals(maKM, other.maKM);
    }
}
