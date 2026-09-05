package gui.dialog;

import entity.KhachHang;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class DialogChiTietKH extends JDialog {
    
    private final KhachHang khachHang;
    
    public DialogChiTietKH(Frame parent, KhachHang kh) {
        super(parent, "Thông tin chi tiết khách hàng", true);
        
        if (kh == null) {
            throw new IllegalArgumentException("KhachHang cannot be null");
        }
        
        this.khachHang = kh;
        
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(600, 350);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(600, 60));
        
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT KHÁCH HÀNG");
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
        
        // Mã khách hàng
        addInfoRow(contentPanel, gbc, row++, "Mã khách hàng:", khachHang.getMaKH(), labelFont, valueFont);
        
        // Họ tên
        addInfoRow(contentPanel, gbc, row++, "Họ tên:", khachHang.getHoTen(), labelFont, valueFont);
        
        // Số điện thoại
        addInfoRow(contentPanel, gbc, row++, "Số điện thoại:", 
                  khachHang.getSoDienThoai() != null ? khachHang.getSoDienThoai() : "Chưa cập nhật", 
                  labelFont, valueFont);
        
        // Email
        addInfoRow(contentPanel, gbc, row++, "Email:", 
                  khachHang.getEmail() != null ? khachHang.getEmail() : "Chưa cập nhật", 
                  labelFont, valueFont);
        
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
        gbc.weightx = 0.35;
        
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        
        JLabel value = new JLabel(valueText);
        value.setFont(valueFont);
        panel.add(value, gbc);
    }
}