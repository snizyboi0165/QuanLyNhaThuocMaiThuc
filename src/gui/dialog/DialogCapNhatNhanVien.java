package gui.dialog;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.toedter.calendar.JDateChooser;

import dao.NhanVienDAO;
import entity.NhanVien;
import gui.form.FormQuanLyNhanVien;

public class DialogCapNhatNhanVien extends JDialog {
    private JTextField txtMaNV;
    private JTextField txtTenNV;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JTextField txtDiaChi;
    private JComboBox<String> cboChucVu;
    private JComboBox<String> cboGioiTinh;
    private JDateChooser dateChooserNgaySinh;
    
    private JButton btnCapNhat;
    private JButton btnHuy;
    
    private NhanVienDAO nvDAO;
    private FormQuanLyNhanVien parentForm;
    private NhanVien nhanVien;
    
    public DialogCapNhatNhanVien(Frame parent, FormQuanLyNhanVien form, NhanVien nv) {
        super(parent, "Cập nhật thông tin nhân viên", true);
        this.parentForm = form;
        this.nhanVien = nv;
        this.nvDAO = new NhanVienDAO();
        
        initComponents();
        loadDataToForm();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(600, 700);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(600, 60));
        JLabel lblTitle = new JLabel("CẬP NHẬT THÔNG TIN NHÂN VIÊN");
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
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblMaNV = new JLabel("Mã nhân viên:");
        lblMaNV.setFont(labelFont);
        formPanel.add(lblMaNV, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtMaNV = new JTextField();
        txtMaNV.setFont(fieldFont);
        txtMaNV.setPreferredSize(new Dimension(300, 35));
        txtMaNV.setEnabled(false); // Không cho sửa mã nhân viên
        txtMaNV.setBackground(new Color(240, 240, 240));
        txtMaNV.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtMaNV, gbc);
        
        // Họ tên
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel lblTenNV = new JLabel("Họ và tên:");
        lblTenNV.setFont(labelFont);
        formPanel.add(lblTenNV, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtTenNV = new JTextField();
        txtTenNV.setFont(fieldFont);
        txtTenNV.setPreferredSize(new Dimension(300, 35));
        txtTenNV.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtTenNV, gbc);
        
        // Chức vụ
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel lblChucVu = new JLabel("Chức vụ:");
        lblChucVu.setFont(labelFont);
        formPanel.add(lblChucVu, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String[] chucVu = {"Nhân viên quản lý", "Nhân viên bán thuốc"};
        cboChucVu = new JComboBox<>(chucVu);
        cboChucVu.setFont(fieldFont);
        cboChucVu.setPreferredSize(new Dimension(300, 35));
        cboChucVu.setBackground(Color.WHITE);
        formPanel.add(cboChucVu, gbc);
        
        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 3;
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
        
        // Ngày sinh
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(labelFont);
        formPanel.add(lblNgaySinh, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        dateChooserNgaySinh = new JDateChooser();
        dateChooserNgaySinh.setDateFormatString("dd/MM/yyyy");
        dateChooserNgaySinh.setFont(fieldFont);
        dateChooserNgaySinh.setPreferredSize(new Dimension(300, 35));
        dateChooserNgaySinh.setBackground(Color.WHITE);
        formPanel.add(dateChooserNgaySinh, gbc);
        
        // Giới tính
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setFont(labelFont);
        formPanel.add(lblGioiTinh, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String[] gioiTinh = {"Nam", "Nữ"};
        cboGioiTinh = new JComboBox<>(gioiTinh);
        cboGioiTinh.setFont(fieldFont);
        cboGioiTinh.setPreferredSize(new Dimension(300, 35));
        cboGioiTinh.setBackground(Color.WHITE);
        formPanel.add(cboGioiTinh, gbc);
        
        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setFont(labelFont);
        formPanel.add(lblDiaChi, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtDiaChi = new JTextField();
        txtDiaChi.setFont(fieldFont);
        txtDiaChi.setPreferredSize(new Dimension(300, 35));
        txtDiaChi.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtDiaChi, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 7;
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
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 245, 245));
        buttonPanel.setPreferredSize(new Dimension(600, 80));
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
        txtMaNV.setText(nhanVien.getMaNV());
        txtTenNV.setText(nhanVien.getTenNV());
        cboChucVu.setSelectedItem(nhanVien.getChucVu());
        txtSoDienThoai.setText(nhanVien.getSoDienThoai());
        dateChooserNgaySinh.setDate(nhanVien.getNgaySinh());
        cboGioiTinh.setSelectedItem(nhanVien.getGioiTinh());
        txtDiaChi.setText(nhanVien.getDiaChi());
        txtEmail.setText(nhanVien.getEmail());
    }
    
    private void xuLyCapNhat() {
        // Validation
        if (!validateInput()) {
            return;
        }
        
        try {
            // Lấy dữ liệu từ form
            String maNV = txtMaNV.getText().trim();
            String tenNV = txtTenNV.getText().trim();
            String chucVu = cboChucVu.getSelectedItem().toString();
            String soDienThoai = txtSoDienThoai.getText().trim();
            Date ngaySinh = dateChooserNgaySinh.getDate();
            String gioiTinh = cboGioiTinh.getSelectedItem().toString();
            String diaChi = txtDiaChi.getText().trim();
            String email = txtEmail.getText().trim();
            
            // Convert Date to java.sql.Date
            java.sql.Date sqlNgaySinh = new java.sql.Date(ngaySinh.getTime());
            
            NhanVien nvCapNhat = new NhanVien(maNV, tenNV, chucVu, soDienThoai, sqlNgaySinh, gioiTinh, diaChi, email);
            
            // Cập nhật vào database
            boolean result = nvDAO.capNhatNhanVien(nvCapNhat);
            
            if (result) {
                JOptionPane.showMessageDialog(this, 
                    "Cập nhật thông tin nhân viên thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Reload table ở form chính
                parentForm.reloadTable();
                
                // Đóng dialog
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Cập nhật thông tin nhân viên thất bại!", 
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
        // Kiểm tra tên nhân viên
        if (txtTenNV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập tên nhân viên!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtTenNV.requestFocus();
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
        if (!sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, 
                "Số điện thoại không hợp lệ! (Phải bắt đầu bằng 0 và có 10 chữ số)", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }
        
        // Kiểm tra ngày sinh
        if (dateChooserNgaySinh.getDate() == null) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn ngày sinh!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Kiểm tra email
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
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