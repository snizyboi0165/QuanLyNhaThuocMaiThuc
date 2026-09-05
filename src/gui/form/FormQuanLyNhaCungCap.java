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

import javax.swing.DefaultComboBoxModel; // THÊM IMPORT
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
import javax.swing.event.DocumentEvent; // THÊM IMPORT
import javax.swing.event.DocumentListener; // THÊM IMPORT
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.NhaCungCapDAO; 
import entity.NhaCungCap;
import gui.dialog.DialogCapNhatNhaCungCap;
import gui.dialog.DialogThemNhaCungCap;
import gui.dialog.DialogThongTinNhaCungCap;

public class FormQuanLyNhaCungCap extends JPanel implements ActionListener {
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
    Font headerTable = new Font("Roboto", Font.BOLD, 18);
    private NhaCungCapDAO nccDAO;

    public FormQuanLyNhaCungCap() {
    	nccDAO = new NhaCungCapDAO(); 
    	taoNoiDung();
        loadDataTable(); 
        addActionListeners(); 
    }
    
    // =======================================================
    // === PHẦN TÌM KIẾM ĐƯỢC THÊM MỚI VÀ CHỈNH SỬA
    // =======================================================

    private void search() {
        String searchText = txtSearch.getText().trim();
        String searchCriteria = cboxSearch.getSelectedItem().toString();

        try {
            // Nếu ô tìm kiếm trống, tải lại toàn bộ danh sách
            if (searchText.isEmpty()) {
                reloadTable();
                return;
            }
            
            ArrayList<NhaCungCap> dsNCC = nccDAO.timKiemNhaCungCap(searchText, searchCriteria);
            
            tableModel.setRowCount(0); // Xóa dữ liệu cũ
            
            if (dsNCC.isEmpty()) {
                // Tùy chọn: Hiển thị thông báo không tìm thấy kết quả
            } else {
                for (NhaCungCap ncc : dsNCC) {
                    tableModel.addRow(new Object[] {
                        ncc.getMaNCC(),
                        ncc.getTenNCC(),
                        ncc.getSoDienThoai()
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm nhà cung cấp!", "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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

        // === CHỈNH SỬA COMBOBOX ===
        cboxSearch.setToolTipText("Tiêu chí tìm kiếm");
        cboxSearch.setPreferredSize(new Dimension(120, 40));
        String[] searchType = {"Tất cả", "Mã", "Tên", "Số điện thoại"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(searchType);
        cboxSearch.setModel(model);
        jPanel3.add(cboxSearch);

        // === THÊM DOCUMENTLISTENER CHO TXTSEARCH ===
        txtSearch.setToolTipText("Nhập thông tin tìm kiếm");
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
        actionPanel.add(btnDelete);
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
        String[] tableTitle = {"Mã nhà cung cấp", "Tên nhà cung cấp", "Số điện thoại"};
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
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
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
        lblTable.setText("THÔNG TIN NHÀ CUNG CẤP");
        jPanel5.add(lblTable, BorderLayout.CENTER);
        tablePanel.add(jPanel5, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    
    private void loadDataTable() {
        tableModel.setRowCount(0); // Xóa hết dữ liệu cũ
        try {
            ArrayList<NhaCungCap> dsNCC = nccDAO.getDSNhaCungCap();
            for (NhaCungCap ncc : dsNCC) {
                tableModel.addRow(new Object[] {
                    ncc.getMaNCC(),
                    ncc.getTenNCC(),
                    ncc.getSoDienThoai()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu nhà cung cấp!", "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public void reloadTable() {
        txtSearch.setText(""); // Xóa ô tìm kiếm khi reload
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
            themNhaCungCap();
        } else if (o.equals(btnReload)) {
            reloadTable();
        } else if (o.equals(btnUpdate)) {
            capNhatNhaCungCap();
        } else if (o.equals(btnDelete)) {
            xoaNhaCungCap();
        } else if (o.equals(btnInfo)) {
            xemThongTin();
        }
    }
    
    private void themNhaCungCap() {
        DialogThemNhaCungCap dialog = new DialogThemNhaCungCap(
            (Frame) SwingUtilities.getWindowAncestor(this), this
        );
        dialog.setVisible(true);
    }

    private void capNhatNhaCungCap() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần cập nhật!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String maNCC = tableModel.getValueAt(selectedRow, 0).toString();
            NhaCungCap ncc = nccDAO.getNhaCungCapTheoMa(maNCC);
            if (ncc != null) {
                DialogCapNhatNhaCungCap dialog = new DialogCapNhatNhaCungCap(
                    (Frame) SwingUtilities.getWindowAncestor(this), this, ncc
                );
                dialog.setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải thông tin nhà cung cấp!", "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaNhaCungCap() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maNCC = tableModel.getValueAt(selectedRow, 0).toString();
        String tenNCC = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa nhà cung cấp '" + tenNCC + "'?\nLưu ý: Hành động này không thể hoàn tác.",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (nccDAO.xoaNhaCungCap(maNCC)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    reloadTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Không thể xóa nhà cung cấp này vì có dữ liệu liên quan (ví dụ: phiếu nhập).", "Lỗi ràng buộc khóa ngoại", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xemThongTin() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp để xem thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String maNCC = tableModel.getValueAt(selectedRow, 0).toString();
            NhaCungCap ncc = nccDAO.getNhaCungCapTheoMa(maNCC);
            if (ncc != null) {
                DialogThongTinNhaCungCap dialog = new DialogThongTinNhaCungCap(
                    (Frame) SwingUtilities.getWindowAncestor(this), ncc
                );
                dialog.setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải thông tin nhà cung cấp!", "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
}
