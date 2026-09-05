package entity;

import java.io.Serializable;
import java.util.Objects;

public class NhaCungCap implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String maNCC;
    private String tenNCC;
    private String soDienThoai;
    
    public static final String REGEX_MA_NCC = "^NCC\\d{5}$";
    public static final String REGEX_SO_DIEN_THOAI = "^(\\+84|0)\\d{9}$";
    
    public NhaCungCap() {
        this("", "", "");
    }
    
    public NhaCungCap(String maNCC, String tenNCC, String soDienThoai) {
        setMaNCC(maNCC);
        setTenNCC(tenNCC);
        setSoDienThoai(soDienThoai);
    }
    
    public NhaCungCap(NhaCungCap ncc) {
        if (ncc != null) {
            this.maNCC = ncc.maNCC;
            this.tenNCC = ncc.tenNCC;
            this.soDienThoai = ncc.soDienThoai;
        }
    }
    
    public NhaCungCap(String maNCC) {
        this.maNCC = maNCC;
    }
    
    public void setMaNCC(String maNCC) {
        if (maNCC == null || maNCC.trim().isEmpty()) {
            this.maNCC = "";
        } else if (!maNCC.matches(REGEX_MA_NCC)) {
            throw new IllegalArgumentException("Mã nhà cung cấp phải có dạng NCC + 5 chữ số (VD: NCC00001)");
        } else {
            this.maNCC = maNCC;
        }
    }
    
    public void setTenNCC(String tenNCC) {
        if (tenNCC == null || tenNCC.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhà cung cấp không được rỗng");
        }
        this.tenNCC = tenNCC;
    }
    
    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || !soDienThoai.matches(REGEX_SO_DIEN_THOAI)) {
            throw new IllegalArgumentException("Số điện thoại phải bắt đầu (+84 | 0) + 9 số");
        }
        this.soDienThoai = soDienThoai;
    }
    
    public String getMaNCC() {
        return maNCC;
    }
    
    public String getTenNCC() {
        return tenNCC;
    }
    
    public String getSoDienThoai() {
        return soDienThoai;
    }
    
    @Override
    public String toString() {
        return "NhaCungCap{" +
                "maNCC='" + maNCC + '\'' +
                ", tenNCC='" + tenNCC + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                '}';
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(maNCC);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NhaCungCap other = (NhaCungCap) obj;
        return Objects.equals(maNCC, other.maNCC);
    }
}
