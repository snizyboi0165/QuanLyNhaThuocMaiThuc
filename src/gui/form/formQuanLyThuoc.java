package gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

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

import dao.DanhMucThuocDAO;
import dao.ThuocDAO;
import entity.TaiKhoan;
import entity.Thuoc;
import gui.dialog.DialogThemThuoc;
import gui.dialog.DialogThongTinChiTietThuoc;
import gui.dialog.DialogSuaThuoc;

public class FormQuanLyThuoc extends JPanel {
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
    private ThuocDAO thuocDAO;
    private DanhMucThuocDAO dmtDAO;
    private ArrayList<Thuoc> dsThuoc;
    private final Map<String, Date> hanSuDungTheoMaThuoc = new HashMap<>();
    private boolean choPhepSuaXoa = true;
    Font headerTable = new Font("Roboto", Font.BOLD, 18);
    
    public FormQuanLyThuoc() {
        this(null);
    }

    public FormQuanLyThuoc(TaiKhoan taiKhoan) {
        choPhepSuaXoa = coQuyenQuanLy(taiKhoan);
        taoNoiDung();
        addEvents();
    }

    private void taoNoiDung() {
        thuocDAO = new ThuocDAO();
        dmtDAO = new DanhMucThuocDAO();
        headerPanel = new JPanel();
        jPanel1 = new JPanel();
        jPanel3 = new JPanel();
        cboxSearch = new JComboBox<>();
        txtSearch = new JTextField();
        btnReload = new JButton();
        actionPanel = new JPanel();
        btnAdd = new JButton();
        btnUpdate = new JButton();
        btnDelete = new JButton();
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
        cboxSearch.setPreferredSize(new Dimension(120, 40));
        String[] searchType = {"Tất cả", "Mã thuốc", "Tên thuốc", "Danh mục", "Xuất xứ"};
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
        if (choPhepSuaXoa) {
            actionPanel.add(btnAdd);
        }

        btnUpdate.setFont(new Font("Roboto", Font.BOLD, 14));
        btnUpdate.setIcon(new FlatSVGIcon(getClass().getResource("/img/update.svg")));
        btnUpdate.setText("SỬA");
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

        btnDelete.setFont(new Font("Roboto", Font.BOLD, 14));
        btnDelete.setIcon(new FlatSVGIcon(getClass().getResource("/img/delete.svg")));
        btnDelete.setText("XÓA");
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
        
        String[] tableTitle = {"Mã thuốc", "Tên thuốc", "Danh mục", "Xuất xứ", "Đơn vị tính", "Giá bán", "Tổng tồn", "HSD gần nhất", "Mô tả", "Thành phần"};
        tableModel = new DefaultTableModel(tableTitle, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.getTableHeader().setFont(headerTable);
        table.setModel(tableModel);

        DefaultTableCellRenderer expiredMedicineRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                String maThuoc = table.getValueAt(row, 0).toString();
                Date hanSuDung = hanSuDungTheoMaThuoc.get(maThuoc);
                boolean hetHan = hanSuDung != null && hanSuDung.before(Date.valueOf(LocalDate.now()));
                boolean sapHetHan = hanSuDung != null && !hetHan 
                        && hanSuDung.before(Date.valueOf(LocalDate.now().plusDays(30)));
                if (isSelected) {
                    comp.setForeground(table.getSelectionForeground());
                } else if (hetHan) {
                    comp.setForeground(Color.RED);
                } else if (sapHetHan) {
                    comp.setForeground(new Color(255, 165, 0));
                } else {
                    comp.setForeground(Color.BLACK);
                }
                return comp;
            }
        };
        table.setDefaultRenderer(Object.class, expiredMedicineRenderer);

        table.getColumnModel().getColumn(0).setCellRenderer(expiredMedicineRenderer);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.setFocusable(false);
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        tablePanel.add(jScrollPane1, BorderLayout.CENTER);

        jPanel5.setBackground(new Color(0, 0, 205));
        jPanel5.setMinimumSize(new Dimension(100, 60));
        jPanel5.setPreferredSize(new Dimension(500, 40));
        jPanel5.setLayout(new BorderLayout());

        lblTable.setFont(new Font("Roboto Medium", Font.BOLD, 30));
        lblTable.setForeground(new Color(255, 255, 255));
        lblTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTable.setText("THÔNG TIN THUỐC");
        jPanel5.add(lblTable, BorderLayout.CENTER);

        tablePanel.add(jPanel5, BorderLayout.NORTH);

        add(tablePanel, BorderLayout.CENTER);
        loadDataTable();
    }
    
    private void addEvents() {
        // Sự kiện nút Thêm
        btnAdd.addActionListener(e -> themThuoc());
        
        // Sự kiện nút Sửa
        btnUpdate.addActionListener(e -> suaThuoc());
        
        // Sự kiện nút Xóa
        btnDelete.addActionListener(e -> xoaThuoc());
        
        // Sự kiện nút Info
        btnInfo.addActionListener(e -> xemThongTin());
        
        // Sự kiện nút Reload
        btnReload.addActionListener(e -> {
            txtSearch.setText("");
            loadDataTable();
        });
        
        // Sự kiện tìm kiếm
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
        
        // Sự kiện thay đổi combo box tìm kiếm
        cboxSearch.addActionListener(e -> {
            if (!txtSearch.getText().trim().isEmpty()) {
                timKiem();
            }
        });
    }
    
    private void loadDataTable() {
        try {
            tableModel.setRowCount(0);
            hanSuDungTheoMaThuoc.clear();
            dsThuoc = thuocDAO.getDsThuoc();
            for (Thuoc t : dsThuoc) {
                themDongThuoc(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải dữ liệu: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void themThuoc() {
        if (!choPhepSuaXoa) {
            thongBaoKhongCoQuyen();
            return;
        }
        DialogThemThuoc dialog = new DialogThemThuoc(
            (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this)
        );
        dialog.setVisible(true);
        
        if (dialog.isSuccess()) {
            loadDataTable();
        }
    }
    
    private void suaThuoc() {
        if (!choPhepSuaXoa) {
            thongBaoKhongCoQuyen();
            return;
        }
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn thuốc cần sửa!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maThuoc = tableModel.getValueAt(selectedRow, 0).toString();
        
        try {
            Thuoc thuoc = thuocDAO.getThuocTheoMaThuoc(maThuoc);
            if (thuoc != null) {
                DialogSuaThuoc dialog = new DialogSuaThuoc(
                    (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this), 
                    thuoc
                );
                dialog.setVisible(true);
                
                if (dialog.isSuccess()) {
                    loadDataTable();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi lấy thông tin thuốc: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xoaThuoc() {
        if (!choPhepSuaXoa) {
            thongBaoKhongCoQuyen();
            return;
        }
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn thuốc cần xóa!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maThuoc = tableModel.getValueAt(selectedRow, 0).toString();
        String tenThuoc = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa thuốc:\n" + 
            "Mã: " + maThuoc + "\n" + 
            "Tên: " + tenThuoc + "?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = thuocDAO.xoaThuoc(maThuoc);
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "Xóa thuốc thành công!", 
                        "Thành công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loadDataTable();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể xóa thuốc. Vui lòng thử lại!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Lỗi khi xóa thuốc: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
private void xemThongTin() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, 
            "Vui lòng chọn thuốc để xem thông tin!", 
            "Cảnh báo", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String maThuoc = tableModel.getValueAt(selectedRow, 0).toString();
    
    // Mở dialog chi tiết thông tin thuốc
    DialogThongTinChiTietThuoc dialog = new DialogThongTinChiTietThuoc(
        (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this), 
        maThuoc
    );
    dialog.setVisible(true);
}
    
    private void timKiem() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        String searchType = cboxSearch.getSelectedItem().toString();
        
        if (keyword.isEmpty()) {
            loadDataTable();
            return;
        }
        
        tableModel.setRowCount(0);
        
        for (Thuoc t : dsThuoc) {
            boolean match = false;
            
            switch (searchType) {
                case "Tất cả":
                    match = safeLower(t.getMaThuoc()).contains(keyword) ||
                            safeLower(t.getTenThuoc()).contains(keyword) ||
                            safeLower(layTenDanhMuc(t)).contains(keyword) ||
                            safeLower(t.getXuatXu()).contains(keyword);
                    break;
                case "Mã thuốc":
                    match = safeLower(t.getMaThuoc()).contains(keyword);
                    break;
                case "Tên thuốc":
                    match = safeLower(t.getTenThuoc()).contains(keyword);
                    break;
                case "Danh mục":
                    match = safeLower(layTenDanhMuc(t)).contains(keyword);
                    break;
                case "Xuất xứ":
                    match = safeLower(t.getXuatXu()).contains(keyword);
                    break;
            }
            
            if (match) {
                themDongThuoc(t);
            }
        }
    }

    private void themDongThuoc(Thuoc t) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (t.getHanSuDung() != null) {
            hanSuDungTheoMaThuoc.put(t.getMaThuoc(), new Date(t.getHanSuDung().getTime()));
        }
        tableModel.addRow(new Object[] {
            t.getMaThuoc(),
            t.getTenThuoc(),
            layTenDanhMuc(t),
            t.getXuatXu(),
            t.getDonViTinh(),
            String.format("%,.0f VNĐ", t.getGiaBan()),
            t.getSoLuongTon(),
            t.getHanSuDung() == null ? "" : sdf.format(t.getHanSuDung()),
            t.getMoTa(),
            t.getThanhPhan()
        });
    }

    private String layTenDanhMuc(Thuoc t) {
        if (t.getDanhMucThuoc() == null || t.getDanhMucThuoc().getMaDanhMuc() == null) {
            return "";
        }
        try {
            return dmtDAO.getDanhMucThuocQuaMaDanhMuc(t.getDanhMucThuoc().getMaDanhMuc()).getTenDanhMuc();
        } catch (SQLException e) {
            return t.getDanhMucThuoc().getTenDanhMuc() == null ? "" : t.getDanhMucThuoc().getTenDanhMuc();
        }
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
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
