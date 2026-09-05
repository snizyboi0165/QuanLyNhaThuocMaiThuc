package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectdb.DatabaseConnection;
import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoanDAO {
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
    
	public ArrayList<TaiKhoan> dsTaiKhoan() throws SQLException {
	    ArrayList<TaiKhoan> temp = new ArrayList<>();
	    String sql = "SELECT * FROM TaiKhoan";
	    try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
	        Statement stmt = connection.createStatement();
	        try (ResultSet rs = stmt.executeQuery(sql)) {
	            while (rs.next()) { 
	                String username = rs.getString("tenDangNhap");
	                String password = rs.getString("matKhau");
	                String vaiTro = rs.getString("vaiTro");
	                String maNV = rs.getString("maNV");
	                String trangThai = rs.getString("trangThai");
	                System.out.println(username + ":" + password + ":" + vaiTro);
	                TaiKhoan tk = new TaiKhoan(username, password, new NhanVien(maNV), trangThai, vaiTro);
	                temp.add(tk);
	            }
	        }
	    }
	    return temp;
	}
	
	public boolean themTaiKhoan(TaiKhoan tk) throws SQLException {
		String sql = "INSERT TaiKhoan VALUES (?,?,?,?,?)";
		
		try (Connection con = getSafeConnection()) {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, tk.getTenDangNhap());
			stmt.setString(2, tk.getMatKhau());
			stmt.setString(3, tk.getTrangThai());
			stmt.setString(4, tk.getNhanVien().getMaNV());
			stmt.setString(5, tk.getVaiTro());
			
			int rowAffected = stmt.executeUpdate();
			return rowAffected > 0;
		}
	}
	
	/**
	 * Cập nhật thông tin tài khoản
	 * @param tk - Tài khoản cần cập nhật
	 * @return true nếu cập nhật thành công, false nếu thất bại
	 * @throws SQLException
	 */
	public boolean capNhatTaiKhoan(TaiKhoan tk) throws SQLException {
	    String sql = "UPDATE TaiKhoan SET matKhau = ?, trangThai = ?, maNV = ?, vaiTro = ? WHERE tenDangNhap = ?";
	    
	    try (Connection con = getSafeConnection()) {
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, tk.getMatKhau());
	        stmt.setString(2, tk.getTrangThai());
	        stmt.setString(3, tk.getNhanVien().getMaNV());
	        stmt.setString(4, tk.getVaiTro());
	        stmt.setString(5, tk.getTenDangNhap());
	        
	        int rowAffected = stmt.executeUpdate();
	        return rowAffected > 0;
	    }
	}
	
	/**
	 * Xóa tài khoản theo tên đăng nhập
	 * @param tenDangNhap - Tên đăng nhập cần xóa
	 * @return true nếu xóa thành công, false nếu thất bại
	 * @throws SQLException
	 */
	public boolean xoaTaiKhoan(String tenDangNhap) throws SQLException {
	    String sql = "DELETE FROM TaiKhoan WHERE tenDangNhap = ?";
	    
	    try (Connection con = getSafeConnection()) {
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, tenDangNhap);
	        
	        int rowAffected = stmt.executeUpdate();
	        return rowAffected > 0;
	    }
	}
	
	/**
	 * Tìm tài khoản theo tên đăng nhập
	 * @param tenDangNhap - Tên đăng nhập cần tìm
	 * @return TaiKhoan nếu tìm thấy, null nếu không tìm thấy
	 * @throws SQLException
	 */
	public TaiKhoan timTaiKhoanTheoTen(String tenDangNhap) throws SQLException {
	    String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ?";
	    
	    try (Connection con = getSafeConnection()) {
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, tenDangNhap);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                String username = rs.getString("tenDangNhap");
	                String password = rs.getString("matKhau");
	                String vaiTro = rs.getString("vaiTro");
	                String maNV = rs.getString("maNV");
	                String trangThai = rs.getString("trangThai");
	                
	                return new TaiKhoan(username, password, new NhanVien(maNV), trangThai, vaiTro);
	            }
	        }
	    }
	    return null;
	}
	
	/**
	 * Kiểm tra tài khoản đã tồn tại chưa
	 * @param tenDangNhap - Tên đăng nhập cần kiểm tra
	 * @return true nếu tồn tại, false nếu không tồn tại
	 * @throws SQLException
	 */
	public boolean kiemTraTonTai(String tenDangNhap) throws SQLException {
	    String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE tenDangNhap = ?";
	    
	    try (Connection con = getSafeConnection()) {
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, tenDangNhap);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1) > 0;
	            }
	        }
	    }
	    return false;
	}
	
	/**
	 * Đổi mật khẩu tài khoản
	 * @param tenDangNhap - Tên đăng nhập
	 * @param matKhauMoi - Mật khẩu mới
	 * @return true nếu đổi thành công, false nếu thất bại
	 * @throws SQLException
	 */
	public boolean doiMatKhau(String tenDangNhap, String matKhauMoi) throws SQLException {
	    String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE tenDangNhap = ?";
	    
	    try (Connection con = getSafeConnection()) {
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, matKhauMoi);
	        stmt.setString(2, tenDangNhap);
	        
	        int rowAffected = stmt.executeUpdate();
	        return rowAffected > 0;
	    }
	}
	
	/**
	 * Đổi trạng thái tài khoản (Hoạt động/Ngưng hoạt động)
	 * @param tenDangNhap - Tên đăng nhập
	 * @param trangThai - Trạng thái mới
	 * @return true nếu đổi thành công, false nếu thất bại
	 * @throws SQLException
	 */
	public boolean doiTrangThai(String tenDangNhap, String trangThai) throws SQLException {
	    String sql = "UPDATE TaiKhoan SET trangThai = ? WHERE tenDangNhap = ?";
	    
	    try (Connection con = getSafeConnection()) {
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, trangThai);
	        stmt.setString(2, tenDangNhap);
	        
	        int rowAffected = stmt.executeUpdate();
	        return rowAffected > 0;
	    }
	}
	
	/**
	 * Tìm kiếm tài khoản theo từ khóa và loại tìm kiếm
	 * @param tuKhoa - Từ khóa tìm kiếm
	 * @param loaiTimKiem - Loại tìm kiếm (Tất cả, Tên, Mã, Vai trò, Trạng thái)
	 * @return Danh sách tài khoản tìm được
	 * @throws SQLException
	 */
	public ArrayList<TaiKhoan> timKiemTaiKhoan(String tuKhoa, String loaiTimKiem) throws SQLException {
	    ArrayList<TaiKhoan> temp = new ArrayList<>();
	    String sql = "";
	    
	    switch (loaiTimKiem) {
	        case "Tên":
	            sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap LIKE ?";
	            break;
	        case "Mã":
	            sql = "SELECT * FROM TaiKhoan WHERE maNV LIKE ?";
	            break;
	        case "Vai trò":
	            sql = "SELECT * FROM TaiKhoan WHERE vaiTro LIKE ?";
	            break;
	        case "Trạng thái":
	            sql = "SELECT * FROM TaiKhoan WHERE trangThai LIKE ?";
	            break;
	        default:
	            sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap LIKE ? OR maNV LIKE ? OR vaiTro LIKE ? OR trangThai LIKE ?";
	            break;
	    }
	    
	    try (Connection con = getSafeConnection()) {
	        PreparedStatement stmt = con.prepareStatement(sql);
	        
	        if (loaiTimKiem.equals("Tất cả") || loaiTimKiem.isEmpty()) {
	            String keyword = "%" + tuKhoa + "%";
	            stmt.setString(1, keyword);
	            stmt.setString(2, keyword);
	            stmt.setString(3, keyword);
	            stmt.setString(4, keyword);
	        } else {
	            stmt.setString(1, "%" + tuKhoa + "%");
	        }
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                String username = rs.getString("tenDangNhap");
	                String password = rs.getString("matKhau");
	                String vaiTro = rs.getString("vaiTro");
	                String maNV = rs.getString("maNV");
	                String trangThai = rs.getString("trangThai");
	                
	                TaiKhoan tk = new TaiKhoan(username, password, new NhanVien(maNV), trangThai, vaiTro);
	                temp.add(tk);
	            }
	        }
	    }
	    return temp;
	}
	
	/**
	 * Đếm số lượng tài khoản theo trạng thái
	 * @param trangThai - Trạng thái cần đếm
	 * @return Số lượng tài khoản
	 * @throws SQLException
	 */
	public int demTaiKhoanTheoTrangThai(String trangThai) throws SQLException {
	    String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE trangThai = ?";
	    
	    try (Connection con = getSafeConnection()) {
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, trangThai);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        }
	    }
	    return 0;
	}
	
	/**
	 * Lấy danh sách tài khoản theo vai trò
	 * @param vaiTro - Vai trò cần lọc
	 * @return Danh sách tài khoản theo vai trò
	 * @throws SQLException
	 */
	public ArrayList<TaiKhoan> dsTaiKhoanTheoVaiTro(String vaiTro) throws SQLException {
	    ArrayList<TaiKhoan> temp = new ArrayList<>();
	    String sql = "SELECT * FROM TaiKhoan WHERE vaiTro = ?";
	    
	    try (Connection con = getSafeConnection()) {
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, vaiTro);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                String username = rs.getString("tenDangNhap");
	                String password = rs.getString("matKhau");
	                String vaiTroRS = rs.getString("vaiTro");
	                String maNV = rs.getString("maNV");
	                String trangThai = rs.getString("trangThai");
	                
	                TaiKhoan tk = new TaiKhoan(username, password, new NhanVien(maNV), trangThai, vaiTroRS);
	                temp.add(tk);
	            }
	        }
	    }
	    return temp;
	}
}