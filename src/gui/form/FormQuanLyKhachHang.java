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

import dao.KhachHangDAO;
import entity.KhachHang;
import entity.TaiKhoan;
import gui.dialog.DialogCapNhatKhachHang;
import gui.dialog.DialogThemKhachHang;
import gui.dialog.DialogThongTinKhachHang;

public class FormQuanLyKhachHang extends JPanel implements ActionListener {
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
    private KhachHangDAO khDAO;
    private ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
    private boolean choPhepXoa = true;
    Font headerTable = new Font("Roboto", Font.BOLD, 18);
    
    public FormQuanLyKhachHang() {
        this(null);
    }

    public FormQuanLyKhachHang(TaiKhoan taiKhoan) {
        choPhepXoa = coQuyenQuanLy(taiKhoan);
        khDAO = new KhachHangDAO();
        taoNoiDung();
        addActionListeners();
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
        String[] searchType = {"Tất cả", "Mã", "Tên", "Số điện thoại", "Email"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(searchType);
        cboxSearch.setModel(model);
        jPanel3.add(cboxSearch);

        txtSearch.setToolTipText("Tìm kiếm");
        txtSearch.setPreferredSize(new Dimension(240, 40));
        txtSearch.setSelectionColor(new Color(230, 245, 245));
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }
        });
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
        actionPanel.add(btnUpdate);

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
        if (choPhepXoa) {
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
        
        String[] tableTitle = {"Mã khách hàng", "Họ Tên", "Số điện thoại", "Email"};
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

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.setFocusable(false);
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
        }
        loadDataTable();

        tablePanel.add(jScrollPane1, BorderLayout.CENTER);

        jPanel5.setBackground(new Color(0, 0, 205));
        jPanel5.setMinimumSize(new Dimension(100, 60));
        jPanel5.setPreferredSize(new Dimension(500, 40));
        jPanel5.setLayout(new BorderLayout());

        lblTable.setFont(new Font("Roboto Medium", Font.BOLD, 30));
        lblTable.setForeground(new Color(255, 255, 255));
        lblTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTable.setText("THÔNG TIN KHÁCH HÀNG");
        jPanel5.add(lblTable, BorderLayout.CENTER);

        tablePanel.add(jPanel5, BorderLayout.NORTH);

        add(tablePanel, BorderLayout.CENTER);
    }
    
    private void loadDataTable() {
        tableModel.setRowCount(0);
        try {
            dsKhachHang = khDAO.getDSKhachHang();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if (dsKhachHang != null) {
            for (KhachHang kh : dsKhachHang) {
                themDongKhachHang(kh);
            }
        }
    }
    
    public void reloadTable() {
        tableModel.setRowCount(0);
        loadDataTable();
    }
    
    private void addActionListeners() {
        btnAdd.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);
        btnInfo.addActionListener(this);
        btnReload.addActionListener(this);
        cboxSearch.addActionListener(e -> search());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj.equals(btnAdd)) {
            themKhachHang();
        } else if (obj.equals(btnReload)) {
            reloadTable();
        } else if (obj.equals(btnUpdate)) {
            capNhatKhachHang();
        } else if (obj.equals(btnDelete)) {
            xoaKhachHang();
        } else if (obj.equals(btnInfo)) {
            xemThongTinKhachHang();
        }
    }
    
    private void themKhachHang() {
        DialogThemKhachHang dialog = new DialogThemKhachHang(
            (Frame) SwingUtilities.getWindowAncestor(this), 
            this
        );
        dialog.setVisible(true);
    }
    
    private void capNhatKhachHang() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn khách hàng cần cập nhật!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String maKH = tableModel.getValueAt(selectedRow, 0).toString();
            KhachHang kh = khDAO.getKhachHangTheoMa(maKH);
            
            if (kh != null) {
                DialogCapNhatKhachHang dialog = new DialogCapNhatKhachHang(
                    (Frame) SwingUtilities.getWindowAncestor(this), 
                    this,
                    kh
                );
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Không tìm thấy thông tin khách hàng!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải thông tin khách hàng: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xoaKhachHang() {
        if (!choPhepXoa) {
            thongBaoKhongCoQuyen();
            return;
        }
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn khách hàng cần xóa!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maKH = tableModel.getValueAt(selectedRow, 0).toString();
        String tenKH = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa khách hàng:\n" + 
            "Mã: " + maKH + "\n" +
            "Tên: " + tenKH + "?\n\n" +
            "Khách hàng sẽ không còn hiển thị trong danh sách, nhưng lịch sử hóa đơn và phiếu đặt vẫn được giữ.",
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean result = khDAO.xoaKhachHang(maKH);
                
                if (result) {
                    JOptionPane.showMessageDialog(this, 
                        "Xóa khách hàng thành công!", 
                        "Thành công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reload table
                    reloadTable();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Xóa khách hàng thất bại!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Lỗi khi xóa khách hàng: " + ex.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void search() 
    {
        String searchText = txtSearch.getText().trim().toLowerCase();
        Object selected = cboxSearch.getSelectedItem();
        String searchCriteria = selected != null ? selected.toString() : "Tất cả";

        if (searchText.isEmpty()) {
            reloadTable();
            return;
        }

        tableModel.setRowCount(0);
        for (KhachHang kh : dsKhachHang) {
            String maKH = safeLower(kh.getMaKH());
            String hoTen = safeLower(kh.getHoTen());
            String soDienThoai = safeLower(kh.getSoDienThoai());
            String email = safeLower(kh.getEmail());
            boolean match = false;

            switch (searchCriteria) 
            {
                case "Mã":
                    match = maKH.contains(searchText);
                    break;
                case "Tên":
                    match = hoTen.contains(searchText);
                    break;
                case "Số điện thoại":
                    match = soDienThoai.contains(searchText);
                    break;
                case "Email":
                    match = email.contains(searchText);
                    break;
                case "Tất cả":
                default:
                    match = maKH.contains(searchText)
                            || hoTen.contains(searchText)
                            || soDienThoai.contains(searchText)
                            || email.contains(searchText);
                    break;
            }

            if (match) {
                themDongKhachHang(kh);
            }
        }
    }

    private void themDongKhachHang(KhachHang kh) {
        tableModel.addRow(new Object[] {
            kh.getMaKH(),
            kh.getHoTen(),
            kh.getSoDienThoai(),
            kh.getEmail()
        });
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

    
    private void xemThongTinKhachHang() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn khách hàng cần xem thông tin!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String maKH = tableModel.getValueAt(selectedRow, 0).toString();
            KhachHang kh = khDAO.getKhachHangTheoMa(maKH);
            
            if (kh != null) {
                DialogThongTinKhachHang dialog = new DialogThongTinKhachHang(
                    (Frame) SwingUtilities.getWindowAncestor(this), 
                    kh
                );
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Không tìm thấy thông tin khách hàng!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải thông tin khách hàng: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
