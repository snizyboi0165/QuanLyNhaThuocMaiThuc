package gui.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import entity.Thue;

public class DialogThongTinThue extends JDialog {
    
    public DialogThongTinThue(Frame parent, Thue thue) {
        super(parent, "Thông Tin Chi Tiết Thuế", true);
        initComponents(thue);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents(Thue thue) {
        setSize(500, 350);
        setResizable(false);
        setLocationRelativeTo(getParent());
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT THUẾ");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 15, 5);

        Font labelFont = new Font("Roboto", Font.BOLD, 15);
        Font valueFont = new Font("Roboto", Font.PLAIN, 15);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        contentPanel.add(new JLabel("Mã thuế:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        JLabel valueMaThue = new JLabel(thue.getMaThue());
        valueMaThue.setFont(valueFont);
        valueMaThue.setForeground(new Color(0, 102, 204));
        contentPanel.add(valueMaThue, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(new JLabel("Tên thuế:"), gbc);
        gbc.gridx = 1;
        JLabel valueTenThue = new JLabel(thue.getTenThue());
        valueTenThue.setFont(valueFont);
        contentPanel.add(valueTenThue, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(new JLabel("Mức thuế:"), gbc);
        gbc.gridx = 1;
        JLabel valueMucThue = new JLabel(String.format("%.1f %%", thue.getPhanTramThue()));
        valueMucThue.setFont(valueFont);
        valueMucThue.setForeground(new Color(204, 0, 0));
        contentPanel.add(valueMucThue, gbc);
        
        add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(230, 245, 245));
        
        JButton btnDong = new JButton("ĐÓNG");
        btnDong.setFont(new Font("Roboto", Font.BOLD, 14));
        btnDong.setPreferredSize(new Dimension(140, 40));
        btnDong.setBackground(new Color(0, 0, 205));
        btnDong.setForeground(Color.WHITE);
        btnDong.setFocusPainted(false);
        btnDong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDong.setBorder(new LineBorder(new Color(0, 0, 205).darker(), 1, true));
        btnDong.addActionListener(e -> dispose());
        
        buttonPanel.add(btnDong);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}