package gui. dialog;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax. swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.itextpdf. text.BaseColor;
import com.itextpdf.text. Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text. Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com. itextpdf.text.pdf.PdfPTable;

import dao.*;
import entity.*;

public class DialogThanhToanPhieuDatThuoc extends JDialog {

    private JLabel lblMaHoaDon;
    private JLabel lblNgayLap;
    private JLabel lblNhanVien;
    private JLabel lblKhachHang;
    private JLabel lblSoDienThoai;
    private JLabel lblMaPhieuDat;
    private JTable tableChiTiet;
    private DefaultTableModel tableModel;
    private JTextField txtTongTien;
    private JTextField txtThue;
    private JTextField txtGiamGia;
    private JTextField txtThanhTien;
    private JTextField txtTienNhanVao;
    private JTextField txtTienThua;
    private JButton btnXacNhan;
    private JButton btnHuy;
    private boolean confirmed = false;
    
    // DAOs
    private ThueDAO thueDAO;
    private KhuyenMaiDAO khuyenMaiDAO;
    private HoaDonDAO hdDAO;
    private ChiTietHoaDonDAO cthdDAO;
    private ThuocDAO thuocDAO;
    private LoThuocDAO loThuocDAO;
    private KhachHangDAO khDAO;
    private PhieuDatThuocDAO pdtDAO;
    private NhanVienDAO nhanVienDAO;
    
    private JPanel mainPanel;
    private boolean isThanhToan = false;
    private KhuyenMai khuyenMaiApDung;
    private NhanVien nv;
    private String maPhieuDatGlobal; 
    private KhachHang khachHangThanhToan;
    private Thue thueApDung;
    private PhieuDatThuoc phieuDatThanhToan;
    private ArrayList<ChiTietPhieuDatThuoc> dsPhieuDatThuocThanhToan;
    private Runnable onThanhToanThanhCong;
    
    public DialogThanhToanPhieuDatThuoc(Frame frame, String maPhieuDat, Date ngayDat, String maKH,
            ArrayList<ChiTietPhieuDatThuoc> dsPhieuDatThuoc, double tongTien, NhanVien nv) throws SQLException {
        // Khởi tạo các DAO
        thueDAO = new ThueDAO();
        thuocDAO = new ThuocDAO();
        loThuocDAO = new LoThuocDAO();
        nhanVienDAO = new NhanVienDAO();
        hdDAO = new HoaDonDAO();
        khDAO = new KhachHangDAO();
        pdtDAO = new PhieuDatThuocDAO();
        cthdDAO = new ChiTietHoaDonDAO(); // ✅ FIX: Thêm dòng này
        khuyenMaiDAO = new KhuyenMaiDAO(); // ✅ FIX: Thêm dòng này
        
        this.nv = resolveNhanVien(nv);
        this.maPhieuDatGlobal = maPhieuDat;
        this.phieuDatThanhToan = pdtDAO.getPhieuDatThuocQuaMaPhieuDat(maPhieuDat);
        this.dsPhieuDatThuocThanhToan = dsPhieuDatThuoc;
        initComponents(maPhieuDat, ngayDat, maKH, dsPhieuDatThuoc, tongTien);
    }

    public void setOnThanhToanThanhCong(Runnable onThanhToanThanhCong) {
        this.onThanhToanThanhCong = onThanhToanThanhCong;
    }

    private NhanVien resolveNhanVien(NhanVien nhanVien) {
        if (nhanVien == null || nhanVien.getMaNV() == null || nhanVien.getMaNV().trim().isEmpty()) {
            return nhanVien;
        }

        try {
            NhanVien fullNhanVien = nhanVienDAO.getNhanVienTheoMa(nhanVien.getMaNV());
            if (fullNhanVien != null) {
                return fullNhanVien;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nhanVien;
    }

    private void initComponents(String maPhieuDat, Date ngayDat, String maKH,
            ArrayList<ChiTietPhieuDatThuoc> dsPhieuDatThuoc, double tongTien) throws SQLException {

        setSize(900, 800);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));

        // ===== TITLE PANEL =====
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205)); 
        titlePanel.setPreferredSize(new Dimension(900, 60));

        JLabel lblTitle = new JLabel("THANH TOÁN PHIẾU ĐẶT");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);

        add(titlePanel, BorderLayout. NORTH);

        // ===== MAIN PANEL =====
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel. setBorder(new EmptyBorder(20, 30, 20, 30));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // ===== THÔNG TIN =====
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setMaximumSize(new Dimension(840, 120));

        Font labelFont = new Font("Roboto", Font.BOLD, 14);
        Font valueFont = new Font("Roboto", Font.PLAIN, 14);

        // Dòng 1: Mã phiếu đặt + Ngày lập
        JPanel row1 = new JPanel(new GridLayout(1, 4, 15, 0));
        row1.setBackground(Color.WHITE);
        row1.setMaximumSize(new Dimension(840, 35));

        JLabel lblMaPhieuDatLabel = new JLabel("Mã phiếu đặt:");
        lblMaPhieuDatLabel.setFont(labelFont);
        row1.add(lblMaPhieuDatLabel);

        lblMaPhieuDat = new JLabel(maPhieuDat); // ✅ FIX: Gán giá trị
        lblMaPhieuDat.setFont(valueFont);
        lblMaPhieuDat. setForeground(new Color(0, 0, 205));
        row1.add(lblMaPhieuDat);

        JLabel lblNgayDatLabel = new JLabel("Ngày đặt:");
        lblNgayDatLabel.setFont(labelFont);
        row1.add(lblNgayDatLabel);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date ngayLapPhieu = ngayDat != null ? ngayDat : new Date();
        lblNgayLap = new JLabel(sdf.format(ngayLapPhieu));
        lblNgayLap.setFont(valueFont);
        row1.add(lblNgayLap);

        infoPanel.add(row1);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Dòng 2: Nhân viên + Khách hàng
        JPanel row2 = new JPanel(new GridLayout(1, 4, 15, 0));
        row2.setBackground(Color.WHITE);
        row2.setMaximumSize(new Dimension(840, 35));

        JLabel lblKHLabel = new JLabel("Khách hàng:");
        lblKHLabel.setFont(labelFont);
        row2.add(lblKHLabel);
        
        khachHangThanhToan = khDAO.getKhachHangTheoMa(maKH);
        lblKhachHang = new JLabel(khachHangThanhToan.getHoTen());
        lblKhachHang.setFont(valueFont);
        row2.add(lblKhachHang);
        
        JLabel lblSDTLabel = new JLabel("Số điện thoại:");
        lblSDTLabel.setFont(labelFont);
        row2.add(lblSDTLabel);

        lblSoDienThoai = new JLabel(khachHangThanhToan. getSoDienThoai());
        lblSoDienThoai.setFont(valueFont);
        row2.add(lblSoDienThoai);

        infoPanel.add(row2);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        
        JPanel row3 = new JPanel(new GridLayout(1, 4, 15, 0));
        row3.setBackground(Color.WHITE);
        row3.setMaximumSize(new Dimension(840, 35));
        
        JLabel lblNVLabel = new JLabel("Nhân viên:");
        lblNVLabel.setFont(labelFont);
        row3.add(lblNVLabel);
        
        String tenNhanVien = nv != null && nv.getTenNV() != null && !nv.getTenNV().trim().isEmpty()
                ? nv.getTenNV()
                : (nv != null ? nv.getMaNV() : "N/A");
        lblNhanVien = new JLabel(tenNhanVien);
        lblNhanVien.setFont(valueFont);
        row3.add(lblNhanVien);
        
        row3.add(new JLabel()); 
        row3.add(new JLabel()); 
        
        infoPanel.add(row3);

        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

       
        JSeparator separator1 = new JSeparator();
        separator1.setMaximumSize(new Dimension(840, 2));
        mainPanel.add(separator1);
        mainPanel.add(Box. createRigidArea(new Dimension(0, 15)));

        // ===== TABLE CHI TIẾT =====
        JLabel lblChiTiet = new JLabel("Chi tiết sản phẩm:");
        lblChiTiet.setFont(new Font("Roboto", Font. BOLD, 16));
        lblChiTiet.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblChiTiet);
        mainPanel.add(Box. createRigidArea(new Dimension(0, 10)));

        String[] columns = {"STT", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChiTiet = new JTable(tableModel);
        tableChiTiet.setFont(new Font("Roboto", Font.PLAIN, 14));
        tableChiTiet.setRowHeight(35);
        tableChiTiet.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        tableChiTiet. getTableHeader().setBackground(new Color(0, 0, 205));
        tableChiTiet.getTableHeader().setForeground(Color.WHITE);
        tableChiTiet.getTableHeader().setPreferredSize(new Dimension(840, 40));

        tableChiTiet.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableChiTiet.getColumnModel().getColumn(1).setPreferredWidth(350);
        tableChiTiet.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableChiTiet.getColumnModel().getColumn(3).setPreferredWidth(130);
        tableChiTiet.getColumnModel().getColumn(4).setPreferredWidth(140);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableChiTiet.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableChiTiet.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tableChiTiet.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tableChiTiet.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        int stt = 1;
        double tongTienTruocKhiKhuyenMai = 0;
        for (ChiTietPhieuDatThuoc item : dsPhieuDatThuoc) {
        	Thuoc thuoc = thuocDAO.getThuocTheoMaThuoc(item.getThuoc().getMaThuoc());
            tableModel.addRow(new Object[]{
                stt++,
                thuoc.getTenThuoc(),
                item. getSoLuong(),
                String.format("%,.0f VNĐ", item.getDonGia()),
                String.format("%,.0f VNĐ", item.getThanhTien())
            });
            tongTienTruocKhiKhuyenMai += item.getThanhTien();
        }

        JScrollPane scrollPane = new JScrollPane(tableChiTiet);
        scrollPane.setMaximumSize(new Dimension(840, 300));
        scrollPane.setPreferredSize(new Dimension(840, 300));
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(scrollPane);
        mainPanel.add(Box. createRigidArea(new Dimension(0, 15)));

        JSeparator separator2 = new JSeparator();
        separator2.setMaximumSize(new Dimension(840, 2));
        mainPanel.add(separator2);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // ===== PAYMENT INFO =====
        JPanel paymentPanel = new JPanel(new GridLayout(6, 2, 15, 12));
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.setMaximumSize(new Dimension(780, 200));

        Font paymentFont = new Font("Roboto", Font.PLAIN, 14);

        // Tổng tiền
        JLabel lblTongTienLabel = new JLabel("Tổng tiền:");
        lblTongTienLabel.setFont(paymentFont);
        paymentPanel.add(lblTongTienLabel);
        
        txtTongTien = new JTextField(String.format("%,.0f VNĐ", tongTienTruocKhiKhuyenMai));
        txtTongTien.setEditable(false);
        txtTongTien.setFont(new Font("Roboto", Font. BOLD, 15));
        paymentPanel.add(txtTongTien);
        
        // ===== GIẢM GIÁ =====
        double giamGia = 0;
        double phanTramGiamGia = 0;
        this.khuyenMaiApDung = layKhuyenMaiDaChot();
        if (this.khuyenMaiApDung != null) {
            phanTramGiamGia = this.khuyenMaiApDung.getPhanTramGiamGia();
            giamGia = tongTienTruocKhiKhuyenMai * phanTramGiamGia / 100;
        }
        
        String labelGiamGia = phanTramGiamGia > 0 ? "Giảm giá (" + " -" + String.format("%.0f%%", phanTramGiamGia) + "):" : "Giảm giá:";
        JLabel lblGiamGiaLabel = new JLabel(labelGiamGia);
        lblGiamGiaLabel.setFont(paymentFont); 
        paymentPanel.add(lblGiamGiaLabel);
        
        txtGiamGia = new JTextField(String.format("%,.0f VNĐ", giamGia));
        txtGiamGia.setEditable(false); 
        txtGiamGia.setFont(paymentFont);
        if (giamGia > 0) { 
            txtGiamGia.setForeground(new Color(0, 153, 0));
            txtGiamGia.setFont(new Font("Roboto", Font.BOLD, 14)); 
        }
        paymentPanel.add(txtGiamGia);
        
       
        // ===== THUẾ =====
        double tienThue = 0;
        double phanTramThue = 0;
        String tenThue = "Thuế (0%)";

        thueApDung = layThueDaChot();
        if (thueApDung != null) {
            phanTramThue = thueApDung.getPhanTramThue();
            tenThue = thueApDung.getTenThue() + " (" + String.format("%.0f%%", phanTramThue) + ")";
        }
        double tienSauGiamGia = tongTienTruocKhiKhuyenMai - giamGia;
        tienThue = tienSauGiamGia * phanTramThue / 100;
        
        JLabel lblThueLabel = new JLabel(tenThue + ":");
        lblThueLabel. setFont(paymentFont);
        paymentPanel.add(lblThueLabel);
        
        txtThue = new JTextField(String. format("%,.0f VNĐ", tienThue));
        txtThue.setEditable(false);
        txtThue.setFont(paymentFont);
        paymentPanel.add(txtThue);

        double thanhTien = tienSauGiamGia + tienThue;

        JLabel lblThanhTienLabel = new JLabel("Thành tiền:");
        lblThanhTienLabel.setFont(new Font("Roboto", Font. BOLD, 16));
        lblThanhTienLabel.setForeground(new Color(255, 51, 0));
        paymentPanel.add(lblThanhTienLabel);

        txtThanhTien = new JTextField(String.format("%,.0f VNĐ", thanhTien));
        txtThanhTien.setEditable(false);
        txtThanhTien. setFont(new Font("Roboto", Font.BOLD, 16));
        txtThanhTien.setForeground(new Color(255, 51, 0));
        paymentPanel.add(txtThanhTien);
        
        // ✅ FIX: Thêm 2 trường tiền nhận vào và tiền thừa
        JLabel lblTienNhanLabel = new JLabel("Tiền nhận vào:");
        lblTienNhanLabel.setFont(paymentFont);
        paymentPanel.add(lblTienNhanLabel);
        
        txtTienNhanVao = new JTextField("0 VNĐ");
        txtTienNhanVao. setEditable(false);
        txtTienNhanVao. setFont(paymentFont);
        paymentPanel.add(txtTienNhanVao);
        
        JLabel lblTienThuaLabel = new JLabel("Tiền thừa:");
        lblTienThuaLabel.setFont(paymentFont);
        paymentPanel.add(lblTienThuaLabel);
        
        txtTienThua = new JTextField("0 VNĐ");
        txtTienThua.setEditable(false);
        txtTienThua.setFont(paymentFont);
        paymentPanel. add(txtTienThua);

        mainPanel.add(paymentPanel);
        add(mainPanel, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 245, 245));
        buttonPanel.setPreferredSize(new Dimension(850, 80));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        btnHuy = new JButton("HỦY BỎ");
        btnHuy.setFont(new Font("Roboto", Font.BOLD, 14));
        btnHuy.setPreferredSize(new Dimension(150, 40));
        btnHuy.setBackground(new Color(255, 102, 102));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFocusPainted(false);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.setBorder(new LineBorder(new Color(255, 102, 102), 2, true));
        btnHuy.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn huỷ thanh toán?", "Xác nhận", JOptionPane. YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        });

        // ✅ FIX:  Thêm nút thanh toán QR
        JButton btnThanhToanQR = new JButton("THANH TOÁN QR");
        btnThanhToanQR.setFont(new Font("Roboto", Font.BOLD, 14));
        btnThanhToanQR.setPreferredSize(new Dimension(180, 40));
        btnThanhToanQR.setBackground(new Color(0, 123, 255));
        btnThanhToanQR.setForeground(Color.WHITE);
        btnThanhToanQR.setFocusPainted(false);
        btnThanhToanQR.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThanhToanQR.setBorder(new LineBorder(new Color(0, 123, 255), 2, true));
        btnThanhToanQR. addActionListener(e -> taoMaQrCode());

        btnXacNhan = new JButton("ĐÃ NHẬN TIỀN");
        btnXacNhan.setFont(new Font("Roboto", Font.BOLD, 14));
        btnXacNhan. setPreferredSize(new Dimension(220, 40));
        btnXacNhan.setBackground(new Color(0, 204, 51));
        btnXacNhan.setForeground(Color. WHITE);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXacNhan.setBorder(new LineBorder(new Color(0, 204, 51), 2, true));
        btnXacNhan.addActionListener(e -> {
        	int confirm = JOptionPane.showConfirmDialog(
        		    this,
        		    "Bạn đã nhận đủ tiền từ khách hàng chưa?",
        		    "Xác nhận thanh toán",
        		    JOptionPane.YES_NO_OPTION,
        		    JOptionPane.QUESTION_MESSAGE
        		);

        		if (confirm != JOptionPane.YES_OPTION) {
        		    return; 
        		} else {
        			isThanhToan = true;
        		}

        		if (! isThanhToan) {
                    return;
                }
            
            String maHD = null;
            try {
                maHD = hdDAO.generateMaHD();
            } catch (SQLException e1) {
                e1.printStackTrace();
                return;
            }
            
            Date ngayThanhToan = new Date();
            HoaDon hd = new HoaDon(maHD, ngayThanhToan, thueApDung, nv, khachHangThanhToan,
                    khuyenMaiApDung, new PhieuDatThuoc(maPhieuDatGlobal));
            
            try {
                ArrayList<ChiTietHoaDon> dsChiTietHoaDon = new ArrayList<>();
                for (ChiTietPhieuDatThuoc ctpdt : dsPhieuDatThuoc) {
                    dsChiTietHoaDon.add(new ChiTietHoaDon(new HoaDon(maHD), ctpdt.getThuoc(),
                            ctpdt.getSoLuong(), ctpdt.getDonGia()));
                }

                if (hdDAO.thanhToanPhieuDat(hd, dsChiTietHoaDon, maPhieuDatGlobal)) {
                        // ✅ FIX:  Gán mã hóa đơn để in
                        lblMaHoaDon = new JLabel(maHD);
                        
                        JOptionPane.showMessageDialog(this, "Thanh toán phiếu đặt thành công!");
                        confirmed = true;
                        thongBaoThanhToanThanhCong();
                        
                        int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn in hoá đơn không?");
                        if (choice == JOptionPane.YES_OPTION) {
                            inHoaDon();
                        } else {
                            this.dispose();
                        }
                        
                } else {
                    JOptionPane. showMessageDialog(this, "Lỗi khi thanh toán phiếu đặt");
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e1.getMessage());
            }
        });

        buttonPanel.add(btnHuy);
        buttonPanel.add(btnThanhToanQR);
        buttonPanel.add(btnXacNhan);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void hoanTatThanhToanPhieuDat() {
        if (confirmed) {
            return;
        }

        String maHD = null;
        try {
            maHD = hdDAO.generateMaHD();
        } catch (SQLException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tạo mã hóa đơn: " + e1.getMessage());
            return;
        }

        Date ngayThanhToan = new Date();
        HoaDon hd = new HoaDon(maHD, ngayThanhToan, thueApDung, nv, khachHangThanhToan,
                khuyenMaiApDung, new PhieuDatThuoc(maPhieuDatGlobal));

        try {
            ArrayList<ChiTietHoaDon> dsChiTietHoaDon = new ArrayList<>();
            for (ChiTietPhieuDatThuoc ctpdt : dsPhieuDatThuocThanhToan) {
                dsChiTietHoaDon.add(new ChiTietHoaDon(new HoaDon(maHD), ctpdt.getThuoc(),
                        ctpdt.getSoLuong(), ctpdt.getDonGia()));
            }

            if (hdDAO.thanhToanPhieuDat(hd, dsChiTietHoaDon, maPhieuDatGlobal)) {
                lblMaHoaDon = new JLabel(maHD);

                JOptionPane.showMessageDialog(this, "Thanh toán phiếu đặt thành công!");
                confirmed = true;
                thongBaoThanhToanThanhCong();

                int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn in hoá đơn không?");
                if (choice == JOptionPane.YES_OPTION) {
                    inHoaDon();
                } else {
                    this.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi thanh toán phiếu đặt");
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e1.getMessage());
        }
    }

    private void thongBaoThanhToanThanhCong() {
        if (onThanhToanThanhCong != null) {
            onThanhToanThanhCong.run();
        }
    }

    private Thue layThueDaChot() {
        if (phieuDatThanhToan == null || phieuDatThanhToan.getThue() == null
                || phieuDatThanhToan.getThue().getMaThue() == null
                || phieuDatThanhToan.getThue().getMaThue().trim().isEmpty()) {
            return null;
        }
        try {
            return thueDAO.getThueTheoMa(phieuDatThanhToan.getThue().getMaThue());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private KhuyenMai layKhuyenMaiDaChot() {
        if (phieuDatThanhToan == null || phieuDatThanhToan.getKhuyenMai() == null
                || phieuDatThanhToan.getKhuyenMai().getMaKM() == null
                || phieuDatThanhToan.getKhuyenMai().getMaKM().trim().isEmpty()) {
            return null;
        }
        try {
            return khuyenMaiDAO.getKhuyenMaiTheoMa(phieuDatThanhToan.getKhuyenMai().getMaKM());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void taoMaQrCode() {
        try {
            String bank = "bidv";
            String account = "7351363429";
            String amountStr = txtThanhTien.getText().trim()
                    .replace(",", "")
                    .replace("VNĐ", "")
                    .replace(".", "")
                    .trim();

            String noiDung = "Thanh toan phieu dat " + lblMaPhieuDat.getText();

            String qrUrl = String.format(
                    "https://img.vietqr.io/image/%s-%s-compact.png?amount=%s&addInfo=%s",
                    bank, account, amountStr, URLEncoder.encode(noiDung, "UTF-8")
            );

            JDialog dlQrCode = new JDialog(this, "Quét mã QR để thanh toán", true);
            dlQrCode.setSize(800, 600);
            dlQrCode.setLayout(new BorderLayout(10, 10));
            dlQrCode.setLocationRelativeTo(this);
            dlQrCode.getContentPane().setBackground(Color.WHITE);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(Color.WHITE);
            infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            JLabel lblAmount = new JLabel("Số tiền:  " + txtThanhTien.getText());
            lblAmount.setFont(new Font("Roboto", Font. BOLD, 18));
            lblAmount.setForeground(new Color(255, 51, 0));
            lblAmount.setAlignmentX(Component.CENTER_ALIGNMENT);

            infoPanel.add(lblAmount);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            infoPanel.add(new JLabel("Ngân hàng: BIDV") {{ setAlignmentX(Component.CENTER_ALIGNMENT); }});
            infoPanel. add(new JLabel("Số tài khoản: " + account) {{ setAlignmentX(Component.CENTER_ALIGNMENT); }});

            dlQrCode.add(infoPanel, BorderLayout. NORTH);

            JPanel qrPanel = new JPanel(new BorderLayout());
            qrPanel.setBackground(Color.WHITE);
            qrPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            URL url = new URL(qrUrl);
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(350, 350, Image.SCALE_SMOOTH);
            icon. setImage(scaledImg);
            JLabel lblQrCode = new JLabel(icon);
            qrPanel.add(lblQrCode, BorderLayout.CENTER);

            dlQrCode.add(qrPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.setBackground(Color.WHITE);

            JButton btnDaNhanTien = new JButton("ĐÃ NHẬN TIỀN");
            btnDaNhanTien. setBackground(new Color(0, 204, 51));
            btnDaNhanTien.setForeground(Color.WHITE);
            btnDaNhanTien.setPreferredSize(new Dimension(180, 40));

            JButton btnHuyBo = new JButton("HỦY BỎ");
            btnHuyBo.setBackground(new Color(255, 102, 102));
            btnHuyBo.setForeground(Color.WHITE);
            btnHuyBo.setPreferredSize(new Dimension(120, 40));

            buttonPanel.add(btnHuyBo);
            buttonPanel.add(btnDaNhanTien);
            dlQrCode.add(buttonPanel, BorderLayout.SOUTH);

            btnDaNhanTien.addActionListener(e -> {
                txtTienNhanVao.setText(txtThanhTien.getText());
                txtTienThua.setText("0 VNĐ");
                txtTienThua.setForeground(new Color(0, 153, 0));
                
                int choice = JOptionPane.showConfirmDialog(this, "Xác nhận đã nhận tiền?");
                if (choice == JOptionPane.YES_OPTION) {
                    isThanhToan = true;
                    dlQrCode.dispose();
                    hoanTatThanhToanPhieuDat();
                }
            });

            btnHuyBo.addActionListener(e -> dlQrCode.dispose());

            dlQrCode.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tạo mã QR: " + e.getMessage());
        }
    }

    /**
     * Thực hiện quy trình in hóa đơn ra file PDF.
     * Quy trình bao gồm: Tạo file, định dạng font chữ Tiếng Việt, vẽ bảng chi tiết sản phẩm 
     * và hiển thị thông tin thanh toán.
     */
    private void inHoaDon() {
        try {
            // --- 1. THIẾT LẬP ĐƯỜNG DẪN VÀ TÊN FILE ---
            String folderPath = "src/LuuHoaDonInRa";
            // Tên file bao gồm mã hóa đơn và thời gian in để tránh trùng lặp
            String fileName = "HoaDon_" + lblMaHoaDon.getText() + "_" +
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
            String outputPath = folderPath + "/" + fileName;

            // Kiểm tra và tạo thư mục lưu trữ nếu chưa tồn tại
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // --- 2. KHỞI TẠO TÀI LIỆU PDF ---
            // Tạo document khổ A4, lề trái-phải-trên-dưới là 40px
            com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 40, 40, 40, 40);
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(outputPath));
            document.open();

            // --- 3. ĐỊNH DẠNG FONT CHỮ (Hỗ trợ Tiếng Việt) ---
            // Nạp font Arial từ hệ thống để hiển thị đúng dấu Tiếng Việt trong PDF
            com.itextpdf.text.pdf.BaseFont bf = com.itextpdf.text.pdf.BaseFont.createFont(
                    "C:/Windows/Fonts/arial.ttf", com.itextpdf.text.pdf.BaseFont.IDENTITY_H, com.itextpdf.text.pdf.BaseFont.EMBEDDED);
            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12);
            com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);

            // --- 4. VẼ TIÊU ĐỀ VÀ THÔNG TIN CHUNG ---
            com.itextpdf.text.Font fontStoreName = new com.itextpdf.text.Font(bf, 20, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Paragraph storeName = new com.itextpdf.text.Paragraph("NHÀ THUỐC MAI THỨC", fontStoreName);
            storeName.setAlignment(Element.ALIGN_CENTER);
            document.add(storeName);

            com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("HÓA ĐƠN THANH TOÁN", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER); // Căn giữa tiêu đề
            document.add(title);
            document.add(new com.itextpdf.text.Paragraph(" ")); // Thêm dòng trống

            // Thêm các thông tin cơ bản của hóa đơn lấy từ các Label trên giao diện
            document.add(new com.itextpdf.text.Paragraph("Mã hóa đơn: " + lblMaHoaDon.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Từ phiếu đặt: " + lblMaPhieuDat.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Ngày lập: " + lblNgayLap.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Nhân viên: " + lblNhanVien.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Khách hàng: " + lblKhachHang.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Số điện thoại: " + lblSoDienThoai.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph(" "));

            // --- 5. TẠO BẢNG CHI TIẾT THUỐC ---
            PdfPTable table = new PdfPTable(5); // Bảng có 5 cột
            table.setWidthPercentage(100); // Chiều rộng bảng chiếm 100% trang giấy
            table.setWidths(new float[]{1f, 3f, 1.5f, 1.5f, 2f}); // Thiết lập tỉ lệ độ rộng các cột

            // Tạo dòng tiêu đề cho bảng (Header)
            String[] headers = {"STT", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, fontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY); // Tô màu nền xám cho header
                table.addCell(cell);
            }

            // Đọc dữ liệu từ JTable (tableChiTiet) và đưa vào bảng PDF
            DefaultTableModel model = (DefaultTableModel) tableChiTiet.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    String cellValue = (value != null) ? value.toString() : ""; 
                    
                    PdfPCell cell = new PdfPCell(new Phrase(cellValue, fontNormal));
                    // Cột tên thuốc (cột 1) căn lề trái, các cột khác căn giữa
                    cell.setHorizontalAlignment(j == 1 ? Element.ALIGN_LEFT : Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
            document.add(table); // Thêm bảng vào tài liệu

            // --- 6. THÔNG TIN TỔNG KẾT THANH TOÁN ---
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Tổng tiền: " + txtTongTien.getText(), fontNormal));
            document.add(new Paragraph("Thuế: " + txtThue.getText(), fontNormal));
            document.add(new Paragraph("Giảm giá: " + txtGiamGia.getText(), fontNormal));
            document.add(new Paragraph("Thành tiền: " + txtThanhTien.getText(), fontBold));
            document.add(new Paragraph("Tiền nhận vào: " + txtTienNhanVao.getText(), fontNormal));
            document.add(new Paragraph("Tiền thừa: " + txtTienThua.getText(), fontNormal));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Cảm ơn quý khách đã mua hàng!", fontBold));
            
            // --- 7. ĐÓNG FILE VÀ HIỂN THỊ ---
            document.close(); 

            JOptionPane.showMessageDialog(this, "✅ In hóa đơn thành công!\nĐã lưu tại: " + outputPath);
            
            // Tự động mở file PDF vừa tạo bằng ứng dụng mặc định của máy tính
            Desktop.getDesktop().open(new java.io.File(outputPath));
            this.dispose(); // Đóng cửa sổ thanh toán sau khi in xong

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn: " + e.getMessage());
        }
    }

    private boolean isInDateRange(Date currentDate, Date startDate, Date endDate) {
        if (currentDate == null || startDate == null || endDate == null) return false;
        return ! currentDate.before(startDate) && !currentDate.after(endDate);
    }

    private boolean isPromotionActive(Date currentDate, KhuyenMai khuyenMai) {
        if (khuyenMai == null || currentDate == null
                || khuyenMai.getNgayBatDau() == null || khuyenMai.getNgayKetThuc() == null) {
            return false;
        }
        if (!khuyenMai.isLapHangNam()) {
            return isInDateRange(currentDate, khuyenMai.getNgayBatDau(), khuyenMai.getNgayKetThuc());
        }
        return isInAnnualDateRange(currentDate, khuyenMai.getNgayBatDau(), khuyenMai.getNgayKetThuc());
    }

    private boolean isInAnnualDateRange(Date currentDate, Date startDate, Date endDate) {
        int current = monthDayValue(currentDate);
        int start = monthDayValue(startDate);
        int end = monthDayValue(endDate);
        return start <= end
                ? current >= start && current <= end
                : current >= start || current <= end;
    }

    private int monthDayValue(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
