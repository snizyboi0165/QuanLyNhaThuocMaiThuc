package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import connectdb.DatabaseConnection;
import entity.DanhMucThuoc;
import entity.Thuoc;

public class ThuocDAO {
    private Connection getSafeConnection() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn == null || conn.isClosed()) {
            conn = DatabaseConnection.getInstance().getConnection();
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Khong the thiet lap ket noi den co so du lieu");
            }
        }
        return conn;
    }

    public String generateMaThuoc() throws SQLException {
        String sql = "SELECT dbo.fn_GenerateMaThuoc() AS maThuoc";
        try (Connection con = getSafeConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("maThuoc");
            }
        }
        throw new SQLException("Khong the tao ma thuoc tu dong");
    }

    public ArrayList<Thuoc> getDsThuoc() throws SQLException {
        ArrayList<Thuoc> temp = new ArrayList<>();
        String sql = "SELECT v.*, dm.tenDanhMuc "
                + "FROM vw_TonKhoThuoc v LEFT JOIN DanhMucThuoc dm ON v.maDanhMuc = dm.maDanhMuc";
        try (Connection con = getSafeConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                temp.add(mapThuoc(rs));
            }
        }
        return temp;
    }

    public Thuoc getThuocTheoMaThuoc(String maThuoc) throws SQLException {
        String sql = "SELECT v.*, dm.tenDanhMuc "
                + "FROM vw_TonKhoThuoc v LEFT JOIN DanhMucThuoc dm ON v.maDanhMuc = dm.maDanhMuc "
                + "WHERE v.maThuoc = ?";
        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maThuoc);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapThuoc(rs);
                }
            }
        }
        return null;
    }

    public ArrayList<Thuoc> getDsThuocBanDuoc() throws SQLException {
        ArrayList<Thuoc> temp = new ArrayList<>();
        String sql = "SELECT t.maThuoc, t.tenThuoc, t.donViTinh, t.giaBan, t.moTa, t.maDanhMuc, "
                + "t.hinhAnh, t.thanhPhan, t.xuatXu, dm.tenDanhMuc, "
                + "SUM(lt.soLuongConLai) AS soLuongTon, MIN(lt.hanSuDung) AS hanSuDungGanNhat "
                + "FROM Thuoc t "
                + "JOIN LoThuoc lt ON t.maThuoc = lt.maThuoc "
                + "LEFT JOIN DanhMucThuoc dm ON t.maDanhMuc = dm.maDanhMuc "
                + "WHERE lt.soLuongConLai > 0 "
                + "AND lt.hanSuDung >= CAST(GETDATE() AS DATE) "
                + "AND lt.trangThai = N'Còn hàng' "
                + "GROUP BY t.maThuoc, t.tenThuoc, t.donViTinh, t.giaBan, t.moTa, t.maDanhMuc, "
                + "t.hinhAnh, t.thanhPhan, t.xuatXu, dm.tenDanhMuc "
                + "ORDER BY t.maThuoc";
        try (Connection con = getSafeConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                temp.add(mapThuoc(rs));
            }
        }
        return temp;
    }

    public Thuoc getThuocBanDuocTheoMa(String maThuoc) throws SQLException {
        String sql = "SELECT t.maThuoc, t.tenThuoc, t.donViTinh, t.giaBan, t.moTa, t.maDanhMuc, "
                + "t.hinhAnh, t.thanhPhan, t.xuatXu, dm.tenDanhMuc, "
                + "SUM(lt.soLuongConLai) AS soLuongTon, MIN(lt.hanSuDung) AS hanSuDungGanNhat "
                + "FROM Thuoc t "
                + "JOIN LoThuoc lt ON t.maThuoc = lt.maThuoc "
                + "LEFT JOIN DanhMucThuoc dm ON t.maDanhMuc = dm.maDanhMuc "
                + "WHERE t.maThuoc = ? "
                + "AND lt.soLuongConLai > 0 "
                + "AND lt.hanSuDung >= CAST(GETDATE() AS DATE) "
                + "AND lt.trangThai = N'Còn hàng' "
                + "GROUP BY t.maThuoc, t.tenThuoc, t.donViTinh, t.giaBan, t.moTa, t.maDanhMuc, "
                + "t.hinhAnh, t.thanhPhan, t.xuatXu, dm.tenDanhMuc";
        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maThuoc);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapThuoc(rs);
                }
            }
        }
        return null;
    }

    private Thuoc mapThuoc(ResultSet rs) throws SQLException {
        String maThuoc = rs.getString("maThuoc");
        String tenThuoc = rs.getString("tenThuoc");
        String donViTinh = rs.getString("donViTinh");
        double giaBan = rs.getDouble("giaBan");
        int soLuongTon = rs.getInt("soLuongTon");
        Date hanSuDungGanNhat = rs.getDate("hanSuDungGanNhat");
        String moTa = rs.getString("moTa");
        String maDanhMuc = rs.getString("maDanhMuc");
        String tenDanhMuc = rs.getString("tenDanhMuc");
        String hinhAnh = rs.getString("hinhAnh");
        String thanhPhan = rs.getString("thanhPhan");
        String xuatXu = rs.getString("xuatXu");

        return new Thuoc(maThuoc, tenThuoc, donViTinh, giaBan, soLuongTon, hanSuDungGanNhat,
                moTa, new DanhMucThuoc(maDanhMuc, tenDanhMuc), hinhAnh, thanhPhan, null, xuatXu);
    }

    public boolean updateSoLuongTonTheoMaThuoc(String maThuoc, int soLuongNew) throws SQLException {
        throw new SQLException("Ton kho duoc quan ly theo LoThuoc, khong cap nhat truc tiep Thuoc.soLuongTon");
    }

    public boolean truSoLuongTonTheoMaThuoc(String maThuoc, int soLuongBan) throws SQLException {
        return new LoThuocDAO().truTonTheoFEFO(maThuoc, soLuongBan);
    }

    public int getSoLuongTonTheoMaThuoc(String maThuoc) throws SQLException {
        String sql = "SELECT soLuongTon FROM vw_TonKhoThuoc WHERE maThuoc = ?";
        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maThuoc);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("soLuongTon");
                }
            }
        }
        return 0;
    }

    public String themThuoc(Thuoc thuoc) throws SQLException {
        String maThuocMoi = generateMaThuoc();
        String sql = "INSERT INTO Thuoc (maThuoc, tenThuoc, donViTinh, giaBan, moTa, maDanhMuc, hinhAnh, thanhPhan, xuatXu) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maThuocMoi);
            stmt.setString(2, thuoc.getTenThuoc());
            stmt.setString(3, thuoc.getDonViTinh());
            stmt.setDouble(4, thuoc.getGiaBan());
            stmt.setString(5, thuoc.getMoTa());
            stmt.setString(6, thuoc.getDanhMucThuoc().getMaDanhMuc());
            stmt.setString(7, thuoc.getHinhAnh());
            stmt.setString(8, thuoc.getThanhPhan());
            stmt.setString(9, thuoc.getXuatXu());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0 ? maThuocMoi : null;
        }
    }

    public boolean capNhatThuoc(Thuoc thuoc) throws SQLException {
        String sql = "UPDATE Thuoc SET tenThuoc = ?, donViTinh = ?, giaBan = ?, moTa = ?, "
                + "maDanhMuc = ?, hinhAnh = ?, thanhPhan = ?, xuatXu = ? WHERE maThuoc = ?";

        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, thuoc.getTenThuoc());
            stmt.setString(2, thuoc.getDonViTinh());
            stmt.setDouble(3, thuoc.getGiaBan());
            stmt.setString(4, thuoc.getMoTa());
            stmt.setString(5, thuoc.getDanhMucThuoc().getMaDanhMuc());
            stmt.setString(6, thuoc.getHinhAnh());
            stmt.setString(7, thuoc.getThanhPhan());
            stmt.setString(8, thuoc.getXuatXu());
            stmt.setString(9, thuoc.getMaThuoc());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean xoaThuoc(String maThuoc) throws SQLException {
        String sql = "DELETE FROM Thuoc WHERE maThuoc = ?";
        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maThuoc);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean kiemTraTonTai(String maThuoc) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Thuoc WHERE maThuoc = ?";
        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maThuoc);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public ArrayList<Thuoc> getThuocSapHetHang(int nguongTon, int gioiHan) throws SQLException {
        ArrayList<Thuoc> ketQua = new ArrayList<>();
        String sql = "SELECT TOP " + gioiHan + " v.*, dm.tenDanhMuc "
                + "FROM vw_TonKhoThuoc v "
                + "LEFT JOIN DanhMucThuoc dm ON v.maDanhMuc = dm.maDanhMuc "
                + "WHERE v.soLuongTon > 0 AND v.soLuongTon <= ? "
                + "ORDER BY v.soLuongTon ASC, v.hanSuDungGanNhat ASC";
        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, nguongTon);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ketQua.add(mapThuoc(rs));
                }
            }
        }
        return ketQua;
    }

    public ArrayList<Thuoc> getThuocSapHetHan(int soNgay, int gioiHan) throws SQLException {
        ArrayList<Thuoc> ketQua = new ArrayList<>();
        LocalDate homNay = LocalDate.now();
        LocalDate denNgay = homNay.plusDays(soNgay);
        String sql = "SELECT TOP " + gioiHan + " t.maThuoc, t.tenThuoc, t.donViTinh, t.giaBan, t.moTa, t.maDanhMuc, "
                + "t.hinhAnh, t.thanhPhan, t.xuatXu, dm.tenDanhMuc, "
                + "SUM(lt.soLuongConLai) AS soLuongTon, MIN(lt.hanSuDung) AS hanSuDungGanNhat "
                + "FROM Thuoc t "
                + "JOIN LoThuoc lt ON t.maThuoc = lt.maThuoc "
                + "LEFT JOIN DanhMucThuoc dm ON t.maDanhMuc = dm.maDanhMuc "
                + "WHERE lt.soLuongConLai > 0 "
                + "AND lt.hanSuDung >= ? AND lt.hanSuDung <= ? "
                + "AND lt.trangThai = N'Còn hàng' "
                + "GROUP BY t.maThuoc, t.tenThuoc, t.donViTinh, t.giaBan, t.moTa, t.maDanhMuc, "
                + "t.hinhAnh, t.thanhPhan, t.xuatXu, dm.tenDanhMuc "
                + "ORDER BY MIN(lt.hanSuDung) ASC, SUM(lt.soLuongConLai) ASC";
        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(homNay));
            stmt.setDate(2, Date.valueOf(denNgay));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ketQua.add(mapThuoc(rs));
                }
            }
        }
        return ketQua;
    }
}
