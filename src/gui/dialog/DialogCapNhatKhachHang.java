package gui.dialog;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import dao.KhachHangDAO;
import entity.KhachHang;
import gui.form.FormQuanLyKhachHang;

public class DialogCapNhatKhachHang extends JDialog {
    private JTextField txtMaKH;
    private JTextField txtHoTen;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    
    private JButton btnCapNhat;
    private JButton btnHuy;
    
    private KhachHangDAO khDAO;
    private FormQuanLyKhachHang parentForm;
    private KhachHang khachHang;
    
    public DialogCapNhatKhachHang(Frame parent, FormQuanLyKhachHang form, KhachHang kh) {
        super(parent, "Cập nhật thông tin khách hàng", true);
        this.parentForm = form;
        this.khachHang = kh;
        this.khDAO = new KhachHangDAO();
        
        initComponents();
        loadDataToForm();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(500, 450);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblTitle = new JLabel("CẬP NHẬT THÔNG TIN KHÁCH HÀNG");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        Font labelFont = new Font("Roboto", Font.PLAIN, 14);
        Font fieldFont = new Font("Roboto", Font.PLAIN, 14);
        
        int row = 0;
        
        // Mã khách hàng
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblMaKH = new JLabel("Mã khách hàng:");
        lblMaKH.setFont(labelFont);
        formPanel.add(lblMaKH, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtMaKH = new JTextField();
        txtMaKH.setFont(fieldFont);
        txtMaKH.setPreferredSize(new Dimension(300, 35));
        txtMaKH.setEnabled(false); // Không cho sửa mã khách hàng
        txtMaKH.setBackground(new Color(240, 240, 240));
        txtMaKH.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtMaKH, gbc);
        row++;
        
        // Họ tên
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblHoTen = new JLabel("Họ và tên:");
        lblHoTen.setFont(labelFont);
        formPanel.add(lblHoTen, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtHoTen = new JTextField();
        txtHoTen.setFont(fieldFont);
        txtHoTen.setPreferredSize(new Dimension(300, 35));
        txtHoTen.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtHoTen, gbc);
        row++;
        
        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setFont(labelFont);
        formPanel.add(lblSDT, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setFont(fieldFont);
        txtSoDienThoai.setPreferredSize(new Dimension(300, 35));
        txtSoDienThoai.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtSoDienThoai, gbc);
        row++;
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        formPanel.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtEmail = new JTextField();
        txtEmail.setFont(fieldFont);
        txtEmail.setPreferredSize(new Dimension(300, 35));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtEmail, gbc);
        row++;
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 245, 245));
        buttonPanel.setPreferredSize(new Dimension(500, 80));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        btnCapNhat = new JButton("CẬP NHẬT");
        btnCapNhat.setFont(new Font("Roboto", Font.BOLD, 14));
        btnCapNhat.setPreferredSize(new Dimension(140, 40));
        btnCapNhat.setBackground(new Color(0, 0, 205));
        btnCapNhat.setForeground(Color.WHITE);
        btnCapNhat.setFocusPainted(false);
        btnCapNhat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCapNhat.setBorder(new LineBorder(new Color(0, 0, 205), 2, true));
        btnCapNhat.addActionListener(e -> xuLyCapNhat());
        
        btnHuy = new JButton("HỦY");
        btnHuy.setFont(new Font("Roboto", Font.BOLD, 14));
        btnHuy.setPreferredSize(new Dimension(140, 40));
        btnHuy.setBackground(Color.WHITE);
        btnHuy.setForeground(new Color(100, 100, 100));
        btnHuy.setFocusPainted(false);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.setBorder(new LineBorder(new Color(200, 200, 200), 2, true));
        btnHuy.addActionListener(e -> dispose());
        
        buttonPanel.add(btnCapNhat);
        buttonPanel.add(btnHuy);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadDataToForm() {
        txtMaKH.setText(khachHang.getMaKH());
        txtHoTen.setText(khachHang.getHoTen());
        txtSoDienThoai.setText(khachHang.getSoDienThoai());
        txtEmail.setText(khachHang.getEmail());
    }
    
    private void xuLyCapNhat() {
        if (!validateInput()) {
            return;
        }
        
        try {
            String maKH = txtMaKH.getText().trim();
            String hoTen = txtHoTen.getText().trim();
            String soDienThoai = txtSoDienThoai.getText().trim();
            String email = txtEmail.getText().trim();
            
            // Kiểm tra số điện thoại đã tồn tại chưa (trừ số điện thoại hiện tại)
            if (!soDienThoai.equals(khachHang.getSoDienThoai()) && khDAO.kiemTraSDTTonTai(soDienThoai)) {
                JOptionPane.showMessageDialog(this, 
                    "Số điện thoại này đã được đăng ký cho khách hàng khác!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                txtSoDienThoai.requestFocus();
                return;
            }
            
            KhachHang khCapNhat = new KhachHang(maKH, hoTen, soDienThoai, email);
            
            boolean result = khDAO.capNhatKhachHang(khCapNhat);
            
            if (result) {
                JOptionPane.showMessageDialog(this, 
                    "Cập nhật thông tin khách hàng thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Reload table ở form chính
                parentForm.reloadTable();
                
                // Đóng dialog
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Cập nhật thông tin khách hàng thất bại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Có lỗi xảy ra: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateInput() {
        // Kiểm tra tên khách hàng
        if (txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập tên khách hàng!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtHoTen.requestFocus();
            return false;
        }
        
        // Kiểm tra số điện thoại
        String sdt = txtSoDienThoai.getText().trim();
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập số điện thoại!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }
        
        // Kiểm tra định dạng số điện thoại
        if (!sdt.matches("^(\\+84|0)\\d{9}$")) {
            JOptionPane.showMessageDialog(this, 
                "Số điện thoại không hợp lệ! (Phải bắt đầu bằng 0 hoặc +84 và theo sau là 9 chữ số)", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }
        
        // Kiểm tra email
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập email!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            JOptionPane.showMessageDialog(this, 
                "Email không hợp lệ!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        return true;
    }
}