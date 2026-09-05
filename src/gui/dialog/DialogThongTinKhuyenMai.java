package gui.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import entity.KhuyenMai;
import java.text.SimpleDateFormat;

public class DialogThongTinKhuyenMai extends JDialog {
    private KhuyenMai khuyenMai;
    
    public DialogThongTinKhuyenMai(Frame parent, KhuyenMai km) {
        super(parent, "Thông Tin Chi Tiết Khuyến Mãi", true);
        this.khuyenMai = km;
        
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(550, 450);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));

        // === Title Panel ===
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT KHUYẾN MÃI");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        // === Content Panel ===
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 15, 5); // Tăng khoảng cách dọc

        Font labelFont = new Font("Roboto", Font.BOLD, 15);
        Font valueFont = new Font("Roboto", Font.PLAIN, 15);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Mã khuyến mãi
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        contentPanel.add(createLabel("Mã khuyến mãi:", labelFont), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        contentPanel.add(createValueLabel(khuyenMai.getMaKM(), valueFont, new Color(0, 102, 204)), gbc);
        
        // Tên khuyến mãi
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(createLabel("Tên khuyến mãi:", labelFont), gbc);
        gbc.gridx = 1;
        contentPanel.add(createValueLabel(khuyenMai.getTenKM(), valueFont, Color.BLACK), gbc);

        // Ngày bắt đầu
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(createLabel("Ngày bắt đầu:", labelFont), gbc);
        gbc.gridx = 1;
        contentPanel.add(createValueLabel(sdf.format(khuyenMai.getNgayBatDau()), valueFont, Color.BLACK), gbc);

        // Ngày kết thúc
        gbc.gridx = 0; gbc.gridy = 3;
        contentPanel.add(createLabel("Ngày kết thúc:", labelFont), gbc);
        gbc.gridx = 1;
        contentPanel.add(createValueLabel(sdf.format(khuyenMai.getNgayKetThuc()), valueFont, Color.BLACK), gbc);

        // Phần trăm giảm giá
        gbc.gridx = 0; gbc.gridy = 4;
        contentPanel.add(createLabel("Mức giảm giá:", labelFont), gbc);
        gbc.gridx = 1;
        String phanTram = String.format("%.1f %%", khuyenMai.getPhanTramGiamGia());
        contentPanel.add(createValueLabel(phanTram, valueFont, new Color(204, 0, 0)), gbc);
        
        add(contentPanel, BorderLayout.CENTER);

        // === Button Panel ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(230, 245, 245));
        JButton btnDong = new JButton("ĐÓNG");
        btnDong.setFont(new Font("Roboto", Font.BOLD, 14));
        btnDong.setPreferredSize(new Dimension(140, 40));
        btnDong.setBackground(new Color(0, 0, 205));
        btnDong.setForeground(Color.WHITE);
        btnDong.addActionListener(e -> dispose());
        buttonPanel.add(btnDong);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JLabel createValueLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }
}