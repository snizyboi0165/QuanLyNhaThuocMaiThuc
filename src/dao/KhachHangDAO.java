package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectdb.DatabaseConnection;
import entity.KhachHang;

public class KhachHangDAO {
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

    private void ensureDaXoaColumn(Connection con) throws SQLException {
        String checkSql = "SELECT COL_LENGTH('dbo.KhachHang', 'daXoa')";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && rs.getObject(1) != null) {
                return;
            }
        }

        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate("ALTER TABLE KhachHang ADD daXoa BIT DEFAULT 0 NOT NULL");
        }
    }

    private KhachHang mapKhachHang(ResultSet rs) throws SQLException {
        return new KhachHang(
            rs.getString("maKH"),
            rs.getString("hoTen"),
            rs.getString("soDienThoai"),
            rs.getString("email")
        );
    }
	
    public KhachHang getKhachHangTheoSDT(String soDienThoai) throws SQLException {
        String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ? AND daXoa = 0";
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            
                pstmt.setString(1, soDienThoai);
                ResultSet rs = pstmt.executeQuery();
            
                if (rs.next()) {
                    return mapKhachHang(rs);
                }
            }
        }
        return null;
    }

    public KhachHang getKhachHangTheoSDTBaoGomDaAn(String soDienThoai) throws SQLException {
        String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {

                pstmt.setString(1, soDienThoai);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    return mapKhachHang(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Lấy danh sách tất cả khách hàng
     */
    public ArrayList<KhachHang> getDSKhachHang() throws SQLException {
        ArrayList<KhachHang> dsKH = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE daXoa = 0 ORDER BY maKH";
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
            
                while (rs.next()) {
                    dsKH.add(mapKhachHang(rs));
                }
            }
        }
        return dsKH;
    }
    
    /**
     * Tự động phát sinh mã khách hàng theo quy luật KH00001, KH00002, ...
     */
    public String generateMaKH() {
        String maKH = "KH00001";
        String sql = "SELECT dbo.fn_GenerateMaKH() AS MaKHMoi";
        
        try (Connection conn = getSafeConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                maKH = rs.getString("MaKHMoi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return maKH;
    }
    
    /**
     * Thêm khách hàng mới vào database
     */
    public boolean themKhachHang(KhachHang kh) throws SQLException {
        String sql = "INSERT INTO KhachHang(maKH, hoTen, soDienThoai, email, daXoa) VALUES (?, ?, ?, ?, 0)";
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
            
                stmt.setString(1, kh.getMaKH());
                stmt.setString(2, kh.getHoTen());
                stmt.setString(3, kh.getSoDienThoai());
                stmt.setString(4, kh.getEmail());
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Lấy thông tin khách hàng theo mã
     */
    public KhachHang getKhachHangTheoMa(String maKH) throws SQLException {
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ensureDaXoaColumn(con);
            
            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapKhachHang(rs);
            }
        }
        return null;
    }
    
    /**
     * Cập nhật thông tin khách hàng
     */
    public boolean capNhatKhachHang(KhachHang kh) throws SQLException {
        String sql = "UPDATE KhachHang SET hoTen = ?, soDienThoai = ?, email = ? WHERE maKH = ?";
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ensureDaXoaColumn(con);
            
            stmt.setString(1, kh.getHoTen());
            stmt.setString(2, kh.getSoDienThoai());
            stmt.setString(3, kh.getEmail());
            stmt.setString(4, kh.getMaKH());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Xóa khách hàng theo mã
     */
    public boolean xoaKhachHang(String maKH) throws SQLException {
        String sql = "UPDATE KhachHang SET daXoa = 1 WHERE maKH = ?";
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
            
                stmt.setString(1, maKH);
            
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Tìm kiếm khách hàng theo các tiêu chí
     */
    public ArrayList<KhachHang> timKiemKhachHang(String maKH, String hoTen, 
    		String soDienThoai, String email) throws SQLException 
    {
        ArrayList<KhachHang> dsKH = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM KhachHang WHERE daXoa = 0");

        // Xây dựng câu truy vấn động theo tiêu chí nhập
        if(!maKH.isEmpty()) 
    	{
        	sql.append(" AND maKH LIKE ?");
    	
    	}
        if(!hoTen.isEmpty()) 
    	{
        	sql.append(" AND hoTen LIKE ?");
    	}
        if(!soDienThoai.isEmpty())
    	{
        	sql.append(" AND soDienThoai LIKE ?");
    	}
        if(!email.isEmpty())
    	{
    		sql.append(" AND email LIKE ?");
    	}
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement stmt = con.prepareStatement(sql.toString())) 
            {

                int index = 1;
                if(!maKH.isEmpty()) 
            	{
                	stmt.setString(index++, "%" + maKH + "%");
            	}
                if(!hoTen.isEmpty())
            	{
                	stmt.setString(index++, "%" + hoTen + "%");
            	}
                if(!soDienThoai.isEmpty())
            	{
                	stmt.setString(index++, "%" + soDienThoai + "%");
            	}
                if(!email.isEmpty())
            	{
                	stmt.setString(index++, "%" + email + "%");
            	}
                ResultSet rs = stmt.executeQuery();
                while(rs.next()) 
                {
                    dsKH.add(mapKhachHang(rs));
                }
            }
        }
        return dsKH;
    }

    
    /**
     * Kiểm tra số điện thoại đã tồn tại chưa
     */
    public boolean kiemTraSDTTonTai(String soDienThoai) throws SQLException {
        String sql = "SELECT COUNT(*) FROM KhachHang WHERE soDienThoai = ?";
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ensureDaXoaColumn(con);
            
            stmt.setString(1, soDienThoai);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean khachHangDangHoatDong(String maKH) throws SQLException {
        String sql = "SELECT COUNT(*) FROM KhachHang WHERE maKH = ? AND daXoa = 0";
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maKH);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean khoiPhucKhachHang(KhachHang kh) throws SQLException {
        String sql = "UPDATE KhachHang SET hoTen = ?, email = ?, daXoa = 0 WHERE maKH = ?";
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, kh.getHoTen());
                stmt.setString(2, kh.getEmail());
                stmt.setString(3, kh.getMaKH());
                return stmt.executeUpdate() > 0;
            }
        }
    }
}
