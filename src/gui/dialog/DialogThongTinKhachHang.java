package gui.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import entity.KhachHang;

public class DialogThongTinKhachHang extends JDialog {
    private KhachHang khachHang;
    
    public DialogThongTinKhachHang(Frame parent, KhachHang kh) {
        super(parent, "Thông tin chi tiết khách hàng", true);
        this.khachHang = kh;
        
        initComponents();
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
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT KHÁCH HÀNG");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 5, 12, 5);
        
        Font labelFont = new Font("Roboto", Font.BOLD, 15);
        Font valueFont = new Font("Roboto", Font.PLAIN, 15);
        
        // Avatar/Icon
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel lblAvatar = new JLabel();
        lblAvatar.setHorizontalAlignment(JLabel.CENTER);
        lblAvatar.setIcon(createAvatarIcon());
        contentPanel.add(lblAvatar, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(12, 5, 12, 5);
        
        // Mã khách hàng
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        JLabel lblMaKH = new JLabel("Mã khách hàng:");
        lblMaKH.setFont(labelFont);
        contentPanel.add(lblMaKH, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        JLabel valueMaKH = new JLabel(khachHang.getMaKH());
        valueMaKH.setFont(valueFont);
        valueMaKH.setForeground(new Color(0, 102, 204));
        contentPanel.add(valueMaKH, gbc);
        
        // Họ tên
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblHoTen = new JLabel("Họ và tên:");
        lblHoTen.setFont(labelFont);
        contentPanel.add(lblHoTen, gbc);
        
        gbc.gridx = 1;
        JLabel valueHoTen = new JLabel(khachHang.getHoTen());
        valueHoTen.setFont(valueFont);
        contentPanel.add(valueHoTen, gbc);
        
        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setFont(labelFont);
        contentPanel.add(lblSDT, gbc);
        
        gbc.gridx = 1;
        JLabel valueSDT = new JLabel(khachHang.getSoDienThoai());
        valueSDT.setFont(valueFont);
        contentPanel.add(valueSDT, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        contentPanel.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        JLabel valueEmail = new JLabel(khachHang.getEmail());
        valueEmail.setFont(valueFont);
        valueEmail.setForeground(new Color(0, 102, 204));
        contentPanel.add(valueEmail, gbc);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 245, 245));
        buttonPanel.setPreferredSize(new Dimension(500, 70));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        JButton btnDong = new JButton("ĐÓNG");
        btnDong.setFont(new Font("Roboto", Font.BOLD, 14));
        btnDong.setPreferredSize(new Dimension(140, 40));
        btnDong.setBackground(new Color(0, 0, 205));
        btnDong.setForeground(Color.WHITE);
        btnDong.setFocusPainted(false);
        btnDong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDong.setBorder(new LineBorder(new Color(0, 0, 205), 2, true));
        btnDong.addActionListener(e -> dispose());
        
        buttonPanel.add(btnDong);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private Icon createAvatarIcon() {
        // Tạo icon đơn giản
        int size = 80;
        Color color = new Color(255, 153, 51); // Màu cam cho khách hàng
        
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Vẽ vòng tròn nền
                g2d.setColor(color);
                g2d.fillOval(x, y, size, size);
                
                // Vẽ viền
                g2d.setColor(color.darker());
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(x, y, size, size);
                
                // Vẽ icon người
                g2d.setColor(Color.WHITE);
                // Đầu
                g2d.fillOval(x + size/2 - 12, y + 15, 24, 24);
                // Thân
                g2d.fillOval(x + size/2 - 20, y + 40, 40, 35);
                
                g2d.dispose();
            }
            
            @Override
            public int getIconWidth() {
                return size;
            }
            
            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }
}