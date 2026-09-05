package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectdb.DatabaseConnection;
import entity.*;

public class PhieuDoiTraDAO {

    public boolean themPhieuDoiTra(PhieuDoiTra phieuDoiTra) throws SQLException {
        String sql = "INSERT INTO PhieuDoiTra (maPhieuDoiTra, ngayDoiTra, loaiPhieu, lyDo, tongTien, trangThai, maNV, maKH, maHDGoc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, phieuDoiTra.getMaPhieuDoiTra());
            stmt.setDate(2, new Date(phieuDoiTra.getNgayDoiTra().getTime()));
            stmt.setString(3, phieuDoiTra.getLoaiPhieu());
            stmt.setString(4, phieuDoiTra.getLyDo());
            stmt.setDouble(5, phieuDoiTra.getTongTien());
            stmt.setString(6, phieuDoiTra.getTrangThai());
            
            // maNV may be null in some flows — handle safely
            if (phieuDoiTra.getNhanVien() != null && phieuDoiTra.getNhanVien().getMaNV() != null) {
                stmt.setString(7, phieuDoiTra.getNhanVien().getMaNV());
            } else {
                stmt.setNull(7, java.sql.Types.NVARCHAR);
            }
            
            // maKH (khách hàng) can be nullable in DB — avoid NPE
            if (phieuDoiTra.getKhachHang() != null && phieuDoiTra.getKhachHang().getMaKH() != null) {
                stmt.setString(8, phieuDoiTra.getKhachHang().getMaKH());
            } else {
                stmt.setNull(8, java.sql.Types.NVARCHAR);
            }
            
            // maHDGoc (hóa đơn gốc) can be nullable
            if (phieuDoiTra.getHoaDonGoc() != null && phieuDoiTra.getHoaDonGoc().getMaHD() != null) {
                stmt.setString(9, phieuDoiTra.getHoaDonGoc().getMaHD());
            } else {
                stmt.setNull(9, java.sql.Types.NVARCHAR);
            }
            
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean themChiTietPhieuDoiTra(ChiTietPhieuDoiTra chiTiet) throws SQLException {
        String sql = "INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, trangThaiThuoc, chenhLechTien) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, chiTiet.getMaPhieuDoiTra());

            if (chiTiet.getThuoc() == null || chiTiet.getThuoc().getMaThuoc() == null || chiTiet.getThuoc().getMaThuoc().trim().isEmpty()) {
                throw new SQLException("maThuoc cannot be null when inserting ChiTietPhieuDoiTra");
            }
            stmt.setString(2, chiTiet.getThuoc().getMaThuoc());

            // Determine effective quantity: prefer soLuong, fall back to soLuongThayThe (backward compatibility)
            Integer qty = (chiTiet.getSoLuong() != null && chiTiet.getSoLuong() > 0) ? chiTiet.getSoLuong() : null;
            if (qty == null && chiTiet.getSoLuongThayThe() != null && chiTiet.getSoLuongThayThe() > 0) {
                qty = chiTiet.getSoLuongThayThe();
            }
            if (qty == null || qty <= 0) {
                throw new SQLException("Invalid soLuong when inserting ChiTietPhieuDoiTra. Quantity must be > 0. phieu=" + chiTiet.getMaPhieuDoiTra() + ", thuoc=" + chiTiet.getThuoc().getMaThuoc());
            }

            stmt.setInt(3, qty);

            // Determine donGia: prefer donGia, fall back to donGiaThayThe
            double donGia = chiTiet.getDonGia();
            if ((donGia == 0.0 || donGia < 0.0) && chiTiet.getDonGiaThayThe() != null && chiTiet.getDonGiaThayThe() > 0.0) {
                donGia = chiTiet.getDonGiaThayThe();
            }
            stmt.setDouble(4, donGia);
            stmt.setString(5, chiTiet.getTrangThaiThuoc() != null ? chiTiet.getTrangThaiThuoc() : "");
            stmt.setDouble(6, chiTiet.getChenhLechTien());

            return stmt.executeUpdate() > 0;
        }
    }

    public List<PhieuDoiTra> getDanhSachPhieuDoiTra() throws SQLException {
        List<PhieuDoiTra> danhSach = new ArrayList<>();
        String sql = "SELECT pdt.*, nv.hoTen as tenNV, kh.hoTen as tenKH, hd.maHD AS maHDGoc " +
                "FROM PhieuDoiTra pdt " +
                "LEFT JOIN NhanVien nv ON pdt.maNV = nv.maNV " +
                "LEFT JOIN KhachHang kh ON pdt.maKH = kh.maKH " +
                "LEFT JOIN HoaDon hd ON pdt.maHDGoc = hd.maHD " +
                "ORDER BY pdt.ngayDoiTra DESC";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PhieuDoiTra phieu = new PhieuDoiTra();
                phieu.setMaPhieuDoiTra(rs.getString("maPhieuDoiTra"));
                phieu.setNgayDoiTra(rs.getDate("ngayDoiTra"));
                phieu.setLoaiPhieu(rs.getString("loaiPhieu"));
                phieu.setLyDo(rs.getString("lyDo"));
                phieu.setTongTien(rs.getDouble("tongTien"));
                phieu.setTrangThai(rs.getString("trangThai"));

                // Tạo đối tượng NhanVien
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV"));
                nv.setTenNV(rs.getString("tenNV"));
                phieu.setNhanVien(nv);

                // Tạo đối tượng KhachHang
                String maKH = rs.getString("maKH");
                String tenKH = rs.getString("tenKH");
                if ((maKH != null && !maKH.trim().isEmpty()) || (tenKH != null && !tenKH.trim().isEmpty())) {
                    KhachHang kh = new KhachHang();
                    if (maKH != null && !maKH.trim().isEmpty()) kh.setMaKH(maKH);
                    // Chỉ gọi setHoTen khi tên không null/empty để tránh IllegalArgumentException
                    if (tenKH != null && !tenKH.trim().isEmpty()) {
                        kh.setHoTen(tenKH);
                    }
                    phieu.setKhachHang(kh);
                } else {
                    phieu.setKhachHang(null);
                }

                // Tạo đối tượng HoaDon
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("maHDGoc"));
                phieu.setHoaDonGoc(hd);

                danhSach.add(phieu);
            }
        }

        return danhSach;
    }

    public List<PhieuDoiTra> getDanhSachPhieuDoiTraTheoLoai(String loaiPhieu) throws SQLException {
        List<PhieuDoiTra> danhSach = new ArrayList<>();
        String sql = "SELECT pdt.*, nv.hoTen as tenNV, kh.hoTen as tenKH, hd.maHD AS maHDGoc " +
                "FROM PhieuDoiTra pdt " +
                "LEFT JOIN NhanVien nv ON pdt.maNV = nv.maNV " +
                "LEFT JOIN KhachHang kh ON pdt.maKH = kh.maKH " +
                "LEFT JOIN HoaDon hd ON pdt.maHDGoc = hd.maHD " +
                "WHERE pdt.loaiPhieu = ? " +
                "ORDER BY pdt.ngayDoiTra DESC";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, loaiPhieu);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PhieuDoiTra phieu = new PhieuDoiTra();
                    phieu.setMaPhieuDoiTra(rs.getString("maPhieuDoiTra"));
                    phieu.setNgayDoiTra(rs.getDate("ngayDoiTra"));
                    phieu.setLoaiPhieu(rs.getString("loaiPhieu"));
                    phieu.setLyDo(rs.getString("lyDo"));
                    phieu.setTongTien(rs.getDouble("tongTien"));
                    phieu.setTrangThai(rs.getString("trangThai"));

                    // Tạo đối tượng NhanVien
                    NhanVien nv = new NhanVien();
                    nv.setMaNV(rs.getString("maNV"));
                    nv.setTenNV(rs.getString("tenNV"));
                    phieu.setNhanVien(nv);

                    // Tạo đối tượng KhachHang
                    String maKH = rs.getString("maKH");
                    String tenKH = rs.getString("tenKH");
                    if ((maKH != null && !maKH.trim().isEmpty()) || (tenKH != null && !tenKH.trim().isEmpty())) {
                        KhachHang kh = new KhachHang();
                        if (maKH != null && !maKH.trim().isEmpty()) kh.setMaKH(maKH);
                        if (tenKH != null && !tenKH.trim().isEmpty()) {
                            kh.setHoTen(tenKH);
                        }
                        phieu.setKhachHang(kh);
                    } else {
                        phieu.setKhachHang(null);
                    }

                    // Tạo đối tượng HoaDon
                    HoaDon hd = new HoaDon();
                    hd.setMaHD(rs.getString("maHDGoc"));
                    phieu.setHoaDonGoc(hd);

                    danhSach.add(phieu);
                }
            }
        }

        return danhSach;
    }

    public List<ChiTietPhieuDoiTra> getChiTietPhieuDoiTra(String maPhieuDoiTra) throws SQLException {
        List<ChiTietPhieuDoiTra> danhSach = new ArrayList<>();
        String sql = "SELECT ct.*, t.tenThuoc, t.donViTinh " +
                "FROM ChiTietPhieuDoiTra ct " +
                "LEFT JOIN Thuoc t ON ct.maThuoc = t.maThuoc " +
                "WHERE ct.maPhieuDoiTra = ?";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maPhieuDoiTra);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuDoiTra chiTiet = new ChiTietPhieuDoiTra();
                    chiTiet.setMaPhieuDoiTra(rs.getString("maPhieuDoiTra"));

                    // Thuốc (single column now)
                    Thuoc thuoc = new Thuoc();
                    String maThuoc = rs.getString("maThuoc");
                    if (maThuoc == null || maThuoc.isEmpty()) {
                        throw new SQLException("Dữ liệu maThuoc không hợp lệ trong bảng ChiTietPhieuDoiTra");
                    }
                    thuoc.setMaThuoc(maThuoc);
                    thuoc.setTenThuoc(rs.getString("tenThuoc"));
                    thuoc.setDonViTinh(rs.getString("donViTinh"));
                    chiTiet.setThuoc(thuoc);

                    chiTiet.setSoLuong(rs.getInt("soLuong"));
                    chiTiet.setDonGia(rs.getDouble("donGia"));
                    try { chiTiet.setTrangThaiThuoc(rs.getString("trangThaiThuoc")); } catch (SQLException ignore) { chiTiet.setTrangThaiThuoc(null); }
                    try { chiTiet.setChenhLechTien(rs.getDouble("chenhLechTien")); } catch (SQLException ignore) { chiTiet.setChenhLechTien(0.0); }

                    danhSach.add(chiTiet);
                }
            }
        }

        return danhSach;
    }

    public String generateMaPhieuDoiTra() throws SQLException {
        String sql = "SELECT maPhieuDoiTra FROM PhieuDoiTra WHERE maPhieuDoiTra LIKE 'PDT%' ORDER BY maPhieuDoiTra DESC";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastMa = rs.getString("maPhieuDoiTra");
                if (lastMa != null && lastMa.matches("PDT\\d{5}")) { // Validate format PDTxxxxx
                    int number = Integer.parseInt(lastMa.substring(3)) + 1;
                    return String.format("PDT%05d", number);
                } else {
                    System.err.println("Invalid format for maPhieuDoiTra: " + lastMa);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while generating maPhieuDoiTra: " + e.getMessage());
        }

        // Fallback: Generate a unique ID using the current timestamp
        long timestamp = System.currentTimeMillis() % 100000;
        return String.format("PDT%05d", timestamp);
    }

    public boolean capNhatTrangThai(String maPhieuDoiTra, String trangThai) throws SQLException {
        String sql = "UPDATE PhieuDoiTra SET trangThai = ? WHERE maPhieuDoiTra = ?";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, trangThai);
            stmt.setString(2, maPhieuDoiTra);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật tồn kho khi xử lý phiếu đổi/trả
     */
    public boolean capNhatTonKhoKhiXuLy(String maPhieuDoiTra) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
            con.setAutoCommit(false); // Bắt đầu transaction

            // Lấy thông tin phiếu
            PhieuDoiTra phieu = getPhieuDoiTraTheoMa(maPhieuDoiTra);
            if (phieu == null) {
                return false;
            }

            // Lấy chi tiết phiếu
            List<ChiTietPhieuDoiTra> chiTietList = getChiTietPhieuDoiTra(maPhieuDoiTra);

            for (ChiTietPhieuDoiTra chiTiet : chiTietList) {
                String maThuoc = chiTiet.getThuoc().getMaThuoc();
                int qty = chiTiet.getSoLuong() != null ? chiTiet.getSoLuong() : 0;
                int delta;
                // chenhLechTien < 0 => thuốc được trả về (tăng tồn), >0 => thuốc được xuất (giảm tồn)
                if (chiTiet.getChenhLechTien() < 0) {
                    delta = qty; // increase
                } else {
                    delta = -qty; // decrease
                }

                if (!capNhatTonKho(con, maThuoc, delta)) {
                    throw new SQLException("Không thể cập nhật tồn kho cho thuốc: " + maThuoc);
                }

                // Ghi log lịch sử thay đổi tồn kho
                ghiLogThayDoiTonKho(con, maThuoc, delta, "Phiếu " + phieu.getLoaiPhieu() + ": " + maPhieuDoiTra);
            }

            con.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    if (!con.isClosed()) {
                        con.rollback(); // Rollback nếu có lỗi
                    } else {
                        System.err.println("Cannot rollback because connection is already closed. Original error: " + e.getMessage());
                    }
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                    rollbackEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    if (!con.isClosed()) {
                        con.setAutoCommit(true); // Khôi phục auto commit
                        con.close();
                    }
                } catch (SQLException e) {
                    System.err.println("Error while closing connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Lấy phiếu đổi trả theo mã
     */
    public PhieuDoiTra getPhieuDoiTraTheoMa(String maPhieuDoiTra) throws SQLException {
        String sql = "SELECT pdt.*, nv.hoTen as tenNV, kh.hoTen as tenKH, hd.maHD AS maHDGoc " +
                "FROM PhieuDoiTra pdt " +
                "LEFT JOIN NhanVien nv ON pdt.maNV = nv.maNV " +
                "LEFT JOIN KhachHang kh ON pdt.maKH = kh.maKH " +
                "LEFT JOIN HoaDon hd ON pdt.maHDGoc = hd.maHD " +
                "WHERE pdt.maPhieuDoiTra = ?";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maPhieuDoiTra);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PhieuDoiTra phieu = new PhieuDoiTra();
                    phieu.setMaPhieuDoiTra(rs.getString("maPhieuDoiTra"));
                    phieu.setNgayDoiTra(rs.getDate("ngayDoiTra"));
                    phieu.setLoaiPhieu(rs.getString("loaiPhieu"));
                    phieu.setLyDo(rs.getString("lyDo"));
                    phieu.setTongTien(rs.getDouble("tongTien"));
                    phieu.setTrangThai(rs.getString("trangThai"));

                    // Tạo đối tượng NhanVien
                    NhanVien nv = new NhanVien();
                    nv.setMaNV(rs.getString("maNV"));
                    nv.setTenNV(rs.getString("tenNV"));
                    phieu.setNhanVien(nv);

                    // Tạo đối tượng KhachHang
                    String maKH = rs.getString("maKH");
                    String tenKH = rs.getString("tenKH");
                    if ((maKH != null && !maKH.trim().isEmpty()) || (tenKH != null && !tenKH.trim().isEmpty())) {
                        KhachHang kh = new KhachHang();
                        if (maKH != null && !maKH.trim().isEmpty()) kh.setMaKH(maKH);
                        // Chỉ gọi setHoTen khi tên không null/empty để tránh IllegalArgumentException
                        if (tenKH != null && !tenKH.trim().isEmpty()) {
                            kh.setHoTen(tenKH);
                        }
                        phieu.setKhachHang(kh);
                    } else {
                        phieu.setKhachHang(null);
                    }

                    // Tạo đối tượng HoaDon
                    HoaDon hd = new HoaDon();
                    hd.setMaHD(rs.getString("maHDGoc"));
                    phieu.setHoaDonGoc(hd);

                    return phieu;
                }
            }
        }

        return null;
    }

    /**
     * Ghi log thay đổi tồn kho
     */
    private void ghiLogThayDoiTonKho(Connection con, String maThuoc, int soLuongThayDoi, String lyDo)
            throws SQLException {
        String sql = "INSERT INTO LogTonKho (maThuoc, soLuongThayDoi, lyDo, ngayThayDoi) VALUES (?, ?, ?, GETDATE())";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maThuoc);
            stmt.setInt(2, soLuongThayDoi);
            stmt.setString(3, lyDo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Nếu bảng LogTonKho chưa tồn tại, bỏ qua lỗi này
            System.err.println("Không thể ghi log tồn kho: " + e.getMessage());
        }
    }

    /**
     * Thống kê phiếu đổi/trả theo thời gian
     */
    public List<ThongKeDoiTra> thongKeDoiTraTheoThang(int nam) throws SQLException {
        List<ThongKeDoiTra> danhSach = new ArrayList<>();
        String sql = "SELECT " +
                "MONTH(ngayDoiTra) as thang, " +
                "loaiPhieu, " +
                "COUNT(*) as soLuongPhieu, " +
                "SUM(tongTien) as tongTien " +
                "FROM PhieuDoiTra " +
                "WHERE YEAR(ngayDoiTra) = ? AND trangThai = N'Đã xử lý' " +
                "GROUP BY MONTH(ngayDoiTra), loaiPhieu " +
                "ORDER BY thang, loaiPhieu";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, nam);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ThongKeDoiTra tk = new ThongKeDoiTra();
                    tk.setThang(rs.getInt("thang"));
                    tk.setLoaiPhieu(rs.getString("loaiPhieu"));
                    tk.setSoLuongPhieu(rs.getInt("soLuongPhieu"));
                    tk.setTongTien(rs.getDouble("tongTien"));

                    danhSach.add(tk);
                }
            }
        }

        return danhSach;
    }

    /**
     * Thống kê thuốc bị trả nhiều nhất
     */
    public List<ThongKeThuocBiTra> thongKeThuocBiTraNhieuNhat(int top) throws SQLException {
        List<ThongKeThuocBiTra> danhSach = new ArrayList<>();
        String sql = "SELECT TOP " + top + " " +
                "ct.maThuoc, " +
                "t.tenThuoc, " +
                "COUNT(*) as soLanTra, " +
                "SUM(ct.soLuong) as tongSoLuongTra, " +
                "SUM(ct.soLuong * ct.donGia) as tongTienTra " +
                "FROM ChiTietPhieuDoiTra ct " +
                "JOIN Thuoc t ON ct.maThuoc = t.maThuoc " +
                "JOIN PhieuDoiTra pdt ON ct.maPhieuDoiTra = pdt.maPhieuDoiTra " +
                "WHERE pdt.trangThai = N'Đã xử lý' " +
                "GROUP BY ct.maThuoc, t.tenThuoc " +
                "ORDER BY tongSoLuongTra DESC";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ThongKeThuocBiTra tk = new ThongKeThuocBiTra();
                    tk.setMaThuoc(rs.getString("maThuoc"));
                    tk.setTenThuoc(rs.getString("tenThuoc"));
                    tk.setSoLanTra(rs.getInt("soLanTra"));
                    tk.setTongSoLuongTra(rs.getInt("tongSoLuongTra"));
                    tk.setTongTienTra(rs.getDouble("tongTienTra"));

                    danhSach.add(tk);
                }
            }
        }

        return danhSach;
    }

    // Inner classes cho thống kê
    public static class ThongKeDoiTra {
        private int thang;
        private String loaiPhieu;
        private int soLuongPhieu;
        private double tongTien;

        // Getters and setters
        public int getThang() {
            return thang;
        }

        public void setThang(int thang) {
            this.thang = thang;
        }

        public String getLoaiPhieu() {
            return loaiPhieu;
        }

        public void setLoaiPhieu(String loaiPhieu) {
            this.loaiPhieu = loaiPhieu;
        }

        public int getSoLuongPhieu() {
            return soLuongPhieu;
        }

        public void setSoLuongPhieu(int soLuongPhieu) {
            this.soLuongPhieu = soLuongPhieu;
        }

        public double getTongTien() {
            return tongTien;
        }

        public void setTongTien(double tongTien) {
            this.tongTien = tongTien;
        }
    }

    public static class ThongKeThuocBiTra {
        private String maThuoc;
        private String tenThuoc;
        private int soLanTra;
        private int tongSoLuongTra;
        private double tongTienTra;

        // Getters and setters
        public String getMaThuoc() {
            return maThuoc;
        }

        public void setMaThuoc(String maThuoc) {
            this.maThuoc = maThuoc;
        }

        public String getTenThuoc() {
            return tenThuoc;
        }

        public void setTenThuoc(String tenThuoc) {
            this.tenThuoc = tenThuoc;
        }

        public int getSoLanTra() {
            return soLanTra;
        }

        public void setSoLanTra(int soLanTra) {
            this.soLanTra = soLanTra;
        }

        public int getTongSoLuongTra() {
            return tongSoLuongTra;
        }

        public void setTongSoLuongTra(int tongSoLuongTra) {
            this.tongSoLuongTra = tongSoLuongTra;
        }

        public double getTongTienTra() {
            return tongTienTra;
        }

        public void setTongTienTra(double tongTienTra) {
            this.tongTienTra = tongTienTra;
        }
    }

    /**
     * Xử lý đổi thuốc - bao gồm cả thuốc cũ và thuốc mới
     */
    public boolean xuLyDoiThuoc(String maPhieuDoiTra, String maNhanVien) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            // Load PhieuDoiTra
            PhieuDoiTra phieu = null;
            String sqlPhieu = "SELECT pdt.*, nv.hoTen as tenNV, kh.hoTen as tenKH, hd.maHD AS maHDGoc " +
                    "FROM PhieuDoiTra pdt " +
                    "LEFT JOIN NhanVien nv ON pdt.maNV = nv.maNV " +
                    "LEFT JOIN KhachHang kh ON pdt.maKH = kh.maKH " +
                    "LEFT JOIN HoaDon hd ON pdt.maHDGoc = hd.maHD " +
                    "WHERE pdt.maPhieuDoiTra = ?";

            try (PreparedStatement psPhieu = con.prepareStatement(sqlPhieu)) {
                psPhieu.setString(1, maPhieuDoiTra);
                try (ResultSet rs = psPhieu.executeQuery()) {
                    if (rs.next()) {
                        phieu = new PhieuDoiTra();
                        phieu.setMaPhieuDoiTra(rs.getString("maPhieuDoiTra"));
                        phieu.setNgayDoiTra(rs.getDate("ngayDoiTra"));
                        phieu.setLoaiPhieu(rs.getString("loaiPhieu"));
                        phieu.setLyDo(rs.getString("lyDo"));
                        phieu.setTongTien(rs.getDouble("tongTien"));
                        phieu.setTrangThai(rs.getString("trangThai"));

                        NhanVien nv = new NhanVien();
                        nv.setMaNV(rs.getString("maNV"));
                        nv.setTenNV(rs.getString("tenNV"));
                        phieu.setNhanVien(nv);

                        String maKH = rs.getString("maKH");
                        String tenKH = rs.getString("tenKH");
                        if ((maKH != null && !maKH.trim().isEmpty()) || (tenKH != null && !tenKH.trim().isEmpty())) {
                            KhachHang kh = new KhachHang();
                            if (maKH != null && !maKH.trim().isEmpty()) kh.setMaKH(maKH);
                            if (tenKH != null && !tenKH.trim().isEmpty()) kh.setHoTen(tenKH);
                            phieu.setKhachHang(kh);
                        }

                        HoaDon hd = new HoaDon();
                        hd.setMaHD(rs.getString("maHDGoc"));
                        phieu.setHoaDonGoc(hd);
                    }
                }
            }

            if (phieu == null || !"DOI".equals(phieu.getLoaiPhieu())) {
                throw new SQLException("Phiếu đổi không hợp lệ");
            }

            // Lấy chi tiết phiếu đổi
            List<ChiTietPhieuDoiTra> chiTietList = new ArrayList<>();
            String sqlChiTiet = "SELECT ct.*, t.tenThuoc as tenThuoc, t.donViTinh as dvt " +
                    "FROM ChiTietPhieuDoiTra ct " +
                    "LEFT JOIN Thuoc t ON ct.maThuoc = t.maThuoc " +
                    "WHERE ct.maPhieuDoiTra = ?";

            try (PreparedStatement psCt = con.prepareStatement(sqlChiTiet)) {
                psCt.setString(1, maPhieuDoiTra);
                try (ResultSet rs = psCt.executeQuery()) {
                    while (rs.next()) {
                        ChiTietPhieuDoiTra chiTiet = new ChiTietPhieuDoiTra();
                        chiTiet.setMaPhieuDoiTra(rs.getString("maPhieuDoiTra"));
                        Thuoc thuoc = new Thuoc();
                        thuoc.setMaThuoc(rs.getString("maThuoc"));
                        thuoc.setTenThuoc(rs.getString("tenThuoc"));
                        thuoc.setDonViTinh(rs.getString("dvt"));
                        chiTiet.setThuoc(thuoc);

                        chiTiet.setSoLuong(rs.getInt("soLuong"));
                        chiTiet.setDonGia(rs.getDouble("donGia"));
                        try { chiTiet.setTrangThaiThuoc(rs.getString("trangThaiThuoc")); } catch (SQLException ignore) { chiTiet.setTrangThaiThuoc(null); }
                        try { chiTiet.setChenhLechTien(rs.getDouble("chenhLechTien")); } catch (SQLException ignore) { chiTiet.setChenhLechTien(0.0); }

                        chiTietList.add(chiTiet);
                    }
                }
            }

            double tongChenhLech = 0;

            for (ChiTietPhieuDoiTra chiTiet : chiTietList) {
                String maThuoc = chiTiet.getThuoc().getMaThuoc();
                int qty = chiTiet.getSoLuong() != null ? chiTiet.getSoLuong() : 0;

                if (chiTiet.getChenhLechTien() < 0) {
                    // Khách trả thuốc cũ => TĂNG tồn kho
                    if (!capNhatTonKho(con, maThuoc, qty)) {
                        throw new SQLException("Lỗi khi cập nhật kho thuốc trả về: " + maThuoc);
                    }
                    ghiLogThayDoiTonKho(con, maThuoc, qty, "Đổi thuốc - Nhập lại thuốc cũ: " + maPhieuDoiTra);

                    // Cập nhật lại chi tiết hóa đơn gốc: giảm số lượng thuốc trả về
                    if (phieu.getHoaDonGoc() != null && phieu.getHoaDonGoc().getMaHD() != null) {
                        adjustChiTietHoaDon(con, phieu.getHoaDonGoc().getMaHD(), maThuoc, -qty, null);
                    }
                } else if (chiTiet.getChenhLechTien() > 0) {
                    // Cấp thuốc mới cho khách => GIẢM tồn kho
                    int delta = -qty;
                    if (!capNhatTonKho(con, maThuoc, delta)) {
                        throw new SQLException("Không đủ tồn kho thuốc thay thế: " + maThuoc);
                    }
                    ghiLogThayDoiTonKho(con, maThuoc, delta, "Đổi thuốc - Xuất thuốc mới: " + maPhieuDoiTra);

                    // Cập nhật chi tiết hóa đơn gốc: tăng/ thêm thuốc thay thế
                    if (phieu.getHoaDonGoc() != null && phieu.getHoaDonGoc().getMaHD() != null) {
                        Double donGiaInsert = chiTiet.getDonGia();
                        adjustChiTietHoaDon(con, phieu.getHoaDonGoc().getMaHD(), maThuoc, qty, donGiaInsert);
                    }
                }

                tongChenhLech += chiTiet.getChenhLechTien();

                try (PreparedStatement up = con.prepareStatement(
                        "UPDATE ChiTietPhieuDoiTra SET daXuLy = 1 WHERE maPhieuDoiTra = ? AND maThuoc = ?")) {
                    up.setString(1, maPhieuDoiTra);
                    up.setString(2, maThuoc);
                    up.executeUpdate();
                }
            }
            // 6. Cập nhật trạng thái phiếu
            capNhatTrangThaiPhieu(con, maPhieuDoiTra, "Đã xử lý");

            // Nếu có hóa đơn gốc, tính lại tổng tiền sau khi điều chỉnh chi tiết
            try {
                if (phieu.getHoaDonGoc() != null && phieu.getHoaDonGoc().getMaHD() != null) {
                    recalcHoaDonTotal(con, phieu.getHoaDonGoc().getMaHD());
                }
            } catch (SQLException ex) {
                System.err.println("Không thể cập nhật tổng tiền hóa đơn gốc: " + ex.getMessage());
            }

            // 7. Cập nhật doanh thu nhân viên
            capNhatDoanhThuNhanVien(con, phieu.getNhanVien().getMaNV(), tongChenhLech);

            // 8. Ghi lịch sử
            ghiLichSuDoiTra(con, maPhieuDoiTra, maNhanVien, "Duyệt phiếu đổi",
                    "Đã xử lý đổi thuốc. Chênh lệch: " + tongChenhLech, "Chờ xử lý", "Đã xử lý");

            con.commit();
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    if (!con.isClosed()) {
                        con.rollback();
                    } else {
                        System.err.println("Cannot rollback because connection is already closed. Original error: " + e.getMessage());
                    }
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    if (!con.isClosed()) {
                        con.setAutoCommit(true);
                        con.close();
                    }
                } catch (SQLException e) {
                    System.err.println("Error while closing connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Xử lý trả thuốc - chỉ xóa thuốc khỏi kho
     */
    public boolean xuLyTraThuoc(String maPhieuDoiTra, String maNhanVien) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            // Load PhieuDoiTra
            PhieuDoiTra phieu = null;
            String sqlPhieu = "SELECT pdt.*, nv.hoTen as tenNV, kh.hoTen as tenKH, hd.maHD AS maHDGoc " +
                    "FROM PhieuDoiTra pdt " +
                    "LEFT JOIN NhanVien nv ON pdt.maNV = nv.maNV " +
                    "LEFT JOIN KhachHang kh ON pdt.maKH = kh.maKH " +
                    "LEFT JOIN HoaDon hd ON pdt.maHDGoc = hd.maHD " +
                    "WHERE pdt.maPhieuDoiTra = ?";

            try (PreparedStatement psPhieu = con.prepareStatement(sqlPhieu)) {
                psPhieu.setString(1, maPhieuDoiTra);
                try (ResultSet rs = psPhieu.executeQuery()) {
                    if (rs.next()) {
                        phieu = new PhieuDoiTra();
                        phieu.setMaPhieuDoiTra(rs.getString("maPhieuDoiTra"));
                        phieu.setNgayDoiTra(rs.getDate("ngayDoiTra"));
                        phieu.setLoaiPhieu(rs.getString("loaiPhieu"));
                        phieu.setLyDo(rs.getString("lyDo"));
                        phieu.setTongTien(rs.getDouble("tongTien"));
                        phieu.setTrangThai(rs.getString("trangThai"));

                        NhanVien nv = new NhanVien();
                        nv.setMaNV(rs.getString("maNV"));
                        nv.setTenNV(rs.getString("tenNV"));
                        phieu.setNhanVien(nv);

                        String maKH = rs.getString("maKH");
                        String tenKH = rs.getString("tenKH");
                        if ((maKH != null && !maKH.trim().isEmpty()) || (tenKH != null && !tenKH.trim().isEmpty())) {
                            KhachHang kh = new KhachHang();
                            if (maKH != null && !maKH.trim().isEmpty()) kh.setMaKH(maKH);
                            if (tenKH != null && !tenKH.trim().isEmpty()) kh.setHoTen(tenKH);
                            phieu.setKhachHang(kh);
                        }

                        HoaDon hd = new HoaDon();
                        hd.setMaHD(rs.getString("maHDGoc"));
                        phieu.setHoaDonGoc(hd);
                    }
                }
            }

            if (phieu == null || !"TRA".equals(phieu.getLoaiPhieu())) {
                throw new SQLException("Phiếu trả không hợp lệ");
            }

            // Lấy chi tiết phiếu trả
            List<ChiTietPhieuDoiTra> chiTietList = new ArrayList<>();
            String sqlChiTiet = "SELECT ct.*, t.tenThuoc as tenThuoc, t.donViTinh as dvt " +
                    "FROM ChiTietPhieuDoiTra ct " +
                    "LEFT JOIN Thuoc t ON ct.maThuoc = t.maThuoc " +
                    "WHERE ct.maPhieuDoiTra = ?";

            try (PreparedStatement psCt = con.prepareStatement(sqlChiTiet)) {
                psCt.setString(1, maPhieuDoiTra);
                try (ResultSet rs = psCt.executeQuery()) {
                    while (rs.next()) {
                        ChiTietPhieuDoiTra chiTiet = new ChiTietPhieuDoiTra();
                        chiTiet.setMaPhieuDoiTra(rs.getString("maPhieuDoiTra"));
                        Thuoc thuoc = new Thuoc();
                        thuoc.setMaThuoc(rs.getString("maThuoc"));
                        thuoc.setTenThuoc(rs.getString("tenThuoc"));
                        thuoc.setDonViTinh(rs.getString("dvt"));
                        chiTiet.setThuoc(thuoc);

                        chiTiet.setSoLuong(rs.getInt("soLuong"));
                        chiTiet.setDonGia(rs.getDouble("donGia"));
                        try { chiTiet.setTrangThaiThuoc(rs.getString("trangThaiThuoc")); } catch (SQLException ignore) { chiTiet.setTrangThaiThuoc(null); }
                        try { chiTiet.setChenhLechTien(rs.getDouble("chenhLechTien")); } catch (SQLException ignore) { chiTiet.setChenhLechTien(0.0); }

                        chiTietList.add(chiTiet);
                    }
                }
            }

            double tongTienTra = 0;

            for (ChiTietPhieuDoiTra chiTiet : chiTietList) {
                String maThuoc = chiTiet.getThuoc().getMaThuoc();
                int qty = chiTiet.getSoLuong() != null ? chiTiet.getSoLuong() : 0;

                if (chiTiet.getChenhLechTien() < 0) {
                    // Cộng lại thuốc bị trả (tăng tồn kho)
                    if (!capNhatTonKho(con, maThuoc, qty)) {
                        throw new SQLException("Lỗi khi cập nhật tồn kho thuốc trả: " + chiTiet.getThuoc().getTenThuoc());
                    }

                    tongTienTra += chiTiet.getThanhTienCu();

                    // Ghi log thay đổi tồn kho
                    ghiLogThayDoiTonKho(con, maThuoc, qty, "Trả thuốc - Nhập lại kho: " + maPhieuDoiTra);

                    // Cập nhật lại chi tiết hóa đơn gốc: giảm số lượng hoặc xóa dòng
                    if (phieu.getHoaDonGoc() != null && phieu.getHoaDonGoc().getMaHD() != null) {
                        adjustChiTietHoaDon(con, phieu.getHoaDonGoc().getMaHD(), maThuoc, -qty, null);
                    }
                } else if (chiTiet.getChenhLechTien() > 0) {
                    // Nếu có dòng nhập (khách nhận hàng mới trong phiếu trả) => giảm tồn kho
                    int delta = -qty;
                    if (!capNhatTonKho(con, maThuoc, delta)) {
                        throw new SQLException("Lỗi khi cập nhật tồn kho thuốc: " + maThuoc);
                    }
                    ghiLogThayDoiTonKho(con, maThuoc, delta, "Xuất thuốc - Trả phiếu: " + maPhieuDoiTra);

                    // Cập nhật chi tiết hóa đơn gốc: tăng/ thêm thuốc
                    if (phieu.getHoaDonGoc() != null && phieu.getHoaDonGoc().getMaHD() != null) {
                        adjustChiTietHoaDon(con, phieu.getHoaDonGoc().getMaHD(), maThuoc, qty, chiTiet.getDonGia());
                    }
                }
            }

            // 5. Cập nhật trạng thái phiếu
            capNhatTrangThaiPhieu(con, maPhieuDoiTra, "Đã xử lý");

            // Nếu có hóa đơn gốc, tính lại tổng tiền sau khi điều chỉnh chi tiết
            try {
                if (phieu.getHoaDonGoc() != null && phieu.getHoaDonGoc().getMaHD() != null) {
                    recalcHoaDonTotal(con, phieu.getHoaDonGoc().getMaHD());
                }
            } catch (SQLException ex) {
                System.err.println("Không thể cập nhật tổng tiền hóa đơn gốc: " + ex.getMessage());
            }

            // 6. Giảm doanh thu nhân viên (trừ tiền đã trả)
            capNhatDoanhThuNhanVien(con, phieu.getNhanVien().getMaNV(), -tongTienTra);

            // 7. Ghi lịch sử
            ghiLichSuDoiTra(con, maPhieuDoiTra, maNhanVien, "Duyệt phi trả",
                    "Đã xử lý trả thuốc. Tổng tiền trả: " + tongTienTra, "Chờ xử lý", "Đã xử lý");

            con.commit();
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    if (!con.isClosed()) {
                        con.rollback();
                    } else {
                        System.err.println("Cannot rollback because connection is already closed. Original error: " + e.getMessage());
                    }
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    if (!con.isClosed()) {
                        con.setAutoCommit(true);
                        con.close();
                    }
                } catch (SQLException e) {
                    System.err.println("Error while closing connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Cập nhật tồn kho thuốc
     */
    private boolean capNhatTonKho(Connection con, String maThuoc, int soLuongThayDoi) throws SQLException {
        String sql = "UPDATE Thuoc SET soLuongTon = soLuongTon + ? WHERE maThuoc = ?";

        // Kiểm tra tồn kho trước khi trừ
        if (soLuongThayDoi < 0) {
            String checkSql = "SELECT soLuongTon FROM Thuoc WHERE maThuoc = ?";
            try (PreparedStatement checkStmt = con.prepareStatement(checkSql)) {
                checkStmt.setString(1, maThuoc);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        int tonKhoHienTai = rs.getInt("soLuongTon");
                        if (tonKhoHienTai < Math.abs(soLuongThayDoi)) {
                            return false; // Không đủ tồn kho
                        }
                    } else {
                        return false; // Thuốc không tồn tại
                    }
                }
            }
        }

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, soLuongThayDoi);
            stmt.setString(2, maThuoc);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật doanh thu nhân viên
     */
    private void capNhatDoanhThuNhanVien(Connection con, String maNhanVien, double soTienThayDoi) throws SQLException {
        // Cập nhật bảng thống kê doanh thu nhân viên (nếu có)
        String sql = "UPDATE ThongKeNhanVien SET tongDoanhThu = tongDoanhThu + ? " +
                "WHERE maNV = ? AND thang = ? AND nam = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDouble(1, soTienThayDoi);
            stmt.setString(2, maNhanVien);
            stmt.setInt(3, java.time.LocalDate.now().getMonthValue());
            stmt.setInt(4, java.time.LocalDate.now().getYear());

            int rowsUpdated = stmt.executeUpdate();

            // Nếu chưa có record thống kê cho tháng này, tạo mới
            if (rowsUpdated == 0) {
                String insertSql = "INSERT INTO ThongKeNhanVien (maNV, thang, nam, tongDoanhThu) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
                    insertStmt.setString(1, maNhanVien);
                    insertStmt.setInt(2, java.time.LocalDate.now().getMonthValue());
                    insertStmt.setInt(3, java.time.LocalDate.now().getYear());
                    insertStmt.setDouble(4, soTienThayDoi);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            // Nếu bảng ThongKeNhanVien chưa tồn tại, bỏ qua
            System.err.println("Không thể cập nhật doanh thu nhân viên: " + e.getMessage());
        }
    }

    /**
     * Cập nhật trạng thái phiếu
     */
    private void capNhatTrangThaiPhieu(Connection con, String maPhieuDoiTra, String trangThai) throws SQLException {
        String sql = "UPDATE PhieuDoiTra SET trangThai = ? WHERE maPhieuDoiTra = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            stmt.setString(2, maPhieuDoiTra);
            stmt.executeUpdate();
        }
    }

    /**
     * Ghi lịch sử thao tác
     */
    private void ghiLichSuDoiTra(Connection con, String maPhieuDoiTra, String nguoiThaoTac,
            String hanhDong, String moTa, String trangThaiCu, String trangThaiMoi) throws SQLException {
        String sql = "INSERT INTO LichSuDoiTra (maLichSu, maPhieuDoiTra, nguoiThaoTac, " +
                "thoiGianThaoTac, hanhDong, moTa, trangThaiCu, trangThaiMoi) " +
                "VALUES (?, ?, ?, GETDATE(), ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, generateMaLichSu());
            stmt.setString(2, maPhieuDoiTra);
            stmt.setString(3, nguoiThaoTac);
            stmt.setString(4, hanhDong);
            stmt.setString(5, moTa);
            stmt.setString(6, trangThaiCu);
            stmt.setString(7, trangThaiMoi);
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Nếu bảng LichSuDoiTra chưa tồn tại, chỉ log lỗi
            System.err.println("Không thể ghi lịch sử: " + e.getMessage());
        }
    }

    /**
     * Generate mã lịch sử
     */
    private String generateMaLichSu() throws SQLException {
        String sql = "SELECT TOP 1 maLichSu FROM LichSuDoiTra ORDER BY maLichSu DESC";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastMa = rs.getString("maLichSu");
                if (lastMa != null && lastMa.startsWith("LS")) {
                    int number = Integer.parseInt(lastMa.substring(2)) + 1;
                    return String.format("LS%05d", number);
                }
            }
        } catch (SQLException e) {
            // Nếu bảng chưa tồn tại hoặc rỗng
        }

        return "LS00001";
    }

    // Helper: điều chỉnh ChiTietHoaDon (cộng/giam số lượng). Nếu delta > 0 thì thêm hoặc tăng, nếu <=0 thì giảm hoặc xóa.
    private void adjustChiTietHoaDon(Connection con, String maHD, String maThuoc, int deltaQty, Double donGiaIfInsert) throws SQLException {
        if (maHD == null || maHD.trim().isEmpty() || maThuoc == null || maThuoc.trim().isEmpty()) return;


        String selectSql = "SELECT soLuong, donGia FROM ChiTietHoaDon WHERE maHD = ? AND maThuoc = ?";
        try (PreparedStatement ps = con.prepareStatement(selectSql)) {
            ps.setString(1, maHD);
            ps.setString(2, maThuoc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int current = rs.getInt("soLuong");
                    int newQty = current + deltaQty;
                    if (newQty > 0) {
                        String updateSql = "UPDATE ChiTietHoaDon SET soLuong = ? WHERE maHD = ? AND maThuoc = ?";
                        try (PreparedStatement up = con.prepareStatement(updateSql)) {
                            up.setInt(1, newQty);
                            up.setString(2, maHD);
                            up.setString(3, maThuoc);
                            int rows = up.executeUpdate();
                        }
                    } else {
                        String delSql = "DELETE FROM ChiTietHoaDon WHERE maHD = ? AND maThuoc = ?";
                        try (PreparedStatement del = con.prepareStatement(delSql)) {
                            del.setString(1, maHD);
                            del.setString(2, maThuoc);
                            int rows = del.executeUpdate();
                        }
                    }
                } else {
                    // Không có dòng hiện tại, nếu deltaQty>0 thì insert
                    if (deltaQty > 0) {
                        double donGia = donGiaIfInsert != null ? donGiaIfInsert : getDonGiaThuoc(con, maThuoc);
                        String insertSql = "INSERT INTO ChiTietHoaDon (maHD, maThuoc, soLuong, donGia) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement ins = con.prepareStatement(insertSql)) {
                            ins.setString(1, maHD);
                            ins.setString(2, maThuoc);
                            ins.setInt(3, deltaQty);
                            ins.setDouble(4, donGia);
                            int rows = ins.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    // Helper: lấy giá bán của thuốc từ bảng Thuoc
    private double getDonGiaThuoc(Connection con, String maThuoc) throws SQLException {
        String sql = "SELECT giaBan FROM Thuoc WHERE maThuoc = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maThuoc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("giaBan");
            }
        }
        return 0.0;
    }

    // Helper: tính lại tổng tiền của hóa đơn dựa trên ChiTietHoaDon
    private void recalcHoaDonTotal(Connection con, String maHD) throws SQLException {
        if (maHD == null || maHD.trim().isEmpty()) return;
        String sql = "SELECT ISNULL(SUM(soLuong * donGia),0) AS tong FROM ChiTietHoaDon WHERE maHD = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double tong = rs.getDouble("tong");
                    String upd = "UPDATE HoaDon SET tongTien = ? WHERE maHD = ?";
                    try (PreparedStatement up = con.prepareStatement(upd)) {
                        up.setDouble(1, tong);
                        up.setString(2, maHD);
                        int rows = up.executeUpdate();
                    }
                }
            }
        }
    }
}
