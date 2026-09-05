package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectdb.DatabaseConnection;
import entity.Thue;

public class ThueDAO {
	
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
	
    public ArrayList<Thue> getDsThue() throws SQLException {
    	ArrayList<Thue> temp = new ArrayList<>();
    	String sql = "SELECT * FROM Thue";
    	try (Connection con = getSafeConnection()) {
    		Statement stmt = con.createStatement();
    		try (ResultSet rs = stmt.executeQuery(sql)) {
    			while (rs.next()) {
    				String maThue = rs.getString("maThue");
    				String tenThue = rs.getString("tenThue");
    				double phanTramThue = rs.getDouble("phanTramThue");
    				Thue thue = new Thue(maThue, tenThue, phanTramThue);
                    temp.add(thue);
    			}
    		}
    	}
    	return temp;
    }

    public String generateMaThue() throws SQLException {
        String maThue = "TE00001";
        String sql = "SELECT dbo.fn_GenerateMaThue() AS MaThueMoi";
        try (Connection conn = getSafeConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                maThue = rs.getString("MaThueMoi");
            }
        }
        return maThue;
    }

    public boolean themThue(Thue thue) throws SQLException {
        String sql = "INSERT INTO Thue(maThue, tenThue, phanTramThue) VALUES (?, ?, ?)";
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, thue.getMaThue());
            stmt.setString(2, thue.getTenThue());
            stmt.setDouble(3, thue.getPhanTramThue());
            return stmt.executeUpdate() > 0;
        }
    }

    public Thue getThueTheoMa(String ma) throws SQLException {
        String sql = "SELECT * FROM Thue WHERE maThue = ?";
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ma);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Thue(rs.getString("maThue"), rs.getString("tenThue"), rs.getDouble("phanTramThue"));
            }
        }
        return null;
    }

    public boolean capNhatThue(Thue thue) throws SQLException {
        String sql = "UPDATE Thue SET tenThue = ?, phanTramThue = ? WHERE maThue = ?";
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, thue.getTenThue());
            stmt.setDouble(2, thue.getPhanTramThue());
            stmt.setString(3, thue.getMaThue());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean xoaThue(String maThue) throws SQLException {
        String sql = "DELETE FROM Thue WHERE maThue = ?";
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maThue);
            return stmt.executeUpdate() > 0;
        }
    }

    public ArrayList<Thue> timKiemThue(String tuKhoa, String tieuChi) throws SQLException {
        ArrayList<Thue> ds = new ArrayList<>();
        String query = "%" + tuKhoa + "%";
        String sql;
        if ("Mã thuế".equals(tieuChi)) {
            sql = "SELECT * FROM Thue WHERE maThue LIKE ?";
        } else if ("Tên thuế".equals(tieuChi)) {
            sql = "SELECT * FROM Thue WHERE tenThue LIKE ?";
        } else {
            sql = "SELECT * FROM Thue WHERE maThue LIKE ? OR tenThue LIKE ?";
        }
        
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            if ("Tất cả".equals(tieuChi)) {
                stmt.setString(1, query);
                stmt.setString(2, query);
            } else {
                stmt.setString(1, query);
            }
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                ds.add(new Thue(rs.getString("maThue"), rs.getString("tenThue"), rs.getDouble("phanTramThue")));
            }
        }
        return ds;
    }
}