package gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
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

import dao.ChiTietPhieuDatThuocDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.PhieuDatThuocDAO;
import entity.ChiTietHoaDon;
import entity.ChiTietPhieuDatThuoc;
import entity.HoaDon;
import entity.KhachHang;
import entity.PhieuDatThuoc;
import entity.TaiKhoan;
import entity.Thuoc;
import gui.dialog.DialogChiTietPhieuDatThuoc;
import gui.dialog.DialogThanhToanHoaDon;
import gui.dialog.DialogThanhToanPhieuDatThuoc;

public class FormPhieuDatThuoc extends JPanel {

    // Components giao diện
    private JPanel pnlHeader;
    private JPanel pnlTimKiemWrapper;
    private JPanel pnlTimKiem;
    private JComboBox<String> cboLoaiTimKiem;
    private JComboBox<String> cboTrangThai;
    private JTextField txtTimKiem;
    private JButton btnLamMoi;
    
    private JPanel pnlThaoTac;
    private JButton btnTaoPhieu; 
    private JButton btnThanhToan;
    private JButton btnXemChiTiet;
    
    private JPanel pnlBangDuLieu;
    private JPanel pnlTieuDeBang;
    private JScrollPane scrBang;
    private JTable tblPhieuDat;
    private JLabel lblTieuDe;
    
    // Model & DAO
    private DefaultTableModel tableModel;
    private PhieuDatThuocDAO pdtDAO;
    private ChiTietPhieuDatThuocDAO ctpdtDAO;
    private HoaDonDAO hdDAO;
    private KhachHangDAO khDAO;
    private TaiKhoan taiKhoan;
    
    private Font fontHeaderTable = new Font("Roboto", Font.BOLD, 18);

    public FormPhieuDatThuoc(TaiKhoan tk) {
        this.taiKhoan = tk;
        khoiTaoGiaoDien();
    }

    private void khoiTaoGiaoDien() {
        // Khởi tạo DAO
        khDAO = new KhachHangDAO();
        hdDAO = new HoaDonDAO();
        ctpdtDAO = new ChiTietPhieuDatThuocDAO();
        pdtDAO = new PhieuDatThuocDAO();

        // Setup Panel chính
        setBackground(new Color(230, 245, 245));
        setBorder(new LineBorder(new Color(230, 245, 245), 6, true));
        setMinimumSize(new Dimension(1130, 800));
        setPreferredSize(new Dimension(1130, 800));
        setLayout(new BorderLayout(0, 10));

        // 1. Phần Header (Tìm kiếm + Thao tác)
        pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(255, 255, 255));
        pnlHeader.setBorder(new LineBorder(new Color(232, 232, 232), 2, true));

        // 1.1 Panel Tìm kiếm (Bên phải)
        pnlTimKiemWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 32, 30));
        pnlTimKiemWrapper.setBackground(new Color(255, 255, 255));
        pnlTimKiemWrapper.setPreferredSize(new Dimension(590, 100));

        pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.TRAILING, 24, 5));
        pnlTimKiem.setBackground(new Color(255, 255, 255));
        pnlTimKiem.setPreferredSize(new Dimension(584, 50));

        cboLoaiTimKiem = new JComboBox<>();
        cboLoaiTimKiem.setPreferredSize(new Dimension(120, 40));
        cboLoaiTimKiem.setModel(new DefaultComboBoxModel<>(new String[]{"Tất cả", "Mã", "Trạng thái"}));
        pnlTimKiem.add(cboLoaiTimKiem);

        txtTimKiem = new JTextField();
        txtTimKiem.setToolTipText("Tìm kiếm");
        txtTimKiem.setPreferredSize(new Dimension(240, 40));
        txtTimKiem.setSelectionColor(new Color(230, 245, 245));
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
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
        pnlTimKiem.add(txtTimKiem);

        cboTrangThai = new JComboBox<>(new String[]{"Tất cả", "Đã hoàn thành", "Chưa hoàn thành"});
        cboTrangThai.setPreferredSize(new Dimension(240, 40));
        cboTrangThai.setVisible(false);
        cboTrangThai.addActionListener(e -> timKiem());
        pnlTimKiem.add(cboTrangThai);

        cboLoaiTimKiem.addActionListener(e -> capNhatKieuTimKiem());

        btnLamMoi = new JButton(new FlatSVGIcon(getClass().getResource("/img/reload.svg")));
        btnLamMoi.setToolTipText("Làm mới");
        btnLamMoi.setBorder(null);
        btnLamMoi.setContentAreaFilled(false);
        btnLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLamMoi.setPreferredSize(new Dimension(48, 48));
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            cboTrangThai.setSelectedIndex(0);
            try { loadTableData(); } catch (Exception ex) { ex.printStackTrace(); }
        });
        pnlTimKiem.add(btnLamMoi);

        pnlTimKiemWrapper.add(pnlTimKiem);
        pnlHeader.add(pnlTimKiemWrapper, BorderLayout.CENTER);

        // 1.2 Panel Thao tác (Bên trái)
        pnlThaoTac = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 15));
        pnlThaoTac.setBackground(new Color(255, 255, 255));
        pnlThaoTac.setPreferredSize(new Dimension(600, 100));

        // --- Nút Tạo Phiếu 
        btnTaoPhieu = createActionButton("TẠO PHIẾU", "/img/add.svg"); 
        btnTaoPhieu.addActionListener(e -> {
			try {
				chuyenSangFormTaoPhieu();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        pnlThaoTac.add(btnTaoPhieu);

        // --- Nút Thanh Toán
        btnThanhToan = createActionButton("THANH TOÁN", "/img/ticket.svg");
        btnThanhToan.addActionListener(e -> {
            try { thanhToanPhieuDatThuoc(); } catch (SQLException ex) { ex.printStackTrace(); } catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        pnlThaoTac.add(btnThanhToan);

        // --- Nút Info
        btnXemChiTiet = createActionButton("CHI TIẾT", "/img/info.svg");
        btnXemChiTiet.addActionListener(e -> xemChiTiet());
        pnlThaoTac.add(btnXemChiTiet);

        pnlHeader.add(pnlThaoTac, BorderLayout.WEST);
        add(pnlHeader, BorderLayout.PAGE_START);

        // 2. Phần Bảng dữ liệu
        pnlBangDuLieu = new JPanel(new BorderLayout());
        pnlBangDuLieu.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));

        // 2.1 Tiêu đề bảng
        pnlTieuDeBang = new JPanel(new BorderLayout());
        pnlTieuDeBang.setBackground(new Color(0, 0, 205));
        pnlTieuDeBang.setPreferredSize(new Dimension(500, 40));

        lblTieuDe = new JLabel("DANH SÁCH PHIẾU ĐẶT");
        lblTieuDe.setFont(new Font("Roboto Medium", Font.BOLD, 30));
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setHorizontalAlignment(JLabel.CENTER);
        pnlTieuDeBang.add(lblTieuDe, BorderLayout.CENTER);
        pnlBangDuLieu.add(pnlTieuDeBang, BorderLayout.NORTH);

        // 2.2 Table
        String[] tableTitle = {"Mã phiếu đặt", "Ngày đặt", "Mã khách hàng", "Địa chỉ", "Hình thức thanh toán", "Trạng thái"};
        tableModel = new DefaultTableModel(tableTitle, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPhieuDat = new JTable(tableModel);
        tblPhieuDat.getTableHeader().setFont(fontHeaderTable);
        tblPhieuDat.setRowHeight(40);
        tblPhieuDat.setFocusable(false);
        tblPhieuDat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPhieuDat.setShowHorizontalLines(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblPhieuDat.setDefaultRenderer(Object.class, centerRenderer);
        tblPhieuDat.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblPhieuDat.getColumnModel().getColumn(0).setPreferredWidth(30);
        
        if (tblPhieuDat.getColumnModel().getColumnCount() > 1) {
            tblPhieuDat.getColumnModel().getColumn(1).setPreferredWidth(150);
        }

        scrBang = new JScrollPane(tblPhieuDat);
        pnlBangDuLieu.add(scrBang, BorderLayout.CENTER);

        add(pnlBangDuLieu, BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        try {
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tạo button
    private JButton createActionButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Roboto", Font.BOLD, 14));
        btn.setIcon(new FlatSVGIcon(getClass().getResource(iconPath)));
        btn.setBorder(null);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        int textWidth = btn.getFontMetrics(btn.getFont()).stringWidth(text);
        int minWidth = textWidth + 24;
        int buttonWidth = Math.max(90, minWidth);
        Dimension buttonSize = new Dimension(buttonWidth, 90);
        btn.setPreferredSize(buttonSize);
        btn.setMinimumSize(buttonSize);
        return btn;
    }

    // ================== LOGIC XỬ LÝ ==================

    /**
     * MỚI: Chuyển sang form FormPhieuDatThuoc (đã tạo ở bước trước)
     * @throws SQLException 
     */
    private void chuyenSangFormTaoPhieu() throws SQLException {
        
        JPanel parent = (JPanel) this.getParent();
        if (parent != null) {
            // Xóa nội dung hiện tại (Form danh sách)
			parent.removeAll();
			
			// Thêm Form tạo phiếu mới (FormPhieuDatThuoc)
			parent.add(new FormThemPhieuDatThuoc(taiKhoan)); 
			
			// Cập nhật giao diện
			parent.revalidate();
			parent.repaint();
        }
    }

    private void loadTableData() throws Exception {
        tableModel.setRowCount(0);
        ArrayList<PhieuDatThuoc> dsPDT = pdtDAO.getDsPhieuDatThuoc();
        for (PhieuDatThuoc pdt : dsPDT) {
            themDongPhieuDat(pdt);
        }
    }

    private void capNhatKieuTimKiem() {
        boolean timTheoTrangThai = "Trạng thái".equals(cboLoaiTimKiem.getSelectedItem().toString());
        txtTimKiem.setVisible(!timTheoTrangThai);
        cboTrangThai.setVisible(timTheoTrangThai);
        if (timTheoTrangThai) {
            txtTimKiem.setText("");
        } else {
            cboTrangThai.setSelectedIndex(0);
        }
        pnlTimKiem.revalidate();
        pnlTimKiem.repaint();
        timKiem();
    }

    private void timKiem() {
        String loaiTimKiem = cboLoaiTimKiem.getSelectedItem().toString();
        String keyword = "Trạng thái".equals(loaiTimKiem)
                ? cboTrangThai.getSelectedItem().toString().trim().toLowerCase()
                : txtTimKiem.getText().trim().toLowerCase();
        if ("Tất cả".equalsIgnoreCase(keyword)) {
            keyword = "";
        }

        try {
            tableModel.setRowCount(0);
            for (PhieuDatThuoc pdt : pdtDAO.getDsPhieuDatThuoc()) {
                String maPhieuDat = safeLower(pdt.getMaPhieuDat());
                String trangThai = safeLower(pdt.getTrangThai());
                boolean match = keyword.isEmpty();

                if (!match) {
                    switch (loaiTimKiem) {
                        case "Mã":
                            match = maPhieuDat.contains(keyword);
                            break;
                        case "Trạng thái":
                            match = trangThai.contains(keyword);
                            break;
                        default:
                            match = maPhieuDat.contains(keyword)
                                    || safeLower(pdt.getKhachHang() != null ? pdt.getKhachHang().getMaKH() : "").contains(keyword)
                                    || safeLower(pdt.getDiaChi()).contains(keyword)
                                    || safeLower(pdt.getHinhThucThanhToan()).contains(keyword)
                                    || trangThai.contains(keyword);
                            break;
                    }
                }

                if (match) {
                    themDongPhieuDat(pdt);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm phiếu đặt: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void themDongPhieuDat(PhieuDatThuoc pdt) {
        tableModel.addRow(new Object[] {
            pdt.getMaPhieuDat(),
            pdt.getNgayDat(),
            pdt.getKhachHang() != null ? pdt.getKhachHang().getMaKH() : "",
            pdt.getDiaChi(),
            pdt.getHinhThucThanhToan(),
            pdt.getTrangThai()
        });
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }

    private void thanhToanPhieuDatThuoc() throws Exception {
    	int row = tblPhieuDat.getSelectedRow();
    	if (row < 0) {
    		JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu đặt cần thanh toán!");
    		return;
    	}
    	String trangThai = (String) tblPhieuDat.getValueAt(row, 5);
    	if (trangThai.equalsIgnoreCase("Đã hoàn thành")) {
    		JOptionPane.showMessageDialog(this, "Phiếu đặt này đã thanh toán!");
    		return;
    	}
    	String maPhieuDat = tblPhieuDat.getValueAt(row, 0).toString();
    	PhieuDatThuoc pdt = pdtDAO.getPhieuDatThuocQuaMaPhieuDat(maPhieuDat);
    	ArrayList<ChiTietPhieuDatThuoc> dsPhieuDatThuoc = ctpdtDAO.getChiTietPhieuDatThuocQuaMaPhieuDatThuoc(maPhieuDat);
    	double tongTien = 0;
    	for (ChiTietPhieuDatThuoc ctpdt : dsPhieuDatThuoc) {
    		tongTien += ctpdt.getThanhTien();
    	}
        Window window = SwingUtilities.getWindowAncestor(this);
        Frame frame = (window instanceof Frame) ? (Frame) window : null;
        
        DialogThanhToanPhieuDatThuoc dialog = new DialogThanhToanPhieuDatThuoc(
            frame, maPhieuDat, pdt.getNgayDat(), pdt.getKhachHang().getMaKH(), dsPhieuDatThuoc, tongTien, taiKhoan.getNhanVien()
        );
        dialog.setOnThanhToanThanhCong(() -> {
            try {
                loadTableData();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            loadTableData();
        }
        
    }

    private void xemChiTiet() {
        int selectedRow = tblPhieuDat.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn phiếu đặt thuốc cần xem chi tiết!",
                "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maPhieuDat = tblPhieuDat.getValueAt(selectedRow, 0).toString();

        DialogChiTietPhieuDatThuoc dialog = new DialogChiTietPhieuDatThuoc(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            maPhieuDat
        );
        dialog.setVisible(true);
    }
}
