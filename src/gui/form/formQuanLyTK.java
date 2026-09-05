package gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
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

import dao.TaiKhoanDAO;
import entity.TaiKhoan;
import gui.dialog.DialogCapNhatTaiKhoan;
import gui.dialog.DialogThemTaiKhoan;
import gui.dialog.DialogThongTinTaiKhoan;

public class FormQuanLyTK extends JPanel implements ActionListener {
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
    private TaiKhoanDAO tkDao;
    Font headerTable = new Font("Roboto", Font.BOLD, 18);
    
    public FormQuanLyTK() {
    	taoNoiDung();
    }

    private void taoNoiDung() {
        headerPanel = new JPanel();
        tkDao = new TaiKhoanDAO();
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
        String[] searchType = {"Tất cả", "Tên", "Mã", "Vai trò", "Trạng thái"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(searchType);
        cboxSearch.setModel(model);
        jPanel3.add(cboxSearch);

        txtSearch.setToolTipText("Tìm kiếm");
        txtSearch.setPreferredSize(new Dimension(240, 40));
        txtSearch.setSelectionColor(new Color(230, 245, 245));
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timKiemTaiKhoan();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timKiemTaiKhoan();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timKiemTaiKhoan();
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
        
        String[] tableTitle = {"Tên đăng nhập", "Mật khẩu", "Trạng thái", "Mã nhân viên", "Vai trò"};
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
        loadDataTable();
        
        jPanel5.setBackground(new Color(0, 0, 205));
        jPanel5.setMinimumSize(new Dimension(100, 60));
        jPanel5.setPreferredSize(new Dimension(500, 40));
        jPanel5.setLayout(new BorderLayout());

        lblTable.setFont(new Font("Roboto Medium", Font.BOLD, 30));
        lblTable.setForeground(new Color(255, 255, 255));
        lblTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTable.setText("THÔNG TIN TÀI KHOẢN");
        jPanel5.add(lblTable, BorderLayout.CENTER);

        tablePanel.add(jPanel5, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        
        // Add action listeners
        btnAdd.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);
        btnReload.addActionListener(this);
        btnInfo.addActionListener(this);
        cboxSearch.addActionListener(e -> timKiemTaiKhoan());
    }
    
    private void loadDataTable() {
    	ArrayList<TaiKhoan> dsTK = null;
    	try {
			dsTK = tkDao.dsTaiKhoan();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	for (TaiKhoan tk : dsTK) {
    		tableModel.addRow(new Object[] {
    		    tk.getTenDangNhap(), 
    		    tk.getMatKhau(), 
    		    tk.getTrangThai(), 
    		    tk.getNhanVien().getMaNV(), 
    		    tk.getVaiTro()
    		});
    	}
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj.equals(btnAdd)) {
			try {
				themTaiKhoan();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if (obj.equals(btnUpdate)) {
			try {
				capNhatTaiKhoan();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if (obj.equals(btnDelete)) {
			try {
				xoaTaiKhoan();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if (obj.equals(btnReload)) {
			reloadTable();
		}
		else if (obj.equals(btnInfo)) {
			xemThongTin();
		}
	}
	
	/**
	 * Mở dialog thêm tài khoản mới
	 */
	private void themTaiKhoan() throws SQLException {
	    DialogThemTaiKhoan dialog = new DialogThemTaiKhoan(
		        (Frame) SwingUtilities.getWindowAncestor(this), 
		        this
		    );
		dialog.setVisible(true);
	}

	/**
	 * Mở dialog cập nhật tài khoản đã chọn
	 */
	private void capNhatTaiKhoan() throws SQLException {
	    int selectedRow = table.getSelectedRow();
	    
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(this, 
	            "Vui lòng chọn tài khoản cần cập nhật!", 
	            "Thông báo", 
	            JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    
	    // Lấy tên đăng nhập từ bảng
	    String tenDangNhap = tableModel.getValueAt(selectedRow, 0).toString();
	    
	    // Tìm tài khoản trong database
	    TaiKhoan tk = tkDao.timTaiKhoanTheoTen(tenDangNhap);
	    
	    if (tk == null) {
	        JOptionPane.showMessageDialog(this, 
	            "Không tìm thấy tài khoản!", 
	            "Lỗi", 
	            JOptionPane.ERROR_MESSAGE);
	        return;
	    }
	    
	    // Mở dialog cập nhật với dữ liệu tài khoản
	    DialogCapNhatTaiKhoan dialog = new DialogCapNhatTaiKhoan(
	        (Frame) SwingUtilities.getWindowAncestor(this), 
	        this,
	        tk
	    );
	    dialog.setVisible(true);
	}
	
	/**
	 * Xóa tài khoản đã chọn
	 */
	private void xoaTaiKhoan() throws SQLException {
	    int selectedRow = table.getSelectedRow();
	    
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(this, 
	            "Vui lòng chọn tài khoản cần xóa!", 
	            "Thông báo", 
	            JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    
	    String tenDangNhap = tableModel.getValueAt(selectedRow, 0).toString();
	    
	    int confirm = JOptionPane.showConfirmDialog(this, 
	        "Bạn có chắc chắn muốn xóa tài khoản '" + tenDangNhap + "'?", 
	        "Xác nhận xóa", 
	        JOptionPane.YES_NO_OPTION,
	        JOptionPane.QUESTION_MESSAGE);
	    
	    if (confirm == JOptionPane.YES_OPTION) {
	        boolean result = tkDao.xoaTaiKhoan(tenDangNhap);
	        
	        if (result) {
	            JOptionPane.showMessageDialog(this, 
	                "Xóa tài khoản thành công!", 
	                "Thành công", 
	                JOptionPane.INFORMATION_MESSAGE);
	            reloadTable();
	        } else {
	            JOptionPane.showMessageDialog(this, 
	                "Xóa tài khoản thất bại!", 
	                "Lỗi", 
	                JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
	
	/**
	 * Tìm kiếm tài khoản theo tiêu chí đã chọn.
	 */
	private void timKiemTaiKhoan() {
	    String tuKhoa = txtSearch.getText().trim();
	    Object selected = cboxSearch.getSelectedItem();
	    String loaiTimKiem = selected != null ? selected.toString() : "Tất cả";
	    
	    if (tuKhoa.isEmpty()) {
	        reloadTable();
	        return;
	    }
	    
	    try {
	        ArrayList<TaiKhoan> dsKetQua = tkDao.timKiemTaiKhoan(tuKhoa, loaiTimKiem);
	        tableModel.setRowCount(0);
	        for (TaiKhoan tk : dsKetQua) {
	            tableModel.addRow(new Object[] {
	                    tk.getTenDangNhap(),
	                    tk.getMatKhau(),
	                    tk.getTrangThai(),
	                    tk.getNhanVien().getMaNV(),
	                    tk.getVaiTro()
	            });
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this,
	                "Lỗi khi tìm kiếm tài khoản: " + e.getMessage(),
	                "Lỗi",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}

	/**
	 * Xem thông tin chi tiết tài khoản
	 */
	private void xemThongTin() {
	    int selectedRow = table.getSelectedRow();
	    
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(this, 
	            "Vui lòng chọn tài khoản cần xem!", 
	            "Thông báo", 
	            JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    
	    String tenDangNhap = tableModel.getValueAt(selectedRow, 0).toString();
	    String trangThai = tableModel.getValueAt(selectedRow, 2).toString();
	    String maNV = tableModel.getValueAt(selectedRow, 3).toString();
	    String vaiTro = tableModel.getValueAt(selectedRow, 4).toString();
	    
    Window window = SwingUtilities.getWindowAncestor(this);
    Frame frame = (window instanceof Frame) ? (Frame) window : null;
    DialogThongTinTaiKhoan dialog = new DialogThongTinTaiKhoan(frame, tenDangNhap, maNV, vaiTro, trangThai);
    dialog.setVisible(true);
	}

	/**
	 * Reload lại dữ liệu bảng
	 */
	public void reloadTable() {
		txtSearch.setText("");
		tableModel.setRowCount(0);
        loadDataTable();
	}
}
