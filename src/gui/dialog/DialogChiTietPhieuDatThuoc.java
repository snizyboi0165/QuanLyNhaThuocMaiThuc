package gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.ChiTietPhieuDatThuocDAO;
import dao.KhachHangDAO;
import dao.NhanVienDAO;
import dao.PhieuDatThuocDAO;
import dao.ThuocDAO;
import dao.ThueDAO;
import dao.KhuyenMaiDAO;
import entity.ChiTietPhieuDatThuoc;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatThuoc;
import entity.Thuoc;
import entity.Thue;
import entity.KhuyenMai;

public class DialogChiTietPhieuDatThuoc extends JDialog {
    private JTable tableCTHD;
    private DefaultTableModel tableModel;
    private JLabel lblMaPhieuDat, lblNgayDat, lblNhanVien, lblKhachHang;
    private JLabel lblTongTien, lblThue, lblKhuyenMai, lblThanhToan;
    
    private PhieuDatThuocDAO pdtDAO;
    private ChiTietPhieuDatThuocDAO chiTietPhieuDatThuocDAO;
    private ThuocDAO thuocDAO;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;
    private ThueDAO thueDAO;
    private KhuyenMaiDAO khuyenMaiDAO;
    
    private PhieuDatThuoc phieuDatThuoc;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    public DialogChiTietPhieuDatThuoc(JFrame parent, String maPhieuDat) {
        super(parent, "Chi tiết đặt thuốc", true);
        
        chiTietPhieuDatThuocDAO = new ChiTietPhieuDatThuocDAO();
        pdtDAO = new PhieuDatThuocDAO();
        thuocDAO = new ThuocDAO();
        khachHangDAO = new KhachHangDAO();
        nhanVienDAO = new NhanVienDAO();
        thueDAO = new ThueDAO();
        khuyenMaiDAO = new KhuyenMaiDAO();
        
        initComponents();
        loadData(maPhieuDat);
        
        setSize(900, 750);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(900, 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        
        JLabel lblTitle = new JLabel("CHI TIẾT PHIẾU ĐẶT THUỐC");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setIcon(new FlatSVGIcon(getClass().getResource("/img/info.svg")).derive(32, 32));
        headerPanel.add(lblTitle);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Thông tin phiếu đặt
        JPanel phieuDatThuocInfoPanel = new JPanel();
        phieuDatThuocInfoPanel.setLayout(new java.awt.GridLayout(2, 4, 15, 10));
        phieuDatThuocInfoPanel.setBackground(Color.WHITE);
        phieuDatThuocInfoPanel.setBorder(new TitledBorder(
            new LineBorder(new Color(0, 0, 205), 2, true),
            "Thông tin phiếu đặt thuốc",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Roboto", Font.BOLD, 14),
            new Color(0, 0, 205)
        ));
        
        phieuDatThuocInfoPanel.add(createInfoLabel("Mã hóa đơn:"));
        lblMaPhieuDat = createValueLabel("");
        phieuDatThuocInfoPanel.add(lblMaPhieuDat);
        
        phieuDatThuocInfoPanel.add(createInfoLabel("Ngày lập:"));
        lblNgayDat = createValueLabel("");
        phieuDatThuocInfoPanel.add(lblNgayDat);
        
        phieuDatThuocInfoPanel.add(createInfoLabel("Nhân viên:"));
        lblNhanVien = createValueLabel("");
        phieuDatThuocInfoPanel.add(lblNhanVien);
        
        phieuDatThuocInfoPanel.add(createInfoLabel("Khách hàng:"));
        lblKhachHang = createValueLabel("");
        phieuDatThuocInfoPanel.add(lblKhachHang);
        
        infoPanel.add(phieuDatThuocInfoPanel, BorderLayout.NORTH);
        
        // Chi tiết sản phẩm
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(new TitledBorder(
            new LineBorder(new Color(0, 0, 205), 2, true),
            "Danh sách sản phẩm",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Roboto", Font.BOLD, 14),
            new Color(0, 0, 205)
        ));
        
        String[] columns = {"STT", "Mã thuốc", "Tên thuốc", "ĐVT", "Số lượng", "Đơn giá", "Thành tiền"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableCTHD = new JTable(tableModel);
        tableCTHD.setFont(new Font("Roboto", Font.PLAIN, 13));
        tableCTHD.setRowHeight(35);
        tableCTHD.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 13));
        tableCTHD.getTableHeader().setBackground(new Color(0, 0, 205));
        tableCTHD.getTableHeader().setForeground(Color.WHITE);
        
        // Center align columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableCTHD.getColumnCount(); i++) {
            tableCTHD.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Column widths
        tableCTHD.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableCTHD.getColumnModel().getColumn(1).setPreferredWidth(80);
        tableCTHD.getColumnModel().getColumn(2).setPreferredWidth(250);
        tableCTHD.getColumnModel().getColumn(3).setPreferredWidth(60);
        tableCTHD.getColumnModel().getColumn(4).setPreferredWidth(80);
        tableCTHD.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableCTHD.getColumnModel().getColumn(6).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(tableCTHD);
        scrollPane.setPreferredSize(new Dimension(850, 250));
        detailPanel.add(scrollPane, BorderLayout.CENTER);
        
        infoPanel.add(detailPanel, BorderLayout.CENTER);
        
        // Summary Panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Tổng tiền
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new java.awt.GridLayout(4, 2, 10, 10));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(new TitledBorder(
            new LineBorder(new Color(0, 0, 205), 2, true),
            "Chi tiết thanh toán",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Roboto", Font.BOLD, 14),
            new Color(0, 0, 205)
        ));
        
        // Tạm tính
        totalPanel.add(createInfoLabel("Tạm tính:"));
        lblTongTien = createMoneyLabel("0 VNĐ");
        totalPanel.add(lblTongTien);
        
        // Thuế
        totalPanel.add(createInfoLabel("Thuế VAT:"));
        lblThue = createValueLabel("0 VNĐ");
        totalPanel.add(lblThue);
        
        // Khuyến mãi
        totalPanel.add(createInfoLabel("Khuyến mãi:"));
        lblKhuyenMai = createValueLabel("0 VNĐ");
        totalPanel.add(lblKhuyenMai);
        
        // Thành tiền
        totalPanel.add(createInfoLabel("TỔNG TIỀN:"));
        lblThanhToan = createFinalMoneyLabel("0 VNĐ");
        totalPanel.add(lblThanhToan);
        
        summaryPanel.add(totalPanel, BorderLayout.CENTER);
        
        infoPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnClose = new JButton("ĐÓNG");
        btnClose.setFont(new Font("Roboto", Font.BOLD, 13));
        btnClose.setPreferredSize(new Dimension(120, 40));
        btnClose.setBackground(new Color(220, 53, 69));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        
        buttonPanel.add(btnClose);
        
        // Add all panels
        add(headerPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.BOLD, 13));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }
    
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.PLAIN, 13));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }
    
    private JLabel createMoneyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }
    
    private JLabel createFinalMoneyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.BOLD, 18));
        label.setForeground(new Color(220, 53, 69));
        return label;
    }
    
    private void loadData(String maPhieuDat) {
        try {
            // Lấy thông tin hóa đơn
            phieuDatThuoc = pdtDAO.getPhieuDatThuocQuaMaPhieuDat(maPhieuDat);
            
            if (phieuDatThuoc == null) {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy hóa đơn!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
            
            // Hiển thị thông tin phiếu đặt thuốc
            lblMaPhieuDat.setText(phieuDatThuoc.getMaPhieuDat());
            lblNgayDat.setText(dateFormat.format(phieuDatThuoc.getNgayDat()));
            String tenNhanVien = null;
            if (phieuDatThuoc.getNhanVien() != null
                    && phieuDatThuoc.getNhanVien().getMaNV() != null
                    && !phieuDatThuoc.getNhanVien().getMaNV().trim().isEmpty()) {
                NhanVien nv = nhanVienDAO.getNhanVienTheoMa(phieuDatThuoc.getNhanVien().getMaNV());
                tenNhanVien = nv != null ? nv.getTenNV() : null;
            }
            if (tenNhanVien == null || tenNhanVien.trim().isEmpty()) {
                tenNhanVien = pdtDAO.getTenNhanVienThanhToanTheoPhieuDat(maPhieuDat);
            }
            lblNhanVien.setText(tenNhanVien != null && !tenNhanVien.trim().isEmpty()
                    ? tenNhanVien.trim()
                    : "N/A");
            
            // Lấy thông tin khách hàng
            if (phieuDatThuoc.getKhachHang() != null && phieuDatThuoc.getKhachHang().getMaKH() != null) {
                KhachHang kh = khachHangDAO.getKhachHangTheoMa(phieuDatThuoc.getKhachHang().getMaKH());
                if (kh != null) {
                    lblKhachHang.setText(kh.getHoTen() + " - " + kh.getSoDienThoai());
                } else {
                    lblKhachHang.setText(phieuDatThuoc.getKhachHang().getMaKH());
                }
            } else {
                lblKhachHang.setText("Khách lẻ");
            }
            
            // Lấy chi tiết hóa đơn
            ArrayList<ChiTietPhieuDatThuoc> dsCTPDT = chiTietPhieuDatThuocDAO.getChiTietPhieuDatThuocQuaMaPhieuDatThuoc(maPhieuDat);
            
            tableModel.setRowCount(0);
            int stt = 1;
            double tamTinh = 0;
            
            for (ChiTietPhieuDatThuoc cthd : dsCTPDT) {
                // Lấy thông tin thuốc
                Thuoc thuoc = thuocDAO.getThuocTheoMaThuoc(cthd.getThuoc().getMaThuoc());
                
                double thanhTien = cthd.getSoLuong() * cthd.getDonGia();
                tamTinh += thanhTien;
                
                tableModel.addRow(new Object[]{
                    stt++,
                    cthd.getThuoc().getMaThuoc(),
                    thuoc != null ? thuoc.getTenThuoc() : "N/A",
                    thuoc != null ? thuoc.getDonViTinh() : "N/A",
                    cthd.getSoLuong(),
                    String.format("%,.0f VNĐ", cthd.getDonGia()),
                    String.format("%,.0f VNĐ", thanhTien)
                });
            }
            // Hiển thị tạm tính
            lblTongTien.setText(String.format("%,.0f VNĐ", tamTinh));

            double tienGiamGia = 0;
            if (phieuDatThuoc.getKhuyenMai() != null
                    && phieuDatThuoc.getKhuyenMai().getMaKM() != null
                    && !phieuDatThuoc.getKhuyenMai().getMaKM().trim().isEmpty()) {
                KhuyenMai km = khuyenMaiDAO.getKhuyenMaiTheoMa(phieuDatThuoc.getKhuyenMai().getMaKM());
                if (km != null) {
                    tienGiamGia = tamTinh * km.getPhanTramGiamGia() / 100;
                    lblKhuyenMai.setText(String.format("%.0f%% (-%,.0f VNĐ)", km.getPhanTramGiamGia(), tienGiamGia));
                }
            } else {
                lblKhuyenMai.setText("0 VNĐ");
            }

            double tienSauGiam = tamTinh - tienGiamGia;
            double tienThue = 0;
            if (phieuDatThuoc.getThue() != null
                    && phieuDatThuoc.getThue().getMaThue() != null
                    && !phieuDatThuoc.getThue().getMaThue().trim().isEmpty()) {
                Thue thue = thueDAO.getThueTheoMa(phieuDatThuoc.getThue().getMaThue());
                if (thue != null) {
                    tienThue = tienSauGiam * thue.getPhanTramThue() / 100;
                    lblThue.setText(String.format("%.0f%% (+%,.0f VNĐ)", thue.getPhanTramThue(), tienThue));
                }
            } else {
                lblThue.setText("0 VNĐ");
            }

            double tongCong = tienSauGiam + tienThue;
            lblThanhToan.setText(String.format("%,.0f VNĐ", tongCong));
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải dữ liệu: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

}
