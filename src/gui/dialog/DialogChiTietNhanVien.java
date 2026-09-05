package gui.dialog;

import entity.NhanVien;
import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.*;

public class DialogChiTietNhanVien extends JDialog {
    
    private final NhanVien nhanVien;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    public DialogChiTietNhanVien(Frame parent, NhanVien nv) {
        super(parent, "Thông tin chi tiết nhân viên", true);
        
        if (nv == null) {
            throw new IllegalArgumentException("NhanVien cannot be null");
        }
        
        this.nhanVien = nv;
        
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(650, 600);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(650, 60));
        
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT NHÂN VIÊN");
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
        gbc.insets = new Insets(12, 10, 12, 10);
        
        Font labelFont = new Font("Roboto", Font.BOLD, 15);
        Font valueFont = new Font("Roboto", Font.PLAIN, 15);
        
        int row = 0;
        
        // Mã nhân viên
        addInfoRow(contentPanel, gbc, row++, "Mã nhân viên:", nhanVien.getMaNV(), labelFont, valueFont);
        
        // Tên nhân viên
        addInfoRow(contentPanel, gbc, row++, "Tên nhân viên:", nhanVien.getTenNV(), labelFont, valueFont);
        
        // Giới tính
        addInfoRow(contentPanel, gbc, row++, "Giới tính:", nhanVien.getGioiTinh(), labelFont, valueFont);
        
        // Chức vụ
        addInfoRow(contentPanel, gbc, row++, "Chức vụ:", nhanVien.getChucVu(), labelFont, valueFont);
        
        // Số điện thoại
        addInfoRow(contentPanel, gbc, row++, "Số điện thoại:", nhanVien.getSoDienThoai(), labelFont, valueFont);
        
        // Email
        addInfoRow(contentPanel, gbc, row++, "Email:", 
                  nhanVien.getEmail() != null ? nhanVien.getEmail() : "Chưa cập nhật", 
                  labelFont, valueFont);
        
        // Ngày sinh
        addInfoRow(contentPanel, gbc, row++, "Ngày sinh:", 
                  nhanVien.getNgaySinh() != null ? sdf.format(nhanVien.getNgaySinh()) : "Chưa cập nhật", 
                  labelFont, valueFont);
        
        // Địa chỉ
        addInfoRow(contentPanel, gbc, row++, "Địa chỉ:", 
                  nhanVien.getDiaChi() != null ? nhanVien.getDiaChi() : "Chưa cập nhật", 
                  labelFont, valueFont);
        
        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(labelFont);
        contentPanel.add(lblTrangThai, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String trangThai = nhanVien.isXoa() ? "Đã xóa" : "Đang làm việc";
        JLabel lblTrangThaiValue = new JLabel(trangThai);
        lblTrangThaiValue.setFont(valueFont);
        
        // Màu sắc theo trạng thái
        if (nhanVien.isXoa()) {
            lblTrangThaiValue.setForeground(Color.RED);
        } else {
            lblTrangThaiValue.setForeground(new Color(46, 125, 50));
        }
        contentPanel.add(lblTrangThaiValue, gbc);
        
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