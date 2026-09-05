package gui.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import dao.KhachHangDAO;
import entity.KhachHang;
import gui.form.FormQuanLyKhachHang;

public class DialogThemKhachHang extends JDialog {
    private JTextField txtHoTen;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    
    private JButton btnXacNhan;
    private JButton btnHuy;
    
    private KhachHangDAO khDAO;
    private FormQuanLyKhachHang parentForm;
    private String soDienThoaiMacDinh;
    
    public DialogThemKhachHang(Frame parent, FormQuanLyKhachHang form) {
        this(parent, form, "");
    }

    public DialogThemKhachHang(Frame parent, FormQuanLyKhachHang form, String soDienThoaiMacDinh) {
        super(parent, "Thêm khách hàng mới", true);
        this.parentForm = form;
        this.khDAO = new KhachHangDAO();
        this.soDienThoaiMacDinh = soDienThoaiMacDinh == null ? "" : soDienThoaiMacDinh.trim();
        
        initComponents();
        ganSoDienThoaiMacDinh();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(500, 400);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblTitle = new JLabel("THÊM KHÁCH HÀNG MỚI");
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
        JLabel lblHoTen = new JLabel("Họ và tên: *");
        lblHoTen.setFont(labelFont);
        lblHoTen.setForeground(Color.BLACK);
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
        JLabel lblNote = new JLabel("* Mã khách hàng sẽ được tự động phát sinh");
        lblNote.setFont(new Font("Roboto", Font.ITALIC, 12));
        lblNote.setForeground(new Color(100, 100, 100));
        formPanel.add(lblNote, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 245, 245));
        buttonPanel.setPreferredSize(new Dimension(500, 80));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        btnXacNhan = new JButton("XÁC NHẬN");
        btnXacNhan.setFont(new Font("Roboto", Font.BOLD, 14));
        btnXacNhan.setPreferredSize(new Dimension(140, 40));
        btnXacNhan.setBackground(new Color(0, 0, 205));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXacNhan.setBorder(new LineBorder(new Color(0, 0, 205), 2, true));
        btnXacNhan.addActionListener(e -> xuLyThemKhachHang());
        
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

    private void ganSoDienThoaiMacDinh() {
        if (!soDienThoaiMacDinh.isEmpty()) {
            txtSoDienThoai.setText(soDienThoaiMacDinh);
            txtHoTen.requestFocusInWindow();
        }
    }
    
    private void xuLyThemKhachHang() {
        if (!validateInput()) {
            return;
        }
        
        try {
            String maKH = khDAO.generateMaKH();
            String hoTen = txtHoTen.getText().trim();
            String soDienThoai = txtSoDienThoai.getText().trim();
            String email = txtEmail.getText().trim();
            
            KhachHang khDaTonTai = khDAO.getKhachHangTheoSDTBaoGomDaAn(soDienThoai);
            if (khDaTonTai != null) {
                if (khDAO.khachHangDangHoatDong(khDaTonTai.getMaKH())) {
                    JOptionPane.showMessageDialog(this, 
                        "Số điện thoại này đã được đăng ký cho khách hàng khác!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                    txtSoDienThoai.requestFocus();
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this,
                    "Khách hàng đã từng tồn tại, bạn có muốn khôi phục khách hàng này?",
                    "Khôi phục khách hàng",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    KhachHang khKhoiPhuc = new KhachHang(khDaTonTai.getMaKH(), hoTen, soDienThoai, email);
                    boolean restored = khDAO.khoiPhucKhachHang(khKhoiPhuc);
                    if (restored) {
                        JOptionPane.showMessageDialog(this, 
                            "Khôi phục khách hàng thành công!\nMã khách hàng: " + khDaTonTai.getMaKH(), 
                            "Thành công", 
                            JOptionPane.INFORMATION_MESSAGE);
                        parentForm.reloadTable();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Khôi phục khách hàng thất bại!", 
                            "Lỗi", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
                return;
            }
            
            KhachHang kh = new KhachHang(maKH, hoTen, soDienThoai, email);
            
            boolean result = khDAO.themKhachHang(kh);
            
            if (result) {
                JOptionPane.showMessageDialog(this, 
                    "Thêm khách hàng thành công!\nMã khách hàng: " + maKH, 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                parentForm.reloadTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Thêm khách hàng thất bại!", 
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
        if (txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập họ và tên khách hàng!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtHoTen.requestFocus();
            return false;
        }

        if (!laTenKhachHangHopLe(txtHoTen.getText().trim())) {
            JOptionPane.showMessageDialog(this,
                "Họ và tên chỉ được chứa chữ cái và khoảng trắng.",
                "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
            txtHoTen.requestFocus();
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

    private boolean laTenKhachHangHopLe(String tenKhachHang) {
        return tenKhachHang != null && tenKhachHang.trim().matches("[\\p{L}\\s]+");
    }
}
