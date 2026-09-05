package gui.dialog;

import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import com.toedter.calendar.JDateChooser;

import dao.NhanVienDAO;
import entity.NhanVien;
import gui.form.FormQuanLyNhanVien;

public class DialogThemNhanVien extends JDialog {
    private JTextField txtTenNV;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JTextField txtDiaChi;
    private JComboBox<String> cboChucVu;
    private JComboBox<String> cboGioiTinh;
    private JDateChooser dateChooserNgaySinh;
    
    private JButton btnXacNhan;
    private JButton btnHuy;
    
    private NhanVienDAO nvDAO;
    private FormQuanLyNhanVien parentForm;
    
    public DialogThemNhanVien(Frame parent, FormQuanLyNhanVien form) {
        super(parent, "Thêm nhân viên mới", true);
        this.parentForm = form;
        this.nvDAO = new NhanVienDAO();
        
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(600, 650);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(600, 60));
        JLabel lblTitle = new JLabel("THÊM NHÂN VIÊN MỚI");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
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
        
        // Họ tên
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblTenNV = new JLabel("Họ và tên: *");
        lblTenNV.setFont(labelFont);
        lblTenNV.setForeground(Color.BLACK);
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
        row++;
        
        // Chức vụ
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblChucVu = new JLabel("Chức vụ: *");
        lblChucVu.setFont(labelFont);
        formPanel.add(lblChucVu, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String[] chucVu = {"Nhân viên bán thuốc", "Nhân viên quản lý"};
        cboChucVu = new JComboBox<>(chucVu);
        cboChucVu.setFont(fieldFont);
        cboChucVu.setPreferredSize(new Dimension(300, 35));
        cboChucVu.setBackground(Color.WHITE);
        formPanel.add(cboChucVu, gbc);
        row++;
        
        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblSDT = new JLabel("Số điện thoại: *");
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
        
        // Ngày sinh
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblNgaySinh = new JLabel("Ngày sinh: *");
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
        row++;
        
        // Giới tính
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblGioiTinh = new JLabel("Giới tính: *");
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
        row++;
        
        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblDiaChi = new JLabel("Địa chỉ: *");
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
        row++;
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblEmail = new JLabel("Email: *");
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
        
        // Note
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        JLabel lblNote = new JLabel("* Mã nhân viên sẽ được tự động phát sinh");
        lblNote.setFont(new Font("Roboto", Font.ITALIC, 12));
        lblNote.setForeground(new Color(100, 100, 100));
        formPanel.add(lblNote, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 245, 245));
        buttonPanel.setPreferredSize(new Dimension(600, 80));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        btnXacNhan = new JButton("XÁC NHẬN");
        btnXacNhan.setFont(new Font("Roboto", Font.BOLD, 14));
        btnXacNhan.setPreferredSize(new Dimension(140, 40));
        btnXacNhan.setBackground(new Color(0, 0, 205));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXacNhan.setBorder(new LineBorder(new Color(0, 0, 205), 2, true));
        btnXacNhan.addActionListener(e -> xuLyThemNhanVien());
        
        btnHuy = new JButton("HỦY");
        btnHuy.setFont(new Font("Roboto", Font.BOLD, 14));
        btnHuy.setPreferredSize(new Dimension(140, 40));
        btnHuy.setBackground(Color.WHITE);
        btnHuy.setForeground(new Color(100, 100, 100));
        btnHuy.setFocusPainted(false);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.setBorder(new LineBorder(new Color(200, 200, 200), 2, true));
        btnHuy.addActionListener(e -> dispose());
        
        buttonPanel.add(btnXacNhan);
        buttonPanel.add(btnHuy);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void xuLyThemNhanVien() {
        if (!validateInput()) {
            return;
        }
        
        try {
            String maNV = nvDAO.generateMaNV();
            
            String tenNV = txtTenNV.getText().trim();
            String chucVu = cboChucVu.getSelectedItem().toString();
            String soDienThoai = txtSoDienThoai.getText().trim();
            Date ngaySinh = dateChooserNgaySinh.getDate();
            String gioiTinh = cboGioiTinh.getSelectedItem().toString();
            String diaChi = txtDiaChi.getText().trim();
            String email = txtEmail.getText().trim();
            
            // Constructor: NhanVien(String maNV, String hoTen, String chucVu, String soDienThoai, 
            //                        Date ngaySinh, String gioiTinh, String diaChi, String email)
            NhanVien nv = new NhanVien(maNV, tenNV, chucVu, soDienThoai, ngaySinh, gioiTinh, diaChi, email);
            
            boolean result = nvDAO.themNhanVien(nv);
            
            if (result) {
                JOptionPane.showMessageDialog(this, 
                    "Thêm nhân viên thành công!\nMã nhân viên: " + maNV, 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                parentForm.reloadTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Thêm nhân viên thất bại!", 
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
        // Họ tên
        if (txtTenNV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập họ và tên nhân viên!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtTenNV.requestFocus();
            return false;
        }
        
        // Số điện thoại
        String sdt = txtSoDienThoai.getText().trim();
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập số điện thoại!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }
        
        if (!sdt.matches("^(\\+84|0)\\d{9}$")) {
            JOptionPane.showMessageDialog(this, 
                "Số điện thoại không hợp lệ!\n(Phải bắt đầu bằng 0 hoặc +84 và theo sau là 9 chữ số)", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }
        
        // Ngày sinh
        if (dateChooserNgaySinh.getDate() == null) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn ngày sinh!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Kiểm tra tuổi >= 18 (KHÔNG CAST sang java.sql.Date)
        Date ngaySinh = dateChooserNgaySinh.getDate();  // Đây là java.util.Date
        Calendar calNgaySinh = Calendar.getInstance();
        calNgaySinh.setTime(ngaySinh);
        
        Calendar calHienTai = Calendar.getInstance();
        
        int tuoi = calHienTai.get(Calendar.YEAR) - calNgaySinh.get(Calendar.YEAR);
        
        if (calHienTai.get(Calendar.MONTH) < calNgaySinh.get(Calendar.MONTH) ||
            (calHienTai.get(Calendar.MONTH) == calNgaySinh.get(Calendar.MONTH) &&
             calHienTai.get(Calendar.DAY_OF_MONTH) < calNgaySinh.get(Calendar.DAY_OF_MONTH))) {
            tuoi--;
        }
        
        if (tuoi < 18) {
            JOptionPane.showMessageDialog(this, 
                "Nhân viên phải >= 18 tuổi!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Ngày sinh phải trước ngày hiện tại
        if (ngaySinh.after(new Date())) {
            JOptionPane.showMessageDialog(this, 
                "Ngày sinh phải trước ngày hiện tại!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Địa chỉ
        if (txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập địa chỉ!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtDiaChi.requestFocus();
            return false;
        }
        
        // Email
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
                "Email không hợp lệ!\n(Email phải có định dạng xxx@gmail.com)", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        return true;
    }
}