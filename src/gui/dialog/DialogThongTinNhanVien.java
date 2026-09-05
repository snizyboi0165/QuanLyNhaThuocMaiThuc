package gui.dialog;

import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import entity.NhanVien;

public class DialogThongTinNhanVien extends JDialog {
    private NhanVien nhanVien;
    
    public DialogThongTinNhanVien(Frame parent, NhanVien nv) {
        super(parent, "Thông tin chi tiết nhân viên", true);
        this.nhanVien = nv;
        
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
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT NHÂN VIÊN");
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
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        // Avatar/Icon
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel lblAvatar = new JLabel();
        lblAvatar.setHorizontalAlignment(JLabel.CENTER);
        lblAvatar.setIcon(createAvatarIcon(nhanVien.getGioiTinh()));
        contentPanel.add(lblAvatar, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(12, 5, 12, 5);
        
        // Mã nhân viên
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        JLabel lblMaNV = new JLabel("Mã nhân viên:");
        lblMaNV.setFont(labelFont);
        contentPanel.add(lblMaNV, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        JLabel valueMaNV = new JLabel(nhanVien.getMaNV());
        valueMaNV.setFont(valueFont);
        valueMaNV.setForeground(new Color(0, 102, 204));
        contentPanel.add(valueMaNV, gbc);
        
        // Họ tên
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblTenNV = new JLabel("Họ và tên:");
        lblTenNV.setFont(labelFont);
        contentPanel.add(lblTenNV, gbc);
        
        gbc.gridx = 1;
        JLabel valueTenNV = new JLabel(nhanVien.getTenNV());
        valueTenNV.setFont(valueFont);
        contentPanel.add(valueTenNV, gbc);
        
        // Chức vụ
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblChucVu = new JLabel("Chức vụ:");
        lblChucVu.setFont(labelFont);
        contentPanel.add(lblChucVu, gbc);
        
        gbc.gridx = 1;
        JLabel valueChucVu = new JLabel(nhanVien.getChucVu());
        valueChucVu.setFont(valueFont);
        valueChucVu.setForeground(new Color(0, 0, 205));
        contentPanel.add(valueChucVu, gbc);
        
        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setFont(labelFont);
        contentPanel.add(lblSDT, gbc);
        
        gbc.gridx = 1;
        JLabel valueSDT = new JLabel(nhanVien.getSoDienThoai());
        valueSDT.setFont(valueFont);
        contentPanel.add(valueSDT, gbc);
        
        // Ngày sinh
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(labelFont);
        contentPanel.add(lblNgaySinh, gbc);
        
        gbc.gridx = 1;
        JLabel valueNgaySinh = new JLabel(
            nhanVien.getNgaySinh() != null ? sdf.format(nhanVien.getNgaySinh()) : "Chưa cập nhật"
        );
        valueNgaySinh.setFont(valueFont);
        contentPanel.add(valueNgaySinh, gbc);
        
        // Giới tính
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setFont(labelFont);
        contentPanel.add(lblGioiTinh, gbc);
        
        gbc.gridx = 1;
        JLabel valueGioiTinh = new JLabel(nhanVien.getGioiTinh());
        valueGioiTinh.setFont(valueFont);
        contentPanel.add(valueGioiTinh, gbc);
        
        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setFont(labelFont);
        contentPanel.add(lblDiaChi, gbc);
        
        gbc.gridx = 1;
        JLabel valueDiaChi = new JLabel("<html>" + nhanVien.getDiaChi() + "</html>");
        valueDiaChi.setFont(valueFont);
        contentPanel.add(valueDiaChi, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 8;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        contentPanel.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        JLabel valueEmail = new JLabel(nhanVien.getEmail());
        valueEmail.setFont(valueFont);
        valueEmail.setForeground(new Color(0, 102, 204));
        contentPanel.add(valueEmail, gbc);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 245, 245));
        buttonPanel.setPreferredSize(new Dimension(600, 70));
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
    
    private Icon createAvatarIcon(String gioiTinh) {
        // Tạo icon đơn giản dựa trên giới tính
        int size = 80;
        Color color = gioiTinh.equals("Nam") ? new Color(100, 149, 237) : new Color(255, 182, 193);
        
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