package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectdb.DatabaseConnection;
import entity.NhaCungCap;

public class NhaCungCapDAO {

    private Connection getSafeConnection() throws SQLException {
        // Giả định rằng lớp DatabaseConnection quản lý kết nối
        return DatabaseConnection.getInstance().getConnection();
    }

    private void ensureDaXoaColumn(Connection con) throws SQLException {
        String checkSql = "SELECT COL_LENGTH('dbo.NhaCungCap', 'daXoa')";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && rs.getObject(1) != null) {
                return;
            }
        }
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate("ALTER TABLE NhaCungCap ADD daXoa BIT DEFAULT 0 NOT NULL");
        }
    }

    /**
     * Tự động phát sinh mã nhà cung cấp theo hàm trong SQL Server
     * @return Mã nhà cung cấp mới (VD: NCC00006)
     */
    public String generateMaNCC() {
        String maNCC = "NCC00001"; // Giá trị mặc định nếu có lỗi
        String sql = "SELECT dbo.fn_GenerateMaNCC() AS MaNCCMoi";
        
        try (Connection conn = getSafeConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                maNCC = rs.getString("MaNCCMoi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maNCC;
    }

    /**
     * Lấy danh sách tất cả các nhà cung cấp từ database
     */
    public ArrayList<NhaCungCap> getDSNhaCungCap() throws SQLException {
        ArrayList<NhaCungCap> dsNCC = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap WHERE daXoa = 0";
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    String maNCC = rs.getString("maNCC");
                    String tenNCC = rs.getString("tenNCC");
                    String soDienThoai = rs.getString("soDienThoai");
                    NhaCungCap ncc = new NhaCungCap(maNCC, tenNCC, soDienThoai);
                    dsNCC.add(ncc);
                }
            }
        }
        return dsNCC;
    }

    /**
     * Thêm một nhà cung cấp mới vào database
     * @param ncc đối tượng NhaCungCap cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean themNhaCungCap(NhaCungCap ncc) throws SQLException {
        String sql = "INSERT INTO NhaCungCap(maNCC, tenNCC, soDienThoai) VALUES (?, ?, ?)";
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setString(1, ncc.getMaNCC());
                stmt.setString(2, ncc.getTenNCC());
                stmt.setString(3, ncc.getSoDienThoai());
                return stmt.executeUpdate() > 0;
            }
        }
    }

    /**
     * Cập nhật thông tin một nhà cung cấp
     * @param ncc đối tượng NhaCungCap cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean capNhatNhaCungCap(NhaCungCap ncc) throws SQLException {
        String sql = "UPDATE NhaCungCap SET tenNCC = ?, soDienThoai = ? WHERE maNCC = ?";
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setString(1, ncc.getTenNCC());
                stmt.setString(2, ncc.getSoDienThoai());
                stmt.setString(3, ncc.getMaNCC());

                return stmt.executeUpdate() > 0;
            }
        }
    }

    /**
     * Xóa một nhà cung cấp khỏi database theo mã
     * @param maNCC mã nhà cung cấp cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean xoaNhaCungCap(String maNCC) throws SQLException {
        String sql = "UPDATE NhaCungCap SET daXoa = 1 WHERE maNCC = ?";
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setString(1, maNCC);
                return stmt.executeUpdate() > 0;
            }
        }
    }
    
    /**
     * Lấy thông tin nhà cung cấp theo mã
     */
    public NhaCungCap getNhaCungCapTheoMa(String maNCC) throws SQLException {
        String sql = "SELECT * FROM NhaCungCap WHERE maNCC = ?";
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setString(1, maNCC);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String tenNCC = rs.getString("tenNCC");
                        String soDienThoai = rs.getString("soDienThoai");
                        return new NhaCungCap(maNCC, tenNCC, soDienThoai);
                    }
                }
            }
        }
        return null;
    }
    
    public ArrayList<NhaCungCap> timKiemNhaCungCap(String searchText, String searchCriteria) throws SQLException {
        ArrayList<NhaCungCap> dsNCC = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap WHERE daXoa = 0 AND (";
        String condition = "";

        switch (searchCriteria) {
            case "Mã":
                condition = "maNCC LIKE ?";
                break;
            case "Tên":
                condition = "tenNCC LIKE ?";
                break;
            case "Số điện thoại":
                condition = "soDienThoai LIKE ?";
                break;
            case "Tất cả":
                condition = "maNCC LIKE ? OR tenNCC LIKE ? OR soDienThoai LIKE ?";
                break;
        }

        sql += condition + ")";

        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {

            if (searchCriteria.equals("Tất cả")) {
                stmt.setString(1, "%" + searchText + "%");
                stmt.setString(2, "%" + searchText + "%");
                stmt.setString(3, "%" + searchText + "%");
            } else {
                stmt.setString(1, "%" + searchText + "%");
            }
            
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String maNCC = rs.getString("maNCC");
                        String tenNCC = rs.getString("tenNCC");
                        String soDienThoai = rs.getString("soDienThoai");
                        NhaCungCap ncc = new NhaCungCap(maNCC, tenNCC, soDienThoai);
                        dsNCC.add(ncc);
                    }
                }
            }
        }
        return dsNCC;
    }
    /**
     * Tìm kiếm nhà cung cấp đơn giản (theo mã, tên hoặc số điện thoại)
     */
    public ArrayList<NhaCungCap> timKiemNhaCungCap(String tuKhoa) throws SQLException {
        ArrayList<NhaCungCap> dsNCC = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap WHERE daXoa = 0 AND (maNCC LIKE ? OR tenNCC LIKE ? OR soDienThoai LIKE ?) ORDER BY maNCC DESC";
        
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {

                String keyword = "%" + tuKhoa + "%";
                stmt.setString(1, keyword);
                stmt.setString(2, keyword);
                stmt.setString(3, keyword);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String maNCC = rs.getString("maNCC");
                        String tenNCC = rs.getString("tenNCC");
                        String soDienThoai = rs.getString("soDienThoai");
                        NhaCungCap ncc = new NhaCungCap(maNCC, tenNCC, soDienThoai);
                        dsNCC.add(ncc);
                    }
                }
            }
        }
        return dsNCC;
    }
    
    /**
     * Tìm kiếm nâng cao nhà cung cấp với nhiều tiêu chí
     */
    public ArrayList<NhaCungCap> timKiemNangCao(
            String maNCC, String tenNCC, String soDienThoai) throws SQLException {
        
        ArrayList<NhaCungCap> ketQua = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM NhaCungCap WHERE daXoa = 0");
        ArrayList<Object> params = new ArrayList<>();
        
        // Thêm điều kiện tìm kiếm
        if (maNCC != null && !maNCC.isEmpty()) {
            sql.append(" AND maNCC LIKE ?");
            params.add("%" + maNCC + "%");
        }
        
        if (tenNCC != null && !tenNCC.isEmpty()) {
            sql.append(" AND tenNCC LIKE ?");
            params.add("%" + tenNCC + "%");
        }
        
        if (soDienThoai != null && !soDienThoai.isEmpty()) {
            sql.append(" AND soDienThoai LIKE ?");
            params.add("%" + soDienThoai + "%");
        }
        
        sql.append(" ORDER BY maNCC DESC");
        
        try (Connection con = getSafeConnection()) {
            ensureDaXoaColumn(con);
            try (PreparedStatement stmt = con.prepareStatement(sql.toString())) {

                // Set parameters
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String maNCCRS = rs.getString("maNCC");
                        String tenNCCRS = rs.getString("tenNCC");
                        String soDienThoaiRS = rs.getString("soDienThoai");
                        NhaCungCap ncc = new NhaCungCap(maNCCRS, tenNCCRS, soDienThoaiRS);
                        ketQua.add(ncc);
                    }
                }
            }
        }
        
        return ketQua;
    }
}
