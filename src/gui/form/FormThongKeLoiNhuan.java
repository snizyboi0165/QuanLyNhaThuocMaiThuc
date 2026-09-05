package gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import dao.ThongKeLoiNhuanDAO;
import dao.ThongKeLoiNhuanDAO.ThongKeSanPham;
import dao.ThongKeLoiNhuanDAO.ThongKeKhachHang;
import dao.ThongKeLoiNhuanDAO.ThongKeNhanVien;
import dao.ThongKeLoiNhuanDAO.TopSanPham;
import dao.ThongKeLoiNhuanDAO.ThongKeTheoNgay;
import dao.ThongKeLoiNhuanDAO.ThongKeTheoThang;

public class FormThongKeLoiNhuan extends JPanel implements ActionListener {
    
    private JDateChooser dateFrom, dateTo;
    private JComboBox<String> cboTimeType;
    private JButton btnTimKiem;
    
    private JTabbedPane tabbedPane;
    
    // Tab 1: Thống kê sản phẩm
    private JTable tableSanPham;
    private DefaultTableModel modelSanPham;
    private JLabel lblTongDoanhThuSP;
    
    // Tab 2: Thống kê khách hàng
    private JTable tableKhachHang;
    private DefaultTableModel modelKhachHang;
    private JLabel lblTongTienKH;
    
    // Tab 3: Thống kê nhân viên
    private JTable tableNhanVien;
    private DefaultTableModel modelNhanVien;
    private JLabel lblTongDoanhThuNV;
    
    // Tab 4: Top sản phẩm
    private JTable tableTop;
    private DefaultTableModel modelTop;
    private JComboBox<Integer> cboTopN;
    private JTable tableBanCham;
    private DefaultTableModel modelBanCham;
    private JComboBox<Integer> cboBottomN;
    
    // Tab 5: Thống kê theo thời gian
    private JTable tableTheoThg;
    private DefaultTableModel modelTheoThg;
    private JLabel lblTongDoanhThuThg;
    
    private ThongKeLoiNhuanDAO dao;
    private NumberFormat currencyFormat;
    private DecimalFormat numberFormat;
    
    public FormThongKeLoiNhuan() {
        dao = new ThongKeLoiNhuanDAO();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        numberFormat = new DecimalFormat("#,##0");
        
        taoGiaoDien();
        addActionListeners();
        hienThiDuLieuMacDinh();
    }
    
    private void taoGiaoDien() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 245, 245));
        setBorder(new LineBorder(new Color(230, 245, 245), 6, true));
        
        // Panel điều khiển
        JPanel controlPanel = taoControlPanel();
        add(controlPanel, BorderLayout.NORTH);
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Tab 1: Thống kê sản phẩm
        JPanel panelSanPham = taoTabSanPham();
        tabbedPane.addTab("Thống kê sản phẩm", panelSanPham);
        
        // Tab 2: Thống kê khách hàng
        JPanel panelKhachHang = taoTabKhachHang();
        tabbedPane.addTab("Thống kê khách hàng", panelKhachHang);
        
        // Tab 3: Thống kê nhân viên
        JPanel panelNhanVien = taoTabNhanVien();
        tabbedPane.addTab("Thống kê nhân viên", panelNhanVien);
        
        // Tab 4: Top sản phẩm
        JPanel panelTop = taoTabTop();
        JPanel panelBanCham = taoTabBanCham();
        tabbedPane.addTab("Top sản phẩm", panelTop);
        
        // Tab 5: Thống kê theo thời gian
        tabbedPane.addTab("Bán chậm", panelBanCham);

        JPanel panelTheoThg = taoTabTheoThg();
        tabbedPane.addTab("Thống kê theo thời gian", panelTheoThg);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel taoControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder("Bộ lọc thời gian"));
        panel.setPreferredSize(new Dimension(0, 80));
        
        // Loại thời gian
        panel.add(new JLabel("Loại thời gian:"));
        cboTimeType = new JComboBox<>(new String[]{"Theo ngày", "Theo tháng", "Theo năm"});
        cboTimeType.setPreferredSize(new Dimension(120, 30));
        panel.add(cboTimeType);
        
        // Từ ngày
        panel.add(new JLabel("Bắt đầu từ:"));
        dateFrom = new JDateChooser();
        dateFrom.setPreferredSize(new Dimension(150, 30));
        dateFrom.setDate(java.sql.Date.valueOf(LocalDate.now().minusMonths(1)));
        
        // Cấu hình định dạng ngày dd/MM/yyyy
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateFrom.getJCalendar().setWeekOfYearVisible(false);
        
        // Tùy chỉnh font cho date chooser
        dateFrom.setFont(new Font("Arial", Font. PLAIN, 12));
        if (dateFrom.getDateEditor() instanceof com.toedter.calendar.JTextFieldDateEditor) {
            com.toedter.calendar.JTextFieldDateEditor editor = 
                (com.toedter.calendar.JTextFieldDateEditor) dateFrom.getDateEditor();
            editor.setFont(new Font("Arial", Font. PLAIN, 12));
        }
        
        panel.add(dateFrom);
        
        // Đến ngày
        panel.add(new JLabel("Kết thúc đến:"));
        dateTo = new JDateChooser();
        dateTo.setPreferredSize(new Dimension(150, 30));
        dateTo.setDate(java.sql.Date.valueOf(LocalDate.now()));
        
        // Cấu hình định dạng ngày dd/MM/yyyy
        dateTo. setDateFormatString("dd/MM/yyyy");
        dateTo.getJCalendar().setWeekOfYearVisible(false);
        
        // Tùy chỉnh font cho date chooser
        dateTo. setFont(new Font("Arial", Font.PLAIN, 12));
        if (dateTo.getDateEditor() instanceof com.toedter.calendar.JTextFieldDateEditor) {
            com.toedter.calendar.JTextFieldDateEditor editor = 
                (com.toedter.calendar. JTextFieldDateEditor) dateTo.getDateEditor();
            editor.setFont(new Font("Arial", Font.PLAIN, 12));
        }
        
        panel.add(dateTo);
        
        // Nút tìm kiếm
        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setBackground(new Color(0, 0, 205));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFont(new Font("Arial", Font.BOLD, 12));
        btnTimKiem.setPreferredSize(new Dimension(100, 30));
        panel.add(btnTimKiem);
        
        return panel;
    }
    
    private JPanel taoTabSanPham() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel tóm tắt
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(new TitledBorder("Tóm tắt"));
        summaryPanel.setPreferredSize(new Dimension(0, 70));
        
        summaryPanel.add(new JLabel("Tổng doanh thu:"));
        lblTongDoanhThuSP = new JLabel("0 ₫");
        lblTongDoanhThuSP.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongDoanhThuSP.setForeground(new Color(0, 128, 0));
        summaryPanel.add(lblTongDoanhThuSP);
        
        panel.add(summaryPanel, BorderLayout.NORTH);
        
        // Bảng
        String[] columns = {"Tên sản phẩm", "Số lượng bán", "Doanh thu"};
        modelSanPham = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSanPham = new JTable(modelSanPham);
        setupTable(tableSanPham);
        
        JScrollPane scrollPane = new JScrollPane(tableSanPham);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel taoTabKhachHang() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel tóm tắt
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(new TitledBorder("Tóm tắt"));
        summaryPanel.setPreferredSize(new Dimension(0, 70));
        
        summaryPanel.add(new JLabel("Tổng tiền mua:"));
        lblTongTienKH = new JLabel("0 ₫");
        lblTongTienKH.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongTienKH.setForeground(new Color(0, 128, 0));
        summaryPanel.add(lblTongTienKH);
        
        panel.add(summaryPanel, BorderLayout.NORTH);
        
        // Bảng
        String[] columns = {"Tên khách hàng", "Số hóa đơn", "Tổng tiền mua"};
        modelKhachHang = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableKhachHang = new JTable(modelKhachHang);
        setupTable(tableKhachHang);
        
        JScrollPane scrollPane = new JScrollPane(tableKhachHang);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel taoTabNhanVien() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel tóm tắt
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(new TitledBorder("Tóm tắt"));
        summaryPanel.setPreferredSize(new Dimension(0, 70));
        
        summaryPanel.add(new JLabel("Tổng doanh thu:"));
        lblTongDoanhThuNV = new JLabel("0 ₫");
        lblTongDoanhThuNV.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongDoanhThuNV.setForeground(new Color(0, 128, 0));
        summaryPanel.add(lblTongDoanhThuNV);
        
        panel.add(summaryPanel, BorderLayout.NORTH);
        
        // Bảng
        String[] columns = {"Tên nhân viên", "Số hóa đơn đã bán", "Doanh thu"};
        modelNhanVien = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableNhanVien = new JTable(modelNhanVien);
        setupTable(tableNhanVien);
        
        JScrollPane scrollPane = new JScrollPane(tableNhanVien);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel taoTabTop() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel điều khiển
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.add(new JLabel("Top"));
        cboTopN = new JComboBox<>(new Integer[]{5, 10, 15, 20});
        cboTopN.setSelectedItem(10);
        cboTopN.setPreferredSize(new Dimension(80, 30));
        controlPanel.add(cboTopN);
        controlPanel.add(new JLabel("sản phẩm bán chạy nhất"));
        
        panel.add(controlPanel, BorderLayout.NORTH);
        
        // Bảng
        String[] columns = {"Thứ hạng", "Tên sản phẩm", "Số lượng bán", "Doanh thu"};
        modelTop = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableTop = new JTable(modelTop);
        setupTable(tableTop);
        
        JScrollPane scrollPane = new JScrollPane(tableTop);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel taoTabBanCham() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.add(new JLabel("Bottom"));
        cboBottomN = new JComboBox<>(new Integer[]{5, 10, 15, 20});
        cboBottomN.setSelectedItem(10);
        cboBottomN.setPreferredSize(new Dimension(80, 30));
        controlPanel.add(cboBottomN);
        controlPanel.add(new JLabel("sản phẩm"));
        
        panel.add(controlPanel, BorderLayout.NORTH);
        
        String[] columns = {"Xếp hạng", "Tên sản phẩm", "Số lượng bán", "Doanh thu"};
        modelBanCham = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableBanCham = new JTable(modelBanCham);
        setupTable(tableBanCham);
        
        JScrollPane scrollPane = new JScrollPane(tableBanCham);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel taoTabTheoThg() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel tóm tắt
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(new TitledBorder("Tóm tắt"));
        summaryPanel.setPreferredSize(new Dimension(0, 70));
        
        summaryPanel.add(new JLabel("Tổng doanh thu:"));
        lblTongDoanhThuThg = new JLabel("0 ₫");
        lblTongDoanhThuThg.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongDoanhThuThg.setForeground(new Color(0, 128, 0));
        summaryPanel.add(lblTongDoanhThuThg);
        
        panel.add(summaryPanel, BorderLayout.NORTH);
        
        // Bảng sẽ được cập nhật động theo loại thời gian
        String[] columns = {"Ngày", "Doanh thu"};
        modelTheoThg = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableTheoThg = new JTable(modelTheoThg);
        setupTable(tableTheoThg);
        
        JScrollPane scrollPane = new JScrollPane(tableTheoThg);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(0, 0, 205));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 230, 230));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == 0) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
            }
        }
    }
    
    private void addActionListeners() {
        btnTimKiem.addActionListener(this);
        cboTopN.addActionListener(e -> hienThiTopSanPham());
        cboBottomN.addActionListener(e -> hienThiSanPhamBanCham());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTimKiem) {
            timKiem();
        }
    }
    
    private void timKiem() {
        try {
            // Kiểm tra 4.1: Người dùng chưa chọn đủ ngày
            if (dateFrom.getDate() == null || dateTo.getDate() == null) {
                JOptionPane.showMessageDialog(
                    this, 
                    "Khoảng thời gian không hợp lệ, vui lòng nhập lại", 
                    "Lỗi nhập liệu",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            
            LocalDate tuNgay = dateFrom.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate denNgay = dateTo. getDate().toInstant()
                .atZone(java. time.ZoneId.systemDefault()).toLocalDate();
            
            // Kiểm tra 4.1: "từ ngày" lớn hơn "đến ngày"
            if (tuNgay.isAfter(denNgay)) {
                JOptionPane.showMessageDialog(
                    this, 
                    "Khoảng thời gian không hợp lệ, vui lòng nhập lại\n" +
                    "(Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc)", 
                    "Lỗi nhập liệu",
                    JOptionPane.WARNING_MESSAGE
                );
                // Focus vào dateFrom để người dùng nhập lại
                dateFrom.requestFocus();
                return;
            }
            
            // Kiểm tra 4.2: Không có dữ liệu trong khoảng thời gian
            if (! dao.kiemTraCoDuLieu(tuNgay, denNgay)) {
                JOptionPane.showMessageDialog(
                    this, 
                    "Không có dữ liệu doanh thu trong khoảng thời gian này.\n" +
                    "Vui lòng chọn khoảng thời gian khác.", 
                    "Không có dữ liệu",
                    JOptionPane. INFORMATION_MESSAGE
                );
                // Xóa dữ liệu cũ trong các bảng
                xoaDuLieuBang();
                return;
            }
            
            // Nếu tất cả điều kiện hợp lệ, hiển thị dữ liệu
            hienThiThongKeSanPham(tuNgay, denNgay);
            hienThiThongKeKhachHang(tuNgay, denNgay);
            hienThiThongKeNhanVien(tuNgay, denNgay);
            hienThiTopSanPham();
            hienThiSanPhamBanCham();
            hienThiThongKeTheoThg(tuNgay, denNgay);
            
            JOptionPane.showMessageDialog(
                this, 
                "Thống kê dữ liệu thành công!", 
                "Thành công",
                JOptionPane. INFORMATION_MESSAGE
            );
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this, 
                "Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage(), 
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Xóa tất cả dữ liệu trong các bảng và reset tổng tiền
     */
    private void xoaDuLieuBang() {
        modelSanPham.setRowCount(0);
        modelKhachHang.setRowCount(0);
        modelNhanVien.setRowCount(0);
        modelTop.setRowCount(0);
        modelBanCham.setRowCount(0);
        modelTheoThg.setRowCount(0);
        
        lblTongDoanhThuSP.setText("0 ₫");
        lblTongTienKH.setText("0 ₫");
        lblTongDoanhThuNV. setText("0 ₫");
        lblTongDoanhThuThg.setText("0 ₫");
    }
    
    private void hienThiDuLieuMacDinh() {
        try {
            LocalDate tuNgay = LocalDate.now().minusMonths(1);
            LocalDate denNgay = LocalDate.now();
            
            hienThiThongKeSanPham(tuNgay, denNgay);
            hienThiThongKeKhachHang(tuNgay, denNgay);
            hienThiThongKeNhanVien(tuNgay, denNgay);
            hienThiTopSanPham();
            hienThiSanPhamBanCham();
            hienThiThongKeTheoThg(tuNgay, denNgay);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void hienThiThongKeSanPham(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        modelSanPham.setRowCount(0);
        
        ArrayList<ThongKeSanPham> danhSach = dao.thongKeSanPham(tuNgay, denNgay);
        double tongDoanhThu = 0;
        
        for (ThongKeSanPham sp : danhSach) {
            modelSanPham.addRow(new Object[]{
                sp.getTenThuoc(),
                numberFormat.format(sp.getSoLuongBan()),
                currencyFormat.format(sp.getDoanhThu())
            });
            tongDoanhThu += sp.getDoanhThu();
        }
        
        lblTongDoanhThuSP.setText(currencyFormat.format(tongDoanhThu));
    }
    
    private void hienThiThongKeKhachHang(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        modelKhachHang.setRowCount(0);
        
        ArrayList<ThongKeKhachHang> danhSach = dao.thongKeKhachHang(tuNgay, denNgay);
        double tongTienMua = 0;
        
        for (ThongKeKhachHang kh : danhSach) {
            modelKhachHang.addRow(new Object[]{
                kh.getTenKH(),
                numberFormat.format(kh.getSoHoaDon()),
                currencyFormat.format(kh.getTongTienMua())
            });
            tongTienMua += kh.getTongTienMua();
        }
        
        lblTongTienKH.setText(currencyFormat.format(tongTienMua));
    }
    
    private void hienThiThongKeNhanVien(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        modelNhanVien.setRowCount(0);
        
        ArrayList<ThongKeNhanVien> danhSach = dao.thongKeNhanVien(tuNgay, denNgay);
        double tongDoanhThu = 0;
        
        for (ThongKeNhanVien nv : danhSach) {
            modelNhanVien.addRow(new Object[]{
                nv.getTenNV(),
                numberFormat.format(nv.getSoHoaDonBan()),
                currencyFormat.format(nv.getDoanhThu())
            });
            tongDoanhThu += nv.getDoanhThu();
        }
        
        lblTongDoanhThuNV.setText(currencyFormat.format(tongDoanhThu));
    }
    
    private void hienThiTopSanPham() {
        try {
            if (dateFrom.getDate() == null || dateTo.getDate() == null) {
                return;
            }
            
            LocalDate tuNgay = dateFrom.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate denNgay = dateTo.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            int topN = (Integer) cboTopN.getSelectedItem();
            
            modelTop.setRowCount(0);
            
            ArrayList<TopSanPham> danhSach = dao.thongKeTopSanPham(tuNgay, denNgay, topN);
            
            for (int i = 0; i < danhSach.size(); i++) {
                TopSanPham sp = danhSach.get(i);
                modelTop.addRow(new Object[]{
                    i + 1,
                    sp.getTenThuoc(),
                    numberFormat.format(sp.getSoLuongBan()),
                    currencyFormat.format(sp.getDoanhThu())
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void hienThiSanPhamBanCham() {
        try {
            if (dateFrom.getDate() == null || dateTo.getDate() == null) {
                return;
            }
            
            LocalDate tuNgay = dateFrom.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate denNgay = dateTo.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            int bottomN = (Integer) cboBottomN.getSelectedItem();
            
            modelBanCham.setRowCount(0);
            
            ArrayList<TopSanPham> danhSach = dao.thongKeSanPhamBanCham(tuNgay, denNgay, bottomN);
            
            for (int i = 0; i < danhSach.size(); i++) {
                TopSanPham sp = danhSach.get(i);
                modelBanCham.addRow(new Object[]{
                    i + 1,
                    sp.getTenThuoc(),
                    numberFormat.format(sp.getSoLuongBan()),
                    currencyFormat.format(sp.getDoanhThu())
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void hienThiThongKeTheoThg(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        modelTheoThg.setRowCount(0);
        
        String timeType = (String) cboTimeType.getSelectedItem();
        double tongDoanhThu = 0;
        
        if (timeType.equals("Theo ngày")) {
            // Hiển thị theo ngày
            updateTableHeaderTheoNgay();
            ArrayList<ThongKeTheoNgay> danhSach = dao.thongKeTheoNgay(tuNgay, denNgay);
            
            for (ThongKeTheoNgay item : danhSach) {
                modelTheoThg.addRow(new Object[]{
                    item.getNgay(),
                    currencyFormat.format(item.getDoanhThu())
                });
                tongDoanhThu += item.getDoanhThu();
            }
        } else if (timeType.equals("Theo tháng")) {
            // Hiển thị theo tháng
            updateTableHeaderTheoThang();
            ArrayList<ThongKeTheoThang> danhSach = dao.thongKeTheoThang(tuNgay, denNgay);
            
            for (ThongKeTheoThang item : danhSach) {
                modelTheoThg.addRow(new Object[]{
                    item.getThangNam(),
                    currencyFormat.format(item.getDoanhThu())
                });
                tongDoanhThu += item.getDoanhThu();
            }
        } else if (timeType.equals("Theo năm")) {
            // Hiển thị theo năm
            updateTableHeaderTheoNam();
            ArrayList<ThongKeTheoThang> danhSach = dao.thongKeTheoNam(tuNgay, denNgay);
            
            for (ThongKeTheoThang item : danhSach) {
                modelTheoThg.addRow(new Object[]{
                    item.getThangNam(),
                    currencyFormat.format(item.getDoanhThu())
                });
                tongDoanhThu += item.getDoanhThu();
            }
        }
        
        lblTongDoanhThuThg.setText(currencyFormat.format(tongDoanhThu));
    }
    
    private void updateTableHeaderTheoNgay() {
        DefaultTableModel newModel = new DefaultTableModel(new String[]{"Ngày", "Doanh thu"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelTheoThg = newModel;
        tableTheoThg.setModel(modelTheoThg);
        setupTable(tableTheoThg);
    }
    
    private void updateTableHeaderTheoThang() {
        DefaultTableModel newModel = new DefaultTableModel(new String[]{"Tháng/Năm", "Doanh thu"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelTheoThg = newModel;
        tableTheoThg.setModel(modelTheoThg);
        setupTable(tableTheoThg);
    }
    
    private void updateTableHeaderTheoNam() {
        DefaultTableModel newModel = new DefaultTableModel(new String[]{"Năm", "Doanh thu"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelTheoThg = newModel;
        tableTheoThg.setModel(modelTheoThg);
        setupTable(tableTheoThg);
    }
}
