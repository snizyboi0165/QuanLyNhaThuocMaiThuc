package gui.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import dao.NhaCungCapDAO;
import entity.NhaCungCap;
import gui.form.FormQuanLyNhaCungCap;

import java.sql.SQLException;

public class DialogThemNhaCungCap extends JDialog {
    private JTextField txtTenNCC;
    private JTextField txtSoDienThoai;
    private JButton btnXacNhan;
    private JButton btnHuy;
    
    private NhaCungCapDAO nccDAO;
    private FormQuanLyNhaCungCap parentForm;
    
    public DialogThemNhaCungCap(Frame parent, FormQuanLyNhaCungCap form) {
        super(parent, "Thêm nhà cung cấp mới", true);
        this.parentForm = form;
        this.nccDAO = new NhaCungCapDAO();
        
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(500, 450);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblTitle = new JLabel("THÊM NHÀ CUNG CẤP");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);
        
        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);
        Font labelFont = new Font("Roboto", Font.PLAIN, 14);
        
        // Tên NCC
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Tên nhà cung cấp: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtTenNCC = new JTextField(20);
        formPanel.add(txtTenNCC, gbc);
        
        // SDT
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Số điện thoại: *"), gbc);
        gbc.gridx = 1;
        txtSoDienThoai = new JTextField(20);
        formPanel.add(txtSoDienThoai, gbc);
        
        // Ghi chú
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JLabel lblNote = new JLabel("* Mã NCC sẽ được tự động phát sinh");
        lblNote.setFont(new Font("Roboto", Font.ITALIC, 12));
        formPanel.add(lblNote, gbc);

        add(formPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(230, 245, 245));
        btnXacNhan = new JButton("XÁC NHẬN");
        btnHuy = new JButton("HỦY");
        btnXacNhan.setPreferredSize(new Dimension(140, 40));
        btnXacNhan.setFont(new Font("Roboto", Font.BOLD, 14));
        btnXacNhan.setBackground(new Color(0, 0, 205));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        btnHuy.setPreferredSize(new Dimension(140, 40));
        btnHuy.setFont(new Font("Roboto", Font.BOLD, 14));
        btnHuy.setFocusPainted(false);
        buttonPanel.add(btnXacNhan);
        buttonPanel.add(btnHuy);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Actions
        btnHuy.addActionListener(e -> dispose());
        btnXacNhan.addActionListener(e -> xuLyThemNCC());
    }
    
    private void xuLyThemNCC() {
        String ten = txtTenNCC.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        
        if (ten.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên và số điện thoại không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String maNCC = nccDAO.generateMaNCC();
            NhaCungCap ncc = new NhaCungCap(maNCC, ten, sdt);
            
            if (nccDAO.themNhaCungCap(ncc)) {
                JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!\nMã NCC: " + maNCC, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                parentForm.reloadTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm vào cơ sở dữ liệu!", "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
