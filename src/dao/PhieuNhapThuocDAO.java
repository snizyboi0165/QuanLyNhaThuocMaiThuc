package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import connectdb.DatabaseConnection;
import entity.ChiTietNhapThuoc;
import entity.LoThuoc;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhapThuoc;
import entity.Thuoc;

public class PhieuNhapThuocDAO {
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

    private void damBaoCotNgayNhapCoGio(Connection con) throws SQLException {
        String checkSql = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE TABLE_NAME = 'PhieuNhapHang' AND COLUMN_NAME = 'ngayNhap'";
        try (Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && !"date".equalsIgnoreCase(rs.getString("DATA_TYPE"))) {
                return;
            }
        }
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate("ALTER TABLE PhieuNhapHang ALTER COLUMN ngayNhap DATETIME NOT NULL");
        }
    }

    public String generateMaPhieuNhap() {
        String maPNH = "PN00001";
        String sql = "SELECT dbo.fn_GenerateMaPhieuNhap() AS MaPhieuNhapMoi";
        try (Connection conn = getSafeConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                maPNH = rs.getString("MaPhieuNhapMoi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maPNH;
    }

    public ArrayList<PhieuNhapThuoc> getDSPhieuNhapThuoc() throws SQLException {
        ArrayList<PhieuNhapThuoc> temp = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNhapHang ORDER BY maPhieuNhap DESC";
        try (Connection con = getSafeConnection()) {
            damBaoCotNgayNhapCoGio(con);
            try (Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    temp.add(new PhieuNhapThuoc(
                            rs.getString("maPhieuNhap"),
                            new NhanVien(rs.getString("maNV")),
                            new NhaCungCap(rs.getString("maNCC")),
                            rs.getTimestamp("ngayNhap")));
                }
            }
        }
        return temp;
    }

    public ArrayList<ChiTietNhapThuoc> getChiTietPhieuNhap(String maPhieuNhap) throws SQLException {
        ArrayList<ChiTietNhapThuoc> temp = new ArrayList<>();
        String sql = "SELECT ctpn.maPhieuNhap, ctpn.maLo, lt.soLo, lt.maThuoc, lt.ngaySanXuat, lt.hanSuDung, "
                + "ctpn.soLuong, ctpn.donGia "
                + "FROM ChiTietPhieuNhap ctpn "
                + "JOIN LoThuoc lt ON ctpn.maLo = lt.maLo "
                + "WHERE ctpn.maPhieuNhap = ? "
                + "ORDER BY ctpn.maLo";
        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maPhieuNhap);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    temp.add(new ChiTietNhapThuoc(
                            rs.getString("maPhieuNhap"),
                            rs.getString("maLo"),
                            rs.getString("soLo"),
                            rs.getString("maThuoc"),
                            rs.getDate("ngaySanXuat"),
                            rs.getDate("hanSuDung"),
                            rs.getInt("soLuong"),
                            rs.getDouble("donGia")));
                }
            }
        }
        return temp;
    }

    public boolean themPhieuNhapThuoc(PhieuNhapThuoc pnh, ArrayList<ChiTietNhapThuoc> listCT) throws SQLException {
        validateInput(pnh, listCT);
        String sqlPhieu = "INSERT INTO PhieuNhapHang(maPhieuNhap, maNV, maNCC, ngayNhap) VALUES (?,?,?,?)";
        String sqlCT = "INSERT INTO ChiTietPhieuNhap(maPhieuNhap, maLo, soLuong, donGia) VALUES (?,?,?,?)";

        try (Connection con = getSafeConnection()) {
            damBaoCotNgayNhapCoGio(con);
            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            try {
                try (PreparedStatement stmt = con.prepareStatement(sqlPhieu)) {
                    stmt.setString(1, pnh.getMaPhieuNhap());
                    stmt.setString(2, pnh.getNhanVien().getMaNV());
                    stmt.setString(3, pnh.getNhaCungCap().getMaNCC());
                    stmt.setTimestamp(4, new Timestamp(pnh.getNgayNhap().getTime()));
                    if (stmt.executeUpdate() == 0) {
                        con.rollback();
                        return false;
                    }
                }

                LoThuocDAO loDAO = new LoThuocDAO();
                for (ChiTietNhapThuoc ct : listCT) {
                    validateChiTiet(ct);
                    String maLo = loDAO.generateMaLo(con);
                    ct.setMaLo(maLo);

                    LoThuoc lo = new LoThuoc(maLo, ct.getSoLo(), new Thuoc(ct.getMaThuoc()), pnh,
                            ct.getNgaySanXuat(), ct.getHanSuDung(), ct.getSoLuong(), ct.getSoLuong(),
                            ct.getDonGia(), "Còn hàng");
                    loDAO.themLoThuoc(con, lo);

                    try (PreparedStatement stmt = con.prepareStatement(sqlCT)) {
                        stmt.setString(1, pnh.getMaPhieuNhap());
                        stmt.setString(2, maLo);
                        stmt.setInt(3, ct.getSoLuong());
                        stmt.setDouble(4, ct.getDonGia());
                        if (stmt.executeUpdate() == 0) {
                            con.rollback();
                            return false;
                        }
                    }
                }

                con.commit();
                con.setAutoCommit(oldAutoCommit);
                return true;
            } catch (SQLException e) {
                con.rollback();
                con.setAutoCommit(oldAutoCommit);
                throw e;
            }
        }
    }

    public boolean capNhatPhieuNhapThuoc(PhieuNhapThuoc pnh, ArrayList<ChiTietNhapThuoc> listCT) throws SQLException {
        validateInput(pnh, listCT);
        String sqlUpdatePhieu = "UPDATE PhieuNhapHang SET maNV=?, maNCC=?, ngayNhap=? WHERE maPhieuNhap=?";
        String sqlDeleteCT = "DELETE FROM ChiTietPhieuNhap WHERE maPhieuNhap=?";
        String sqlDeleteLo = "DELETE FROM LoThuoc WHERE maPhieuNhap=? AND soLuongConLai = soLuongNhap";
        String sqlCT = "INSERT INTO ChiTietPhieuNhap(maPhieuNhap, maLo, soLuong, donGia) VALUES (?,?,?,?)";

        try (Connection con = getSafeConnection()) {
            damBaoCotNgayNhapCoGio(con);
            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            try {
                if (coLoDaPhatSinhBanHang(con, pnh.getMaPhieuNhap())) {
                    throw new SQLException("Khong the cap nhat phieu nhap vi da co lo thuoc phat sinh ban hang");
                }

                try (PreparedStatement stmt = con.prepareStatement(sqlUpdatePhieu)) {
                    stmt.setString(1, pnh.getNhanVien().getMaNV());
                    stmt.setString(2, pnh.getNhaCungCap().getMaNCC());
                    stmt.setTimestamp(3, new Timestamp(pnh.getNgayNhap().getTime()));
                    stmt.setString(4, pnh.getMaPhieuNhap());
                    stmt.executeUpdate();
                }

                try (PreparedStatement stmt = con.prepareStatement(sqlDeleteCT)) {
                    stmt.setString(1, pnh.getMaPhieuNhap());
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = con.prepareStatement(sqlDeleteLo)) {
                    stmt.setString(1, pnh.getMaPhieuNhap());
                    stmt.executeUpdate();
                }

                LoThuocDAO loDAO = new LoThuocDAO();
                for (ChiTietNhapThuoc ct : listCT) {
                    validateChiTiet(ct);
                    String maLo = ct.getMaLo();
                    if (maLo == null || maLo.trim().isEmpty()) {
                        maLo = loDAO.generateMaLo(con);
                        ct.setMaLo(maLo);
                    }
                    LoThuoc lo = new LoThuoc(maLo, ct.getSoLo(), new Thuoc(ct.getMaThuoc()), pnh,
                            ct.getNgaySanXuat(), ct.getHanSuDung(), ct.getSoLuong(), ct.getSoLuong(),
                            ct.getDonGia(), "Còn hàng");
                    loDAO.themLoThuoc(con, lo);

                    try (PreparedStatement stmt = con.prepareStatement(sqlCT)) {
                        stmt.setString(1, pnh.getMaPhieuNhap());
                        stmt.setString(2, maLo);
                        stmt.setInt(3, ct.getSoLuong());
                        stmt.setDouble(4, ct.getDonGia());
                        stmt.executeUpdate();
                    }
                }

                con.commit();
                con.setAutoCommit(oldAutoCommit);
                return true;
            } catch (SQLException e) {
                con.rollback();
                con.setAutoCommit(oldAutoCommit);
                throw e;
            }
        }
    }

    public boolean xoaPhieuNhapThuoc(String maPhieuNhap) throws SQLException {
        String sqlDeleteCT = "DELETE FROM ChiTietPhieuNhap WHERE maPhieuNhap=?";
        String sqlDeleteLo = "DELETE FROM LoThuoc WHERE maPhieuNhap=? AND soLuongConLai = soLuongNhap";
        String sqlDeletePhieu = "DELETE FROM PhieuNhapHang WHERE maPhieuNhap=?";

        try (Connection con = getSafeConnection()) {
            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            try {
                try (PreparedStatement stmt = con.prepareStatement(sqlDeleteCT)) {
                    stmt.setString(1, maPhieuNhap);
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = con.prepareStatement(sqlDeleteLo)) {
                    stmt.setString(1, maPhieuNhap);
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = con.prepareStatement(sqlDeletePhieu)) {
                    stmt.setString(1, maPhieuNhap);
                    boolean ok = stmt.executeUpdate() > 0;
                    if (!ok) {
                        con.rollback();
                        return false;
                    }
                }
                con.commit();
                con.setAutoCommit(oldAutoCommit);
                return true;
            } catch (SQLException e) {
                con.rollback();
                con.setAutoCommit(oldAutoCommit);
                throw e;
            }
        }
    }

    public PhieuNhapThuoc getPhieuNhapTheoMa(String maPhieuNhap) throws SQLException {
        String sql = "SELECT * FROM PhieuNhapHang WHERE maPhieuNhap = ?";
        try (Connection con = getSafeConnection()) {
            damBaoCotNgayNhapCoGio(con);
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maPhieuNhap);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new PhieuNhapThuoc(maPhieuNhap, new NhanVien(rs.getString("maNV")),
                                new NhaCungCap(rs.getString("maNCC")), rs.getTimestamp("ngayNhap"));
                    }
                }
            }
        }
        return null;
    }

    public boolean kiemTraMaPhieuNhapTonTai(String maPhieuNhap) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PhieuNhapHang WHERE maPhieuNhap = ?";
        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maPhieuNhap);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public ArrayList<PhieuNhapThuoc> timKiemPhieuNhap(String maPhieuNhap, String maNV, String maNCC,
            java.util.Date ngayTu, java.util.Date ngayDen) throws SQLException {
        ArrayList<PhieuNhapThuoc> ketQua = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM PhieuNhapHang WHERE 1=1");
        ArrayList<Object> params = new ArrayList<>();

        if (maPhieuNhap != null && !maPhieuNhap.isEmpty()) {
            sql.append(" AND maPhieuNhap LIKE ?");
            params.add("%" + maPhieuNhap + "%");
        }
        if (maNV != null && !maNV.isEmpty()) {
            sql.append(" AND maNV LIKE ?");
            params.add("%" + maNV + "%");
        }
        if (maNCC != null && !maNCC.isEmpty()) {
            sql.append(" AND maNCC LIKE ?");
            params.add("%" + maNCC + "%");
        }
        if (ngayTu != null) {
            sql.append(" AND ngayNhap >= ?");
            params.add(new Timestamp(ngayTu.getTime()));
        }
        if (ngayDen != null) {
            sql.append(" AND ngayNhap <= ?");
            params.add(new Timestamp(ngayDen.getTime()));
        }

        sql.append(" ORDER BY maPhieuNhap DESC");

        try (Connection con = getSafeConnection()) {
            damBaoCotNgayNhapCoGio(con);
            try (PreparedStatement stmt = con.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        ketQua.add(new PhieuNhapThuoc(
                                rs.getString("maPhieuNhap"),
                                new NhanVien(rs.getString("maNV")),
                                new NhaCungCap(rs.getString("maNCC")),
                                rs.getTimestamp("ngayNhap")));
                    }
                }
            }
        }
        return ketQua;
    }

    private void validateInput(PhieuNhapThuoc pnh, ArrayList<ChiTietNhapThuoc> listCT) throws SQLException {
        if (pnh == null || pnh.getMaPhieuNhap() == null || pnh.getMaPhieuNhap().trim().isEmpty()) {
            throw new SQLException("Ma phieu nhap khong hop le");
        }
        if (pnh.getNhanVien() == null || pnh.getNhaCungCap() == null) {
            throw new SQLException("Nhan vien hoac nha cung cap khong hop le");
        }
        if (listCT == null || listCT.isEmpty()) {
            throw new SQLException("Phai co it nhat mot chi tiet phieu nhap");
        }
    }

    private void validateChiTiet(ChiTietNhapThuoc ct) throws SQLException {
        if (ct.getMaThuoc() == null || ct.getMaThuoc().trim().isEmpty()) {
            throw new SQLException("Ma thuoc khong hop le");
        }
        if (ct.getSoLo() == null || ct.getSoLo().trim().isEmpty()) {
            throw new SQLException("So lo khong duoc rong");
        }
        if (ct.getHanSuDung() == null) {
            throw new SQLException("Han su dung cua lo khong duoc rong");
        }
        if (ct.getNgaySanXuat() != null && ct.getNgaySanXuat().after(ct.getHanSuDung())) {
            throw new SQLException("Ngay san xuat phai truoc han su dung");
        }
        if (ct.getSoLuong() <= 0 || ct.getDonGia() < 0) {
            throw new SQLException("So luong phai > 0 va gia khong duoc am");
        }
    }

    private boolean coLoDaPhatSinhBanHang(Connection con, String maPhieuNhap) throws SQLException {
        String sql = "SELECT COUNT(*) FROM LoThuoc WHERE maPhieuNhap = ? AND soLuongConLai < soLuongNhap";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maPhieuNhap);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
