package gui.dialog;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.NhanVienDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.Thue;
import gui.form.FormLapHoaDon;
import entity.NhanVien;


public class DialogThanhToanHoaDon extends JDialog  {
    
    private JLabel lblMaHoaDon;
    private JLabel lblNgayLap;
    private JLabel lblNhanVien;
    private JLabel lblKhachHang;
    private JLabel lblSoDienThoai;
    private JLabel lblMaPhieuDat;
    private JTable tableChiTiet;
    private DefaultTableModel tableModel;
    private JTextField txtTongTien;
    private JTextField txtThue;
    private JTextField txtGiamGia;
    private JTextField txtThanhTien;
    private JTextField txtTienNhanVao;
    private JTextField txtTienThua;
    private JButton btnXacNhan;
    private JButton btnHuy;
    private boolean confirmed = false;
    private NhanVienDAO nhanVienDAO;
	private HoaDonDAO hdDAO;
	private KhachHangDAO khDAO;
	private JPanel mainPanel;
	private KhuyenMai khuyenMaiApDung;
//	private Date ngayLap;
	private boolean isThanhToan = false;
    /**
     * Constructor
     */
    public DialogThanhToanHoaDon(Frame parent, String maHoaDon, String tenKhachHang, 
                               String soDienThoai, ArrayList<ChiTietHoaDon> dsChiTietHoaDon, 
                               double tongTienGoc, double giamGia, double tienThue, double thanhTien,
                               KhuyenMai khuyenMaiApDung, Thue thueApDung,
                               double tienNhanVao, String maNhanVien, String maPhieuDat) {
        super(parent, "Chi Tiết Hóa Đơn", true);
        nhanVienDAO = new NhanVienDAO();
        hdDAO = new HoaDonDAO();
        khDAO = new KhachHangDAO();
        this.khuyenMaiApDung = khuyenMaiApDung;
        initComponents(maHoaDon, tenKhachHang, soDienThoai, dsChiTietHoaDon,
                tongTienGoc, giamGia, tienThue, thanhTien, thueApDung,
                tienNhanVao, maNhanVien, maPhieuDat);
        setLocationRelativeTo(parent);
        
    }
    
    /**
     * Khởi tạo các components
     */
    private void initComponents(String maHoaDon, String tenKhachHang, String soDienThoai,
                               ArrayList<ChiTietHoaDon> dsChiTietHoaDon, double tongTienGoc,
                               double giamGia, double tienThue, double thanhTien, Thue thueApDung,
                               double tienNhanVao, String maNhanVien, String maPhieuDat) {
        
        setSize(900, 800);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // ===== TITLE PANEL =====
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(900, 60));
        
        JLabel lblTitle = new JLabel("CHI TIẾT HÓA ĐƠN");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // ===== MAIN PANEL =====
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        // ===== THÔNG TIN HÓA ĐƠN - LAYOUT NGANG =====
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setMaximumSize(new Dimension(840, 120));
        
        Font labelFont = new Font("Roboto", Font.BOLD, 14);
        Font valueFont = new Font("Roboto", Font.PLAIN, 14);
        
        // Lấy thông tin nhân viên từ mã nhân viên
        String tenNhanVien = "N/A";
        try {
            NhanVien nv = nhanVienDAO.getNhanVienTheoMa(maNhanVien);
            if (nv != null && nv.getTenNV() != null && !nv.getTenNV().trim().isEmpty()) {
                tenNhanVien = dinhDangTenNhanVien(nv.getTenNV());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("✗ Lỗi khi lấy thông tin nhân viên: " + e.getMessage());
        }
        
        // Dòng 1: Mã hóa đơn + Ngày lập
        JPanel row1 = new JPanel(new GridLayout(1, 4, 15, 0));
        row1.setBackground(Color.WHITE);
        row1.setMaximumSize(new Dimension(840, 35));
        
        JLabel lblMaHDLabel = new JLabel("Mã hóa đơn:");
        lblMaHDLabel.setFont(labelFont);
        row1.add(lblMaHDLabel);
        
        lblMaHoaDon = new JLabel(maHoaDon == null || maHoaDon.trim().isEmpty() ? "Tự động khi xác nhận" : maHoaDon);
        lblMaHoaDon.setFont(valueFont);
        lblMaHoaDon.setForeground(new Color(0, 0, 205));
        row1.add(lblMaHoaDon);
        
        JLabel lblNgayLabel = new JLabel("Ngày lập:");
        lblNgayLabel.setFont(labelFont);
        row1.add(lblNgayLabel);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        lblNgayLap = new JLabel(sdf.format(new Date()));
        
        lblNgayLap.setFont(valueFont);
        row1.add(lblNgayLap);
        
        infoPanel.add(row1);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Dòng 2: Nhân viên + Khách hàng
        JPanel row2 = new JPanel(new GridLayout(1, 4, 15, 0));
        row2.setBackground(Color.WHITE);
        row2.setMaximumSize(new Dimension(840, 35));
        
        JLabel lblNVLabel = new JLabel("Nhân viên:");
        lblNVLabel.setFont(labelFont);
        row2.add(lblNVLabel);
        
        lblNhanVien = new JLabel(dinhDangTenNhanVien(tenNhanVien));
        lblNhanVien.setFont(valueFont);
        lblNhanVien.setForeground(new Color(0, 102, 204));
        row2.add(lblNhanVien);
        
        JLabel lblKHLabel = new JLabel("Khách hàng:");
        lblKHLabel.setFont(labelFont);
        row2.add(lblKHLabel);
        
        lblKhachHang = new JLabel(tenKhachHang.isEmpty() ? "Khách lẻ" : tenKhachHang);
        lblKhachHang.setFont(valueFont);
        row2.add(lblKhachHang);
        
        infoPanel.add(row2);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Dòng 3: Số điện thoại
        JPanel row3 = new JPanel(new GridLayout(1, 4, 15, 0));
        row3.setBackground(Color.WHITE);
        row3.setMaximumSize(new Dimension(840, 35));
        
        JLabel lblSDTLabel = new JLabel("Số điện thoại:");
        lblSDTLabel.setFont(labelFont);
        row3.add(lblSDTLabel);
        
        lblSoDienThoai = new JLabel(soDienThoai.isEmpty() ? "N/A" : soDienThoai);
        lblSoDienThoai.setFont(valueFont);
        row3.add(lblSoDienThoai);
        
        // Nếu có mã phiếu đặt thì thêm vào
        if (maPhieuDat != null) {
	        JLabel lblMaPhieuDatLabel = new JLabel("Mã phiếu đặt");
	        lblMaPhieuDatLabel.setFont(labelFont);
	        row3.add(lblMaPhieuDatLabel);
	        
	        lblMaPhieuDat = new JLabel(maPhieuDat == null ? "" : maPhieuDat);
	        lblMaPhieuDat.setFont(valueFont);
	        row3.add(lblMaPhieuDat);
        } else {
            row3.add(new JLabel(""));
            row3.add(new JLabel(""));
        }
        
        infoPanel.add(row3);
        
        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // ===== SEPARATOR =====
        JSeparator separator1 = new JSeparator();
        separator1.setMaximumSize(new Dimension(840, 2));
        mainPanel.add(separator1);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // ===== TABLE CHI TIẾT
        JLabel lblChiTiet = new JLabel("Chi tiết sản phẩm:");
        lblChiTiet.setFont(new Font("Roboto", Font.BOLD, 16));
        lblChiTiet.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblChiTiet);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        String[] columns = {"STT", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableChiTiet = new JTable(tableModel);
        tableChiTiet.setFont(new Font("Roboto", Font.PLAIN, 14));
        tableChiTiet.setRowHeight(35);
        tableChiTiet.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        tableChiTiet.getTableHeader().setBackground(new Color(0, 0, 205));
        tableChiTiet.getTableHeader().setForeground(Color.WHITE);
        tableChiTiet.getTableHeader().setPreferredSize(new Dimension(840, 40));
        
        // Set column width
        tableChiTiet.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableChiTiet.getColumnModel().getColumn(1).setPreferredWidth(350);
        tableChiTiet.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableChiTiet.getColumnModel().getColumn(3).setPreferredWidth(130);
        tableChiTiet.getColumnModel().getColumn(4).setPreferredWidth(140);
        
        // Cell renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableChiTiet.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableChiTiet.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tableChiTiet.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tableChiTiet.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        // Load data
        int stt = 1;
        for (ChiTietHoaDon item : dsChiTietHoaDon) {
            tableModel.addRow(new Object[]{
                stt++,
                item.getThuoc().getTenThuoc(),
                item.getSoLuong(),
                String.format("%,.0f VNĐ", item.getDonGia()),
                String.format("%,.0f VNĐ", item.getThanhTien())
            });
        }
        
        JScrollPane scrollPane = new JScrollPane(tableChiTiet);
        scrollPane.setMaximumSize(new Dimension(840, 300));
        scrollPane.setPreferredSize(new Dimension(840, 300));
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // ===== SEPARATOR =====
        JSeparator separator2 = new JSeparator();
        separator2.setMaximumSize(new Dimension(840, 2));
        mainPanel.add(separator2);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // ===== PAYMENT INFO =====
        JPanel paymentPanel = new JPanel(new GridLayout(6, 2, 15, 12));
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.setMaximumSize(new Dimension(780, 200));
        
        Font paymentFont = new Font("Roboto", Font.PLAIN, 14);
        
        // Tổng tiền
        JLabel lblTongTienLabel = new JLabel("Tổng tiền:");
        lblTongTienLabel.setFont(paymentFont);
        paymentPanel.add(lblTongTienLabel);
        
        txtTongTien = new JTextField(String.format("%,.0f VNĐ", tongTienGoc));
        txtTongTien.setEditable(false);
        txtTongTien.setFont(new Font("Roboto", Font.BOLD, 15));
        paymentPanel.add(txtTongTien);
        
        double phanTramGiamGia = khuyenMaiApDung != null ? khuyenMaiApDung.getPhanTramGiamGia() : 0;
        String labelGiamGia = phanTramGiamGia > 0 ? "Giảm giá (" + " -" + String.format("%.0f%%", phanTramGiamGia) + "):" : "Giảm giá:";
        JLabel lblGiamGiaLabel = new JLabel(labelGiamGia);
        lblGiamGiaLabel.setFont(paymentFont); paymentPanel.add(lblGiamGiaLabel);
        txtGiamGia = new JTextField(String.format("%,.0f VNĐ", giamGia));
        txtGiamGia.setEditable(false); txtGiamGia.setFont(paymentFont);
        if (giamGia > 0) { 
        	txtGiamGia.setForeground(new Color(0, 153, 0));
        	txtGiamGia.setFont(new Font("Roboto", Font.BOLD, 14)); 
        }
        paymentPanel.add(txtGiamGia);

        String tenThue = "Thuế (0%)";
        if (thueApDung != null) {
            tenThue = thueApDung.getTenThue() + " (" + String.format("%.0f%%", thueApDung.getPhanTramThue()) + ")";
        }
        
        JLabel lblThueLabel = new JLabel(tenThue + ":");
        lblThueLabel.setFont(paymentFont);
        paymentPanel.add(lblThueLabel);
        
        txtThue = new JTextField(String.format("%,.0f VNĐ", tienThue));
        txtThue.setEditable(false);
        txtThue.setFont(paymentFont);
        paymentPanel.add(txtThue);
        
     

        final double thanhTienChot = Math.round(thanhTien);
        
        JLabel lblThanhTienLabel = new JLabel("Thành tiền:");
        lblThanhTienLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        lblThanhTienLabel.setForeground(new Color(255, 51, 0));
        paymentPanel.add(lblThanhTienLabel);
        
        txtThanhTien = new JTextField(String.format("%,.0f VNĐ", thanhTienChot));
        txtThanhTien.setEditable(false);
        txtThanhTien.setFont(new Font("Roboto", Font.BOLD, 16));
        txtThanhTien.setForeground(new Color(255, 51, 0));
        paymentPanel.add(txtThanhTien);
        
        // Tiền nhận vào
        JLabel lbltienNhanVaoLabel = new JLabel("Tiền nhận vào:");
        lbltienNhanVaoLabel.setFont(paymentFont);
        paymentPanel.add(lbltienNhanVaoLabel);
        
        txtTienNhanVao = new JTextField(String.format("%,.0f VNĐ", tienNhanVao));
        txtTienNhanVao.setEditable(false);
        txtTienNhanVao.setFont(new Font("Roboto", Font.BOLD, 15));
        paymentPanel.add(txtTienNhanVao);
        
        // Tiền thừa = Tiền nhận vào - Thành tiền
        double tienThua = 0;
        if (tienNhanVao != 0) {
        	tienThua = tienNhanVao - thanhTienChot;
        } 
        JLabel lblTienThuaLabel = new JLabel("Tiền thừa:");
        lblTienThuaLabel.setFont(paymentFont);
        paymentPanel.add(lblTienThuaLabel);
        
        txtTienThua = new JTextField(String.format("%,.0f VNĐ", tienThua));
        txtTienThua.setEditable(false);
        txtTienThua.setFont(new Font("Roboto", Font.BOLD, 15));
        txtTienThua.setForeground(tienThua >= 0 ? new Color(0, 153, 0) : Color.RED);
        paymentPanel.add(txtTienThua);
        
        mainPanel.add(paymentPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 245, 245));
        buttonPanel.setPreferredSize(new Dimension(850, 80));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        btnHuy = new JButton("HỦY BỎ");
        btnHuy.setFont(new Font("Roboto", Font.BOLD, 14));
        btnHuy.setPreferredSize(new Dimension(150, 40));
        btnHuy.setBackground(new Color(255, 102, 102));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFocusPainted(false);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.setBorder(new LineBorder(new Color(255, 102, 102), 2, true));
        btnHuy.addActionListener(e -> {
        	int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn huỷ ?", "Xác nhận" ,JOptionPane.YES_NO_OPTION);
        	if (choice == JOptionPane.YES_OPTION) {
        		this.dispose();
        	}
        });
        
        btnXacNhan = new JButton("XÁC NHẬN THANH TOÁN");
        btnXacNhan.setFont(new Font("Roboto", Font.BOLD, 14));
        btnXacNhan.setPreferredSize(new Dimension(220, 40));
        btnXacNhan.setBackground(new Color(0, 204, 51));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXacNhan.setBorder(new LineBorder(new Color(0, 204, 51), 2, true));
        btnXacNhan.addActionListener(e -> {
        	if (tienNhanVao == 0) {
	        	try {
					taoMaQrCode();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	} else {
        		isThanhToan = true;
        	}
            if (isThanhToan && tienNhanVao > 0 && tienNhanVao < thanhTienChot) {
                JOptionPane.showMessageDialog(this, "Tiền nhận vào chưa đủ để thanh toán hóa đơn.");
                isThanhToan = false;
                return;
            }
        	if (isThanhToan) {
                String maHoaDonChot = lblMaHoaDon.getText();
                if (maHoaDonChot == null || maHoaDonChot.trim().isEmpty()
                        || "Tự động khi xác nhận".equals(maHoaDonChot)) {
                    try {
                        maHoaDonChot = hdDAO.generateMaHD();
                        lblMaHoaDon.setText(maHoaDonChot);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Lỗi tạo mã hóa đơn: " + e1.getMessage(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                for (ChiTietHoaDon cthd : dsChiTietHoaDon) {
                    cthd.getHoaDon().setMaHD(maHoaDonChot);
                }
        		KhachHang kh = null;
        		if (tenKhachHang.isEmpty() && soDienThoai.isEmpty()) {
        			kh = null;
        		} else {
        			try {
						kh = khDAO.getKhachHangTheoSDT(soDienThoai);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        		}
	            HoaDon hd = new HoaDon(maHoaDonChot, new Date(),
	                thueApDung,
	                new NhanVien(maNhanVien),
	                kh,
	                khuyenMaiApDung,
	                null);
	            try {
	            	if (hdDAO.thanhToanHoaDon(hd, dsChiTietHoaDon)) {
	            	    	
	            	        JOptionPane.showMessageDialog(this, "✅ Thanh toán thành công");
	            	        confirmed = true;
	            	        int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn in hoá đơn không ?");
	            	        
	            	        if (choice == JOptionPane.YES_OPTION) {
	            	        	inHoaDon();
	            	        } else {
	            	        	this.dispose();
	            	        }
	            	      
	            	} else {
	            	    JOptionPane.showMessageDialog(this, " Lỗi khi thanh toán hóa đơn");
	            	}
	
				} catch (SQLException e1) {
					e1.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi thanh toán: " + e1.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
        	}
        });
        
        buttonPanel.add(btnHuy);
        buttonPanel.add(btnXacNhan);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
private void taoMaQrCode() {
    try {
        String bank = "bidv";
        String account = "7351363429";
        String amountStr = txtThanhTien.getText().trim()
                .replace(",", "")
                .replace("VNĐ", "")
                .replace(".", "")
                .trim();
        
        String noiDung = "Thanh toan san pham"; 
        
        // Tạo URL cho QR code
        String qrUrl = String.format(
            "https://img.vietqr.io/image/%s-%s-compact.png?amount=%s&addInfo=%s",
            bank, account, amountStr, URLEncoder.encode(noiDung, "UTF-8")
        );
        
        // Tạo dialog
        JDialog dlQrCode = new JDialog(this, "Quét mã QR để thanh toán", true);
        dlQrCode.setSize(800, 600);
        dlQrCode.setLayout(new BorderLayout(10, 10));
        dlQrCode.setLocationRelativeTo(this);
        dlQrCode.getContentPane().setBackground(Color.WHITE);
        
        // Panel chứa thông tin
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel lblAmount = new JLabel("Số tiền: " + txtThanhTien.getText());
        lblAmount.setFont(new Font("Roboto", Font.BOLD, 18));
        lblAmount.setForeground(new Color(255, 51, 0));
        lblAmount.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblBank = new JLabel("Ngân hàng: BIDV");
        lblBank.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblBank.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblAccount = new JLabel("Số tài khoản: " + account);
        lblAccount.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(lblAmount);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(lblBank);
        infoPanel.add(lblAccount);
        
        dlQrCode.add(infoPanel, BorderLayout.NORTH);
        
        // Qr Code
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setBackground(Color.WHITE);
        qrPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        URL url = new URL(qrUrl);
        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        icon.setImage(scaledImg);
        JLabel lblQrCode = new JLabel(icon);
        lblQrCode.setFont(new Font("Roboto", Font.ITALIC, 14));
        lblQrCode.setForeground(Color.GRAY);
        qrPanel.add(lblQrCode, BorderLayout.CENTER);
        
        dlQrCode.add(qrPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnDaNhanTien = new JButton("ĐÃ NHẬN TIỀN");
        btnDaNhanTien.setFont(new Font("Roboto", Font.BOLD, 14));
        btnDaNhanTien.setPreferredSize(new Dimension(180, 40));
        btnDaNhanTien.setBackground(new Color(0, 204, 51));
        btnDaNhanTien.setForeground(Color.WHITE);
        btnDaNhanTien.setFocusPainted(false);
        btnDaNhanTien.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDaNhanTien.setBorder(new LineBorder(new Color(0, 204, 51), 2, true));
        
        JButton btnHuyBo = new JButton("HỦY BỎ");
        btnHuyBo.setFont(new Font("Roboto", Font.BOLD, 14));
        btnHuyBo.setPreferredSize(new Dimension(120, 40));
        btnHuyBo.setBackground(new Color(255, 102, 102));
        btnHuyBo.setForeground(Color.WHITE);
        btnHuyBo.setFocusPainted(false);
        btnHuyBo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuyBo.setBorder(new LineBorder(new Color(255, 102, 102), 2, true));
        
        buttonPanel.add(btnHuyBo);
        buttonPanel.add(btnDaNhanTien);
        
        dlQrCode.add(buttonPanel, BorderLayout.SOUTH);
        
        // Event handlers
        btnDaNhanTien.addActionListener(e -> {
            txtTienNhanVao.setText(txtThanhTien.getText());
            txtTienThua.setText("0 VNĐ");
            txtTienThua.setForeground(new Color(0, 153, 0));
            int choice = JOptionPane.showConfirmDialog(this, "Xác nhận đã nhận tiền ?");
            if (choice == JOptionPane.YES_OPTION) {
	            JOptionPane.showMessageDialog(dlQrCode, 
	                "✓ Đã xác nhận nhận tiền qua chuyển khoản", 
	                "Thông báo", 
	                JOptionPane.INFORMATION_MESSAGE);
	            isThanhToan = true;
	            dlQrCode.dispose();
            }
        });
        
        btnHuyBo.addActionListener(e -> {
            dlQrCode.dispose();
        });
        
        dlQrCode.setVisible(true);
        
        
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("✗ Lỗi tạo QR code: " + e.getMessage());
        JOptionPane.showMessageDialog(this, 
            "Lỗi tạo mã QR: " + e.getMessage(), 
            "Lỗi", 
            JOptionPane.ERROR_MESSAGE);
    }
}
	private void inHoaDon() {
        try {
            // Đường dẫn
        	String folderPath = "src/LuuHoaDonInRa";
        	String fileName = "HoaDon_" + lblMaHoaDon.getText() + "_" +
        	        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
        	String outputPath = folderPath + "/" + fileName;

            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs(); // 
            }
            // === TẠO TÀI LIỆU PDF ===
            com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 40, 40, 40, 40);
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(outputPath));
            document.open();

            // Font 
            com.itextpdf.text.pdf.BaseFont bf = com.itextpdf.text.pdf.BaseFont.createFont(
                    "C:/Windows/Fonts/arial.ttf", com.itextpdf.text.pdf.BaseFont.IDENTITY_H, com.itextpdf.text.pdf.BaseFont.EMBEDDED);
            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12);
            com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);

            // Title
            com.itextpdf.text.Font fontStoreName = new com.itextpdf.text.Font(bf, 20, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Paragraph storeName = new com.itextpdf.text.Paragraph("NHÀ THUỐC MAI THỨC", fontStoreName);
            storeName.setAlignment(Element.ALIGN_CENTER);
            document.add(storeName);

            com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("HÓA ĐƠN BÁN HÀNG", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new com.itextpdf.text.Paragraph(" "));

            // Thông tin chung
            document.add(new com.itextpdf.text.Paragraph("Mã hóa đơn: " + lblMaHoaDon.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Ngày lập: " + lblNgayLap.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Nhân viên: " + dinhDangTenNhanVien(lblNhanVien.getText()), fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Khách hàng: " + lblKhachHang.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Số điện thoại: " + lblSoDienThoai.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph(" "));
            
            // Chi tiết sản phẩm
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 3f, 1.5f, 1.5f, 2f});

            String[] headers = {"STT", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, fontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }
            
            DefaultTableModel model = (DefaultTableModel) tableChiTiet.getModel();
            
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    PdfPCell cell = new PdfPCell(new Phrase(model.getValueAt(i, j).toString(), fontNormal));
                    cell.setHorizontalAlignment(j == 1 ? Element.ALIGN_LEFT : Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Tổng tiền: " + txtTongTien.getText(), fontNormal));
            document.add(new Paragraph("Thuế: " + txtThue.getText(), fontNormal));
            document.add(new Paragraph("Giảm giá: " + txtGiamGia.getText(), fontNormal));
            document.add(new Paragraph("Thành tiền: " + txtThanhTien.getText(), fontBold));
            document.add(new Paragraph("Tiền nhận vào: " + txtTienNhanVao.getText(), fontNormal));
            document.add(new Paragraph("Tiền thừa: " + txtTienThua.getText(), fontNormal));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Cảm ơn quý khách đã mua hàng!", fontBold));
            document.close();
            
            JOptionPane.showMessageDialog(this, "In hóa đơn thành công!\nĐã lưu tại: " + outputPath);
            Desktop.getDesktop().open(new java.io.File(outputPath));
            this.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn: " + e.getMessage());
        }
    }


	/**
     * Kiểm tra ngày hiện tại có nằm trong khoảng ngày bắt đầu - ngày kết thúc không
     */
    private boolean isInDateRange(Date currentDate, Date startDate, Date endDate) {
        if (currentDate == null || startDate == null || endDate == null) {
            return false;
        }
        
        java.util.Calendar calCurrent = java.util.Calendar.getInstance();
        calCurrent.setTime(currentDate);
        calCurrent.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calCurrent.set(java.util.Calendar.MINUTE, 0);
        calCurrent.set(java.util.Calendar.SECOND, 0);
        calCurrent.set(java.util.Calendar.MILLISECOND, 0);
        Date current = calCurrent.getTime();
        
        java.util.Calendar calStart = java.util.Calendar.getInstance();
        calStart.setTime(startDate);
        calStart.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calStart.set(java.util.Calendar.MINUTE, 0);
        calStart.set(java.util.Calendar.SECOND, 0);
        calStart.set(java.util.Calendar.MILLISECOND, 0);
        Date start = calStart.getTime();
        
        java.util.Calendar calEnd = java.util.Calendar.getInstance();
        calEnd.setTime(endDate);
        calEnd.set(java.util.Calendar.HOUR_OF_DAY, 23);
        calEnd.set(java.util.Calendar.MINUTE, 59);
        calEnd.set(java.util.Calendar.SECOND, 59);
        calEnd.set(java.util.Calendar.MILLISECOND, 999);
        Date end = calEnd.getTime();
        
        return (current.equals(start) || current.after(start)) && 
               (current.equals(end) || current.before(end));
    }
    
    /**
     * Kiểm tra user có xác nhận thanh toán không
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    private String dinhDangTenNhanVien(String tenNhanVien) {
        if (tenNhanVien == null || tenNhanVien.trim().isEmpty()) {
            return "N/A";
        }
        return tenNhanVien.replaceAll("\\s*\\(NV\\d+\\)\\s*$", "").trim();
    }

}
