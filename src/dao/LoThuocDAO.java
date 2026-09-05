package dao;

import entity.LoThuoc;
import entity.PhieuNhapThuoc;
import entity.Thuoc;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectdb.DatabaseConnection;

public class LoThuocDAO {
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

    public String generateMaLo() throws SQLException {
        try (Connection con = getSafeConnection()) {
            return generateMaLo(con);
        }
    }

    String generateMaLo(Connection con) throws SQLException {
        String sql = "SELECT dbo.fn_GenerateMaLo() AS maLo";
        try (Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("maLo");
            }
        }
        throw new SQLException("Khong the tao ma lo tu dong");
    }

    public boolean themLoThuoc(LoThuoc lo) throws SQLException {
        try (Connection con = getSafeConnection()) {
            return themLoThuoc(con, lo);
        }
    }

    boolean themLoThuoc(Connection con, LoThuoc lo) throws SQLException {
        String maLo = lo.getMaLo();
        if (maLo == null || maLo.trim().isEmpty()) {
            maLo = generateMaLo(con);
            lo.setMaLo(maLo);
        }

        String sql = "INSERT INTO LoThuoc(maLo, soLo, maThuoc, maPhieuNhap, ngaySanXuat, hanSuDung, "
                + "soLuongNhap, soLuongConLai, donGiaNhap, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, lo.getMaLo());
            stmt.setString(2, lo.getSoLo());
            stmt.setString(3, lo.getThuoc().getMaThuoc());
            stmt.setString(4, lo.getPhieuNhap().getMaPhieuNhap());
            if (lo.getNgaySanXuat() != null) {
                stmt.setDate(5, new Date(lo.getNgaySanXuat().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            stmt.setDate(6, new Date(lo.getHanSuDung().getTime()));
            stmt.setInt(7, lo.getSoLuongNhap());
            stmt.setInt(8, lo.getSoLuongConLai());
            stmt.setDouble(9, lo.getDonGiaNhap());
            stmt.setString(10, lo.getTrangThai());
            return stmt.executeUpdate() > 0;
        }
    }

    public LoThuoc getLoThuocTheoMa(String maLo) throws SQLException {
        String sql = "SELECT * FROM LoThuoc WHERE maLo = ?";
        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maLo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapLo(rs);
                }
            }
        }
        return null;
    }

    public ArrayList<LoThuoc> getLoThuocTheoMaThuoc(String maThuoc) throws SQLException {
        String sql = "SELECT * FROM LoThuoc WHERE maThuoc = ? ORDER BY hanSuDung ASC, maLo ASC";
        return getLoTheoSql(sql, maThuoc);
    }

    public ArrayList<LoThuoc> getLoConHangTheoMaThuoc(String maThuoc) throws SQLException {
        String sql = "SELECT * FROM LoThuoc WHERE maThuoc = ? AND soLuongConLai > 0 ORDER BY hanSuDung ASC, maLo ASC";
        return getLoTheoSql(sql, maThuoc);
    }

    private ArrayList<LoThuoc> getLoTheoSql(String sql, String maThuoc) throws SQLException {
        ArrayList<LoThuoc> result = new ArrayList<>();
        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maThuoc);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapLo(rs));
                }
            }
        }
        return result;
    }

    public boolean truTonTheoFEFO(String maThuoc, int soLuongBan) throws SQLException {
        if (soLuongBan <= 0) {
            return true;
        }

        try (Connection con = getSafeConnection()) {
            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            try {
                boolean ok = truTonTheoFEFO(con, maThuoc, soLuongBan);
                if (!ok) {
                    con.rollback();
                    return false;
                }
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

    boolean truTonTheoFEFO(Connection con, String maThuoc, int soLuongBan) throws SQLException {
        if (soLuongBan <= 0) {
            return true;
        }

        String selectSql = "SELECT maLo, soLuongConLai FROM LoThuoc "
                + "WHERE maThuoc = ? "
                + "AND soLuongConLai > 0 "
                + "AND hanSuDung >= CAST(GETDATE() AS DATE) "
                + "AND trangThai = N'Còn hàng' "
                + "ORDER BY hanSuDung ASC, maLo ASC";
        String updateSql = "UPDATE LoThuoc SET soLuongConLai = ?, trangThai = ? WHERE maLo = ?";

        ArrayList<String> maLos = new ArrayList<>();
        ArrayList<Integer> soLuongs = new ArrayList<>();
        int tongTon = 0;

        try (PreparedStatement stmt = con.prepareStatement(selectSql)) {
            stmt.setString(1, maThuoc);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    maLos.add(rs.getString("maLo"));
                    int conLai = rs.getInt("soLuongConLai");
                    soLuongs.add(conLai);
                    tongTon += conLai;
                }
            }
        }

        if (tongTon < soLuongBan) {
            return false;
        }

        int canTru = soLuongBan;
        try (PreparedStatement stmt = con.prepareStatement(updateSql)) {
            for (int i = 0; i < maLos.size() && canTru > 0; i++) {
                int hienTai = soLuongs.get(i);
                int tru = Math.min(hienTai, canTru);
                int moi = hienTai - tru;
                stmt.setInt(1, moi);
                stmt.setString(2, moi == 0 ? "Hết hàng" : "Còn hàng");
                stmt.setString(3, maLos.get(i));
                stmt.addBatch();
                canTru -= tru;
            }
            stmt.executeBatch();
        }

        return true;
    }

    private LoThuoc mapLo(ResultSet rs) throws SQLException {
        String maLo = rs.getString("maLo");
        String soLo = rs.getString("soLo");
        String maThuoc = rs.getString("maThuoc");
        String maPhieuNhap = rs.getString("maPhieuNhap");
        Date ngaySanXuat = rs.getDate("ngaySanXuat");
        Date hanSuDung = rs.getDate("hanSuDung");
        int soLuongNhap = rs.getInt("soLuongNhap");
        int soLuongConLai = rs.getInt("soLuongConLai");
        double donGiaNhap = rs.getDouble("donGiaNhap");
        String trangThai = rs.getString("trangThai");
        return new LoThuoc(maLo, soLo, new Thuoc(maThuoc), new PhieuNhapThuoc(maPhieuNhap, null, null, null),
                ngaySanXuat, hanSuDung, soLuongNhap, soLuongConLai, donGiaNhap, trangThai);
    }
}
