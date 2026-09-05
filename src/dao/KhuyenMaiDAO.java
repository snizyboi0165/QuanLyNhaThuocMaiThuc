package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectdb.DatabaseConnection;
import entity.KhuyenMai;
import entity.Thue;

public class KhuyenMaiDAO {
	private Connection connection;
    
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

    private void ensureLapHangNamColumn(Connection con) throws SQLException {
        String checkSql = "SELECT COL_LENGTH('dbo.KhuyenMai', 'lapHangNam')";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && rs.getObject(1) != null) {
                return;
            }
        }
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate("ALTER TABLE KhuyenMai ADD lapHangNam BIT DEFAULT 1 NOT NULL");
        }
    }

    private void ensureDaXoaColumn(Connection con) throws SQLException {
        String checkSql = "SELECT COL_LENGTH('dbo.KhuyenMai', 'daXoa')";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && rs.getObject(1) != null) {
                return;
            }
        }
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate("ALTER TABLE KhuyenMai ADD daXoa BIT DEFAULT 0 NOT NULL");
        }
    }

    private void ensureKhuyenMaiColumns(Connection con) throws SQLException {
        ensureLapHangNamColumn(con);
        ensureDaXoaColumn(con);
    }
    
    public ArrayList<KhuyenMai> getDsKhuyenMai() throws SQLException {
    	ArrayList<KhuyenMai> temp = new ArrayList<>();
    	String sql = "SELECT * FROM KhuyenMai WHERE daXoa = 0";
    	try (Connection con = getSafeConnection()) {
            ensureKhuyenMaiColumns(con);
    		Statement stmt = con.createStatement();
    		try (ResultSet rs = stmt.executeQuery(sql)) {
    			while (rs.next()) {
    				String maKM = rs.getString("maKM");
    				String tenThue = rs.getString("tenKm");
    				Date ngayBatdau = rs.getDate("ngayBatDau");
    				Date ngayKetThuc = rs.getDate("ngayKetThuc");
    				double phanTramGiamGia = rs.getDouble("phanTramGiamGia");
    				KhuyenMai km = new KhuyenMai(maKM, tenThue, ngayBatdau, ngayKetThuc, phanTramGiamGia,
                            rs.getBoolean("lapHangNam"));
                    temp.add(km);
    			}
    		}
    	}
    	return temp;
    }
 // Tự động phát sinh mã khuyến mãi
    public String generateMaKM() throws SQLException {
        String maKM = "KM00001";
        String sql = "SELECT dbo.fn_GenerateMaKM() AS MaKMMoi";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                maKM = rs.getString("MaKMMoi");
            }
        }
        return maKM;
    }

    // Thêm khuyến mãi mới
    public boolean themKhuyenMai(KhuyenMai km) throws SQLException {
        String sql = "INSERT INTO KhuyenMai(maKM, tenKM, ngayBatDau, ngayKetThuc, phanTramGiamGia, lapHangNam) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getInstance().getConnection()) {
            ensureKhuyenMaiColumns(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setString(1, km.getMaKM());
                stmt.setString(2, km.getTenKM());
                stmt.setDate(3, new java.sql.Date(km.getNgayBatDau().getTime()));
                stmt.setDate(4, new java.sql.Date(km.getNgayKetThuc().getTime()));
                stmt.setDouble(5, km.getPhanTramGiamGia());
                stmt.setBoolean(6, km.isLapHangNam());

                return stmt.executeUpdate() > 0;
            }
        }
    }

    // Lấy thông tin khuyến mãi theo mã
    public KhuyenMai getKhuyenMaiTheoMa(String maKM) throws SQLException {
        String sql = "SELECT * FROM KhuyenMai WHERE maKM = ?";
        try (Connection con = DatabaseConnection.getInstance().getConnection()) {
            ensureKhuyenMaiColumns(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maKM);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new KhuyenMai(
                        rs.getString("maKM"),
                        rs.getString("tenKM"),
                        rs.getDate("ngayBatDau"),
                        rs.getDate("ngayKetThuc"),
                        rs.getDouble("phanTramGiamGia"),
                        rs.getBoolean("lapHangNam")
                    );
                }
            }
        }
        return null;
    }

    // Cập nhật thông tin khuyến mãi
    public boolean capNhatKhuyenMai(KhuyenMai km) throws SQLException {
        String sql = "UPDATE KhuyenMai SET tenKM = ?, ngayBatDau = ?, ngayKetThuc = ?, phanTramGiamGia = ?, lapHangNam = ? WHERE maKM = ?";
        try (Connection con = DatabaseConnection.getInstance().getConnection()) {
            ensureKhuyenMaiColumns(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setString(1, km.getTenKM());
                stmt.setDate(2, new java.sql.Date(km.getNgayBatDau().getTime()));
                stmt.setDate(3, new java.sql.Date(km.getNgayKetThuc().getTime()));
                stmt.setDouble(4, km.getPhanTramGiamGia());
                stmt.setBoolean(5, km.isLapHangNam());
                stmt.setString(6, km.getMaKM());

                return stmt.executeUpdate() > 0;
            }
        }
    }

    // Xóa khuyến mãi theo mã
    public boolean xoaKhuyenMai(String maKM) throws SQLException {
        String sql = "UPDATE KhuyenMai SET daXoa = 1 WHERE maKM = ?";
        try (Connection con = DatabaseConnection.getInstance().getConnection()) {
            ensureKhuyenMaiColumns(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maKM);
                return stmt.executeUpdate() > 0;
            }
        }
    }
}
