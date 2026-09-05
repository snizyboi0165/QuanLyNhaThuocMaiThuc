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

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.NhanVienDAO;
import dao.ThuocDAO;
import dao.ThueDAO;
import dao.KhuyenMaiDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.Thuoc;
import entity.Thue;
import entity.KhuyenMai;

public class DialogChiTietHoaDon extends JDialog {
    private JTable tableCTHD;
    private DefaultTableModel tableModel;
    private JLabel lblMaHD, lblNgayLap, lblNhanVien, lblKhachHang;
    private JLabel lblTongTien, lblThue, lblKhuyenMai, lblThanhToan;
    
    private HoaDonDAO hoaDonDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private ThuocDAO thuocDAO;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;
    private ThueDAO thueDAO;
    private KhuyenMaiDAO khuyenMaiDAO;
    
    private HoaDon hoaDon;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    public DialogChiTietHoaDon(JFrame parent, String maHD) {
        super(parent, "Chi tiết hóa đơn", true);
        
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        thuocDAO = new ThuocDAO();
        khachHangDAO = new KhachHangDAO();
        nhanVienDAO = new NhanVienDAO();
        thueDAO = new ThueDAO();
        khuyenMaiDAO = new KhuyenMaiDAO();
        
        initComponents();
        loadData(maHD);
        
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
        
        JLabel lblTitle = new JLabel("CHI TIẾT HÓA ĐƠN");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setIcon(new FlatSVGIcon(getClass().getResource("/img/info.svg")).derive(32, 32));
        headerPanel.add(lblTitle);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Thông tin hóa đơn
        JPanel hoaDonInfoPanel = new JPanel();
        hoaDonInfoPanel.setLayout(new java.awt.GridLayout(2, 4, 15, 10));
        hoaDonInfoPanel.setBackground(Color.WHITE);
        hoaDonInfoPanel.setBorder(new TitledBorder(
            new LineBorder(new Color(0, 0, 205), 2, true),
            "Thông tin hóa đơn",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Roboto", Font.BOLD, 14),
            new Color(0, 0, 205)
        ));
        
        hoaDonInfoPanel.add(createInfoLabel("Mã hóa đơn:"));
        lblMaHD = createValueLabel("");
        hoaDonInfoPanel.add(lblMaHD);
        
        hoaDonInfoPanel.add(createInfoLabel("Ngày lập:"));
        lblNgayLap = createValueLabel("");
        hoaDonInfoPanel.add(lblNgayLap);
        
        hoaDonInfoPanel.add(createInfoLabel("Nhân viên:"));
        lblNhanVien = createValueLabel("");
        hoaDonInfoPanel.add(lblNhanVien);
        
        hoaDonInfoPanel.add(createInfoLabel("Khách hàng:"));
        lblKhachHang = createValueLabel("");
        hoaDonInfoPanel.add(lblKhachHang);
        
        infoPanel.add(hoaDonInfoPanel, BorderLayout.NORTH);
        
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
    
    private void loadData(String maHD) {
        try {
            // Lấy thông tin hóa đơn
            hoaDon = hoaDonDAO.getHoaDonTheoMa(maHD);
            
            if (hoaDon == null) {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy hóa đơn!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
            
            // Hiển thị thông tin hóa đơn
            lblMaHD.setText(hoaDon.getMaHD());
            lblNgayLap.setText(dateFormat.format(hoaDon.getNgayLap()));
            
            // Lấy thông tin nhân viên
            NhanVien nv = nhanVienDAO.getNhanVienTheoMa(hoaDon.getNhanVien().getMaNV());
            if (nv != null) {
                lblNhanVien.setText(dinhDangTenNhanVien(nv.getTenNV()));
            } else {
                lblNhanVien.setText("N/A");
            }
            
            // Lấy thông tin khách hàng
            if (hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getMaKH() != null) {
                KhachHang kh = khachHangDAO.getKhachHangTheoMa(hoaDon.getKhachHang().getMaKH());
                if (kh != null) {
                    lblKhachHang.setText(kh.getHoTen() + " - " + kh.getSoDienThoai());
                } else {
                    lblKhachHang.setText(hoaDon.getKhachHang().getMaKH());
                }
            } else {
                lblKhachHang.setText("Khách lẻ");
            }
            
            // Lấy chi tiết hóa đơn
            ArrayList<ChiTietHoaDon> dsCTHD = chiTietHoaDonDAO.getChiTietHoaDonTheoMaHD(maHD);
            
            tableModel.setRowCount(0);
            int stt = 1;
            double tamTinh = 0;
            
            for (ChiTietHoaDon cthd : dsCTHD) {
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
            
            double tamTinhGoc = tamTinh;
            
         // Tính khuyến mãi
            double tienGiamGia = 0;
            double tienSauGiamGia = tamTinhGoc;
            if (hoaDon.getKhuyenMai() != null && hoaDon.getKhuyenMai().getMaKM() != null) {
                try {
                    KhuyenMai km = khuyenMaiDAO.getKhuyenMaiTheoMa(hoaDon.getKhuyenMai().getMaKM());
                    if (km != null) {
                        double tyLeGiam = km.getPhanTramGiamGia();
                        tienGiamGia = tamTinhGoc * (tyLeGiam / 100);
                        tienSauGiamGia = tamTinhGoc - tienGiamGia;
                        lblKhuyenMai.setText(String.format("%.0f%% (-%,.0f VNĐ)", tyLeGiam, tienGiamGia));
                    }
                } catch (Exception e) {
                    lblKhuyenMai.setText("0 VNĐ");
                }
            } else {
                lblKhuyenMai.setText("0 VNĐ");
            }
            
            // Tính thuế
            double tienThue = 0;
            double tyLeThue = 0;
            if (hoaDon.getThue() != null && hoaDon.getThue().getMaThue() != null) {
                try {
                    Thue thue = thueDAO.getThueTheoMa(hoaDon.getThue().getMaThue());
                    if (thue != null && tienSauGiamGia > 0) {
                        tyLeThue = thue.getPhanTramThue();
                        tienThue = tienSauGiamGia * (tyLeThue / 100);
                        lblThue.setText(String.format("%.0f%% (+%,.0f VNĐ)", tyLeThue, tienThue));
                    }
                } catch (Exception e) {
                    lblThue.setText("0 VNĐ");
                }
            } else {
                lblThue.setText("0 VNĐ");
            }
            
            double tongThanhToan = tienSauGiamGia + tienThue;
            lblThanhToan.setText(String.format("%,.0f VNĐ", tongThanhToan));
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải dữ liệu: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private String dinhDangTenNhanVien(String tenNhanVien) {
        if (tenNhanVien == null || tenNhanVien.trim().isEmpty()) {
            return "N/A";
        }
        return tenNhanVien.replaceAll("\\s*\\(NV\\d+\\)\\s*$", "").trim();
    }

}
