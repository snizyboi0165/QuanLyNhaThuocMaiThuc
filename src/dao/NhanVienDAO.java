package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectdb.DatabaseConnection;
import entity.NhanVien;

public class NhanVienDAO {
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
     * Tự động phát sinh mã nhân viên theo quy luật NV00001, NV00002, ...
     * @return mã nhân viên mới
     */
    public String generateMaNV() {
        String maNV = "NV00001";
        String sql = "SELECT dbo.fn_GenerateMaNV() AS MaNVMoi";
        
        try (Connection conn = getSafeConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                maNV = rs.getString("MaNVMoi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return maNV;
    }
    
    /**
     * Lấy danh sách nhân viên chưa bị xóa (daXoa = 0 hoặc NULL)
     */
    public ArrayList<NhanVien> getDSNhanVien() throws SQLException {
        ArrayList<NhanVien> temp = new ArrayList<NhanVien>();
        String sql = "SELECT * FROM NhanVien WHERE (daXoa = 0 OR daXoa IS NULL)";
        try (Connection con = getSafeConnection()) {
            Statement stmt = con.createStatement();
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String maNV = rs.getString("maNV");
                    String hoTen = rs.getString("hoTen");
                    String chucVu = rs.getString("chucVu");
                    String soDienThoai = rs.getString("soDienThoai");
                    Date ngaySinh = rs.getDate("ngaySinh");
                    String gioiTinh = rs.getString("gioiTinh");
                    String diaChi = rs.getString("diaChi");
                    String email = rs.getString("email");
                    
                    boolean daXoa = rs.getBoolean("daXoa");
                    if (rs.wasNull()) {
                        daXoa = false;
                    }
                    
                    NhanVien nv = new NhanVien(maNV, hoTen, chucVu, soDienThoai, ngaySinh, gioiTinh, diaChi, email, daXoa);
                    temp.add(nv);
                }
            }
        }
        return temp;
    }
    
    /**
     * Lấy tất cả nhân viên (bao gồm cả đã xóa) - dùng cho admin
     */
    public ArrayList<NhanVien> getTatCaNhanVien() throws SQLException {
        ArrayList<NhanVien> temp = new ArrayList<NhanVien>();
        String sql = "SELECT * FROM NhanVien";
        try (Connection con = getSafeConnection()) {
            Statement stmt = con.createStatement();
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String maNV = rs.getString("maNV");
                    String hoTen = rs.getString("hoTen");
                    String chucVu = rs.getString("chucVu");
                    String soDienThoai = rs.getString("soDienThoai");
                    Date ngaySinh = rs.getDate("ngaySinh");
                    String gioiTinh = rs.getString("gioiTinh");
                    String diaChi = rs.getString("diaChi");
                    String email = rs.getString("email");
                    boolean daXoa = rs.getBoolean("daXoa");
                    
                    NhanVien nv = new NhanVien(maNV, hoTen, chucVu, soDienThoai, ngaySinh, gioiTinh, diaChi, email, daXoa);
                    temp.add(nv);
                }
            }
        }
        return temp;
    }
    

    public boolean themNhanVien(NhanVien nv) throws SQLException {
        String sql = "INSERT INTO NhanVien(maNV,hoTen,chucVu,soDienThoai,ngaySinh,gioiTinh,diaChi,email,daXoa) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nv.getMaNV());
            stmt.setString(2, nv.getTenNV());
            stmt.setString(3, nv.getChucVu());
            stmt.setString(4, nv.getSoDienThoai());
            
            // FIX: Chuyển đổi java.util.Date sang java.sql.Date đúng cách
            java.util.Date utilDate = nv.getNgaySinh();
            if (utilDate != null) {
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                stmt.setDate(5, sqlDate);
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            
            stmt.setString(6, nv.getGioiTinh());
            stmt.setString(7, nv.getDiaChi());
            stmt.setString(8, nv.getEmail());
            stmt.setBoolean(9, false); // Mặc định chưa bị xóa
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e; // Ném lại exception để xử lý ở tầng trên
        }
    }

    public NhanVien getNhanVienTheoMa(String maNV) throws SQLException {
        String sql = "SELECT * FROM NhanVien WHERE maNV = ? AND (daXoa = 0 OR daXoa IS NULL)";
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maNV);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hoTen = rs.getString("hoTen");
                    String chucVu = rs.getString("chucVu");
                    String soDienThoai = rs.getString("soDienThoai");
                    Date ngaySinh = rs.getDate("ngaySinh");
                    String gioiTinh = rs.getString("gioiTinh");
                    String diaChi = rs.getString("diaChi");
                    String email = rs.getString("email");
                    boolean daXoa = rs.getBoolean("daXoa");
                    
                    return new NhanVien(maNV, hoTen, chucVu, soDienThoai, ngaySinh, gioiTinh, diaChi, email, daXoa);
                }
            }
        }
        return null;
    }
    

    public boolean capNhatNhanVien(NhanVien nv) throws SQLException {
        String sql = "UPDATE NhanVien SET hoTen=?, chucVu=?, soDienThoai=?, ngaySinh=?, gioiTinh=?, diaChi=?, email=? WHERE maNV=? AND (daXoa = 0 OR daXoa IS NULL)";
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nv.getTenNV());
            stmt.setString(2, nv.getChucVu());
            stmt.setString(3, nv.getSoDienThoai());
            

            java.util.Date utilDate = nv.getNgaySinh();
            if (utilDate != null) {
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                stmt.setDate(4, sqlDate);
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }
            
            stmt.setString(5, nv.getGioiTinh());
            stmt.setString(6, nv.getDiaChi());
            stmt.setString(7, nv.getEmail());
            stmt.setString(8, nv.getMaNV());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    // Xoá mềm, nếu xoá thì daXoa = 1
    public boolean xoaNhanVien(String maNV) throws SQLException {
        String sqlXoaNhanVien = "UPDATE NhanVien SET daXoa = 1 WHERE maNV = ?";
        String sqlKhoaTaiKhoan = "UPDATE TaiKhoan SET trangThai = N'Ngừng hoạt động' WHERE maNV = ?";

        try (Connection con = getSafeConnection()) {
            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            try (PreparedStatement stmtXoa = con.prepareStatement(sqlXoaNhanVien);
                 PreparedStatement stmtKhoa = con.prepareStatement(sqlKhoaTaiKhoan)) {

                stmtXoa.setString(1, maNV);
                int rowsAffected = stmtXoa.executeUpdate();
                if (rowsAffected == 0) {
                    con.rollback();
                    return false;
                }

                stmtKhoa.setString(1, maNV);
                stmtKhoa.executeUpdate();

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
    
    
    public ArrayList<NhanVien> getDSNhanVienDaXoa() throws SQLException {
        ArrayList<NhanVien> temp = new ArrayList<NhanVien>();
        String sql = "SELECT * FROM NhanVien WHERE daXoa = 1";
        try (Connection con = getSafeConnection()) {
            Statement stmt = con.createStatement();
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String maNV = rs.getString("maNV");
                    String hoTen = rs.getString("hoTen");
                    String chucVu = rs.getString("chucVu");
                    String soDienThoai = rs.getString("soDienThoai");
                    Date ngaySinh = rs.getDate("ngaySinh");
                    String gioiTinh = rs.getString("gioiTinh");
                    String diaChi = rs.getString("diaChi");
                    String email = rs.getString("email");
                    boolean daXoa = true;
                    
                    NhanVien nv = new NhanVien(maNV, hoTen, chucVu, soDienThoai, ngaySinh, gioiTinh, diaChi, email, daXoa);
                    temp.add(nv);
                }
            }
        }
        return temp;
    }
    
    public ArrayList<NhanVien> timKiemNangCao(
            String maNV, String tenNV, String soDienThoai, 
            String email, String diaChi, String gioiTinh, 
            String chucVu, String trangThai,
            java.util.Date ngaySinhTu, java.util.Date ngaySinhDen) throws SQLException {
        
        ArrayList<NhanVien> ketQua = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM NhanVien WHERE 1=1");
        ArrayList<Object> params = new ArrayList<>();
        
        // Thêm điều kiện tìm kiếm
        if (maNV != null && !maNV.isEmpty()) {
            sql.append(" AND maNV LIKE ?");
            params.add("%" + maNV + "%");
        }
        
        if (tenNV != null && !tenNV.isEmpty()) {
            sql.append(" AND hoTen LIKE ?");
            params.add("%" + tenNV + "%");
        }
        
        if (soDienThoai != null && !soDienThoai.isEmpty()) {
            sql.append(" AND soDienThoai LIKE ?");
            params.add("%" + soDienThoai + "%");
        }
        
        if (email != null && !email.isEmpty()) {
            sql.append(" AND email LIKE ?");
            params.add("%" + email + "%");
        }
        
        if (diaChi != null && !diaChi.isEmpty()) {
            sql.append(" AND diaChi LIKE ?");
            params.add("%" + diaChi + "%");
        }
        
        if (gioiTinh != null && !gioiTinh.isEmpty()) {
            sql.append(" AND gioiTinh = ?");
            params.add(gioiTinh);
        }
        
        if (chucVu != null && !chucVu.isEmpty()) {
            sql.append(" AND chucVu = ?");
            params.add(chucVu);
        }
        
        // Xử lý trạng thái (đang làm việc / đã xóa)
        if (trangThai != null && !trangThai.isEmpty()) {
            if (trangThai.equals("Đang làm việc")) {
                sql.append(" AND (daXoa = 0 OR daXoa IS NULL)");
            } else if (trangThai.equals("Đã xóa")) {
                sql.append(" AND daXoa = 1");
            }
        }
        
        if (ngaySinhTu != null) {
            sql.append(" AND ngaySinh >= ?");
            params.add(new java.sql.Date(ngaySinhTu.getTime()));
        }
        
        if (ngaySinhDen != null) {
            sql.append(" AND ngaySinh <= ?");
            params.add(new java.sql.Date(ngaySinhDen.getTime()));
        }
        
        sql.append(" ORDER BY maNV DESC");
        
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {
            
            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maNVRS = rs.getString("maNV");
                    String hoTen = rs.getString("hoTen");
                    String chucVuRS = rs.getString("chucVu");
                    String soDienThoaiRS = rs.getString("soDienThoai");
                    java.sql.Date ngaySinh = rs.getDate("ngaySinh");
                    String gioiTinhRS = rs.getString("gioiTinh");
                    String diaChiRS = rs.getString("diaChi");
                    String emailRS = rs.getString("email");
                    boolean daXoa = rs.getBoolean("daXoa");
                    
                    NhanVien nv = new NhanVien(maNVRS, hoTen, chucVuRS, soDienThoaiRS, 
                                              ngaySinh, gioiTinhRS, diaChiRS, emailRS, daXoa);
                    ketQua.add(nv);
                }
            }
        }
        
        return ketQua;
    }
    
    
    public ArrayList<NhanVien> timKiemNhanVien(String tuKhoa) throws SQLException {
        ArrayList<NhanVien> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE (maNV LIKE ? OR hoTen LIKE ? OR soDienThoai LIKE ?) " +
                     "AND (daXoa = 0 OR daXoa IS NULL) ORDER BY maNV DESC";
        
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            String keyword = "%" + tuKhoa + "%";
            stmt.setString(1, keyword);
            stmt.setString(2, keyword);
            stmt.setString(3, keyword);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maNV = rs.getString("maNV");
                    String hoTen = rs.getString("hoTen");
                    String chucVu = rs.getString("chucVu");
                    String soDienThoai = rs.getString("soDienThoai");
                    java.sql.Date ngaySinh = rs.getDate("ngaySinh");
                    String gioiTinh = rs.getString("gioiTinh");
                    String diaChi = rs.getString("diaChi");
                    String email = rs.getString("email");
                    boolean daXoa = rs.getBoolean("daXoa");
                    
                    NhanVien nv = new NhanVien(maNV, hoTen, chucVu, soDienThoai, 
                                              ngaySinh, gioiTinh, diaChi, email, daXoa);
                    ketQua.add(nv);
                }
            }
        }
        return ketQua;
    }


    public ArrayList<NhanVien> dsNhanVienTheoChucVu(String chucVu) throws SQLException {
        ArrayList<NhanVien> dsNV = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE chucVu = ? AND (daXoa = 0 OR daXoa IS NULL) ORDER BY maNV DESC";
        
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, chucVu);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maNV = rs.getString("maNV");
                    String hoTen = rs.getString("hoTen");
                    String chucVuRS = rs.getString("chucVu");
                    String soDienThoai = rs.getString("soDienThoai");
                    java.sql.Date ngaySinh = rs.getDate("ngaySinh");
                    String gioiTinh = rs.getString("gioiTinh");
                    String diaChi = rs.getString("diaChi");
                    String email = rs.getString("email");
                    boolean daXoa = rs.getBoolean("daXoa");
                    
                    NhanVien nv = new NhanVien(maNV, hoTen, chucVuRS, soDienThoai, 
                                              ngaySinh, gioiTinh, diaChi, email, daXoa);
                    dsNV.add(nv);
                }
            }
        }
        return dsNV;
    }

    /**
     * Lấy danh sách nhân viên theo giới tính
     */
    public ArrayList<NhanVien> dsNhanVienTheoGioiTinh(String gioiTinh) throws SQLException {
        ArrayList<NhanVien> dsNV = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE gioiTinh = ? AND (daXoa = 0 OR daXoa IS NULL) ORDER BY maNV DESC";
        
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, gioiTinh);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maNV = rs.getString("maNV");
                    String hoTen = rs.getString("hoTen");
                    String chucVu = rs.getString("chucVu");
                    String soDienThoai = rs.getString("soDienThoai");
                    java.sql.Date ngaySinh = rs.getDate("ngaySinh");
                    String gioiTinhRS = rs.getString("gioiTinh");
                    String diaChi = rs.getString("diaChi");
                    String email = rs.getString("email");
                    boolean daXoa = rs.getBoolean("daXoa");
                    
                    NhanVien nv = new NhanVien(maNV, hoTen, chucVu, soDienThoai, 
                                              ngaySinh, gioiTinhRS, diaChi, email, daXoa);
                    dsNV.add(nv);
                }
            }
        }
        return dsNV;
    }

    /**
     * Đếm số lượng nhân viên đang làm việc
     */
    public int demSoLuongNhanVien() throws SQLException {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE (daXoa = 0 OR daXoa IS NULL)";
        
        try (Connection con = getSafeConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }


    public int demSoLuongTheoChucVu(String chucVu) throws SQLException {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE chucVu = ? AND (daXoa = 0 OR daXoa IS NULL)";
        
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, chucVu);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public ArrayList<NhanVien> dsNhanVienSinhNhatThangNay() throws SQLException {
        ArrayList<NhanVien> dsNV = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE MONTH(ngaySinh) = MONTH(GETDATE()) " +
                     "AND (daXoa = 0 OR daXoa IS NULL) ORDER BY DAY(ngaySinh)";
        
        try (Connection con = getSafeConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                String hoTen = rs.getString("hoTen");
                String chucVu = rs.getString("chucVu");
                String soDienThoai = rs.getString("soDienThoai");
                java.sql.Date ngaySinh = rs.getDate("ngaySinh");
                String gioiTinh = rs.getString("gioiTinh");
                String diaChi = rs.getString("diaChi");
                String email = rs.getString("email");
                boolean daXoa = rs.getBoolean("daXoa");
                
                NhanVien nv = new NhanVien(maNV, hoTen, chucVu, soDienThoai, 
                                          ngaySinh, gioiTinh, diaChi, email, daXoa);
                dsNV.add(nv);
            }
        }
        return dsNV;
    }


    public boolean kiemTraSDTTonTai(String soDienThoai) throws SQLException {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE soDienThoai = ? AND (daXoa = 0 OR daXoa IS NULL)";
        
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, soDienThoai);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


    public boolean kiemTraEmailTonTai(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE email = ? AND (daXoa = 0 OR daXoa IS NULL)";
        
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
