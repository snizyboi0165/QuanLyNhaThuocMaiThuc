package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import connectdb.DatabaseConnection;

public class ThongKeLoiNhuanDAO {
    private static final String DOANH_THU =
            "(cthd.soLuong * cthd.donGia "
            + "* (1 - ISNULL(km.phanTramGiamGia, 0) / 100.0) "
            + "* (1 + ISNULL(thue.phanTramThue, 0) / 100.0))";
    
    /**
     * Class để lưu thông tin thống kê sản phẩm
     */
    public static class ThongKeSanPham {
        private String maThuoc;
        private String tenThuoc;
        private int soLuongBan;
        private double doanhThu;
        
        public ThongKeSanPham(String maThuoc, String tenThuoc, int soLuongBan, double doanhThu) {
            this.maThuoc = maThuoc;
            this.tenThuoc = tenThuoc;
            this.soLuongBan = soLuongBan;
            this.doanhThu = doanhThu;
        }
        
        public String getMaThuoc() { return maThuoc; }
        public String getTenThuoc() { return tenThuoc; }
        public int getSoLuongBan() { return soLuongBan; }
        public double getDoanhThu() { return doanhThu; }
    }
    
    /**
     * Class để lưu thông tin thống kê khách hàng
     */
    public static class ThongKeKhachHang {
        private String maKH;
        private String tenKH;
        private int soHoaDon;
        private double tongTienMua;
        
        public ThongKeKhachHang(String maKH, String tenKH, int soHoaDon, double tongTienMua) {
            this.maKH = maKH;
            this.tenKH = tenKH;
            this.soHoaDon = soHoaDon;
            this.tongTienMua = tongTienMua;
        }
        
        public String getMaKH() { return maKH; }
        public String getTenKH() { return tenKH; }
        public int getSoHoaDon() { return soHoaDon; }
        public double getTongTienMua() { return tongTienMua; }
    }
    
    /**
     * Class để lưu thông tin thống kê nhân viên
     */
    public static class ThongKeNhanVien {
        private String maNV;
        private String tenNV;
        private int soHoaDonBan;
        private double doanhThu;
        
        public ThongKeNhanVien(String maNV, String tenNV, int soHoaDonBan, double doanhThu) {
            this.maNV = maNV;
            this.tenNV = tenNV;
            this.soHoaDonBan = soHoaDonBan;
            this.doanhThu = doanhThu;
        }
        
        public String getMaNV() { return maNV; }
        public String getTenNV() { return tenNV; }
        public int getSoHoaDonBan() { return soHoaDonBan; }
        public double getDoanhThu() { return doanhThu; }
    }
    
    /**
     * Class để lưu thông tin top sản phẩm
     */
    public static class TopSanPham {
        private String maThuoc;
        private String tenThuoc;
        private int soLuongBan;
        private double doanhThu;
        
        public TopSanPham(String maThuoc, String tenThuoc, int soLuongBan, double doanhThu) {
            this.maThuoc = maThuoc;
            this.tenThuoc = tenThuoc;
            this.soLuongBan = soLuongBan;
            this.doanhThu = doanhThu;
        }
        
        public String getMaThuoc() { return maThuoc; }
        public String getTenThuoc() { return tenThuoc; }
        public int getSoLuongBan() { return soLuongBan; }
        public double getDoanhThu() { return doanhThu; }
    }
    
    /**
     * Class để lưu thông tin thống kê theo ngày
     */
    public static class ThongKeTheoNgay {
        private String ngay;
        private double doanhThu;
        
        public ThongKeTheoNgay(String ngay, double doanhThu) {
            this.ngay = ngay;
            this.doanhThu = doanhThu;
        }
        
        public String getNgay() { return ngay; }
        public double getDoanhThu() { return doanhThu; }
    }
    
    /**
     * Class để lưu thông tin thống kê theo tháng/năm
     */
    public static class ThongKeTheoThang {
        private String thangNam;
        private double doanhThu;
        
        public ThongKeTheoThang(String thangNam, double doanhThu) {
            this.thangNam = thangNam;
            this.doanhThu = doanhThu;
        }
        
        public String getThangNam() { return thangNam; }
        public double getDoanhThu() { return doanhThu; }
    }
    
    private Connection getSafeConnection() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn == null || conn.isClosed()) {
            conn = DatabaseConnection.getInstance().getConnection();
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Không thể thiết lập kết nối đến cơ sở dữ liệu");
            }
        }
        return conn;
    }
    
    /**
     * Thống kê theo sản phẩm
     */
    public ArrayList<ThongKeSanPham> thongKeSanPham(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        ArrayList<ThongKeSanPham> ketQua = new ArrayList<>();
        
        String sql = "SELECT " +
                    "t.maThuoc, " +
                    "t.tenThuoc, " +
                    "SUM(cthd.soLuong) as soLuongBan, " +
                    "SUM(" + DOANH_THU + ") as doanhThu " +
                    "FROM ChiTietHoaDon cthd " +
                    "INNER JOIN HoaDon hd ON cthd.maHD = hd.maHD " +
                    "INNER JOIN Thuoc t ON cthd.maThuoc = t.maThuoc " +
                    "LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM " +
                    "LEFT JOIN Thue thue ON hd.maThue = thue.maThue " +
                    "WHERE CAST(hd.ngayLap AS DATE) BETWEEN ? AND ? " +
                    "GROUP BY t.maThuoc, t.tenThuoc " +
                    "ORDER BY doanhThu DESC";
        
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            con = getSafeConnection();
            stmt = con.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(tuNgay));
            stmt.setDate(2, java.sql.Date.valueOf(denNgay));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                String maThuoc = rs.getString("maThuoc");
                String tenThuoc = rs.getString("tenThuoc");
                int soLuongBan = rs.getInt("soLuongBan");
                double doanhThu = rs.getDouble("doanhThu");
                
                ThongKeSanPham sp = new ThongKeSanPham(maThuoc, tenThuoc, soLuongBan, doanhThu);
                ketQua.add(sp);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
        
        return ketQua;
    }

    /**
     * Sản phẩm bán chậm
     */
    public ArrayList<TopSanPham> thongKeSanPhamBanCham(LocalDate tuNgay, LocalDate denNgay, int topN) throws SQLException {
        ArrayList<TopSanPham> ketQua = new ArrayList<>();
        
        String sql = "SELECT TOP " + topN + " " +
                    "t.maThuoc, " +
                    "t.tenThuoc, " +
                    "SUM(cthd.soLuong) as soLuongBan, " +
                    "SUM(" + DOANH_THU + ") as doanhThu " +
                    "FROM ChiTietHoaDon cthd " +
                    "INNER JOIN HoaDon hd ON cthd.maHD = hd.maHD " +
                    "INNER JOIN Thuoc t ON cthd.maThuoc = t.maThuoc " +
                    "LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM " +
                    "LEFT JOIN Thue thue ON hd.maThue = thue.maThue " +
                    "WHERE CAST(hd.ngayLap AS DATE) BETWEEN ? AND ? " +
                    "GROUP BY t.maThuoc, t.tenThuoc " +
                    "ORDER BY soLuongBan ASC, doanhThu ASC";
        
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            con = getSafeConnection();
            stmt = con.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(tuNgay));
            stmt.setDate(2, java.sql.Date.valueOf(denNgay));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                String maThuoc = rs.getString("maThuoc");
                String tenThuoc = rs.getString("tenThuoc");
                int soLuongBan = rs.getInt("soLuongBan");
                double doanhThu = rs.getDouble("doanhThu");
                
                TopSanPham sp = new TopSanPham(maThuoc, tenThuoc, soLuongBan, doanhThu);
                ketQua.add(sp);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
        
        return ketQua;
    }
    
    /**
     * Thống kê theo khách hàng
     */
    public ArrayList<ThongKeKhachHang> thongKeKhachHang(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        ArrayList<ThongKeKhachHang> ketQua = new ArrayList<>();
        
        String sql = "SELECT " +
                    "COALESCE(kh.maKH, 'KHACHLE') as maKH, " +
                    "COALESCE(kh.hoTen, N'Khách lẻ') as hoTen, " +
                    "COUNT(DISTINCT hd.maHD) as soHoaDon, " +
                    "SUM(" + DOANH_THU + ") as tongTienMua " +
                    "FROM HoaDon hd " +
                    "LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH " +
                    "INNER JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD " +
                    "LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM " +
                    "LEFT JOIN Thue thue ON hd.maThue = thue.maThue " +
                    "WHERE CAST(hd.ngayLap AS DATE) BETWEEN ? AND ? " +
                    "GROUP BY COALESCE(kh.maKH, 'KHACHLE'), COALESCE(kh.hoTen, N'Khách lẻ') " +
                    "ORDER BY tongTienMua DESC";
        
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            con = getSafeConnection();
            stmt = con.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(tuNgay));
            stmt.setDate(2, java.sql.Date.valueOf(denNgay));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                String maKH = rs.getString("maKH");
                String tenKH = rs.getString("hoTen");
                int soHoaDon = rs.getInt("soHoaDon");
                double tongTienMua = rs.getDouble("tongTienMua");
                
                ThongKeKhachHang kh = new ThongKeKhachHang(maKH, tenKH, soHoaDon, tongTienMua);
                ketQua.add(kh);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
        
        return ketQua;
    }
    
    /**
     * Thống kê theo nhân viên
     */
    public ArrayList<ThongKeNhanVien> thongKeNhanVien(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        ArrayList<ThongKeNhanVien> ketQua = new ArrayList<>();
        
        String sql = "SELECT " +
                    "nv.maNV, " +
                    "nv.hoTen, " +
                    "COUNT(DISTINCT hd.maHD) as soHoaDonBan, " +
                    "SUM(" + DOANH_THU + ") as doanhThu " +
                    "FROM HoaDon hd " +
                    "INNER JOIN NhanVien nv ON hd.maNV = nv.maNV " +
                    "INNER JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD " +
                    "LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM " +
                    "LEFT JOIN Thue thue ON hd.maThue = thue.maThue " +
                    "WHERE CAST(hd.ngayLap AS DATE) BETWEEN ? AND ? " +
                    "GROUP BY nv.maNV, nv.hoTen " +
                    "ORDER BY doanhThu DESC";
        
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            con = getSafeConnection();
            stmt = con.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(tuNgay));
            stmt.setDate(2, java.sql.Date.valueOf(denNgay));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                String tenNV = rs.getString("hoTen");
                int soHoaDonBan = rs.getInt("soHoaDonBan");
                double doanhThu = rs.getDouble("doanhThu");
                
                ThongKeNhanVien nv = new ThongKeNhanVien(maNV, tenNV, soHoaDonBan, doanhThu);
                ketQua.add(nv);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
        
        return ketQua;
    }
    
    /**
     * Top sản phẩm
     */
    public ArrayList<TopSanPham> thongKeTopSanPham(LocalDate tuNgay, LocalDate denNgay, int topN) throws SQLException {
        ArrayList<TopSanPham> ketQua = new ArrayList<>();
        
        String sql = "SELECT TOP " + topN + " " +
                    "t.maThuoc, " +
                    "t.tenThuoc, " +
                    "SUM(cthd.soLuong) as soLuongBan, " +
                    "SUM(" + DOANH_THU + ") as doanhThu " +
                    "FROM ChiTietHoaDon cthd " +
                    "INNER JOIN HoaDon hd ON cthd.maHD = hd.maHD " +
                    "INNER JOIN Thuoc t ON cthd.maThuoc = t.maThuoc " +
                    "LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM " +
                    "LEFT JOIN Thue thue ON hd.maThue = thue.maThue " +
                    "WHERE CAST(hd.ngayLap AS DATE) BETWEEN ? AND ? " +
                    "GROUP BY t.maThuoc, t.tenThuoc " +
                    "ORDER BY soLuongBan DESC, doanhThu DESC";
        
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            con = getSafeConnection();
            stmt = con.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(tuNgay));
            stmt.setDate(2, java.sql.Date.valueOf(denNgay));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                String maThuoc = rs.getString("maThuoc");
                String tenThuoc = rs.getString("tenThuoc");
                int soLuongBan = rs.getInt("soLuongBan");
                double doanhThu = rs.getDouble("doanhThu");
                
                TopSanPham sp = new TopSanPham(maThuoc, tenThuoc, soLuongBan, doanhThu);
                ketQua.add(sp);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
        
        return ketQua;
    }
    
    /**
     * Thống kê theo ngày
     */
    public ArrayList<ThongKeTheoNgay> thongKeTheoNgay(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        ArrayList<ThongKeTheoNgay> ketQua = new ArrayList<>();
        
        String sql = "SELECT " +
                    "CONVERT(VARCHAR(10), CAST(hd.ngayLap AS DATE), 103) as ngay, " +
                    "SUM(" + DOANH_THU + ") as doanhThu " +
                    "FROM HoaDon hd " +
                    "INNER JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD " +
                    "LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM " +
                    "LEFT JOIN Thue thue ON hd.maThue = thue.maThue " +
                    "WHERE CAST(hd.ngayLap AS DATE) BETWEEN ? AND ? " +
                    "GROUP BY CAST(hd.ngayLap AS DATE) " +
                    "ORDER BY CAST(hd.ngayLap AS DATE) ASC";
        
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            con = getSafeConnection();
            stmt = con.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(tuNgay));
            stmt.setDate(2, java.sql.Date.valueOf(denNgay));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                String ngay = rs.getString("ngay");
                double doanhThu = rs.getDouble("doanhThu");
                
                ThongKeTheoNgay item = new ThongKeTheoNgay(ngay, doanhThu);
                ketQua.add(item);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
        
        return ketQua;
    }
    
    /**
     * Thống kê theo tháng
     */
    public ArrayList<ThongKeTheoThang> thongKeTheoThang(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        ArrayList<ThongKeTheoThang> ketQua = new ArrayList<>();
        
        String sql = "SELECT " +
                    "MONTH(hd.ngayLap) as thang, " +
                    "YEAR(hd.ngayLap) as nam, " +
                    "SUM(" + DOANH_THU + ") as doanhThu " +
                    "FROM HoaDon hd " +
                    "INNER JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD " +
                    "LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM " +
                    "LEFT JOIN Thue thue ON hd.maThue = thue.maThue " +
                    "WHERE CAST(hd.ngayLap AS DATE) BETWEEN ? AND ? " +
                    "GROUP BY YEAR(hd.ngayLap), MONTH(hd.ngayLap) " +
                    "ORDER BY YEAR(hd.ngayLap) ASC, MONTH(hd.ngayLap) ASC";
        
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            con = getSafeConnection();
            stmt = con.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(tuNgay));
            stmt.setDate(2, java.sql.Date.valueOf(denNgay));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                int thang = rs.getInt("thang");
                int nam = rs.getInt("nam");
                double doanhThu = rs.getDouble("doanhThu");
                
                String thangNam = String.format("Tháng %02d/%d", thang, nam);
                ThongKeTheoThang item = new ThongKeTheoThang(thangNam, doanhThu);
                ketQua.add(item);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
        
        return ketQua;
    }
    
    /**
     * Thống kê theo năm
     */
    public ArrayList<ThongKeTheoThang> thongKeTheoNam(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        ArrayList<ThongKeTheoThang> ketQua = new ArrayList<>();
        
        String sql = "SELECT " +
                    "YEAR(hd.ngayLap) as nam, " +
                    "SUM(" + DOANH_THU + ") as doanhThu " +
                    "FROM HoaDon hd " +
                    "INNER JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD " +
                    "LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM " +
                    "LEFT JOIN Thue thue ON hd.maThue = thue.maThue " +
                    "WHERE CAST(hd.ngayLap AS DATE) BETWEEN ? AND ? " +
                    "GROUP BY YEAR(hd.ngayLap) " +
                    "ORDER BY YEAR(hd.ngayLap) ASC";
        
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            con = getSafeConnection();
            stmt = con.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(tuNgay));
            stmt.setDate(2, java.sql.Date.valueOf(denNgay));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                int nam = rs.getInt("nam");
                double doanhThu = rs.getDouble("doanhThu");
                
                String namStr = "Năm " + nam;
                ThongKeTheoThang item = new ThongKeTheoThang(namStr, doanhThu);
                ketQua.add(item);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
        
        return ketQua;
    }
    
 // Thêm phương thức này vào cuối class ThongKeLoiNhuanDAO

    /**
     * Kiểm tra xem có dữ liệu hóa đơn trong khoảng thời gian hay không
     */
    public boolean kiemTraCoDuLieu(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM HoaDon hd " +
                    "WHERE CAST(hd.ngayLap AS DATE) BETWEEN ? AND ? ";
        
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            con = getSafeConnection();
            stmt = con.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(tuNgay));
            stmt.setDate(2, java.sql.Date.valueOf(denNgay));
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            return false;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
    }
    
}
