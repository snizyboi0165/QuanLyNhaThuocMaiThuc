package gui.dialog;

import entity.Thuoc;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.*;

public class DialogChiTietThuoc extends JDialog {
    
    private final Thuoc thuoc;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    
    public DialogChiTietThuoc(Frame parent, Thuoc t) {
        super(parent, "Thông tin chi tiết thuốc", true);
        
        if (t == null) {
            throw new IllegalArgumentException("Thuoc cannot be null");
        }
        
        this.thuoc = t;
        
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(700, 750);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(700, 60));
        
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT THUỐC");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        contentPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 10, 12, 10);
        
        Font labelFont = new Font("Roboto", Font.BOLD, 15);
        Font valueFont = new Font("Roboto", Font.PLAIN, 14);
        
        int row = 0;
        
        // Mã thuốc
        addInfoRow(contentPanel, gbc, row++, "Mã thuốc:", thuoc.getMaThuoc(), labelFont, valueFont);
        
        // Tên thuốc
        addInfoRow(contentPanel, gbc, row++, "Tên thuốc:", thuoc.getTenThuoc(), labelFont, valueFont);
        
        // Đơn vị tính
        addInfoRow(contentPanel, gbc, row++, "Đơn vị tính:", thuoc.getDonViTinh(), labelFont, valueFont);
        
        // Giá bán
        String giaBan = df.format(thuoc.getGiaBan()) + " đ";
        addInfoRow(contentPanel, gbc, row++, "Giá bán:", giaBan, labelFont, valueFont);
        
        // Số lượng tồn
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblSoLuong = new JLabel("Số lượng tồn:");
        lblSoLuong.setFont(labelFont);
        contentPanel.add(lblSoLuong, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String soLuongStr = String.valueOf(thuoc.getSoLuongTon());
        JLabel lblSoLuongValue = new JLabel(soLuongStr);
        lblSoLuongValue.setFont(valueFont);
        
        // Màu sắc theo số lượng
        if (thuoc.getSoLuongTon() <= 0) {
            lblSoLuongValue.setForeground(Color.RED); // Hết hàng
        } else if (thuoc.getSoLuongTon() < 10) {
            lblSoLuongValue.setForeground(new Color(255, 152, 0)); // Sắp hết
        } else {
            lblSoLuongValue.setForeground(new Color(46, 125, 50)); // Còn hàng
        }
        contentPanel.add(lblSoLuongValue, gbc);
        row++;
        
        // Hạn sử dụng
        String hanSuDung = thuoc.getHanSuDung() != null ? 
            sdf.format(thuoc.getHanSuDung()) : "Chưa cập nhật";
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lblHanSuDung = new JLabel("Hạn sử dụng:");
        lblHanSuDung.setFont(labelFont);
        contentPanel.add(lblHanSuDung, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel lblHanSuDungValue = new JLabel(hanSuDung);
        lblHanSuDungValue.setFont(valueFont);
        
        // Kiểm tra hạn sử dụng
        if (thuoc.getHanSuDung() != null) {
            java.util.Date today = new java.util.Date();
            if (thuoc.getHanSuDung().before(today)) {
                lblHanSuDungValue.setForeground(Color.RED); // Hết hạn
            }
        }
        contentPanel.add(lblHanSuDungValue, gbc);
        row++;
        
        // Ngày sản xuất
        String ngaySanXuat = thuoc.getNgaySanXuat() != null ? 
            sdf.format(thuoc.getNgaySanXuat()) : "Chưa cập nhật";
        addInfoRow(contentPanel, gbc, row++, "Ngày sản xuất:", ngaySanXuat, labelFont, valueFont);
        
        // Xuất xứ
        addInfoRow(contentPanel, gbc, row++, "Xuất xứ:", thuoc.getXuatXu(), labelFont, valueFont);
        
        // Danh mục
        String danhMuc = thuoc.getDanhMucThuoc() != null ? 
            thuoc.getDanhMucThuoc().getMaDanhMuc() : "Chưa cập nhật";
        addInfoRow(contentPanel, gbc, row++, "Danh mục:", danhMuc, labelFont, valueFont);
        
        // Thành phần
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel lblThanhPhan = new JLabel("Thành phần:");
        lblThanhPhan.setFont(labelFont);
        contentPanel.add(lblThanhPhan, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        JTextArea txtThanhPhan = new JTextArea(thuoc.getThanhPhan());
        txtThanhPhan.setFont(valueFont);
        txtThanhPhan.setEditable(false);
        txtThanhPhan.setLineWrap(true);
        txtThanhPhan.setWrapStyleWord(true);
        txtThanhPhan.setBackground(new Color(245, 245, 245));
        txtThanhPhan.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        JScrollPane scrollThanhPhan = new JScrollPane(txtThanhPhan);
        scrollThanhPhan.setPreferredSize(new Dimension(300, 60));
        contentPanel.add(scrollThanhPhan, gbc);
        row++;
        
        // Mô tả
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel lblMoTa = new JLabel("Mô tả:");
        lblMoTa.setFont(labelFont);
        contentPanel.add(lblMoTa, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JTextArea txtMoTa = new JTextArea(thuoc.getMoTa());
        txtMoTa.setFont(valueFont);
        txtMoTa.setEditable(false);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setBackground(new Color(245, 245, 245));
        txtMoTa.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        scrollMoTa.setPreferredSize(new Dimension(300, 60));
        contentPanel.add(scrollMoTa, gbc);
        
        JScrollPane mainScroll = new JScrollPane(contentPanel);
        add(mainScroll, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(230, 245, 245));
        
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
    
    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, 
                           String labelText, String valueText, Font labelFont, Font valueFont) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        
        JLabel value = new JLabel(valueText);
        value.setFont(valueFont);
        panel.add(value, gbc);
    }
}