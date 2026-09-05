package gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon; // Import thư viện icon
import dao.KhuyenMaiDAO;
import entity.KhuyenMai;
import entity.TaiKhoan;
import gui.dialog.DialogCapNhatKhuyenMai;
import gui.dialog.DialogThemKhuyenMai;
import gui.dialog.DialogThongTinKhuyenMai;

public class FormKhuyenMai extends JPanel implements ActionListener {
    private JPanel actionPanel;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnInfo;
    private JButton btnReload;
    private JButton btnUpdate;
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
    private KhuyenMaiDAO kmDAO;
    private boolean choPhepSuaXoa = true;
    private final Font headerTable = new Font("Roboto", Font.BOLD, 18);
    
    public FormKhuyenMai() {
        this(null);
    }

    public FormKhuyenMai(TaiKhoan taiKhoan) {
        choPhepSuaXoa = coQuyenQuanLy(taiKhoan);
        kmDAO = new KhuyenMaiDAO();
    	taoNoiDung();
        loadDataTable();
        addActionListeners();
    }

    private void taoNoiDung() {
        // KHỞI TẠO CÁC PANEL CHỨA
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new LineBorder(new Color(232, 232, 232), 2, true));
        
        actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 15));
        actionPanel.setBackground(Color.WHITE);
        
        jPanel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 32, 30));
        jPanel1.setBackground(Color.WHITE);

        jPanel3 = new JPanel(new FlowLayout(FlowLayout.TRAILING, 24, 5));
        jPanel3.setBackground(Color.WHITE);
        
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        
        // === PHẦN THAY ĐỔI GIAO DIỆN CÁC NÚT ===

        // Nút THÊM
        btnAdd = new JButton("THÊM");
        btnAdd.setFont(new Font("Roboto", Font.BOLD, 14));
        btnAdd.setIcon(new FlatSVGIcon(getClass().getResource("/img/add.svg")));
        btnAdd.setBorder(null);
        btnAdd.setBorderPainted(false);
        btnAdd.setContentAreaFilled(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setFocusPainted(false);
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setPreferredSize(new Dimension(90, 90));
        btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        if (choPhepSuaXoa) {
            actionPanel.add(btnAdd);
        }

        // Nút SỬA
        btnUpdate = new JButton("SỬA");
        btnUpdate.setFont(new Font("Roboto", Font.BOLD, 14));
        btnUpdate.setIcon(new FlatSVGIcon(getClass().getResource("/img/update.svg")));
        btnUpdate.setBorder(null);
        btnUpdate.setBorderPainted(false);
        btnUpdate.setContentAreaFilled(false);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdate.setFocusPainted(false);
        btnUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUpdate.setPreferredSize(new Dimension(90, 90));
        btnUpdate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        if (choPhepSuaXoa) {
            actionPanel.add(btnUpdate);
        }

        // Nút XÓA
        btnDelete = new JButton("XÓA");
        btnDelete.setFont(new Font("Roboto", Font.BOLD, 14));
        btnDelete.setIcon(new FlatSVGIcon(getClass().getResource("/img/delete.svg")));
        btnDelete.setBorder(null);
        btnDelete.setBorderPainted(false);
        btnDelete.setContentAreaFilled(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setFocusPainted(false);
        btnDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelete.setPreferredSize(new Dimension(90, 90));
        btnDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        if (choPhepSuaXoa) {
            actionPanel.add(btnDelete);
        }

        // Nút chi tiết
        btnInfo = new JButton("CHI TIẾT");
        btnInfo.setFont(new Font("Roboto", Font.BOLD, 14));
        btnInfo.setIcon(new FlatSVGIcon(getClass().getResource("/img/info.svg")));
        btnInfo.setBorder(null);
        btnInfo.setBorderPainted(false);
        btnInfo.setContentAreaFilled(false);
        btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInfo.setFocusPainted(false);
        btnInfo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnInfo.setPreferredSize(new Dimension(90, 90));
        btnInfo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        actionPanel.add(btnInfo);
        
        // THANH TÌM KIẾM
        cboxSearch = new JComboBox<>(new String[]{"Tất cả", "Tên"});
        cboxSearch.setPreferredSize(new Dimension(120, 40));
        
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(240, 40));
        
        // Nút LÀM MỚI
        btnReload = new JButton();
        btnReload.setIcon(new FlatSVGIcon(getClass().getResource("/img/reload.svg")));
        btnReload.setToolTipText("Làm mới");
        btnReload.setBorder(null);
        btnReload.setBorderPainted(false);
        btnReload.setContentAreaFilled(false);
        btnReload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReload.setPreferredSize(new Dimension(48, 48));

        jPanel3.add(cboxSearch);
        jPanel3.add(txtSearch);
        jPanel3.add(btnReload);
        
        
        
        headerPanel.add(actionPanel, BorderLayout.WEST);
        headerPanel.add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(jPanel3);
        
        String[] tableTitle = {"Mã khuyến mãi", "Tên khuyến mãi", "Ngày bắt đầu", "Ngày kết thúc", "Phần trăm giảm giá"};
        tableModel = new DefaultTableModel(tableTitle, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel); 
        table.getTableHeader().setFont(headerTable);
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        
        jScrollPane1 = new JScrollPane(table);
        
        jPanel5 = new JPanel(new BorderLayout());
        jPanel5.setBackground(new Color(0, 0, 205));
        jPanel5.setPreferredSize(new Dimension(0, 40));
        
        lblTable = new JLabel("THÔNG TIN KHUYẾN MÃI");
        lblTable.setFont(new Font("Roboto", Font.BOLD, 20));
        lblTable.setForeground(Color.WHITE);
        lblTable.setHorizontalAlignment(JLabel.CENTER);
        jPanel5.add(lblTable, BorderLayout.CENTER);
        
        tablePanel.add(jPanel5, BorderLayout.NORTH);
        tablePanel.add(jScrollPane1, BorderLayout.CENTER);

        setLayout(new BorderLayout(0, 10));
        setBorder(new LineBorder(new Color(230, 245, 245), 6, true));
        add(headerPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }
    
    
    private void loadDataTable() {
        tableModel.setRowCount(0);
        try {
            ArrayList<KhuyenMai> dsKM = kmDAO.getDsKhuyenMai();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (KhuyenMai km : dsKM) {
                tableModel.addRow(new Object[] {
                    km.getMaKM(),
                    km.getTenKM(),
                    sdf.format(km.getNgayBatDau()),
                    sdf.format(km.getNgayKetThuc()),
                    String.format("%.1f%%", km.getPhanTramGiamGia())
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu khuyến mãi", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void reloadTable() {
        loadDataTable();
    }
    
    private void addActionListeners() {
        btnAdd.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);
        btnInfo.addActionListener(this);
        btnReload.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnAdd)) {
            if (!choPhepSuaXoa) {
                thongBaoKhongCoQuyen();
                return;
            }
            new DialogThemKhuyenMai((Frame) SwingUtilities.getWindowAncestor(this), this).setVisible(true);
        } else if (o.equals(btnReload)) {
            txtSearch.setText("");
            reloadTable();
        } else if (o.equals(btnDelete)) {
            xoaKhuyenMai();
        } else if (o.equals(btnUpdate)) {
            capNhatKhuyenMai();
        } else if (o.equals(btnInfo)) {
            xemThongTin();
        }
    }

    private void xoaKhuyenMai() {
        if (!choPhepSuaXoa) {
            thongBaoKhongCoQuyen();
            return;
        }
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maKM = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khuyến mãi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (kmDAO.xoaKhuyenMai(maKM)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    reloadTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void capNhatKhuyenMai() {
        if (!choPhepSuaXoa) {
            thongBaoKhongCoQuyen();
            return;
        }
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần cập nhật!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maKM = tableModel.getValueAt(selectedRow, 0).toString();
        try {
            KhuyenMai km = kmDAO.getKhuyenMaiTheoMa(maKM);
            if (km != null) {
                new DialogCapNhatKhuyenMai((Frame) SwingUtilities.getWindowAncestor(this), this, km).setVisible(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xemThongTin() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi để xem thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maKM = tableModel.getValueAt(selectedRow, 0).toString();
        try {
            KhuyenMai km = kmDAO.getKhuyenMaiTheoMa(maKM);
            if (km != null) {
                new DialogThongTinKhuyenMai((Frame) SwingUtilities.getWindowAncestor(this), km).setVisible(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean coQuyenQuanLy(TaiKhoan taiKhoan) {
        return taiKhoan == null || "Nhân viên quản lý".equals(taiKhoan.getVaiTro());
    }

    private void thongBaoKhongCoQuyen() {
        JOptionPane.showMessageDialog(this,
                "Nhân viên không được phép thực hiện chức năng này",
                "Không có quyền",
                JOptionPane.WARNING_MESSAGE);
    }
}
