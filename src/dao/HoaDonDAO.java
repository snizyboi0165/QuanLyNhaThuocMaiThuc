package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import connectdb.DatabaseConnection;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.PhieuDatThuoc;
import entity.Thue;

public class HoaDonDAO {
    private static final String DOANH_THU =
            "(cthd.soLuong * cthd.donGia "
            + "* (1 - ISNULL(km.phanTramGiamGia, 0) / 100.0) "
            + "* (1 + ISNULL(thue.phanTramThue, 0) / 100.0))";

    public static class HoaDonHienThi {
        private final String maHD;
        private final Date ngayLap;
        private final String maNV;
        private final String tenNV;
        private final String maKH;
        private final String tenKH;
        private final String maThue;
        private final String maKM;
        private final String maPhieuDat;

        public HoaDonHienThi(String maHD, Date ngayLap, String maNV, String tenNV,
                String maKH, String tenKH, String maThue, String maKM, String maPhieuDat) {
            this.maHD = maHD;
            this.ngayLap = ngayLap;
            this.maNV = maNV;
            this.tenNV = tenNV;
            this.maKH = maKH;
            this.tenKH = tenKH;
            this.maThue = maThue;
            this.maKM = maKM;
            this.maPhieuDat = maPhieuDat;
        }

        public String getMaHD() {
            return maHD;
        }

        public Date getNgayLap() {
            return ngayLap;
        }

        public String getMaNV() {
            return maNV;
        }

        public String getTenNV() {
            return tenNV;
        }

        public String getMaKH() {
            return maKH;
        }

        public String getTenKH() {
            return tenKH;
        }

        public String getMaThue() {
            return maThue;
        }

        public String getMaKM() {
            return maKM;
        }

        public String getMaPhieuDat() {
            return maPhieuDat;
        }
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

    private void damBaoCotNgayLapCoGio(Connection con) throws SQLException {
        String checkSql = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE TABLE_NAME = 'HoaDon' AND COLUMN_NAME = 'ngayLap'";
        try (Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && !"date".equalsIgnoreCase(rs.getString("DATA_TYPE"))) {
                return;
            }
        }
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate("ALTER TABLE HoaDon ALTER COLUMN ngayLap DATETIME NOT NULL");
        }
    }
    
    /**
     * Lấy tất cả hóa đơn
     */
    public ArrayList<HoaDon> getDsHoaDon() throws SQLException {
        ArrayList<HoaDon> temp = new ArrayList<HoaDon>();
        String sql = "SELECT * FROM HoaDon ORDER BY CAST(SUBSTRING(maHD, 3, 5) AS INT) DESC";
        try (Connection con = getSafeConnection()) {
            damBaoCotNgayLapCoGio(con);
            Statement stmt = con.createStatement();
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String maHD = rs.getString("maHD");
                    Date ngayLap = rs.getTimestamp("ngayLap");
                    String maThue = rs.getString("maThue");
                    String maNhanVien = rs.getString("maNV");
                    String maKH = rs.getString("maKH");
                    String maKM = rs.getString("maKM");
                    String maPDT = rs.getString("maPhieuDat");
                    
                    HoaDon hd = new HoaDon(maHD, ngayLap, 
                        maThue != null ? new Thue(maThue) : null, 
                        new NhanVien(maNhanVien), 
                        maKH != null ? new KhachHang(maKH) : null,
                        maKM != null ? new KhuyenMai(maKM) : null, 
                        maPDT != null ? new PhieuDatThuoc(maPDT) : null);
                    temp.add(hd);
                }
            }
        }
        return temp;
    }

    public ArrayList<HoaDonHienThi> getDsHoaDonHienThi() throws SQLException {
        ArrayList<HoaDonHienThi> temp = new ArrayList<>();
        String sql = "SELECT hd.maHD, hd.ngayLap, hd.maNV, nv.hoTen AS tenNV, "
                + "hd.maKH, kh.hoTen AS tenKH, hd.maThue, hd.maKM, hd.maPhieuDat "
                + "FROM HoaDon hd "
                + "LEFT JOIN NhanVien nv ON hd.maNV = nv.maNV "
                + "LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH "
                + "ORDER BY CAST(SUBSTRING(hd.maHD, 3, 5) AS INT) DESC";
        try (Connection con = getSafeConnection()) {
            damBaoCotNgayLapCoGio(con);
            try (Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    temp.add(new HoaDonHienThi(
                            rs.getString("maHD"),
                            rs.getTimestamp("ngayLap"),
                            rs.getString("maNV"),
                            rs.getString("tenNV"),
                            rs.getString("maKH"),
                            rs.getString("tenKH"),
                            rs.getString("maThue"),
                            rs.getString("maKM"),
                            rs.getString("maPhieuDat")));
                }
            }
        }
        return temp;
    }
    
    /**
     * Lấy hóa đơn theo mã
     */
    public HoaDon getHoaDonTheoMa(String maHD) throws SQLException {
        String sql = "SELECT * FROM HoaDon WHERE maHD = ?";
        
        try (Connection con = getSafeConnection()) {
            damBaoCotNgayLapCoGio(con);
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maHD);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Date ngayLap = rs.getTimestamp("ngayLap");
                    String maThue = rs.getString("maThue");
                    String maNhanVien = rs.getString("maNV");
                    String maKH = rs.getString("maKH");
                    String maKM = rs.getString("maKM");
                    String maPDT = rs.getString("maPhieuDat");
                    
                    return new HoaDon(maHD, ngayLap, 
                        maThue != null ? new Thue(maThue) : null, 
                        new NhanVien(maNhanVien), 
                        maKH != null ? new KhachHang(maKH) : null,
                        maKM != null ? new KhuyenMai(maKM) : null, 
                        maPDT != null ? new PhieuDatThuoc(maPDT) : null);
                }
            }
        }
        return null;
    }
    
    /**
     * Tạo mã hóa đơn tự động
     */
    public String generateMaHD() throws SQLException {
        String maHD = "HD00001";
        String sql = "SELECT dbo.fn_GenerateMaHD() AS MaHDMoi";
        
        try (Connection conn = getSafeConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                maHD = rs.getString("MaHDMoi");
            }
        }
        return maHD;
    }
    
    /**
     * Thêm hóa đơn mới
     */
    public boolean themHoaDon(HoaDon hd) throws SQLException {
        try (Connection con = getSafeConnection()) {
            damBaoCotNgayLapCoGio(con);
            return themHoaDon(con, hd);
        }
    }

    public boolean thanhToanHoaDon(HoaDon hd, ArrayList<ChiTietHoaDon> dsChiTietHoaDon) throws SQLException {
        return thanhToanHoaDon(hd, dsChiTietHoaDon, null, true);
    }

    public boolean thanhToanHoaDon(HoaDon hd, ArrayList<ChiTietHoaDon> dsChiTietHoaDon,
            String maPhieuDatHoanThanh) throws SQLException {
        return thanhToanHoaDon(hd, dsChiTietHoaDon, maPhieuDatHoanThanh, true);
    }

    public boolean thanhToanPhieuDat(HoaDon hd, ArrayList<ChiTietHoaDon> dsChiTietHoaDon,
            String maPhieuDatHoanThanh) throws SQLException {
        return thanhToanHoaDon(hd, dsChiTietHoaDon, maPhieuDatHoanThanh, false);
    }

    private boolean thanhToanHoaDon(HoaDon hd, ArrayList<ChiTietHoaDon> dsChiTietHoaDon,
            String maPhieuDatHoanThanh, boolean truTonKho) throws SQLException {
        if (hd == null || dsChiTietHoaDon == null || dsChiTietHoaDon.isEmpty()) {
            throw new SQLException("Hoa don phai co it nhat mot chi tiet");
        }

        try (Connection con = getSafeConnection()) {
            damBaoCotNgayLapCoGio(con);
            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            try {
                if (!themHoaDon(con, hd)) {
                    con.rollback();
                    return false;
                }

                LoThuocDAO loDAO = new LoThuocDAO();
                for (ChiTietHoaDon cthd : dsChiTietHoaDon) {
                    cthd.getHoaDon().setMaHD(hd.getMaHD());
                    if (!themChiTietHoaDon(con, cthd)) {
                        con.rollback();
                        return false;
                    }
                    if (truTonKho) {
                        String maThuoc = cthd.getThuoc().getMaThuoc();
                        if (!loDAO.truTonTheoFEFO(con, maThuoc, cthd.getSoLuong())) {
                            throw new SQLException("Ton kho lo khong du cho thuoc " + maThuoc);
                        }
                    }
                }

                if (maPhieuDatHoanThanh != null && !maPhieuDatHoanThanh.trim().isEmpty()) {
                    if (!capNhatTrangThaiPhieuDat(con, maPhieuDatHoanThanh, "Đã hoàn thành")) {
                        throw new SQLException("Khong the cap nhat trang thai phieu dat " + maPhieuDatHoanThanh);
                    }
                }

                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(oldAutoCommit);
            }
        }
    }

    private boolean themHoaDon(Connection con, HoaDon hd) throws SQLException {
        String sql = "INSERT INTO HoaDon (maHD,ngayLap,maThue,maNV,maKH,maKM,maPhieuDat)"
                + " VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, hd.getMaHD());
            stmt.setTimestamp(2, new Timestamp(hd.getNgayLap().getTime()));
            if (hd.getThue() != null && hd.getThue().getMaThue() != null) {
                stmt.setString(3, hd.getThue().getMaThue());
            } else {
                stmt.setNull(3, java.sql.Types.VARCHAR);
            }
            stmt.setString(4, hd.getNhanVien().getMaNV());

            if (hd.getKhachHang() != null && hd.getKhachHang().getMaKH() != null) {
                stmt.setString(5, hd.getKhachHang().getMaKH());
            } else {
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }

            if (hd.getKhuyenMai() != null && hd.getKhuyenMai().getMaKM() != null) {
                stmt.setString(6, hd.getKhuyenMai().getMaKM());
            } else {
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }

            if (hd.getPhieuDatThuoc() != null && hd.getPhieuDatThuoc().getMaPhieuDat() != null) {
                stmt.setString(7, hd.getPhieuDatThuoc().getMaPhieuDat());
            } else {
                stmt.setNull(7, java.sql.Types.VARCHAR);
            }

            return stmt.executeUpdate() > 0;
        }
    }

    private boolean themChiTietHoaDon(Connection con, ChiTietHoaDon cthd) throws SQLException {
        String sql = "INSERT ChiTietHoaDon (maHD,maThuoc,soLuong,donGia) VALUES (?,?,?,?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cthd.getHoaDon().getMaHD());
            stmt.setString(2, cthd.getThuoc().getMaThuoc());
            stmt.setInt(3, cthd.getSoLuong());
            stmt.setDouble(4, cthd.getDonGia());
            return stmt.executeUpdate() > 0;
        }
    }

    private boolean capNhatTrangThaiPhieuDat(Connection con, String maPhieuDat, String trangThaiMoi)
            throws SQLException {
        String sql = "UPDATE PhieuDatThuoc SET trangThai = ? WHERE maPhieuDat = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, trangThaiMoi);
            stmt.setString(2, maPhieuDat);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Tính tổng tiền của hóa đơn (từ chi tiết hóa đơn)
     */
    public double getTongTienHoaDon(String maHD) throws SQLException {
        String sql = "SELECT SUM(soLuong * donGia) AS tongTien FROM ChiTietHoaDon WHERE maHD = ?";
        
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maHD);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("tongTien");
                }
            }
        }
        return 0;
    }

    public double getDoanhThuHomNay() throws SQLException {
        String sql = "SELECT ISNULL(SUM(" + DOANH_THU + "), 0) AS doanhThu "
                + "FROM HoaDon hd "
                + "INNER JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD "
                + "LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM "
                + "LEFT JOIN Thue thue ON hd.maThue = thue.maThue "
                + "WHERE CAST(hd.ngayLap AS DATE) = CAST(GETDATE() AS DATE)";
        try (Connection con = getSafeConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("doanhThu");
            }
        }
        return 0;
    }
    
    /**
     * Tìm kiếm hóa đơn theo nhiều tiêu chí
     */
    public ArrayList<HoaDon> timKiemHoaDon(String tuKhoa, Date tuNgay, Date denNgay) throws SQLException {
        ArrayList<HoaDon> dsHD = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM HoaDon WHERE 1=1");
        
        if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
            sql.append(" AND (maHD LIKE ? OR maNV LIKE ? OR maKH LIKE ?)");
        }
        
        if (tuNgay != null) {
            sql.append(" AND ngayLap >= ?");
        }
        
        if (denNgay != null) {
            sql.append(" AND ngayLap <= ?");
        }
        
        sql.append(" ORDER BY CAST(SUBSTRING(maHD, 3, 5) AS INT) DESC");
        
        try (Connection con = getSafeConnection()) {
            damBaoCotNgayLapCoGio(con);
            PreparedStatement stmt = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            
            if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
                String keyword = "%" + tuKhoa + "%";
                stmt.setString(paramIndex++, keyword);
                stmt.setString(paramIndex++, keyword);
                stmt.setString(paramIndex++, keyword);
            }
            
            if (tuNgay != null) {
                stmt.setTimestamp(paramIndex++, new Timestamp(tuNgay.getTime()));
            }
            
            if (denNgay != null) {
                stmt.setTimestamp(paramIndex++, new Timestamp(denNgay.getTime()));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maHD = rs.getString("maHD");
                    Date ngayLap = rs.getTimestamp("ngayLap");
                    String maThue = rs.getString("maThue");
                    String maNhanVien = rs.getString("maNV");
                    String maKH = rs.getString("maKH");
                    String maKM = rs.getString("maKM");
                    String maPDT = rs.getString("maPhieuDat");
                    
                    HoaDon hd = new HoaDon(maHD, ngayLap, 
                        maThue != null ? new Thue(maThue) : null, 
                        new NhanVien(maNhanVien), 
                        maKH != null ? new KhachHang(maKH) : null,
                        maKM != null ? new KhuyenMai(maKM) : null, 
                        maPDT != null ? new PhieuDatThuoc(maPDT) : null);
                    dsHD.add(hd);
                }
            }
        }
        return dsHD;
    }
}
