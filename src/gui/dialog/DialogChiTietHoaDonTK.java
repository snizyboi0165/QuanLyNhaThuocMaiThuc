package gui.dialog;

import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.*;

import dao.HoaDonDAO;
import dao.NhanVienDAO;
import dao.KhachHangDAO;
import entity.HoaDon;
import entity.NhanVien;
import entity.KhachHang;

public class DialogChiTietHoaDonTK extends JDialog {
    
    private HoaDon hoaDon;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private HoaDonDAO hoaDonDAO;
    private NhanVienDAO nhanVienDAO;
    private KhachHangDAO khachHangDAO;
    
    public DialogChiTietHoaDonTK(Frame parent, HoaDon hd) {
        super(parent, "Thông tin chi tiết hóa đơn", true);
        this.hoaDon = hd;
        this.hoaDonDAO = new HoaDonDAO();
        this.nhanVienDAO = new NhanVienDAO();
        this.khachHangDAO = new KhachHangDAO();
        
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(700, 400);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(700, 60));
        
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT HÓA ĐƠN");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        contentPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        Font labelFont = new Font("Roboto", Font.BOLD, 14);
        Font valueFont = new Font("Roboto", Font.PLAIN, 14);
        
        int row = 0;
        
        // Mã hóa đơn
        addInfoRow(contentPanel, gbc, row++, "Mã hóa đơn:", hoaDon.getMaHD(), labelFont, valueFont);
        
        // Ngày lập
        addInfoRow(contentPanel, gbc, row++, "Ngày lập:", 
                  hoaDon.getNgayLap() != null ? sdf.format(hoaDon.getNgayLap()) : "Chưa cập nhật",
                  labelFont, valueFont);
        
        // Mã nhân viên
        String maNV = hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getMaNV() : "Không có";
        addInfoRow(contentPanel, gbc, row++, "Mã nhân viên:", maNV, labelFont, valueFont);
        
        // Tên nhân viên
        String tenNV = getTenNhanVien();
        addInfoRow(contentPanel, gbc, row++, "Tên nhân viên:", tenNV, labelFont, valueFont);
        
        // Mã khách hàng
        String maKH = hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getMaKH() : "Không có";
        addInfoRow(contentPanel, gbc, row++, "Mã khách hàng:", maKH, labelFont, valueFont);
        
        // Tên khách hàng
        String tenKH = getTenKhachHang();
        addInfoRow(contentPanel, gbc, row++, "Tên khách hàng:", tenKH, labelFont, valueFont);
        
        // Mã thuế
        String maThue = hoaDon.getThue() != null ? hoaDon.getThue().getMaThue() : "Không có";
        addInfoRow(contentPanel, gbc, row++, "Mã thuế:", maThue, labelFont, valueFont);
        
        // Mã khuyến mãi
        String maKM = hoaDon.getKhuyenMai() != null ? hoaDon.getKhuyenMai().getMaKM() : "Không có";
        addInfoRow(contentPanel, gbc, row++, "Mã khuyến mãi:", maKM, labelFont, valueFont);
        
        // Mã phiếu đặt
        String maPDT = hoaDon.getPhieuDatThuoc() != null ? hoaDon.getPhieuDatThuoc().getMaPhieuDat() : "Không có";
        addInfoRow(contentPanel, gbc, row++, "Mã phiếu đặt:", maPDT, labelFont, valueFont);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(230, 245, 245));
        
        JButton btnDong = new JButton("ĐÓNG");
        btnDong.setFont(new Font("Roboto", Font.BOLD, 14));
        btnDong.setPreferredSize(new Dimension(140, 40));
        btnDong.setBackground(new Color(0, 0, 205));
        btnDong.setForeground(Color.WHITE);
        btnDong.setFocusPainted(false);
        btnDong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDong.setBorder(new LineBorder(new Color(0, 0, 205), 2, true));
        btnDong.addActionListener(e -> dispose());
        
        buttonPanel.add(btnDong);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Lấy tên nhân viên từ mã nhân viên
     */
    private String getTenNhanVien() {
        try {
            if (hoaDon.getNhanVien() == null || hoaDon.getNhanVien().getMaNV() == null 
                || hoaDon.getNhanVien().getMaNV().isEmpty()) {
                return "Không có";
            }
            
            String maNV = hoaDon.getNhanVien().getMaNV();
            NhanVien nv = nhanVienDAO.getNhanVienTheoMa(maNV);
            
            if (nv != null && nv.getTenNV() != null) {
                return nv.getTenNV();
            }
            
            return "Không có";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi lấy dữ liệu";
        }
    }
    
    /**
     * Lấy tên khách hàng từ mã khách hàng
     */
    private String getTenKhachHang() {
        try {
            if (hoaDon.getKhachHang() == null || hoaDon.getKhachHang().getMaKH() == null 
                || hoaDon.getKhachHang().getMaKH().isEmpty()) {
                return "Không có";
            }
            
            String maKH = hoaDon.getKhachHang().getMaKH();
            KhachHang kh = khachHangDAO.getKhachHangTheoMa(maKH);
            
            if (kh != null && kh.getHoTen() != null) {
                return kh.getHoTen();
            }
            
            return "Không có";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi lấy dữ liệu";
        }
    }
    
    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, 
                           String labelText, String valueText, Font labelFont, Font valueFont) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        
        JLabel value = new JLabel(valueText);
        value.setFont(valueFont);
        panel.add(value, gbc);
    }
}