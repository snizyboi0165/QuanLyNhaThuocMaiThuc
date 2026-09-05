package gui.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import entity.NhaCungCap;

public class DialogThongTinNhaCungCap extends JDialog {
    
    public DialogThongTinNhaCungCap(Frame parent, NhaCungCap ncc) {
        super(parent, "Thông tin chi tiết nhà cung cấp", true);
        
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblTitle = new JLabel("THÔNG TIN NHÀ CUNG CẤP");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 10, 5);
        Font labelFont = new Font("Roboto", Font.BOLD, 15);
        Font valueFont = new Font("Roboto", Font.PLAIN, 15);

        // Dữ liệu
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMa = new JLabel("Mã nhà cung cấp:");
        lblMa.setFont(labelFont);
        contentPanel.add(lblMa, gbc);
        gbc.gridx = 1;
        JLabel valMa = new JLabel(ncc.getMaNCC());
        valMa.setFont(valueFont);
        contentPanel.add(valMa, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblTen = new JLabel("Tên nhà cung cấp:");
        lblTen.setFont(labelFont);
        contentPanel.add(lblTen, gbc);
        gbc.gridx = 1;
        JLabel valTen = new JLabel(ncc.getTenNCC());
        valTen.setFont(valueFont);
        contentPanel.add(valTen, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setFont(labelFont);
        contentPanel.add(lblSDT, gbc);
        gbc.gridx = 1;
        JLabel valSDT = new JLabel(ncc.getSoDienThoai());
        valSDT.setFont(valueFont);
        contentPanel.add(valSDT, gbc);

        add(contentPanel, BorderLayout.CENTER);
        
        // Close Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(230, 245, 245));
        JButton btnDong = new JButton("ĐÓNG");
        btnDong.setPreferredSize(new Dimension(140, 40));
        btnDong.addActionListener(e -> dispose());
        buttonPanel.add(btnDong);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
