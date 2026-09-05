package gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.HoaDonDAO;
import dao.HoaDonDAO.HoaDonHienThi;
import entity.TaiKhoan;
import gui.dialog.DialogChiTietHoaDon;

public class FormQuanLyHoaDon extends JPanel implements ActionListener {
    private JPanel actionPanel;
    private JButton btnAdd;
    private JButton btnInfo;
    private JButton btnReload;
    private JComboBox<String> cboxSearch;
    private JPanel headerPanel;
    private JPanel jPanel1;
    private JPanel jPanel3;
    private JPanel jPanel5;
    private JScrollPane jScrollPane1;
    private JLabel lblTable;
    private JTable table;
    private JPanel tablePanel;
    private JTextField txtSearch;
    private DefaultTableModel tableModel;
    private HoaDonDAO hdDAO;
    private TaiKhoan tk;
    private ArrayList<HoaDonHienThi> dsHoaDon;
    
    Font headerTable = new Font("Roboto", Font.BOLD, 18);
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public FormQuanLyHoaDon(ManHinhChinh parent,TaiKhoan taiKhoan) {
        this.tk = taiKhoan;
        hdDAO = new HoaDonDAO();
        taoNoiDung();
        addEvents();
        btnAdd.addActionListener(e -> {
        	try {
				parent.hienThiForm(new FormLapHoaDon(tk));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
    }

    private void taoNoiDung() {
        headerPanel = new JPanel();
        jPanel1 = new JPanel();
        jPanel3 = new JPanel();
        cboxSearch = new JComboBox<>();
        txtSearch = new JTextField();
        btnReload = new JButton();
        actionPanel = new JPanel();
        btnAdd = new JButton();
        btnInfo = new JButton();
        tablePanel = new JPanel();
        jScrollPane1 = new JScrollPane();
        table = new JTable();
        jPanel5 = new JPanel();
        lblTable = new JLabel();

        setBackground(new Color(230, 245, 245));
        setBorder(new LineBorder(new Color(230, 245, 245), 6, true));
        setMinimumSize(new Dimension(1130, 800));
        setPreferredSize(new Dimension(1130, 800));
        setLayout(new BorderLayout(0, 10));

        headerPanel.setBackground(new Color(255, 255, 255));
        headerPanel.setBorder(new LineBorder(new Color(232, 232, 232), 2, true));
        headerPanel.setLayout(new BorderLayout());

        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.setPreferredSize(new Dimension(590, 100));
        jPanel1.setLayout(new FlowLayout(FlowLayout.RIGHT, 32, 30));

        jPanel3.setBackground(new Color(255, 255, 255));
        jPanel3.setPreferredSize(new Dimension(584, 50));
        jPanel3.setLayout(new FlowLayout(FlowLayout.TRAILING, 24, 5));

        cboxSearch.setToolTipText("");
        cboxSearch.setPreferredSize(new Dimension(150, 40));
        String[] searchType = {"Tất cả", "Mã hóa đơn", "Tên nhân viên", "Tên khách hàng"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(searchType);
        cboxSearch.setModel(model);
        jPanel3.add(cboxSearch);

        txtSearch.setToolTipText("Tìm kiếm");
        txtSearch.setPreferredSize(new Dimension(240, 40));
        txtSearch.setSelectionColor(new Color(230, 245, 245));
        jPanel3.add(txtSearch);

        btnReload.setIcon(new FlatSVGIcon(getClass().getResource("/img/reload.svg")));
        btnReload.setToolTipText("Làm mới");
        btnReload.setBorder(null);
        btnReload.setBorderPainted(false);
        btnReload.setContentAreaFilled(false);
        btnReload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReload.setFocusPainted(false);
        btnReload.setFocusable(false);
        btnReload.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReload.setPreferredSize(new Dimension(48, 48));
        jPanel3.add(btnReload);

        jPanel1.add(jPanel3);
        headerPanel.add(jPanel1, BorderLayout.CENTER);

        actionPanel.setBackground(new Color(255, 255, 255));
        actionPanel.setPreferredSize(new Dimension(600, 100));
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 24, 15));

        btnAdd.setFont(new Font("Roboto", Font.BOLD, 14));
        btnAdd.setIcon(new FlatSVGIcon(getClass().getResource("/img/add.svg")));
        btnAdd.setText("THÊM");
        btnAdd.setBorder(null);
        btnAdd.setBorderPainted(false);
        btnAdd.setContentAreaFilled(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setFocusPainted(false);
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setPreferredSize(new Dimension(90, 90));
        btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        actionPanel.add(btnAdd);

        btnInfo.setFont(new Font("Roboto", Font.BOLD, 14));
        btnInfo.setIcon(new FlatSVGIcon(getClass().getResource("/img/info.svg")));
        btnInfo.setText("CHI TIẾT");
        btnInfo.setBorder(null);
        btnInfo.setBorderPainted(false);
        btnInfo.setContentAreaFilled(false);
        btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInfo.setFocusPainted(false);
        btnInfo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnInfo.setPreferredSize(new Dimension(90, 90));
        btnInfo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        actionPanel.add(btnInfo);

        headerPanel.add(actionPanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.PAGE_START);

        tablePanel.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        tablePanel.setLayout(new BorderLayout());

        String[] tableTitle = {"Mã hóa đơn", "Ngày lập", "Nhân viên", "Khách hàng", "Mã thuế", "Khuyến mãi", "Phiếu đặt"};
        tableModel = new DefaultTableModel(tableTitle, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.getTableHeader().setFont(headerTable);
        table.setModel(tableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.setFocusable(false);
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(table);

        tablePanel.add(jScrollPane1, BorderLayout.CENTER);

        jPanel5.setBackground(new Color(0, 0, 205));
        jPanel5.setMinimumSize(new Dimension(100, 60));
        jPanel5.setPreferredSize(new Dimension(500, 40));
        jPanel5.setLayout(new BorderLayout());

        lblTable.setFont(new Font("Roboto Medium", Font.BOLD, 30));
        lblTable.setForeground(new Color(255, 255, 255));
        lblTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTable.setText("THÔNG TIN HÓA ĐƠN");
        jPanel5.add(lblTable, BorderLayout.CENTER);

        tablePanel.add(jPanel5, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        
        loadTableData();
    }

    private void addEvents() {
        btnAdd.addActionListener(this);
        btnInfo.addActionListener(this);
        btnReload.addActionListener(e -> {
            txtSearch.setText("");
            loadTableData();
        });

        // Tìm kiếm real-time
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timKiem();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timKiem();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timKiem();
            }
        });

        cboxSearch.addActionListener(e -> {
            if (!txtSearch.getText().trim().isEmpty()) {
                timKiem();
            }
        });
    }

    public void loadTableData() {
        try {
            tableModel.setRowCount(0);
            dsHoaDon = hdDAO.getDsHoaDonHienThi();
            
            for (HoaDonHienThi hd : dsHoaDon) {
                themDongHoaDon(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải dữ liệu: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void themDongHoaDon(HoaDonHienThi hd) {
        tableModel.addRow(new Object[]{
            hd.getMaHD(),
            dateFormat.format(hd.getNgayLap()),
            hienThiNhanVien(hd),
            hienThiKhachHang(hd),
            hd.getMaThue() != null ? hd.getMaThue() : "N/A",
            hd.getMaKM() != null ? hd.getMaKM() : "Không có",
            hd.getMaPhieuDat() != null ? hd.getMaPhieuDat() : "Không có"
        });
    }

    private String hienThiNhanVien(HoaDonHienThi hd) {
        if (hd.getTenNV() != null && !hd.getTenNV().trim().isEmpty()) {
            return hd.getTenNV();
        }
        return hd.getMaNV() != null ? hd.getMaNV() : "N/A";
    }

    private String hienThiKhachHang(HoaDonHienThi hd) {
        if (hd.getTenKH() != null && !hd.getTenKH().trim().isEmpty()) {
            return hd.getTenKH();
        }
        return hd.getMaKH() != null ? hd.getMaKH() : "Khách lẻ";
    }

    private void timKiem() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        String searchType = cboxSearch.getSelectedItem().toString();

        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }

        tableModel.setRowCount(0);

        for (HoaDonHienThi hd : dsHoaDon) {
            boolean match = false;
            String maHD = safeLower(hd.getMaHD());
            String maNV = safeLower(hd.getMaNV());
            String tenNV = hienThiNhanVien(hd);
            String maKH = safeLower(hd.getMaKH());
            String tenKH = hienThiKhachHang(hd);

            switch (searchType) {
                case "Tất cả":
                    match = maHD.contains(keyword)
                            || safeLower(tenNV).contains(keyword)
                            || safeLower(tenKH).contains(keyword);
                    break;
                case "Mã hóa đơn":
                    match = maHD.contains(keyword);
                    break;
                case "Tên nhân viên":
                    match = safeLower(tenNV).contains(keyword);
                    break;
                case "Tên khách hàng":
                    match = safeLower(tenKH).contains(keyword);
                    break;
            }

            if (match) {
                themDongHoaDon(hd);
            }
        }
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj.equals(btnInfo)) {
            hienThiChiTietHoaDon();
        }
    }

    private void hienThiChiTietHoaDon() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn hóa đơn cần xem chi tiết!",
                "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maHD = table.getValueAt(selectedRow, 0).toString();

        DialogChiTietHoaDon dialog = new DialogChiTietHoaDon(
            (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this),
            maHD
        );
        dialog.setVisible(true);
    }
    
}
