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

import dao.NhanVienDAO;
import entity.NhanVien;
import entity.TaiKhoan;
import gui.dialog.DialogCapNhatNhanVien;
import gui.dialog.DialogThemNhanVien;
import gui.dialog.DialogThongTinNhanVien;

public class FormQuanLyNhanVien extends JPanel implements ActionListener {
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
    private NhanVienDAO nvdao;
    private ArrayList<NhanVien> dsNhanVien = new ArrayList<>();
    Font headerTable = new Font("Roboto", Font.BOLD, 18);
    public FormQuanLyNhanVien() {
    	taoNoiDung();
    	addActionListeners();
    }

    private void taoNoiDung() {
    	nvdao = new NhanVienDAO();
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
        // Increase right padding from 16 to 32, and top/bottom from 24 to 30
        jPanel1.setLayout(new FlowLayout(FlowLayout.RIGHT, 32, 30));

        jPanel3.setBackground(new Color(255, 255, 255));
        jPanel3.setPreferredSize(new Dimension(584, 50));
        // Increase gap between components from default to 24
        jPanel3.setLayout(new FlowLayout(FlowLayout.TRAILING, 24, 5));

        cboxSearch.setToolTipText("");
        cboxSearch.setPreferredSize(new Dimension(120, 40));
        String[] searchType = {"Tất cả", "Mã", "Tên", "Số điện thoại"};
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
        
        String[] tableTitle = {"Mã nhân viên", "Họ tên", "Chức vụ", "Số điện thoại", "Ngày sinh", "Giới tính", "Địa chỉ", "Email"};
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
        lblTable.setText("THÔNG TIN NHÂN VIÊN");
        jPanel5.add(lblTable, BorderLayout.CENTER);

        tablePanel.add(jPanel5, BorderLayout.NORTH);
        
        add(tablePanel, BorderLayout.CENTER);
    }
    private void loadDataTable() {
        tableModel.setRowCount(0);
    	try {
			dsNhanVien = nvdao.getDSNhanVien();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	for (NhanVien nv : dsNhanVien) {
    		themDongNhanVien(nv);
    	}
    }
 
    public void reloadTable() {
       
        tableModel.setRowCount(0);
        
        
        loadDataTable();
    }
	@Override
	public void actionPerformed(ActionEvent e) {
	    Object obj = e.getSource();
	    if (obj.equals(btnAdd)) {
	        themNhanVien();
	    } else if (obj.equals(btnReload)) {
	        reloadTable();
	    } else if (obj.equals(btnUpdate)) {
	        capNhatNhanVien();
	    } else if (obj.equals(btnDelete)) {
	        xoaNhanVien();
	    } else if (obj.equals(btnInfo)) {
	        xemThongTinNhanVien();
	    }
	}
	private void addActionListeners() {
	    btnAdd.addActionListener(this);
	    btnUpdate.addActionListener(this);
	    btnDelete.addActionListener(this);
	    btnInfo.addActionListener(this);
	    btnReload.addActionListener(this);
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
        cboxSearch.addActionListener(e -> timKiem());
	}

    private void timKiem() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        String searchType = cboxSearch.getSelectedItem().toString();

        if (keyword.isEmpty()) {
            reloadTable();
            return;
        }

        tableModel.setRowCount(0);
        for (NhanVien nv : dsNhanVien) {
            String maNV = safeLower(nv.getMaNV());
            String tenNV = safeLower(nv.getTenNV());
            String sdt = safeLower(nv.getSoDienThoai());
            boolean match = false;

            switch (searchType) {
                case "Mã":
                    match = maNV.contains(keyword);
                    break;
                case "Tên":
                    match = tenNV.contains(keyword);
                    break;
                case "Số điện thoại":
                    match = sdt.contains(keyword);
                    break;
                default:
                    match = maNV.contains(keyword)
                            || tenNV.contains(keyword)
                            || safeLower(nv.getChucVu()).contains(keyword)
                            || sdt.contains(keyword)
                            || safeLower(nv.getGioiTinh()).contains(keyword)
                            || safeLower(nv.getDiaChi()).contains(keyword)
                            || safeLower(nv.getEmail()).contains(keyword);
                    break;
            }

            if (match) {
                themDongNhanVien(nv);
            }
        }
    }

    private void themDongNhanVien(NhanVien nv) {
        tableModel.addRow(new Object[] {
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getChucVu(),
                nv.getSoDienThoai(),
                new SimpleDateFormat("dd/MM/yyyy").format(nv.getNgaySinh()),
                nv.getGioiTinh(),
                nv.getDiaChi(),
                nv.getEmail()
        });
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }
	
	private void themNhanVien() {
	    DialogThemNhanVien dialog = new DialogThemNhanVien(
	        (Frame) SwingUtilities.getWindowAncestor(this), 
	        this
	    );
	    dialog.setVisible(true);
	}
	
	// Phương thức xem thông tin chi tiết nhân viên
	private void xemThongTinNhanVien() {
	    int selectedRow = table.getSelectedRow();
	    
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(this, 
	            "Vui lòng chọn nhân viên cần xem thông tin!", 
	            "Cảnh báo", 
	            JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    
	    try {
	        String maNV = tableModel.getValueAt(selectedRow, 0).toString();
	        NhanVien nv = nvdao.getNhanVienTheoMa(maNV);
	        
	        if (nv != null) {
	            DialogThongTinNhanVien dialog = new DialogThongTinNhanVien(
	                (Frame) SwingUtilities.getWindowAncestor(this), 
	                nv
	            );
	            dialog.setVisible(true);
	        } else {
	            JOptionPane.showMessageDialog(this, 
	                "Không tìm thấy thông tin nhân viên!", 
	                "Lỗi", 
	                JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, 
	            "Lỗi khi tải thông tin nhân viên: " + ex.getMessage(), 
	            "Lỗi", 
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	// Phương thức cập nhật nhân viên
	private void capNhatNhanVien() {
	    int selectedRow = table.getSelectedRow();
	    
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(this, 
	            "Vui lòng chọn nhân viên cần cập nhật!", 
	            "Cảnh báo", 
	            JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    
	    try {
	        String maNV = tableModel.getValueAt(selectedRow, 0).toString();
	        NhanVien nv = nvdao.getNhanVienTheoMa(maNV);
	        
	        if (nv != null) {
	            DialogCapNhatNhanVien dialog = new DialogCapNhatNhanVien(
	                (Frame) SwingUtilities.getWindowAncestor(this), 
	                this,
	                nv
	            );
	            dialog.setVisible(true);
	        } else {
	            JOptionPane.showMessageDialog(this, 
	                "Không tìm thấy thông tin nhân viên!", 
	                "Lỗi", 
	                JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, 
	            "Lỗi khi tải thông tin nhân viên: " + ex.getMessage(), 
	            "Lỗi", 
	            JOptionPane.ERROR_MESSAGE);
	    }
	}

	// Phương thức xóa nhân viên (soft delete)
	private void xoaNhanVien() {
	    int selectedRow = table.getSelectedRow();
	    
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(this, 
	            "Vui lòng chọn nhân viên cần xóa!", 
	            "Cảnh báo", 
	            JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    
	    String maNV = tableModel.getValueAt(selectedRow, 0).toString();
	    String tenNV = tableModel.getValueAt(selectedRow, 1).toString();
	    
	    
	    int confirm = JOptionPane.showConfirmDialog(this, 
	        "Bạn có chắc chắn muốn xóa nhân viên:\n" + 
	        "Mã: " + maNV + "\n" +
	        "Tên: " + tenNV + "?\n\n",
	        "Xác nhận xóa", 
	        JOptionPane.YES_NO_OPTION,
	        JOptionPane.QUESTION_MESSAGE);
	    
	    if (confirm == JOptionPane.YES_OPTION) {
	        try {
	            
	            boolean result = nvdao.xoaNhanVien(maNV);
	            
	            if (result) {
	                JOptionPane.showMessageDialog(this, 
	                    "Xóa nhân viên thành công!\n",
	                    "Thành công", 
	                    JOptionPane.INFORMATION_MESSAGE);
	                
	                // Reload table
	                reloadTable();
	            } else {
	                JOptionPane.showMessageDialog(this, 
	                    "Xóa nhân viên thất bại!", 
	                    "Lỗi", 
	                    JOptionPane.ERROR_MESSAGE);
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(this, 
	                "Lỗi khi xóa nhân viên: " + ex.getMessage(), 
	                "Lỗi", 
	                JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
}
