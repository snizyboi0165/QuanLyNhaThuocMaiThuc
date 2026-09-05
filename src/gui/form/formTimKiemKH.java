package gui.form;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.KhachHangDAO;
import entity.KhachHang;
import gui.dialog.DialogChiTietKH;

public class FormTimKiemKH extends JPanel 
{
    // Search Panel Components
    private JTextField txtMaKH;
    private JTextField txtTenKH;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    
    // Button Components
    private JButton btnTimKiem;
    private JButton btnLamMoi;
    private JButton btnXuatExcel;
    private JButton btnXemChiTiet;
    
    // Table Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JLabel lblKetQua;
    
    // DAO
    private KhachHangDAO khachHangDAO;
    
    // Fonts
    private Font labelFont = new Font("Roboto", Font.PLAIN, 14);
    private Font fieldFont = new Font("Roboto", Font.PLAIN, 14);
    private Font titleFont = new Font("Roboto", Font.BOLD, 24);
    private Font headerTableFont = new Font("Roboto", Font.BOLD, 16);
    
    public FormTimKiemKH() {
        khachHangDAO = new KhachHangDAO();
        initComponents();
        hienThiTatCaKhachHang();
    }
    
    private void initComponents() {
        setBackground(new Color(230, 245, 245));
        setBorder(new LineBorder(new Color(230, 245, 245), 6, true));
        setMinimumSize(new Dimension(1130, 800));
        setPreferredSize(new Dimension(1130, 800));
        setLayout(new BorderLayout(0, 10));
        
        // Header Panel
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Center Panel (Search Form + Table)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(new Color(230, 245, 245));
        centerPanel.add(createSearchPanel(), BorderLayout.NORTH);
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * Tạo header panel với tiêu đề
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(1130, 70));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel lblTitle = new JLabel("TÌM KIẾM KHÁCH HÀNG");
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblIcon = new JLabel(new FlatSVGIcon(getClass().getResource("/img/search.svg")));
        lblIcon.setBorder(new EmptyBorder(0, 20, 0, 0));
        
        headerPanel.add(lblIcon, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Tạo panel tìm kiếm với tiêu chí
     */
    private JPanel createSearchPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel lblSearchTitle = new JLabel("Tiêu chí tìm kiếm");
        lblSearchTitle.setFont(new Font("Roboto", Font.BOLD, 18));
        lblSearchTitle.setForeground(new Color(0, 0, 205));
        lblSearchTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        mainPanel.add(lblSearchTitle, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        // Row 1: Mã KH, Tên KH, Số điện thoại, Email
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.15;
        formPanel.add(createLabel("Mã khách hàng:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.25;
        txtMaKH = createTextField();
        formPanel.add(txtMaKH, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.15;
        formPanel.add(createLabel("Họ tên:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.25;
        txtTenKH = createTextField();
        formPanel.add(txtTenKH, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0.15;
        formPanel.add(createLabel("Số điện thoại:"), gbc);
        
        gbc.gridx = 5; gbc.weightx = 0.25;
        txtSoDienThoai = createTextField();
        formPanel.add(txtSoDienThoai, gbc);
        
        // Row 2: Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.15;
        formPanel.add(createLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.25;
        txtEmail = createTextField();
        formPanel.add(txtEmail, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Tạo panel chứa các nút chức năng
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        
        // Nút Tìm kiếm
        btnTimKiem = new JButton("TÌM KIẾM");
        btnTimKiem.setFont(new Font("Roboto", Font.BOLD, 14));
        btnTimKiem.setPreferredSize(new Dimension(150, 40));
        btnTimKiem.setBackground(new Color(0, 0, 205));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTimKiem.setBorder(new LineBorder(new Color(0, 0, 205), 2, true));
        btnTimKiem.setIcon(new FlatSVGIcon(getClass().getResource("/img/search.svg")));
        btnTimKiem.addActionListener(e -> xuLyTimKiem());
        
        // Nút Làm mới
        btnLamMoi = new JButton("LÀM MỚI");
        btnLamMoi.setFont(new Font("Roboto", Font.BOLD, 14));
        btnLamMoi.setPreferredSize(new Dimension(150, 40));
        btnLamMoi.setBackground(Color.WHITE);
        btnLamMoi.setForeground(new Color(100, 100, 100));
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLamMoi.setBorder(new LineBorder(new Color(200, 200, 200), 2, true));
        btnLamMoi.setIcon(new FlatSVGIcon(getClass().getResource("/img/reload.svg")));
        btnLamMoi.addActionListener(e -> lamMoi());
        
        // Nút Xuất Excel
        btnXuatExcel = new JButton("XUẤT EXCEL");
        btnXuatExcel.setFont(new Font("Roboto", Font.BOLD, 14));
        btnXuatExcel.setPreferredSize(new Dimension(150, 40));
        btnXuatExcel.setBackground(new Color(46, 125, 50));
        btnXuatExcel.setForeground(Color.WHITE);
        btnXuatExcel.setFocusPainted(false);
        btnXuatExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXuatExcel.setBorder(new LineBorder(new Color(46, 125, 50), 2, true));
        btnXuatExcel.setIcon(new FlatSVGIcon(getClass().getResource("/img/excel.svg")));
        btnXuatExcel.addActionListener(e -> xuatExcel());
        
        buttonPanel.add(btnTimKiem);
        buttonPanel.add(btnLamMoi);
        buttonPanel.add(btnXuatExcel);
        
        return buttonPanel;
    }
    
    /**
     * Tạo panel hiển thị bảng kết quả
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(new Color(230, 245, 245));
        
        // Result Info Panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        resultPanel.setPreferredSize(new Dimension(1130, 50));
        
        lblKetQua = new JLabel("Tìm thấy 0 kết quả");
        lblKetQua.setFont(new Font("Roboto", Font.BOLD, 16));
        lblKetQua.setForeground(new Color(0, 0, 205));
        lblKetQua.setBorder(new EmptyBorder(10, 20, 10, 10));
        
        btnXemChiTiet = new JButton("XEM CHI TIẾT");
        btnXemChiTiet.setFont(new Font("Roboto", Font.BOLD, 13));
        btnXemChiTiet.setPreferredSize(new Dimension(140, 35));
        btnXemChiTiet.setBackground(new Color(33, 150, 243));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.setFocusPainted(false);
        btnXemChiTiet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXemChiTiet.setBorder(new LineBorder(new Color(33, 150, 243), 2, true));
        btnXemChiTiet.addActionListener(e -> xemChiTiet());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 7));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnXemChiTiet);
        
        resultPanel.add(lblKetQua, BorderLayout.WEST);
        resultPanel.add(btnPanel, BorderLayout.EAST);
        
        tablePanel.add(resultPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Mã KH", "Họ tên", "Số điện thoại", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Roboto", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setFont(headerTableFont);
        table.getTableHeader().setBackground(new Color(0, 0, 205));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Center align
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(300);
        
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    /**
     * Tạo label với font chuẩn
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(labelFont);
        return label;
    }
    
    /**
     * Tạo text field với style chuẩn
     */
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(fieldFont);
        textField.setPreferredSize(new Dimension(200, 35));
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }
    
    /**
     * Xử lý tìm kiếm khách hàng
     */
    private void xuLyTimKiem() {
        try {
            // Lấy các tiêu chí tìm kiếm
            String maKH = txtMaKH.getText().trim();
            String tenKH = txtTenKH.getText().trim();
            String soDienThoai = txtSoDienThoai.getText().trim();
            String email = txtEmail.getText().trim();
            
            // Tìm kiếm
            ArrayList<KhachHang> ketQua = khachHangDAO.timKiemKhachHang(
                maKH, tenKH, soDienThoai, email
            );
            
            // Hiển thị kết quả
            hienThiKetQua(ketQua);
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tìm kiếm: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Hiển thị kết quả tìm kiếm lên bảng
     */
    private void hienThiKetQua(ArrayList<KhachHang> dsKH) {
        tableModel.setRowCount(0);
        
        for (KhachHang kh : dsKH) {
            tableModel.addRow(new Object[]{
                kh.getMaKH(),
                kh.getHoTen(),
                kh.getSoDienThoai(),
                kh.getEmail() != null ? kh.getEmail() : ""
            });
        }
        
        lblKetQua.setText("Tìm thấy " + dsKH.size() + " kết quả");
        
        if (dsKH.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy khách hàng nào phù hợp!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Làm mới form
     */
    private void lamMoi() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        
        tableModel.setRowCount(0);
        lblKetQua.setText("Tìm thấy 0 kết quả");
        
        hienThiTatCaKhachHang();
    }
    
    /**
     * Xem chi tiết khách hàng
     */
    private void xemChiTiet() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn khách hàng cần xem!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maKH = tableModel.getValueAt(selectedRow, 0).toString();
        String hoTen = tableModel.getValueAt(selectedRow, 1).toString();
        String soDienThoai = tableModel.getValueAt(selectedRow, 2).toString();
        String email = tableModel.getValueAt(selectedRow, 3).toString();
        
        try {
            KhachHang kh = khachHangDAO.getKhachHangTheoMa(maKH);
            
            if (kh != null) {
                // Hiển thị dialog chi tiết
                DialogChiTietKH dialog = new DialogChiTietKH(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    kh
                );
                dialog.setVisible(true);
            } else {
                // Nếu không tìm thấy, tạo object từ dữ liệu bảng
                kh = new KhachHang();
                kh.setMaKH(maKH);
                kh.setHoTen(hoTen);
                kh.setSoDienThoai(soDienThoai);
                kh.setEmail(email);
                
                DialogChiTietKH dialog = new DialogChiTietKH(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    kh
                );
                dialog.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi lấy thông tin: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xuatExcel() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "Không có dữ liệu để xuất!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this,
            "Chức năng xuất Excel đang được phát triển!",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
        
        // TODO: Implement Excel export
    }
    
    /**
     * Hiển thị toàn bộ danh sách khách hàng khi mở form
     */
    private void hienThiTatCaKhachHang() {
        try {
            ArrayList<KhachHang> dsKH = khachHangDAO.getDSKhachHang();
            hienThiKetQua(dsKH);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách khách hàng: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
