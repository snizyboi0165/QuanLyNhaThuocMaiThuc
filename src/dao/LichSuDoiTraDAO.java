package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectdb.DatabaseConnection;

public class LichSuDoiTraDAO {
    
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
     * Thêm lịch sử đổi trả
     */
    public boolean themLichSu(LichSuDoiTra lichSu) throws SQLException {
        String sql = "INSERT INTO LichSuDoiTra (maLichSu, maPhieuDoiTra, nguoiThaoTac, " +
                    "thoiGianThaoTac, hanhDong, moTa, trangThaiCu, trangThaiMoi) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, lichSu.getMaLichSu());
            stmt.setString(2, lichSu.getMaPhieuDoiTra());
            stmt.setString(3, lichSu.getNguoiThaoTac());
            stmt.setTimestamp(4, new Timestamp(lichSu.getThoiGianThaoTac().getTime()));
            stmt.setString(5, lichSu.getHanhDong());
            stmt.setString(6, lichSu.getMoTa());
            stmt.setString(7, lichSu.getTrangThaiCu());
            stmt.setString(8, lichSu.getTrangThaiMoi());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Lấy lịch sử theo mã phiếu đổi trả
     */
    public List<LichSuDoiTra> getLichSuTheoMaPhieu(String maPhieuDoiTra) throws SQLException {
        List<LichSuDoiTra> danhSach = new ArrayList<>();
        String sql = "SELECT ls.*, nv.hoTen as tenNhanVien " +
                    "FROM LichSuDoiTra ls " +
                    "LEFT JOIN NhanVien nv ON ls.nguoiThaoTac = nv.maNV " +
                    "WHERE ls.maPhieuDoiTra = ? " +
                    "ORDER BY ls.thoiGianThaoTac DESC";
        
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuDoiTra);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LichSuDoiTra lichSu = new LichSuDoiTra();
                    lichSu.setMaLichSu(rs.getString("maLichSu"));
                    lichSu.setMaPhieuDoiTra(rs.getString("maPhieuDoiTra"));
                    lichSu.setNguoiThaoTac(rs.getString("nguoiThaoTac"));
                    lichSu.setThoiGianThaoTac(rs.getTimestamp("thoiGianThaoTac"));
                    lichSu.setHanhDong(rs.getString("hanhDong"));
                    lichSu.setMoTa(rs.getString("moTa"));
                    lichSu.setTrangThaiCu(rs.getString("trangThaiCu"));
                    lichSu.setTrangThaiMoi(rs.getString("trangThaiMoi"));
                    
                    danhSach.add(lichSu);
                }
            }
        }
        
        return danhSach;
    }
    
    /**
     * Lấy toàn bộ lịch sử đổi trả
     */
    public List<LichSuDoiTra> getToanBoLichSu() throws SQLException {
        List<LichSuDoiTra> danhSach = new ArrayList<>();
        String sql = "SELECT ls.*, nv.hoTen as tenNhanVien, pdt.loaiPhieu " +
                    "FROM LichSuDoiTra ls " +
                    "LEFT JOIN NhanVien nv ON ls.nguoiThaoTac = nv.maNV " +
                    "LEFT JOIN PhieuDoiTra pdt ON ls.maPhieuDoiTra = pdt.maPhieuDoiTra " +
                    "ORDER BY ls.thoiGianThaoTac DESC";
        
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                LichSuDoiTra lichSu = new LichSuDoiTra();
                lichSu.setMaLichSu(rs.getString("maLichSu"));
                lichSu.setMaPhieuDoiTra(rs.getString("maPhieuDoiTra"));
                lichSu.setNguoiThaoTac(rs.getString("nguoiThaoTac"));
                lichSu.setThoiGianThaoTac(rs.getTimestamp("thoiGianThaoTac"));
                lichSu.setHanhDong(rs.getString("hanhDong"));
                lichSu.setMoTa(rs.getString("moTa"));
                lichSu.setTrangThaiCu(rs.getString("trangThaiCu"));
                lichSu.setTrangThaiMoi(rs.getString("trangThaiMoi"));
                
                danhSach.add(lichSu);
            }
        }
        
        return danhSach;
    }
    
    /**
     * Lấy lịch sử theo người thao tác
     */
    public List<LichSuDoiTra> getLichSuTheoNguoiThaoTac(String maNhanVien) throws SQLException {
        List<LichSuDoiTra> danhSach = new ArrayList<>();
        String sql = "SELECT ls.*, nv.hoTen as tenNhanVien, pdt.loaiPhieu " +
                    "FROM LichSuDoiTra ls " +
                    "LEFT JOIN NhanVien nv ON ls.nguoiThaoTac = nv.maNV " +
                    "LEFT JOIN PhieuDoiTra pdt ON ls.maPhieuDoiTra = pdt.maPhieuDoiTra " +
                    "WHERE ls.nguoiThaoTac = ? " +
                    "ORDER BY ls.thoiGianThaoTac DESC";
        
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maNhanVien);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LichSuDoiTra lichSu = new LichSuDoiTra();
                    lichSu.setMaLichSu(rs.getString("maLichSu"));
                    lichSu.setMaPhieuDoiTra(rs.getString("maPhieuDoiTra"));
                    lichSu.setNguoiThaoTac(rs.getString("nguoiThaoTac"));
                    lichSu.setThoiGianThaoTac(rs.getTimestamp("thoiGianThaoTac"));
                    lichSu.setHanhDong(rs.getString("hanhDong"));
                    lichSu.setMoTa(rs.getString("moTa"));
                    lichSu.setTrangThaiCu(rs.getString("trangThaiCu"));
                    lichSu.setTrangThaiMoi(rs.getString("trangThaiMoi"));
                    
                    danhSach.add(lichSu);
                }
            }
        }
        
        return danhSach;
    }
    
    /**
     * Xóa lịch sử đổi trả (nếu cần)
     */
    public boolean xoaLichSu(String maLichSu) throws SQLException {
        String sql = "DELETE FROM LichSuDoiTra WHERE maLichSu = ?";
        
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maLichSu);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Generate mã lịch sử mới
     */
    public String generateMaLichSu() throws SQLException {
        String sql = "SELECT TOP 1 maLichSu FROM LichSuDoiTra ORDER BY maLichSu DESC";
        
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                String lastMa = rs.getString("maLichSu");
                if (lastMa != null && lastMa.startsWith("LS")) {
                    int number = Integer.parseInt(lastMa.substring(2)) + 1;
                    return String.format("LS%05d", number);
                }
            }
        }
        
        return "LS00001";
    }
    
    /**
     * Thống kê số lượng thao tác theo loại hành động
     */
    public List<ThongKeHanhDong> thongKeHanhDong(java.util.Date tuNgay, java.util.Date denNgay) throws SQLException {
        List<ThongKeHanhDong> danhSach = new ArrayList<>();
        String sql = "SELECT hanhDong, COUNT(*) as soLuong " +
                    "FROM LichSuDoiTra " +
                    "WHERE thoiGianThaoTac BETWEEN ? AND ? " +
                    "GROUP BY hanhDong " +
                    "ORDER BY soLuong DESC";
        
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, new Timestamp(tuNgay.getTime()));
            stmt.setTimestamp(2, new Timestamp(denNgay.getTime()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ThongKeHanhDong tk = new ThongKeHanhDong();
                    tk.setHanhDong(rs.getString("hanhDong"));
                    tk.setSoLuong(rs.getInt("soLuong"));
                    danhSach.add(tk);
                }
            }
        }
        
        return danhSach;
    }
    
    // Inner class cho thống kê
    public static class ThongKeHanhDong {
        private String hanhDong;
        private int soLuong;
        
        // Getters and setters
        public String getHanhDong() { return hanhDong; }
        public void setHanhDong(String hanhDong) { this.hanhDong = hanhDong; }
        
        public int getSoLuong() { return soLuong; }
        public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    }
    
    /**
     * Ghi log thao tác đổi trả hàng nâng cao
     */
    public boolean ghiLogDoiTra(String maHoaDon, String maNhanVien, String hanhDong, String lyDo, double chenhLech) {
        String sql = "INSERT INTO LichSuDoiTra (maHD, maNV, ngayThaoTac, hanhDong, lyDo, chenhLechTien) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maHoaDon);
            stmt.setString(2, maNhanVien);
            stmt.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
            stmt.setString(4, hanhDong);
            stmt.setString(5, lyDo);
            stmt.setDouble(6, chenhLech);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            // Nếu bảng LichSuDoiTra không tồn tại, tạo bảng mới
            if (e.getMessage().contains("Invalid object name")) {
                try {
                    createLichSuDoiTraTable();
                    return ghiLogDoiTra(maHoaDon, maNhanVien, hanhDong, lyDo, chenhLech);
                } catch (SQLException createEx) {
                    System.err.println("Không thể tạo bảng LichSuDoiTra: " + createEx.getMessage());
                    return false;
                }
            }
            System.err.println("Lỗi khi ghi log đổi trả: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tạo bảng LichSuDoiTra nếu chưa tồn tại
     */
    private void createLichSuDoiTraTable() throws SQLException {
        String createTableSQL = """
            CREATE TABLE LichSuDoiTra (
                id INT IDENTITY(1,1) PRIMARY KEY,
                maHD NVARCHAR(20) NOT NULL,
                maNV NVARCHAR(20) NOT NULL,
                ngayThaoTac DATETIME NOT NULL DEFAULT GETDATE(),
                hanhDong NVARCHAR(500) NOT NULL,
                lyDo NVARCHAR(1000),
                chenhLechTien DECIMAL(18,2) DEFAULT 0,
                FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
                FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
            )
            """;
        
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(createTableSQL);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Lấy lịch sử đổi trả theo mã hóa đơn
     */
    public List<LichSuDoiTra> getLichSuTheoHoaDon(String maHoaDon) {
        List<LichSuDoiTra> lichSu = new ArrayList<>();
        String sql = "SELECT * FROM LichSuDoiTra WHERE maHD = ? ORDER BY ngayThaoTac DESC";
        
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maHoaDon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LichSuDoiTra ls = new LichSuDoiTra();
                    ls.setId(rs.getInt("id"));
                    ls.setMaHD(rs.getString("maHD"));
                    ls.setMaNV(rs.getString("maNV"));
                    ls.setNgayThaoTac(rs.getTimestamp("ngayThaoTac"));
                    ls.setHanhDong(rs.getString("hanhDong"));
                    ls.setLyDo(rs.getString("lyDo"));
                    ls.setChenhLechTien(rs.getDouble("chenhLechTien"));
                    lichSu.add(ls);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch sử đổi trả: " + e.getMessage());
        }
        
        return lichSu;
    }
    
    /**
     * Lấy tất cả lịch sử đổi trả
     */
    public List<LichSuDoiTra> getTatCaLichSu() {
        List<LichSuDoiTra> lichSu = new ArrayList<>();
        String sql = "SELECT * FROM LichSuDoiTra ORDER BY ngayThaoTac DESC";
        
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LichSuDoiTra ls = new LichSuDoiTra();
                    ls.setId(rs.getInt("id"));
                    ls.setMaHD(rs.getString("maHD"));
                    ls.setMaNV(rs.getString("maNV"));
                    ls.setNgayThaoTac(rs.getTimestamp("ngayThaoTac"));
                    ls.setHanhDong(rs.getString("hanhDong"));
                    ls.setLyDo(rs.getString("lyDo"));
                    ls.setChenhLechTien(rs.getDouble("chenhLechTien"));
                    lichSu.add(ls);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả lịch sử đổi trả: " + e.getMessage());
        }
        
        return lichSu;
    }
    
    /**
     * Inner class để đại diện cho bản ghi lịch sử
     */
    public static class LichSuDoiTra {
        private int id;
        private String maHD;
        private String maNV;
        private Timestamp ngayThaoTac;
        private String hanhDong;
        private String lyDo;
        private double chenhLechTien;
        private String maLichSu;
        private String maPhieuDoiTra;
        private String nguoiThaoTac;
        private Timestamp thoiGianThaoTac;
        private String moTa;
        private String trangThaiCu;
        private String trangThaiMoi;
        
        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getMaHD() { return maHD; }
        public void setMaHD(String maHD) { this.maHD = maHD; }
        
        public String getMaNV() { return maNV; }
        public void setMaNV(String maNV) { this.maNV = maNV; }
        
        public Timestamp getNgayThaoTac() { return ngayThaoTac; }
        public void setNgayThaoTac(Timestamp ngayThaoTac) { this.ngayThaoTac = ngayThaoTac; }
        
        public String getHanhDong() { return hanhDong; }
        public void setHanhDong(String hanhDong) { this.hanhDong = hanhDong; }
        
        public String getLyDo() { return lyDo; }
        public void setLyDo(String lyDo) { this.lyDo = lyDo; }
        
        public double getChenhLechTien() { return chenhLechTien; }
        public void setChenhLechTien(double chenhLechTien) { this.chenhLechTien = chenhLechTien; }

        public String getMaLichSu() { return maLichSu; }
        public void setMaLichSu(String maLichSu) { this.maLichSu = maLichSu; }

        public String getMaPhieuDoiTra() { return maPhieuDoiTra; }
        public void setMaPhieuDoiTra(String maPhieuDoiTra) { this.maPhieuDoiTra = maPhieuDoiTra; }

        public String getNguoiThaoTac() { return nguoiThaoTac; }
        public void setNguoiThaoTac(String nguoiThaoTac) { this.nguoiThaoTac = nguoiThaoTac; }

        public Timestamp getThoiGianThaoTac() { return thoiGianThaoTac; }
        public void setThoiGianThaoTac(Timestamp thoiGianThaoTac) { this.thoiGianThaoTac = thoiGianThaoTac; }

        public String getMoTa() { return moTa; }
        public void setMoTa(String moTa) { this.moTa = moTa; }

        public String getTrangThaiCu() { return trangThaiCu; }
        public void setTrangThaiCu(String trangThaiCu) { this.trangThaiCu = trangThaiCu; }

        public String getTrangThaiMoi() { return trangThaiMoi; }
        public void setTrangThaiMoi(String trangThaiMoi) { this.trangThaiMoi = trangThaiMoi; }
    }
}
