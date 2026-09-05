package gui.dialog;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import com.toedter.calendar.JDateChooser;

import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;
import gui.form.FormQuanLyTK;

public class DialogThemTaiKhoan extends JDialog {
    private JTextField txtTenDangNhap;
    private JTextField txtMatKhau;
    private JComboBox<String> cboTrangThai;
    private JComboBox<String> cboMaNhanVien;
    private JComboBox<String> cboVaiTro;
   
    private JButton btnXacNhan;
    private JButton btnHuy;
    
    private NhanVienDAO nvDao;
    private TaiKhoanDAO tkDAO;
    private FormQuanLyTK parentForm;
//    public static final String REGEX_MAT_KHAU = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$";

    public DialogThemTaiKhoan(Frame parent, FormQuanLyTK form) throws SQLException {
        super(parent, "Thêm tài khoản mới", true);
        this.parentForm = form;
        this.tkDAO = new TaiKhoanDAO();
        this.nvDao = new NhanVienDAO();
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() throws SQLException {
        setSize(600, 650);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(600, 60));
        JLabel lblTitle = new JLabel("THÊM TÀI KHOẢN MỚI");
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
        
        // Tên đăng nhập
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblTenDangNhap = new JLabel("Tên đăng nhập: *");
        lblTenDangNhap.setFont(labelFont);
        lblTenDangNhap.setForeground(Color.BLACK);
        formPanel.add(lblTenDangNhap, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtTenDangNhap = new JTextField();
        txtTenDangNhap.setFont(fieldFont);
        txtTenDangNhap.setPreferredSize(new Dimension(300, 35));
        txtTenDangNhap.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtTenDangNhap, gbc);
        row++;
        
        // Mật khẩu
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblMatKhau = new JLabel("Mật khẩu: *");
        lblMatKhau.setFont(labelFont);
        formPanel.add(lblMatKhau, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtMatKhau = new JTextField();
        txtMatKhau.setFont(fieldFont);
        txtMatKhau.setPreferredSize(new Dimension(300, 35));
        txtMatKhau.setBackground(Color.WHITE);
        formPanel.add(txtMatKhau, gbc);
        row++;
        
        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblSDT = new JLabel("Trạng thái: *");
        lblSDT.setFont(labelFont);
        formPanel.add(lblSDT, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String[] trangThai = {"Hoạt động", "Ngừng hoạt động"};
        cboTrangThai = new JComboBox<String>(trangThai);
        cboTrangThai.setFont(fieldFont);
        cboTrangThai.setPreferredSize(new Dimension(300, 35));
        cboTrangThai.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(cboTrangThai, gbc);
        row++;
        
        // Nhân viên
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        cboMaNhanVien = new JComboBox<String>();
        ArrayList<NhanVien> dsNhanVien = nvDao.getDSNhanVien();
        int count = 0;
        for (NhanVien nv : dsNhanVien) {
        	boolean isFound = false;
        	for (TaiKhoan tk : tkDAO.dsTaiKhoan()) {
        		if (nv.getMaNV().equals(tk.getNhanVien().getMaNV())) {
        			isFound = true;
        		}
        	}
        	if (!isFound) {
        		count++;
        		cboMaNhanVien.addItem(nv.getMaNV());
        	}
        }
        if (count == 0) {
        	cboMaNhanVien.addItem("Không tìm thấy mã nhân viên chưa có tài khoản");
        }
        JLabel lblMaNhanVien = new JLabel("Mã nhân viên: *");
        lblMaNhanVien.setFont(labelFont);
        formPanel.add(lblMaNhanVien, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cboMaNhanVien.setFont(fieldFont);
        cboMaNhanVien.setPreferredSize(new Dimension(300, 35));
        cboMaNhanVien.setBackground(Color.WHITE);
        formPanel.add(cboMaNhanVien, gbc);
        row++;
        
        // Giới tính
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblVaiTro = new JLabel("Vai trò: *");
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
        row++;
     
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
        btnXacNhan.addActionListener(e -> xyLyThemTaiKhoan());
        
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
    
    private void xyLyThemTaiKhoan() {
        if (!validateInput()) {
            return;
        }
        
        try {
            String tenDangNhap = txtTenDangNhap.getText().trim();
            String matKhau = txtMatKhau.getText().trim();
            String trangThai = (String) cboTrangThai.getSelectedItem();
            NhanVien nv = new NhanVien((String) cboMaNhanVien.getSelectedItem());
            String vaiTro = (String) cboVaiTro.getSelectedItem();
            TaiKhoan tk = new TaiKhoan(tenDangNhap, matKhau, nv, trangThai, vaiTro);
            boolean result = tkDAO.themTaiKhoan(tk);
            
            if (result) {
                JOptionPane.showMessageDialog(this, 
                    "Thêm tài khoản thành công", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                parentForm.reloadTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Thêm tài khoản thất bại!", 
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
        // Tên đăng nhập
        if (txtTenDangNhap.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập tên đăng nhập!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtTenDangNhap.requestFocus();
            return false;
        }
        
        // Mật khẩu
        String matKhau = txtMatKhau.getText().trim();
        if (matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Mật khẩu phải trên 6 ký tự bao gồm: chữ cái, số, ký tự đặc biệt", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtMatKhau.requestFocus();
            return false;
        }
        if (!TaiKhoan.isValidPasswordFormat(matKhau)) {
            JOptionPane.showMessageDialog(this,
                "Mật khẩu phải trên 6 ký tự bao gồm: chữ cái, số, ký tự đặc biệt",
                "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
            txtMatKhau.requestFocus();
            return false;
        }
        return true;
     }
}
