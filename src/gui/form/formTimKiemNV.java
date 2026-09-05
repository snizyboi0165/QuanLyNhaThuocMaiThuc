package gui.form;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.toedter.calendar.JDateChooser;

import dao.NhanVienDAO;
import entity.NhanVien;
import gui.dialog.DialogChiTietNhanVien;

public class FormTimKiemNV extends JPanel 
{
    // Search Panel Components
    private JTextField txtMaNV;
    private JTextField txtTenNV;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JTextField txtDiaChi;
    private JComboBox<String> cboGioiTinh;
    private JComboBox<String> cboChucVu;
    private JComboBox<String> cboTrangThai;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    
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
    private NhanVienDAO nvDAO;
    
    // Fonts
    private Font labelFont = new Font("Roboto", Font.PLAIN, 14);
    private Font fieldFont = new Font("Roboto", Font.PLAIN, 14);
    private Font titleFont = new Font("Roboto", Font.BOLD, 24);
    private Font headerTableFont = new Font("Roboto", Font.BOLD, 16);
    
    public FormTimKiemNV() {
        nvDAO = new NhanVienDAO();
        initComponents();
        hienThiTatCaNhanVien();
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
        
        JLabel lblTitle = new JLabel("TÌM KIẾM NHÂN VIÊN");
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
     * Tạo panel tìm kiếm với nhiều tiêu chí
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
        
        // Row 1: Mã NV, Tên NV, Số điện thoại
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        formPanel.add(createLabel("Mã nhân viên:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.3;
        txtMaNV = createTextField();
        formPanel.add(txtMaNV, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.2;
        formPanel.add(createLabel("Tên nhân viên:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.3;
        txtTenNV = createTextField();
        formPanel.add(txtTenNV, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0.2;
        formPanel.add(createLabel("Số điện thoại:"), gbc);
        
        gbc.gridx = 5; gbc.weightx = 0.3;
        txtSoDienThoai = createTextField();
        formPanel.add(txtSoDienThoai, gbc);
        
        // Row 2: Email, Địa chỉ, Giới tính
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        formPanel.add(createLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.3;
        txtEmail = createTextField();
        formPanel.add(txtEmail, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.2;
        formPanel.add(createLabel("Địa chỉ:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.3;
        txtDiaChi = createTextField();
        formPanel.add(txtDiaChi, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0.2;
        formPanel.add(createLabel("Giới tính:"), gbc);
        
        gbc.gridx = 5; gbc.weightx = 0.3;
        String[] gioiTinh = {"Tất cả", "Nam", "Nữ"};
        cboGioiTinh = new JComboBox<>(gioiTinh);
        cboGioiTinh.setFont(fieldFont);
        cboGioiTinh.setPreferredSize(new Dimension(200, 35));
        cboGioiTinh.setBackground(Color.WHITE);
        formPanel.add(cboGioiTinh, gbc);
        
        // Row 3: Ngày sinh từ, Ngày sinh đến, Chức vụ
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
        formPanel.add(createLabel("Ngày sinh từ:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.3;
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateFrom.setFont(fieldFont);
        dateFrom.setPreferredSize(new Dimension(200, 35));
        formPanel.add(dateFrom, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.2;
        formPanel.add(createLabel("Ngày sinh đến:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.3;
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        dateTo.setFont(fieldFont);
        dateTo.setPreferredSize(new Dimension(200, 35));
        formPanel.add(dateTo, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0.2;
        formPanel.add(createLabel("Chức vụ:"), gbc);
        
        gbc.gridx = 5; gbc.weightx = 0.3;
        String[] chucVu = {"Tất cả", "Nhân viên quản lý", "Nhân viên bán thuốc"};
        cboChucVu = new JComboBox<>(chucVu);
        cboChucVu.setFont(fieldFont);
        cboChucVu.setPreferredSize(new Dimension(200, 35));
        cboChucVu.setBackground(Color.WHITE);
        formPanel.add(cboChucVu, gbc);
        
        // Row 4: Trạng thái
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.2;
        formPanel.add(createLabel("Trạng thái:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.3;
        String[] trangThai = {"Tất cả", "Đang làm việc", "Đã xóa"};
        cboTrangThai = new JComboBox<>(trangThai);
        cboTrangThai.setFont(fieldFont);
        cboTrangThai.setPreferredSize(new Dimension(200, 35));
        cboTrangThai.setBackground(Color.WHITE);
        formPanel.add(cboTrangThai, gbc);
        
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
        String[] columns = {"Mã NV", "Tên nhân viên", "Giới tính", "Số điện thoại", 
                           "Email", "Ngày sinh", "Chức vụ", "Trạng thái"};
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
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);
        table.getColumnModel().getColumn(7).setPreferredWidth(120);
        
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
     * Xử lý tìm kiếm nhân viên
     */
    private void xuLyTimKiem() {
        try {
            // Lấy các tiêu chí tìm kiếm
            String maNV = txtMaNV.getText().trim();
            String tenNV = txtTenNV.getText().trim();
            String soDienThoai = txtSoDienThoai.getText().trim();
            String email = txtEmail.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            String gioiTinh = cboGioiTinh.getSelectedItem().toString();
            String chucVu = cboChucVu.getSelectedItem().toString();
            String trangThai = cboTrangThai.getSelectedItem().toString();
            
            // Tìm kiếm
            ArrayList<NhanVien> ketQua = nvDAO.timKiemNangCao(
                maNV, tenNV, soDienThoai, email, diaChi, 
                gioiTinh.equals("Tất cả") ? "" : gioiTinh,
                chucVu.equals("Tất cả") ? "" : chucVu,
                trangThai.equals("Tất cả") ? "" : trangThai,
                dateFrom.getDate(), 
                dateTo.getDate()
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
    private void hienThiKetQua(ArrayList<NhanVien> dsNV) {
        tableModel.setRowCount(0);
        
        for (NhanVien nv : dsNV) {
            tableModel.addRow(new Object[]{
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getGioiTinh(),
                nv.getSoDienThoai(),
                nv.getEmail(),
                nv.getNgaySinh() != null ? 
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(nv.getNgaySinh()) : "",
                nv.getChucVu(),
                nv.isXoa() ? "Đã xóa" : "Đang làm việc"
            });
        }
        
        lblKetQua.setText("Tìm thấy " + dsNV.size() + " kết quả");
        
        if (dsNV.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy nhân viên nào phù hợp!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Làm mới form
     */
    private void lamMoi() {
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        cboGioiTinh.setSelectedIndex(0);
        cboChucVu.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        dateFrom.setDate(null);
        dateTo.setDate(null);

        hienThiTatCaNhanVien();
    }
    
    /**
     * Xem chi tiết nhân viên
     */
    private void xemChiTiet() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn nhân viên cần xem!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maNV = tableModel.getValueAt(selectedRow, 0).toString();
        
        try {
            NhanVien nv = nvDAO.getNhanVienTheoMa(maNV);
            
            if (nv == null) {
                // Nếu không tìm thấy trong danh sách chưa xóa, thử tìm trong tất cả
                ArrayList<NhanVien> allNV = nvDAO.getTatCaNhanVien();
                for (NhanVien n : allNV) {
                    if (n.getMaNV().equals(maNV)) {
                        nv = n;
                        break;
                    }
                }
            }
            
            if (nv != null) {
                // Hiển thị dialog chi tiết
                DialogChiTietNhanVien dialog = new DialogChiTietNhanVien(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    nv
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
    
    /**
     * Xuất kết quả ra file Excel
     */
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
     * Hiển thị toàn bộ danh sách nhân viên khi mở form
     */
    private void hienThiTatCaNhanVien() 
    {
        try 
        {
            ArrayList<NhanVien> dsNV = nvDAO.getTatCaNhanVien();
            hienThiKetQua(dsNV);
        } 
        catch(SQLException e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
            		"Lỗi khi tải danh sách nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

}
