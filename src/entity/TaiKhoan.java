package entity;

import java.io.Serializable;
import java.util.Objects;

public class TaiKhoan implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private NhanVien nhanVien;
    private String tenDangNhap;
    private String matKhau;
    private String trangThai;
    private String vaiTro;
    
    public static final String REGEX_MAT_KHAU = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$";
    
    public TaiKhoan() {
        
    }
    
	public TaiKhoan(String tenDangNhap, String matKhau, NhanVien nhanVien, String trangThai, String vaiTro) {
		super();
		setTenDangNhap(tenDangNhap);
		setMatKhau(matKhau);
		setNhanVien(nhanVien);
		setTrangThai(trangThai);
		setVaiTro(vaiTro);
	}
    
    public TaiKhoan(TaiKhoan tk) {
        if (tk != null) {
            this.nhanVien = tk.nhanVien;
            this.tenDangNhap = tk.tenDangNhap;
            this.matKhau = tk.matKhau;
            this.trangThai = tk.trangThai;
            this.vaiTro = tk.vaiTro;
        }
    }
    
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
    
    public void setTenDangNhap(String tenDangNhap) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được rỗng");
        }
        this.tenDangNhap = tenDangNhap;
    }
    
    public void setMatKhau(String matKhau) {
        if (matKhau == null || matKhau.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được rỗng");
        }
        this.matKhau = matKhau;
    }

    public static boolean isValidPasswordFormat(String matKhau) {
        return matKhau != null && matKhau.matches(REGEX_MAT_KHAU);
    }
    
    public void setTrangThai(String trangThai) {
        if (trangThai == null || 
            (!trangThai.equals("Hoạt động") && !trangThai.equals("Ngừng hoạt động"))) {
            throw new IllegalArgumentException("Trạng thái chỉ là Hoạt động hoặc Ngừng hoạt động");
        }
        this.trangThai = trangThai;
    }
    
    public void setVaiTro(String vaiTro) {
        if (vaiTro == null || 
            (!vaiTro.equals("Nhân viên quản lý") && !vaiTro.equals("Nhân viên bán thuốc"))) {
            throw new IllegalArgumentException("Vai trò chỉ là Nhân viên quản lý hoặc Nhân viên bán thuốc");
        }
        this.vaiTro = vaiTro;
    }
    
    public NhanVien getNhanVien() {
        return nhanVien;
    }
    
    public String getTenDangNhap() {
        return tenDangNhap;
    }
    
    public String getMatKhau() {
        return matKhau;
    }
    
    public String getTrangThai() {
        return trangThai;
    }
    
    public String getVaiTro() {
        return vaiTro;
    }
    
    @Override
    public String toString() {
        return "TaiKhoan{" +
                "nhanVien=" + (nhanVien != null ? nhanVien.getMaNV() : "null") +
                ", tenDangNhap='" + tenDangNhap + '\'' +
                ", matKhau='" + "******" + '\'' +
                ", trangThai='" + trangThai + '\'' +
                ", vaiTro='" + vaiTro + '\'' +
                '}';
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(tenDangNhap);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaiKhoan other = (TaiKhoan) obj;
        return Objects.equals(tenDangNhap, other.tenDangNhap);
    }
}
