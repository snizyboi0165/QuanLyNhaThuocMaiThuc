package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectdb.DatabaseConnection;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Thuoc;

public class ChiTietHoaDonDAO {
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
    
    public boolean themChiTietHoaDon(ChiTietHoaDon cthd) throws SQLException {
    	String sql = "INSERT ChiTietHoaDon (maHD,maThuoc,soLuong,donGia)"
    			+ " VALUES (?,?,?,?)";
    	try (Connection con = getSafeConnection()) {
    		PreparedStatement stmt = con.prepareStatement(sql);
    		stmt.setString(1, cthd.getHoaDon().getMaHD());
    		stmt.setString(2, cthd.getThuoc().getMaThuoc());
    		stmt.setInt(3, cthd.getSoLuong());
    		stmt.setDouble(4, cthd.getDonGia());
    		
    		int rowAffected = stmt.executeUpdate();
    		return rowAffected > 0;
    	}
    }
    
    public ArrayList<ChiTietHoaDon> getChiTietHoaDonTheoMaHD(String maHD) throws SQLException {
    	ArrayList<ChiTietHoaDon> temp = new ArrayList<ChiTietHoaDon>();
    	String sql = "SELECT * FROM ChiTietHoaDon WHERE maHD = ? ";
    	try (Connection con = getSafeConnection()) {
    		PreparedStatement stmt = con.prepareStatement(sql);
    		stmt.setString(1, maHD);
    		try (ResultSet rs = stmt.executeQuery()) {
    			while (rs.next()) {
    				String maThuoc = rs.getString("maThuoc");
    				int soLuong = rs.getInt("soLuong");
    				double donGia = rs.getDouble("donGia");
    				ChiTietHoaDon cthd = new ChiTietHoaDon(new HoaDon(maHD), new Thuoc(maThuoc), soLuong, donGia);
    				temp.add(cthd);
    			}
    		}
    	}
    	return temp;
    }
}
