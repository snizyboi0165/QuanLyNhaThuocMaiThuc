package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectdb.DatabaseConnection;
import entity.DanhMucThuoc;

public class DanhMucThuocDAO {
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
    
    public ArrayList<DanhMucThuoc> getDsDanhMucThuoc() throws SQLException {
    	ArrayList<DanhMucThuoc> temp = new ArrayList<DanhMucThuoc>();
    	String sql = "SELECT * FROM DanhMucThuoc";
    	try (Connection con = getSafeConnection()) {
    		Statement stmt = con.createStatement();
    		try (ResultSet rs = stmt.executeQuery(sql)) {
    			while (rs.next()) {
    				String maDanhMuc = rs.getString("maDanhMuc");
    				String tenDanhMuc = rs.getString("tenDanhMuc");
    				DanhMucThuoc dmt = new DanhMucThuoc(maDanhMuc, tenDanhMuc);
    				temp.add(dmt);
    			}
    		}
    	}
    	return temp;
    }
    
    public String generateMaDanhMucThuoc() throws SQLException {
    	String sql = "select dbo.fn_GenerateMaDanhMuc() as maDanhMucNew";
    	try (Connection con = getSafeConnection()) {
    		PreparedStatement stmt = con.prepareStatement(sql);
    		try (ResultSet rs = stmt.executeQuery()) {
    			if (rs.next()) {
    				String maDanhMuc = rs.getString("maDanhMucNew");
    				return maDanhMuc;
    			}
    		}
    	}
    	return null;
    }
    
    public DanhMucThuoc getDanhMucThuocQuaMaDanhMuc(String maDanhMuc) throws SQLException {
    	String sql = "SELECT * FROM DanhMucThuoc WHERE maDanhMuc = ?";
    	try (Connection con = getSafeConnection()) {
    		PreparedStatement stmt = con.prepareStatement(sql);
    		stmt.setString(1, maDanhMuc);
    		try (ResultSet rs = stmt.executeQuery()) {
    			if (rs.next()) {
    				String tenDanhMuc = rs.getString("tenDanhMuc");
    				return new DanhMucThuoc(maDanhMuc, tenDanhMuc);
    			}
    		}
    	}
    	return null;
    }
    
    public boolean themDanhMucThuoc(DanhMucThuoc dmt) throws SQLException {
    	String sql = "INSERT DanhMucThuoc(maDanhMuc,tenDanhMuc) VALUES (?,?)";
    	try (Connection con = getSafeConnection()) {
    		PreparedStatement stmt = con.prepareStatement(sql);
    		stmt.setString(1, dmt.getMaDanhMuc());
    		stmt.setString(2, dmt.getTenDanhMuc());
    		
    		int rowAffected = stmt.executeUpdate();
    		return rowAffected > 0;
    	}
    }
    
    public boolean capNhatDanhMuc(String maDanhMuc, DanhMucThuoc newDanhMucThuoc) throws SQLException {
    	String sql = "UPDATE DanhMucThuoc "
    			+ "SET tenDanhMuc = ? WHERE maDanhMuc = ?";
    	try (Connection con = getSafeConnection()) {
    		PreparedStatement stmt = con.prepareStatement(sql);
    		stmt.setString(1, newDanhMucThuoc.getTenDanhMuc());
    		stmt.setString(2, maDanhMuc);
    		
    		int rowAfftected = stmt.executeUpdate();
    		return rowAfftected > 0;
    	}
    }
    
    
}
