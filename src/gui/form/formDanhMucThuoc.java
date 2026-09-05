package gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;

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
import entity.DanhMucThuoc;
import entity.TaiKhoan;
import gui.dialog.DialogCapNhatDanhMucThuoc;
import gui.dialog.DialogThemDanhMucThuoc;

public class FormDanhMucThuoc extends JPanel {
    private JPanel actionPanel;
    private JButton btnAdd;
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
    private DanhMucThuocDAO dmtDAO;
    private boolean choPhepSuaXoa = true;
    
    Font headerTable = new Font("Roboto", Font.BOLD, 18);
    public FormDanhMucThuoc() throws SQLException {
    	this(null);
    }

    public FormDanhMucThuoc(TaiKhoan taiKhoan) throws SQLException {
    	choPhepSuaXoa = coQuyenQuanLy(taiKhoan);
    	taoNoiDung();
    }

    private void taoNoiDung() throws SQLException  {
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
        // Increase right padding from 16 to 32, and top/bottom from 24 to 30
        jPanel1.setLayout(new FlowLayout(FlowLayout.RIGHT, 32, 30));

        jPanel3.setBackground(new Color(255, 255, 255));
        jPanel3.setPreferredSize(new Dimension(584, 50));
        // Increase gap between components from default to 24
        jPanel3.setLayout(new FlowLayout(FlowLayout.TRAILING, 24, 5));

        cboxSearch.setToolTipText("");
        cboxSearch.setPreferredSize(new Dimension(120, 40));
        String[] searchType = {"Tất cả", "Mã danh mục", "Tên danh mục"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(searchType);
        cboxSearch.setModel(model);
        cboxSearch.addActionListener(e -> search());
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
        btnReload.setContentAreaFilled(false); // REMOVE BUTTON BACKGROUND
        btnReload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReload.setFocusPainted(false);
        btnReload.setFocusable(false);
        btnReload.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReload.setPreferredSize(new Dimension(48, 48)); // Slightly larger button
        btnReload.addActionListener(e -> {
            txtSearch.setText("");
            try {
                reloadTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Loi khi tai lai du lieu danh muc thuoc!", "Loi SQL", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        jPanel3.add(btnReload);

        jPanel1.add(jPanel3);

        headerPanel.add(jPanel1, BorderLayout.CENTER);

        actionPanel.setBackground(new Color(255, 255, 255));
        actionPanel.setPreferredSize(new Dimension(600, 100));
        // Increase gap between buttons from 6 to 24, vertical gap 15
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
        btnAdd.addActionListener(e -> {
        	themDanhMucThuoc();
        });
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
        
        btnUpdate.addActionListener(e -> {
        	capNhatDanhMucThuoc();
        });
        if (choPhepSuaXoa) {
            actionPanel.add(btnUpdate);
        }

        headerPanel.add(actionPanel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.PAGE_START);

        tablePanel.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        tablePanel.setLayout(new BorderLayout());
        
        String[] tableTitle = {"Mã danh mục", "Tên danh mục"};
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

        tablePanel.add(jScrollPane1, BorderLayout.CENTER);

        jPanel5.setBackground(new Color(0, 0, 205));
        jPanel5.setMinimumSize(new Dimension(100, 60));
        jPanel5.setPreferredSize(new Dimension(500, 40));
        jPanel5.setLayout(new BorderLayout());

        lblTable.setFont(new Font("Roboto Medium", Font.BOLD, 30));
        lblTable.setForeground(new Color(255, 255, 255));
        lblTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTable.setText("THÔNG TIN DANH MỤC THUỐC");
        jPanel5.add(lblTable, BorderLayout.CENTER);

        tablePanel.add(jPanel5, BorderLayout.NORTH);

        add(tablePanel, BorderLayout.CENTER);
        
        loadDataTable();
    }

    private void search() {
        String keyword = normalize(txtSearch.getText().trim());
        int searchIndex = cboxSearch.getSelectedIndex();

        try {
            tableModel.setRowCount(0);
            ArrayList<DanhMucThuoc> dsDMT = dmtDAO.getDsDanhMucThuoc();

            for (DanhMucThuoc dmt : dsDMT) {
                String maDanhMuc = dmt.getMaDanhMuc();
                String tenDanhMuc = dmt.getTenDanhMuc();

                if (keyword.isEmpty()
                        || (searchIndex == 0 && (containsKeyword(maDanhMuc, keyword) || containsKeyword(tenDanhMuc, keyword)))
                        || (searchIndex == 1 && containsKeyword(maDanhMuc, keyword))
                        || (searchIndex == 2 && containsKeyword(tenDanhMuc, keyword))) {
                    tableModel.addRow(new Object[] { maDanhMuc, tenDanhMuc });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Loi khi tim kiem danh muc thuoc!", "Loi SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean containsKeyword(String value, String keyword) {
        return normalize(value).contains(keyword);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replace('đ', 'd')
                .replace('Đ', 'D');
        return normalized.toLowerCase(Locale.ROOT);
    }
    
    private void capNhatDanhMucThuoc() {
    	if (!choPhepSuaXoa) {
    		thongBaoKhongCoQuyen();
    		return;
    	}
    	int selectedRow = table.getSelectedRow();
    	if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục thuốc cần cập nhật!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
    	try {
			DanhMucThuoc dmt = dmtDAO.getDanhMucThuocQuaMaDanhMuc((String)table.getValueAt(selectedRow, 0));
			DialogCapNhatDanhMucThuoc dialog = new DialogCapNhatDanhMucThuoc((Frame) SwingUtilities.getWindowAncestor(this),this,dmt);
			dialog.setVisible(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadDataTable() throws SQLException {
        tableModel.setRowCount(0);
    	ArrayList<DanhMucThuoc> dsDMT = dmtDAO.getDsDanhMucThuoc();
    	for (DanhMucThuoc dmt : dsDMT) {
    		tableModel.addRow(new Object[] {dmt.getMaDanhMuc(), dmt.getTenDanhMuc()});
    	}
    }
    
    private void themDanhMucThuoc() {
    	DialogThemDanhMucThuoc dialog = new DialogThemDanhMucThuoc(
    			(Frame) SwingUtilities.getWindowAncestor(this),this);
    	dialog.setVisible(true);
    }

	public void reloadTable() throws SQLException {
		loadDataTable();
		
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
