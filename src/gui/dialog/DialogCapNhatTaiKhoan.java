package gui.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import dao.TaiKhoanDAO;
import dao.NhanVienDAO;
import entity.TaiKhoan;
import gui.form.FormQuanLyTK;
import entity.NhanVien;
import java.sql.SQLException;
import java.util.ArrayList;

public class DialogCapNhatTaiKhoan extends JDialog {
    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JPasswordField txtXacNhanMatKhau;
    private JComboBox<String> cboTrangThai;
    private JComboBox<String> cboVaiTro;
    private JTextField txtNhanVien;
    
    private JButton btnCapNhat;
    private JButton btnHuy;
    
    private TaiKhoanDAO tkDAO;
    private NhanVienDAO nvDAO;
    private FormQuanLyTK parentForm;
    private TaiKhoan taiKhoan;
    private ArrayList<NhanVien> dsNhanVien;
    
    public DialogCapNhatTaiKhoan(Frame parent, FormQuanLyTK form, TaiKhoan tk) {
        super(parent, "Cập nhật thông tin tài khoản", true);
        this.parentForm = form;
        this.taiKhoan = tk;
        this.tkDAO = new TaiKhoanDAO();
        this.nvDAO = new NhanVienDAO();
        
        try {
            dsNhanVien = nvDAO.getDSNhanVien();
        } catch (SQLException e) {
            e.printStackTrace();
            dsNhanVien = new ArrayList<>();
        }
        
        initComponents();
        loadDataToForm();
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
        JLabel lblTitle = new JLabel("CẬP NHẬT THÔNG TIN TÀI KHOẢN");
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
        
        // Tên đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblTenDangNhap = new JLabel("Tên đăng nhập:");
        lblTenDangNhap.setFont(labelFont);
        formPanel.add(lblTenDangNhap, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtTenDangNhap = new JTextField();
        txtTenDangNhap.setFont(fieldFont);
        txtTenDangNhap.setPreferredSize(new Dimension(300, 35));
        txtTenDangNhap.setEnabled(false); // Không cho sửa tên đăng nhập
        txtTenDangNhap.setBackground(new Color(240, 240, 240));
        txtTenDangNhap.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtTenDangNhap, gbc);
        
        // Mật khẩu mới
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel lblMatKhau = new JLabel("Mật khẩu mới:");
        lblMatKhau.setFont(labelFont);
        formPanel.add(lblMatKhau, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(fieldFont);
        txtMatKhau.setPreferredSize(new Dimension(300, 35));
        txtMatKhau.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtMatKhau, gbc);
        
        // Xác nhận mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel lblXacNhanMatKhau = new JLabel("Xác nhận mật khẩu:");
        lblXacNhanMatKhau.setFont(labelFont);
        formPanel.add(lblXacNhanMatKhau, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtXacNhanMatKhau = new JPasswordField();
        txtXacNhanMatKhau.setFont(fieldFont);
        txtXacNhanMatKhau.setPreferredSize(new Dimension(300, 35));
        txtXacNhanMatKhau.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtXacNhanMatKhau, gbc);
        
        // Nhân viên
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        JLabel lblNhanVien = new JLabel("Nhân viên:");
        lblNhanVien.setFont(labelFont);
        formPanel.add(lblNhanVien, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtNhanVien = new JTextField();
        txtNhanVien.setFont(fieldFont);
        txtNhanVien.setPreferredSize(new Dimension(300, 35));
        txtNhanVien.setBackground(Color.WHITE);
        // Load danh sách nhân viên
        txtNhanVien.setText(taiKhoan.getNhanVien().getMaNV());
        txtNhanVien.setEditable(false);
        formPanel.add(txtNhanVien, gbc);
        
        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(labelFont);
        formPanel.add(lblTrangThai, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String[] trangThai = {"Hoạt động", "Ngừng hoạt động"};
        cboTrangThai = new JComboBox<>(trangThai);
        cboTrangThai.setFont(fieldFont);
        cboTrangThai.setPreferredSize(new Dimension(300, 35));
        cboTrangThai.setBackground(Color.WHITE);
        formPanel.add(cboTrangThai, gbc);
        
        // Vai trò
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        JLabel lblVaiTro = new JLabel("Vai trò:");
        lblVaiTro.setFont(labelFont);
        formPanel.add(lblVaiTro, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String[] vaiTro = {"Nhân viên quản lý", "Nhân viên bán thuốc"};
        cboVaiTro = new JComboBox<>(vaiTro);
        cboVaiTro.setFont(fieldFont);
        cboVaiTro.setPreferredSize(new Dimension(300, 35));
        cboVaiTro.setBackground(Color.WHITE);
        formPanel.add(cboVaiTro, gbc);
        
        // Note label
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 8, 5);
        JLabel lblNote = new JLabel("<html><i>* Để trống mật khẩu nếu không muốn thay đổi</i></html>");
        lblNote.setFont(new Font("Roboto", Font.ITALIC, 12));
        lblNote.setForeground(new Color(100, 100, 100));
        formPanel.add(lblNote, gbc);
        
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
        txtTenDangNhap.setText(taiKhoan.getTenDangNhap());
        
        // Load nhân viên
        String maNV = taiKhoan.getNhanVien().getMaNV();
        txtNhanVien.setText(maNV);
        
        cboTrangThai.setSelectedItem(taiKhoan.getTrangThai());
        cboVaiTro.setSelectedItem(taiKhoan.getVaiTro());
    }
    
    private void xuLyCapNhat() {
        // Validation
        if (!validateInput()) {
            return;
        }
        
        try {
            String tenDangNhap = txtTenDangNhap.getText().trim();
            String matKhau = new String(txtMatKhau.getPassword()).trim();
            String trangThai = cboTrangThai.getSelectedItem().toString();
            String vaiTro = cboVaiTro.getSelectedItem().toString();
           
            
            // Tìm nhân viên
            
            // Nếu không nhập mật khẩu mới, giữ nguyên mật khẩu cũ
            if (matKhau.isEmpty()) {
                matKhau = taiKhoan.getMatKhau();
            }
            
            TaiKhoan tkCapNhat = new TaiKhoan(tenDangNhap, matKhau, new NhanVien(taiKhoan.getNhanVien().getMaNV()), trangThai, vaiTro);
            
            // Cập nhật vào database
            boolean result = tkDAO.capNhatTaiKhoan(tkCapNhat);
            
            if (result) {
                JOptionPane.showMessageDialog(this, 
                    "Cập nhật tài khoản thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Reload table ở form chính
                parentForm.reloadTable();
                
                // Đóng dialog
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Cập nhật tài khoản thất bại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Có lỗi xảy ra: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateInput() {
        String matKhau = new String(txtMatKhau.getPassword()).trim();
        String xacNhanMatKhau = new String(txtXacNhanMatKhau.getPassword()).trim();
        
        // Nếu có nhập mật khẩu mới, phải validate
        if (!matKhau.isEmpty()) {
            // Kiểm tra định dạng mật khẩu
            if (!TaiKhoan.isValidPasswordFormat(matKhau)) {
                JOptionPane.showMessageDialog(this, 
                    "Mật khẩu phải trên 6 ký tự bao gồm: chữ cái, số, ký tự đặc biệt!", 
                    "Cảnh báo", 
                    JOptionPane.WARNING_MESSAGE);
                txtMatKhau.requestFocus();
                return false;
            }
            
            // Kiểm tra xác nhận mật khẩu
            if (!matKhau.equals(xacNhanMatKhau)) {
                JOptionPane.showMessageDialog(this, 
                    "Mật khẩu xác nhận không khớp!", 
                    "Cảnh báo", 
                    JOptionPane.WARNING_MESSAGE);
                txtXacNhanMatKhau.requestFocus();
                return false;
            }
        }
        
        
        return true;
    }
}
