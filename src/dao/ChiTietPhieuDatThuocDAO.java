package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectdb.DatabaseConnection;
import entity.ChiTietHoaDon;
import entity.ChiTietPhieuDatThuoc;
import entity.PhieuDatThuoc;
import entity.Thuoc;

public class ChiTietPhieuDatThuocDAO {
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
	
	public ArrayList<ChiTietPhieuDatThuoc> getChiTietPhieuDatThuocQuaMaPhieuDatThuoc(String maPhieuDat) throws SQLException {
		ArrayList<ChiTietPhieuDatThuoc> temp = new ArrayList<ChiTietPhieuDatThuoc>();
		String sql = "SELECT * FROM ChiTietPhieuDatThuoc WHERE maPhieuDat = ?";
		try (Connection con = getSafeConnection()) {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, maPhieuDat);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String maThuoc = rs.getString("maThuoc");
					int soLuong = rs.getInt("soLuong");
					double donGia = rs.getDouble("donGia");
					
					ChiTietPhieuDatThuoc ctpdt = new ChiTietPhieuDatThuoc(new Thuoc(maThuoc), new PhieuDatThuoc(maPhieuDat), donGia, soLuong);
					temp.add(ctpdt);
				}
			}
		}
		return temp;
	}
	
    public boolean themChiTietPhieuDatThuoc(ChiTietPhieuDatThuoc ctpdt) throws SQLException {
    	String sql = "INSERT ChiTietPhieuDatThuoc (maPhieuDat,maThuoc,soLuong,donGia)"
    			+ " VALUES (?,?,?,?)";
    	try (Connection con = getSafeConnection()) {
    		PreparedStatement stmt = con.prepareStatement(sql);
    		stmt.setString(1, ctpdt.getPhieuDatThuoc().getMaPhieuDat());
    		stmt.setString(2, ctpdt.getThuoc().getMaThuoc());
    		stmt.setInt(3, ctpdt.getSoLuong());
    		stmt.setDouble(4, ctpdt.getDonGia());
    		
    		int rowAffected = stmt.executeUpdate();
    		return rowAffected > 0;
    	}
    }
}
