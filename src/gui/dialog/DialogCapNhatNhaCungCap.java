package gui.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import dao.NhaCungCapDAO;
import entity.NhaCungCap;
import gui.form.FormQuanLyNhaCungCap;

import java.sql.SQLException;

public class DialogCapNhatNhaCungCap extends JDialog {
    private JTextField txtMaNCC;
    private JTextField txtTenNCC;
    private JTextField txtSoDienThoai;
    private JButton btnCapNhat;
    private JButton btnHuy;

    private NhaCungCapDAO nccDAO;
    private FormQuanLyNhaCungCap parentForm;
    private NhaCungCap ncc;

    public DialogCapNhatNhaCungCap(Frame parent, FormQuanLyNhaCungCap form, NhaCungCap ncc) {
        super(parent, "Cập nhật thông tin nhà cung cấp", true);
        this.parentForm = form;
        this.nccDAO = new NhaCungCapDAO();
        this.ncc = ncc;

        initComponents();
        loadDataToForm();
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
        JLabel lblTitle = new JLabel("CẬP NHẬT NHÀ CUNG CẤP");
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

        // Mã NCC
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Mã nhà cung cấp:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtMaNCC = new JTextField(20);
        txtMaNCC.setEnabled(false);
        formPanel.add(txtMaNCC, gbc);

        // Tên NCC
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên nhà cung cấp: *"), gbc);
        gbc.gridx = 1;
        txtTenNCC = new JTextField(20);
        formPanel.add(txtTenNCC, gbc);
        
        // SDT
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Số điện thoại: *"), gbc);
        gbc.gridx = 1;
        txtSoDienThoai = new JTextField(20);
        formPanel.add(txtSoDienThoai, gbc);
        
        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(230, 245, 245));
        btnCapNhat = new JButton("CẬP NHẬT");
        btnHuy = new JButton("HỦY");
        btnCapNhat.setPreferredSize(new Dimension(140, 40));
        btnCapNhat.setFont(new Font("Roboto", Font.BOLD, 14));
        btnCapNhat.setBackground(new Color(0, 0, 205));
        btnCapNhat.setForeground(Color.WHITE);
        btnCapNhat.setFocusPainted(false);
        btnHuy.setPreferredSize(new Dimension(140, 40));
        btnHuy.setFont(new Font("Roboto", Font.BOLD, 14));
        btnHuy.setFocusPainted(false);
        buttonPanel.add(btnCapNhat);
        buttonPanel.add(btnHuy);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        btnHuy.addActionListener(e -> dispose());
        btnCapNhat.addActionListener(e -> xuLyCapNhatNCC());
    }

    private void loadDataToForm() {
        txtMaNCC.setText(ncc.getMaNCC());
        txtTenNCC.setText(ncc.getTenNCC());
        txtSoDienThoai.setText(ncc.getSoDienThoai());
    }

    private void xuLyCapNhatNCC() {
        String ten = txtTenNCC.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();

        if (ten.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên và số điện thoại không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            NhaCungCap nccCapNhat = new NhaCungCap(txtMaNCC.getText(), ten, sdt);

            if (nccDAO.capNhatNhaCungCap(nccCapNhat)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                parentForm.reloadTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật cơ sở dữ liệu!", "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
