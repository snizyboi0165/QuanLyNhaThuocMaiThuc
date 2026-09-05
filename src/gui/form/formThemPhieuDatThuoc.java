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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.ChiTietPhieuDatThuocDAO;
import dao.DanhMucThuocDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.KhuyenMaiDAO;
import dao.PhieuDatThuocDAO;
import dao.ThueDAO;
import dao.ThuocDAO;
import entity.ChiTietPhieuDatThuoc;
import entity.ChiTietPhieuDatThuoc;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.PhieuDatThuoc;
import entity.TaiKhoan;
import entity.Thue;
import entity.Thuoc;
import gui.dialog.DialogThanhToanHoaDon;
import gui.dialog.DialogThemKhachHang;
import gui.form.FormQuanLyKhachHang; 
import utils.ImageHelper;

public class FormThemPhieuDatThuoc extends JPanel {

    // DAOs & Entities
    private KhachHangDAO khachHangDAO;
    private PhieuDatThuocDAO pdtDAO;
    private KhuyenMaiDAO khuyenMaiDAO;
    private ThuocDAO thuocDAO;
    private DanhMucThuocDAO dmtDAO;
    private ThueDAO thueDAO;
    private ChiTietPhieuDatThuocDAO ctpdtDAO;
    
    private ArrayList<ChiTietPhieuDatThuoc> dsChiTietPhieuDatThuoc;
    private DefaultTableModel modelDanhSachThuoc;
    private DefaultTableModel modelGioHang;
    
    private String currentMaThuoc = "";
    private double tongTien = 0;
    private Thue thueApDung;
    private KhuyenMai khuyenMaiApDung;
    private final Map<String, Date> hanSuDungMap = new HashMap<>();
    private boolean daCanhBaoKyTuTimKiem = false;
    private TaiKhoan taiKhoan;

    private JPanel pnlChinh;
    private JPanel pnlTopWrapper; // Chứa Khuyến mãi + Thông tin thuốc
    
    // GUI Components - Phần Khuyến Mãi
    private JPanel pnlKhuyenMai;
    private JPanel pnlHeaderKhuyenMai;
    private JLabel lblTieuDeKhuyenMai;
    private JPanel pnlNoiDungKhuyenMai; // Chứa các card
    private JScrollPane scrKhuyenMai;

    // Phần Thông Tin Thuốc (Góc trên)
    private JPanel pnlThongTinThuoc;
    private JPanel pnlHeaderThongTinThuoc;
    private JLabel lblTieuDeThongTinThuoc;
    private JPanel pnlNoiDungThongTin;
    private JPanel pnlAnhThuoc;
    private JLabel lblHinhAnh;
    
    // Các panel con nhập liệu thuốc
    private JPanel pnlChiTietThuoc;
    private JTextField txtMaThuoc;
    private JTextField txtTenThuoc;
    private JTextArea txaThanhPhan; 
    private JTextField txtDonGia;
    
    // Phần Danh Sách Thuốc & Thao Tác (Giữa)
    private JPanel pnlDanhSachVaThaoTac;
    private JPanel pnlThaoTac;
    private JPanel pnlTimKiem;
    private JComboBox<String> cboLoaiTimKiem; 
    private JTextField txtTimKiem;
    private JButton btnLamMoi;
    
    private JPanel pnlThemVaoGio;
    private JTextField txtSoLuongNhap; 
    private JButton btnThemVaoGio; 
    
    private JPanel pnlBangThuoc;
    private JScrollPane scrBangThuoc;
    private JTable tblDanhSachThuoc; 

    // GUI Components - Phần Hóa Đơn & Thanh Toán (Bên Phải)
    private JPanel pnlKhuVucThanhToan; 
    
    // Phần Giỏ Hàng
    private JPanel pnlGioHang; 
    private JPanel pnlHeaderGioHang;
    private JLabel lblTieuDeGioHang;
    private JScrollPane scrGioHang;
    private JTable tblGioHang; 
    private JPanel pnlThaoTacGioHang;
    private JButton btnXoaKhoiGio;
    
    // Phần Form Thanh Toán
    private JPanel pnlThongTinThanhToan; 
    private JPanel pnlHeaderThanhToan;
    private JLabel lblTieuDeHoaDon;
    private JPanel pnlFormNhapLieuThanhToan;
    
    // Các input trong form thanh toán
    private JTextField txtMaPhieuDat;
    private JTextField txtSdtKH;
    private JButton btnTimKiemKH;
    private JButton btnThemNhanhKH;
    private JTextField txtHoTenKH;
    
    private JTextField txtTongTien;
    
    private JPanel pnlNutDatHang;
    private JButton btnHuyPhieuDat;
    private JButton btnDatThuoc;
	private JTextField txtDiaChi;
	private JComboBox<String> cboHinhThucThanhToan;

    public FormThemPhieuDatThuoc(TaiKhoan tk) throws SQLException {
        this.taiKhoan = tk;
        // Khởi tạo List và DAO
        dsChiTietPhieuDatThuoc = new ArrayList<>();
        thuocDAO = new ThuocDAO();
        khachHangDAO = new KhachHangDAO();
        pdtDAO = new PhieuDatThuocDAO();
        dmtDAO = new DanhMucThuocDAO();
        thueDAO = new ThueDAO();
        khuyenMaiDAO = new KhuyenMaiDAO();
        ctpdtDAO = new ChiTietPhieuDatThuocDAO();
        
        initComponents();
        configureProductLayout(); 
    }

    private void generateMaPhieuDatThuoc() {
        try {
            String maPhieuDatThuoc = pdtDAO.generateMaPhieuDat();
            txtMaPhieuDat.setText(maPhieuDatThuoc);
        } catch (Exception e) {
            e.printStackTrace();
            txtMaPhieuDat.setText("PDT00001");
        }
    }

    private void configureProductLayout() {
        txtSoLuongNhap.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Số lượng...");
        btnLamMoi.putClientProperty(FlatClientProperties.STYLE, "arc: 15");

        txtTimKiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm...");
        txtTimKiem.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon(getClass().getResource("/img/search.svg")));

        String[] searchType = {"Tất cả", "Mã", "Tên"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(searchType);
        cboLoaiTimKiem.setModel(model);
    }

    private void initComponents() throws SQLException {
        // Main Panel
        setBackground(new Color(230, 245, 245));
        setBorder(new LineBorder(new Color(230, 245, 245), 6, true));
        setLayout(new BorderLayout(5, 0));

        pnlChinh = new JPanel();
        pnlChinh.setBackground(new Color(230, 245, 245));
        pnlChinh.setLayout(new BorderLayout(5, 5));

        // Panel Khuyến Mãi
        pnlKhuyenMai = new JPanel();
        pnlKhuyenMai.setBackground(Color.WHITE);
        pnlKhuyenMai.setBorder(new LineBorder(new Color(237, 237, 237), 2, true));
        pnlKhuyenMai.setPreferredSize(new Dimension(320, 300));
        pnlKhuyenMai.setLayout(new BorderLayout());

        pnlHeaderKhuyenMai = new JPanel();
        pnlHeaderKhuyenMai.setBackground(new Color(255, 153, 0));
        pnlHeaderKhuyenMai.setPreferredSize(new Dimension(320, 45));
        pnlHeaderKhuyenMai.setLayout(new BorderLayout());

        lblTieuDeKhuyenMai = new JLabel("🎁 KHUYẾN MÃI ĐANG CÓ");
        lblTieuDeKhuyenMai.setFont(new Font("Roboto", Font.BOLD, 15));
        lblTieuDeKhuyenMai.setForeground(Color.WHITE);
        lblTieuDeKhuyenMai.setHorizontalAlignment(SwingConstants.CENTER);
        pnlHeaderKhuyenMai.add(lblTieuDeKhuyenMai, BorderLayout.CENTER);
        pnlKhuyenMai.add(pnlHeaderKhuyenMai, BorderLayout.NORTH);

        pnlNoiDungKhuyenMai = new JPanel();
        pnlNoiDungKhuyenMai.setLayout(new BoxLayout(pnlNoiDungKhuyenMai, BoxLayout.Y_AXIS));
        pnlNoiDungKhuyenMai.setBackground(new Color(245, 245, 245));
        pnlNoiDungKhuyenMai.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));

        loadKhuyenMai(); 

        scrKhuyenMai = new JScrollPane(pnlNoiDungKhuyenMai);
        scrKhuyenMai.setBorder(null);
        scrKhuyenMai.getVerticalScrollBar().setUnitIncrement(16);
        scrKhuyenMai.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pnlKhuyenMai.add(scrKhuyenMai, BorderLayout.CENTER);

        // Panel thông tin thuốc
        pnlThongTinThuoc = new JPanel();
        pnlThongTinThuoc.setBackground(Color.WHITE);
        pnlThongTinThuoc.setBorder(new LineBorder(new Color(237, 237, 237), 2, true));
        pnlThongTinThuoc.setPreferredSize(new Dimension(832, 300));
        pnlThongTinThuoc.setLayout(new BorderLayout());

        pnlHeaderThongTinThuoc = new JPanel();
        pnlHeaderThongTinThuoc.setBackground(new Color(0, 0, 205));
        pnlHeaderThongTinThuoc.setPreferredSize(new Dimension(500, 30));
        pnlHeaderThongTinThuoc.setLayout(new BorderLayout());

        lblTieuDeThongTinThuoc = new JLabel("Thông tin thuốc");
        lblTieuDeThongTinThuoc.setFont(new Font("Roboto Medium", Font.BOLD, 16));
        lblTieuDeThongTinThuoc.setForeground(Color.WHITE);
        lblTieuDeThongTinThuoc.setHorizontalAlignment(SwingConstants.CENTER);
        pnlHeaderThongTinThuoc.add(lblTieuDeThongTinThuoc, BorderLayout.CENTER);
        pnlThongTinThuoc.add(pnlHeaderThongTinThuoc, BorderLayout.NORTH);

        pnlNoiDungThongTin = new JPanel();
        pnlNoiDungThongTin.setBackground(Color.WHITE);
        pnlNoiDungThongTin.setLayout(new BorderLayout(16, 16));

        // Ảnh thuốc
        pnlAnhThuoc = new JPanel();
        pnlAnhThuoc.setBackground(Color.WHITE);
        pnlAnhThuoc.setPreferredSize(new Dimension(300, 200));
        pnlAnhThuoc.setLayout(new BorderLayout(20, 20));

        lblHinhAnh = new JLabel("Chọn thuốc để xem");
        lblHinhAnh.setBorder(new LineBorder(new Color(230, 230, 230), 4, true));
        lblHinhAnh.setPreferredSize(new Dimension(300, 200));
        lblHinhAnh.setHorizontalAlignment(SwingConstants.CENTER);
        pnlAnhThuoc.add(lblHinhAnh, BorderLayout.CENTER);
        pnlNoiDungThongTin.add(pnlAnhThuoc, BorderLayout.WEST);

        // Chi tiết thông tin (Mã, Tên, Thành phần, Đơn giá)
        pnlChiTietThuoc = new JPanel();
        pnlChiTietThuoc.setBackground(Color.WHITE);
        pnlChiTietThuoc.setBorder(new javax.swing.border.EmptyBorder(0, 16, 0, 16));

        // Setup các panel con cho từng dòng
        JPanel pnlDongMa = createInputRow("Mã thuốc:", txtMaThuoc = new JTextField());
        txtMaThuoc.setEditable(false); txtMaThuoc.setPreferredSize(new Dimension(120, 40));
        
        JPanel pnlDongTen = createInputRow("Tên thuốc:", txtTenThuoc = new JTextField());
        txtTenThuoc.setEditable(false); txtTenThuoc.setPreferredSize(new Dimension(300, 40));
        
        JPanel pnlDongThanhPhan = new JPanel(new BorderLayout(14, 0));
        pnlDongThanhPhan.setBackground(Color.WHITE);
        pnlDongThanhPhan.setMaximumSize(new Dimension(500, 40));
        pnlDongThanhPhan.setPreferredSize(new Dimension(500, 40));
        pnlDongThanhPhan.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lblThanhPhan = new JLabel("Thành phần:");
        lblThanhPhan.setFont(new Font("Roboto", Font.BOLD, 16));
        lblThanhPhan.setPreferredSize(new Dimension(110, 40));          
        lblThanhPhan.setVerticalAlignment(SwingConstants.CENTER);      
        txaThanhPhan = new JTextArea();
        txaThanhPhan.setEditable(false);
        txaThanhPhan.setFont(new Font("Roboto", Font.PLAIN, 16));
        txaThanhPhan.setLineWrap(true);
        txaThanhPhan.setWrapStyleWord(true);                            
        JScrollPane scrThanhPhan = new JScrollPane(txaThanhPhan);
        scrThanhPhan.setPreferredSize(new Dimension(300, 40));          
        scrThanhPhan.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER); // thêm dòng này
        pnlDongThanhPhan.add(lblThanhPhan, BorderLayout.WEST);
        pnlDongThanhPhan.add(scrThanhPhan, BorderLayout.CENTER);

        JPanel pnlDonGia = createInputRow("Đơn giá:", txtDonGia = new JTextField());
        txtDonGia.setEditable(false); 
        txtDonGia.setFont(new Font("Roboto Mono Medium", Font.PLAIN, 16));
        txtDonGia.setPreferredSize(new Dimension(120, 40));

        
        pnlChiTietThuoc.setLayout(new BoxLayout(pnlChiTietThuoc, BoxLayout.Y_AXIS));
        pnlChiTietThuoc.add(Box.createVerticalStrut(10));
        pnlChiTietThuoc.add(pnlDongMa);
        pnlChiTietThuoc.add(Box.createVerticalStrut(5));
        pnlChiTietThuoc.add(pnlDongTen);
        pnlChiTietThuoc.add(Box.createVerticalStrut(5));
        pnlChiTietThuoc.add(pnlDongThanhPhan);
        pnlChiTietThuoc.add(Box.createVerticalStrut(5)); // Spacer
        pnlChiTietThuoc.add(pnlDonGia);

        pnlNoiDungThongTin.add(pnlChiTietThuoc, BorderLayout.CENTER);
        pnlThongTinThuoc.add(pnlNoiDungThongTin, BorderLayout.CENTER);

        // Khuyến mãi + Thông tin thuốc
        pnlTopWrapper = new JPanel(new BorderLayout(5, 5));
        pnlTopWrapper.setBackground(new Color(230, 245, 245));
        pnlTopWrapper.add(pnlKhuyenMai, BorderLayout.WEST);
        pnlTopWrapper.add(pnlThongTinThuoc, BorderLayout.CENTER);
        pnlChinh.add(pnlTopWrapper, BorderLayout.PAGE_START);

        // Panel danh sách thuốc 
        pnlDanhSachVaThaoTac = new JPanel();
        pnlDanhSachVaThaoTac.setBackground(new Color(230, 245, 245));
        pnlDanhSachVaThaoTac.setLayout(new BorderLayout(0, 5));

        // Panel Thao tác (Tìm kiếm, Thêm giỏ)
        pnlThaoTac = new JPanel(new BorderLayout());
        pnlThaoTac.setBackground(Color.WHITE);
        pnlThaoTac.setBorder(new LineBorder(new Color(237, 237, 237), 2, true));
        pnlThaoTac.setPreferredSize(new Dimension(605, 60));

        pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 8));
        pnlTimKiem.setBackground(Color.WHITE);
        
        cboLoaiTimKiem = new JComboBox<>();
        cboLoaiTimKiem.setPreferredSize(new Dimension(100, 40));
        cboLoaiTimKiem.addActionListener(evt -> locDanhSachThuoc());
        
        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(200, 40));
        // Tìm kiếm thuốc
        txtTimKiem.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                locDanhSachThuoc();
            }
        });

        btnLamMoi = new JButton();
        btnLamMoi.setIcon(new FlatSVGIcon(getClass().getResource("/img/reload.svg")));
        btnLamMoi.setToolTipText("Làm mới");
        btnLamMoi.setPreferredSize(new Dimension(40, 40));
        btnLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLamMoi.addActionListener(evt -> btnReloadActionPerformed(evt));

        pnlTimKiem.add(cboLoaiTimKiem);
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnLamMoi);
        pnlThaoTac.add(pnlTimKiem, BorderLayout.CENTER);

        pnlThemVaoGio = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 8));
        pnlThemVaoGio.setBackground(Color.WHITE);
        pnlThemVaoGio.setPreferredSize(new Dimension(260, 60));

        txtSoLuongNhap = new JTextField();
        txtSoLuongNhap.setFont(new Font("Roboto", Font.PLAIN, 12));
        txtSoLuongNhap.setPreferredSize(new Dimension(120, 40));

        btnThemVaoGio = new JButton("THÊM");
        btnThemVaoGio.setBackground(new Color(0, 179, 246));
        btnThemVaoGio.setForeground(new Color(255, 220, 0));
        btnThemVaoGio.setFont(new Font("Roboto Black", Font.PLAIN, 16));
        btnThemVaoGio.setIcon(new FlatSVGIcon(getClass().getResource("/img/add-to-cart.svg")));
        btnThemVaoGio.setPreferredSize(new Dimension(120, 40));
        btnThemVaoGio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThemVaoGio.addActionListener(evt -> btnAddCartActionPerformed(evt));

        pnlThemVaoGio.add(txtSoLuongNhap);
        pnlThemVaoGio.add(btnThemVaoGio);
        pnlThaoTac.add(pnlThemVaoGio, BorderLayout.EAST);
        pnlDanhSachVaThaoTac.add(pnlThaoTac, BorderLayout.PAGE_START);

        // Panel Bảng thuốc
        pnlBangThuoc = new JPanel(new BorderLayout());
        pnlBangThuoc.setBackground(Color.WHITE);
        pnlBangThuoc.setBorder(new LineBorder(new Color(237, 237, 237), 2, true));

        String[] headers = {"STT", "Mã thuốc", "Tên thuốc", "Danh mục", "Xuất xứ", "Đơn vị tính", "Số lượng tồn", "Đơn giá (VND)"};
        modelDanhSachThuoc = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDanhSachThuoc = new JTable(modelDanhSachThuoc);
        tblDanhSachThuoc.getTableHeader().setFont(new Font("Time New Roman", Font.BOLD, 20));
        tblDanhSachThuoc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblDanhSachThuoc.setRowHeight(30);
        tblDanhSachThuoc.setShowGrid(true);
        tblDanhSachThuoc.setGridColor(Color.BLACK);
        
        tblDanhSachThuoc.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        tblDanhSachThuoc.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                applySelectedThuocRowToDetailPanel();
            }
        });

        scrBangThuoc = new JScrollPane(tblDanhSachThuoc);
        scrBangThuoc.setBorder(null);
        pnlBangThuoc.add(scrBangThuoc, BorderLayout.CENTER);
        
        loadDataThuoc(); // Load data

        pnlDanhSachVaThaoTac.add(pnlBangThuoc, BorderLayout.CENTER);
        pnlChinh.add(pnlDanhSachVaThaoTac, BorderLayout.CENTER);

        add(pnlChinh, BorderLayout.CENTER);

        // ==============================================================================
        // 4. PHẦN Dat Thuoc (BÊN PHẢI)
        // ==============================================================================
        pnlKhuVucThanhToan = new JPanel(new BorderLayout(0, 5));
        pnlKhuVucThanhToan.setBackground(new Color(230, 245, 245));
        pnlKhuVucThanhToan.setPreferredSize(new Dimension(460, 800));

        // --- Giỏ hàng ---
        pnlGioHang = new JPanel(new BorderLayout());
        pnlGioHang.setBackground(Color.WHITE);
        pnlGioHang.setBorder(new LineBorder(new Color(238, 238, 238), 2, true));
        pnlGioHang.setPreferredSize(new Dimension(600, 500));

        pnlHeaderGioHang = new JPanel(new BorderLayout());
        pnlHeaderGioHang.setBackground(new Color(0, 0, 205));
        pnlHeaderGioHang.setPreferredSize(new Dimension(500, 30));
        
        lblTieuDeGioHang = new JLabel("Giỏ hàng");
        lblTieuDeGioHang.setFont(new Font("Roboto Medium", Font.BOLD, 16));
        lblTieuDeGioHang.setForeground(Color.WHITE);
        lblTieuDeGioHang.setHorizontalAlignment(SwingConstants.CENTER);
        pnlHeaderGioHang.add(lblTieuDeGioHang, BorderLayout.CENTER);
        pnlGioHang.add(pnlHeaderGioHang, BorderLayout.NORTH);

        String[] cartHeaders = {"STT", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
        modelGioHang = new DefaultTableModel(cartHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblGioHang = new JTable(modelGioHang) {
            @Override
            public String getToolTipText(MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row >= 0 && col == 1) {
                    Object value = getValueAt(row, col);
                    return value == null ? null : value.toString();
                }
                return super.getToolTipText(e);
            }
        };
        cauHinhBangGioHang();
        scrGioHang = new JScrollPane(tblGioHang);
        pnlGioHang.add(scrGioHang, BorderLayout.CENTER);

        pnlThaoTacGioHang = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 2));
        pnlThaoTacGioHang.setBackground(Color.WHITE);
        
        btnXoaKhoiGio = new JButton();
        btnXoaKhoiGio.setBackground(new Color(255, 102, 102));
        btnXoaKhoiGio.setIcon(new FlatSVGIcon(getClass().getResource("/img/trash-cart.svg")));
        btnXoaKhoiGio.setPreferredSize(new Dimension(50, 38));
        btnXoaKhoiGio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoaKhoiGio.addActionListener(evt -> btnDeleteCartItemActionPerformed(evt));
        
        pnlThaoTacGioHang.add(btnXoaKhoiGio);
        pnlGioHang.add(pnlThaoTacGioHang, BorderLayout.PAGE_END);
        pnlKhuVucThanhToan.add(pnlGioHang, BorderLayout.CENTER);

        // --- Form Thanh Toán ---
        pnlThongTinThanhToan = new JPanel(new BorderLayout());
        pnlThongTinThanhToan.setBackground(Color.WHITE);
        pnlThongTinThanhToan.setBorder(new LineBorder(new Color(238, 238, 238), 2, true));
        pnlThongTinThanhToan.setPreferredSize(new Dimension(500, 380));

        pnlHeaderThanhToan = new JPanel(new BorderLayout());
        pnlHeaderThanhToan.setBackground(new Color(0, 0, 205));
        pnlHeaderThanhToan.setPreferredSize(new Dimension(500, 30));
        
        lblTieuDeHoaDon = new JLabel("Hóa đơn");
        lblTieuDeHoaDon.setFont(new Font("Roboto Medium", Font.BOLD, 16));
        lblTieuDeHoaDon.setForeground(Color.WHITE);
        lblTieuDeHoaDon.setHorizontalAlignment(SwingConstants.CENTER);
        pnlHeaderThanhToan.add(lblTieuDeHoaDon, BorderLayout.CENTER);
        pnlThongTinThanhToan.add(pnlHeaderThanhToan, BorderLayout.NORTH);

        pnlFormNhapLieuThanhToan = new JPanel();
        pnlFormNhapLieuThanhToan.setLayout(new BoxLayout(pnlFormNhapLieuThanhToan, BoxLayout.Y_AXIS));
        pnlFormNhapLieuThanhToan.setBackground(Color.WHITE);
        pnlFormNhapLieuThanhToan.setBorder(new javax.swing.border.EmptyBorder(12, 18, 4, 18));

        // Group: Thông tin chung
        JPanel pnlGroupThongTin = new JPanel();
        pnlGroupThongTin.setLayout(new BoxLayout(pnlGroupThongTin, BoxLayout.Y_AXIS));
        pnlGroupThongTin.setBackground(Color.WHITE);
        pnlGroupThongTin.setMaximumSize(new Dimension(440, 190));
        pnlGroupThongTin.setAlignmentX(LEFT_ALIGNMENT);

        // Mã phiếu đặt thuốc vẫn được tạo tự động nhưng không hiển thị trên giao diện
        txtMaPhieuDat = new JTextField();
        txtMaPhieuDat.setEditable(false);
        
        // Dòng SĐT + Button
        JPanel pnlDongSDT = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlDongSDT.setBackground(Color.WHITE);
        pnlDongSDT.setMaximumSize(new Dimension(410, 44));
        pnlDongSDT.setPreferredSize(new Dimension(410, 44));
        pnlDongSDT.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lblSdt = new JLabel("Số điện thoại:");
        lblSdt.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblSdt.setPreferredSize(new Dimension(120, 40));
        txtSdtKH = new JTextField();
        txtSdtKH.setPreferredSize(new Dimension(175, 40));
        
        btnTimKiemKH = new JButton(new FlatSVGIcon(getClass().getResource("/img/search.svg")));
        btnTimKiemKH.setPreferredSize(new Dimension(40, 40));
        btnTimKiemKH.addActionListener(evt -> btnSearchKHActionPerformed(evt));
        
        btnThemNhanhKH = new JButton(new FlatSVGIcon(getClass().getResource("/img/add-customer.svg")));
        btnThemNhanhKH.setPreferredSize(new Dimension(40, 40));
        btnThemNhanhKH.addActionListener(evt -> {
            try { btnAddCustomerActionPerformed(evt); } catch (SQLException e) { e.printStackTrace(); }
        });
        
        pnlDongSDT.add(lblSdt); 
        pnlDongSDT.add(txtSdtKH); 
        pnlDongSDT.add(btnTimKiemKH); 
        pnlDongSDT.add(btnThemNhanhKH);
        pnlGroupThongTin.add(pnlDongSDT);
        pnlGroupThongTin.add(Box.createVerticalStrut(6));

        // Dòng Tên KH
        JPanel pnlDongTenKh = createInputRowWithLabelSize("Tên khách hàng", txtHoTenKH = new JTextField(), 200);
        pnlGroupThongTin.add(pnlDongTenKh);
        pnlGroupThongTin.add(Box.createVerticalStrut(6));
        

        // Dia Chi
        JPanel pnlDongDiaChi = createInputRowWithLabelSize("Địa chỉ", txtDiaChi = new JTextField(), 200);
        pnlGroupThongTin.add(pnlDongDiaChi);
        pnlGroupThongTin.add(Box.createVerticalStrut(6));
        cboHinhThucThanhToan = new JComboBox<String>();
        String[] hinhThucThanhToan = {"Tại chỗ" , "Thanh toán online"};
        for (String hinhThuc : hinhThucThanhToan) {
        	cboHinhThucThanhToan.addItem(hinhThuc);
        }
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setBackground(Color.WHITE);
        p.setMaximumSize(new Dimension(440, 44));
        p.setPreferredSize(new Dimension(440, 44));
        p.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lbl = new JLabel("Hình thức thanh toán");
        lbl.setFont(new Font("Roboto", Font.PLAIN, 14));
        lbl.setPreferredSize(new Dimension(120, 40));
        cboHinhThucThanhToan.setPreferredSize(new Dimension(200, 40));
        p.add(lbl);
        p.add(cboHinhThucThanhToan);
        p.setPreferredSize(new Dimension(440, 40));
        
        pnlGroupThongTin.add(p);
        pnlFormNhapLieuThanhToan.add(pnlGroupThongTin);
        pnlFormNhapLieuThanhToan.add(Box.createVerticalStrut(10));

        // Group: Tiền
        JPanel pnlGroupTien = new JPanel();
        pnlGroupTien.setLayout(new BoxLayout(pnlGroupTien, BoxLayout.Y_AXIS));
        pnlGroupTien.setBackground(Color.WHITE);
        pnlGroupTien.setMaximumSize(new Dimension(440, 50));
        pnlGroupTien.setAlignmentX(LEFT_ALIGNMENT);

        // Tổng tiền
        JPanel pnlDongTong = createInputRowWithLabelSize("Tổng hóa đơn:", txtTongTien = new JTextField(), 200);
        ((JLabel)pnlDongTong.getComponent(0)).setForeground(new Color(255, 51, 0));
        ((JLabel)pnlDongTong.getComponent(0)).setFont(new Font("Roboto", Font.BOLD, 14));
        txtTongTien.setEditable(false); txtTongTien.setForeground(new Color(255, 51, 0));
        txtTongTien.setFont(new Font("Roboto Mono Medium", Font.PLAIN, 14));
        pnlGroupTien.add(pnlDongTong);


        pnlFormNhapLieuThanhToan.add(pnlGroupTien);
        pnlThongTinThanhToan.add(pnlFormNhapLieuThanhToan, BorderLayout.CENTER);

        // Button Action
        pnlNutDatHang = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        pnlNutDatHang.setBackground(Color.WHITE);
        pnlNutDatHang.setPreferredSize(new Dimension(500, 58));

        btnHuyPhieuDat = new JButton("HỦY BỎ");
        btnHuyPhieuDat.setBackground(new Color(255, 102, 102));
        btnHuyPhieuDat.setForeground(Color.WHITE);
        btnHuyPhieuDat.setFont(new Font("Roboto Mono Medium", Font.PLAIN, 16));
        btnHuyPhieuDat.setPreferredSize(new Dimension(200, 40));
        btnHuyPhieuDat.addActionListener(evt -> btnHuyActionPerformed(evt));
        
        btnDatThuoc = new JButton("ĐẶT THUỐC");
        btnDatThuoc.setBackground(new Color(0, 204, 51));
        btnDatThuoc.setForeground(Color.WHITE);
        btnDatThuoc.setFont(new Font("Roboto Mono Medium", Font.PLAIN, 16));
        btnDatThuoc.setPreferredSize(new Dimension(200, 40));
        btnDatThuoc.addActionListener(evt -> {
			try {
				btnDatThuoc(evt);
			} catch (SQLException e) {
				e.printStackTrace();
				showError("Không thể đặt thuốc: " + e.getMessage());
			}
		});

        pnlNutDatHang.add(btnHuyPhieuDat);
        pnlNutDatHang.add(btnDatThuoc);
        pnlThongTinThanhToan.add(pnlNutDatHang, BorderLayout.PAGE_END);

        pnlKhuVucThanhToan.add(pnlThongTinThanhToan, BorderLayout.SOUTH);
        add(pnlKhuVucThanhToan, BorderLayout.EAST);
    }
    
    // Helper để tạo dòng input nhanh gọn
    private JPanel createInputRow(String labelText, JTextField textField) {
        JPanel p = new JPanel(new BorderLayout(14, 0));
        p.setBackground(Color.WHITE);
        p.setMaximumSize(new Dimension(500, 40));
        p.setPreferredSize(new Dimension(500, 40));
        p.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Roboto", Font.BOLD, 16));
        lbl.setPreferredSize(new Dimension(110, 40));
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("Roboto", Font.PLAIN, 16));
        p.add(lbl, BorderLayout.WEST);
        p.add(textField, BorderLayout.CENTER);
        return p;
    }

    private void cauHinhBangGioHang() {
        tblGioHang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tblGioHang.setFont(new Font("Arial", Font.PLAIN, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        tblGioHang.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblGioHang.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblGioHang.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblGioHang.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
    }
    
    private JPanel createInputRowWithLabelSize(String labelText, JTextField textField, int textWidth) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setBackground(Color.WHITE);
        p.setMaximumSize(new Dimension(440, 44));
        p.setPreferredSize(new Dimension(440, 44));
        p.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Roboto", Font.PLAIN, 14));
        lbl.setPreferredSize(new Dimension(120, 40));
        textField.setPreferredSize(new Dimension(textWidth, 40));
        p.add(lbl);
        p.add(textField);
        return p;
    }

    private void loadDataThuoc() throws SQLException {
        hanSuDungMap.clear();
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                if (!isSelected) {
                    comp.setForeground(Color.BLACK);
                }
                return comp;
            }
        };

        for (int i = 0; i < tblDanhSachThuoc.getColumnCount(); i++) {
            tblDanhSachThuoc.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        modelDanhSachThuoc.setRowCount(0);
        ArrayList<Thuoc> dsThuoc = thuocDAO.getDsThuocBanDuoc();
        int count = 1;
        for (Thuoc thuoc : dsThuoc) {
            hanSuDungMap.put(thuoc.getMaThuoc(), thuoc.getHanSuDung());
            modelDanhSachThuoc.addRow(new Object[] {
                count++,
                thuoc.getMaThuoc(),
                thuoc.getTenThuoc(),
                dmtDAO.getDanhMucThuocQuaMaDanhMuc(thuoc.getDanhMucThuoc().getMaDanhMuc()).getTenDanhMuc(),
                thuoc.getXuatXu(),
                thuoc.getDonViTinh(),
                thuoc.getSoLuongTon(),
                String.format("%,.0f", thuoc.getGiaBan())
            });
        }
        apDungSoLuongTrongGioVaoBangThuoc();
        if (modelGioHang != null) {
            updateCartTable();
        }
    }

    private void apDungSoLuongTrongGioVaoBangThuoc() {
        if (dsChiTietPhieuDatThuoc == null || dsChiTietPhieuDatThuoc.isEmpty()) {
            return;
        }

        for (int i = 0; i < modelDanhSachThuoc.getRowCount(); i++) {
            String maThuoc = String.valueOf(modelDanhSachThuoc.getValueAt(i, 1));
            int soLuongTrongGio = getSoLuongTrongGio(maThuoc);
            if (soLuongTrongGio <= 0) {
                continue;
            }

            Object tonHienTaiObj = modelDanhSachThuoc.getValueAt(i, 6);
            int tonHienTai = (tonHienTaiObj instanceof Integer)
                    ? (Integer) tonHienTaiObj
                    : Integer.parseInt(tonHienTaiObj.toString().replace(",", "").replace(".", ""));
            modelDanhSachThuoc.setValueAt(Math.max(0, tonHienTai - soLuongTrongGio), i, 6);
        }
    }

    private int getSoLuongTrongGio(String maThuoc) {
        int soLuong = 0;
        for (ChiTietPhieuDatThuoc item : dsChiTietPhieuDatThuoc) {
            if (item.getThuoc() != null && maThuoc.equals(item.getThuoc().getMaThuoc())) {
                soLuong += item.getSoLuong();
            }
        }
        return soLuong;
    }
    
    private void tableMouseClicked(MouseEvent evt) {
        applySelectedThuocRowToDetailPanel();
    }

    private void applySelectedThuocRowToDetailPanel() {
        int index = tblDanhSachThuoc.getSelectedRow();
        if (index < 0) {
            return;
        }
        String maThuoc = (String) tblDanhSachThuoc.getValueAt(index, 1);
        try {
            Thuoc thuoc = thuocDAO.getThuocBanDuocTheoMa(maThuoc);
            if (thuoc != null) {
                txtMaThuoc.setText(thuoc.getMaThuoc());
                txtTenThuoc.setText(thuoc.getTenThuoc());
                txaThanhPhan.setText(thuoc.getThanhPhan());
                txtDonGia.setText(String.format("%,.0f", thuoc.getGiaBan()));
                ImageHelper.setImageKeepRatio(lblHinhAnh, thuoc.getHinhAnh(), 280, 180);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void btnSearchKHActionPerformed(ActionEvent evt) {
        String sdt = txtSdtKH.getText().trim();
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSdtKH.requestFocus();
            return;
        }
        if (!sdt.matches("0\\d{9}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ! Vui lòng nhập đúng 10 chữ số bắt đầu bằng 0.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSdtKH.requestFocus();
            return;
        }
        try {
            KhachHang kh = khachHangDAO.getKhachHangTheoSDT(sdt);
            if (kh != null) {
                txtHoTenKH.setText(kh.getHoTen());
                txtHoTenKH.setEditable(false);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                txtHoTenKH.setText("");
                txtHoTenKH.setEditable(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Lỗi: " + e.getMessage());
        }
    }

    private void btnAddCartActionPerformed(ActionEvent evt) {
        currentMaThuoc = txtMaThuoc.getText();
        if (currentMaThuoc.isEmpty()) {
            showWarning("Vui lòng chọn thuốc trước!");
            return;
        }
        String soLuongStr = txtSoLuongNhap.getText().trim();
        if (soLuongStr.isEmpty()) {
            showWarning("Vui lòng nhập số lượng!");
            txtSoLuongNhap.requestFocus();
            return;
        }
        try {
            int soLuong = Integer.parseInt(soLuongStr);
            if (soLuong <= 0) {
                showWarning("Số lượng phải lớn hơn 0!");
                return;
            }
            Thuoc thuoc = thuocDAO.getThuocBanDuocTheoMa(currentMaThuoc);
            if (thuoc == null) {
                showError("Không tìm thấy thuốc!");
                return;
            }
            // Kiểm tra hạn sử dụng
            if (thuoc.getHanSuDung() != null) {
                java.sql.Date sqlHsd = new java.sql.Date(thuoc.getHanSuDung().getTime());
                if (sqlHsd.before(java.sql.Date.valueOf(LocalDate.now()))) {
                    showWarning("Thuốc \"" + thuoc.getTenThuoc() + "\" đã hết hạn sử dụng!\nKhông thể thêm vào giỏ hàng.");
                    return;
                }
            }
            int rowSelected = tblDanhSachThuoc.getSelectedRow();
            if (rowSelected < 0) {
                showWarning("Vui lòng chọn thuốc từ bảng!");
                return;
            }
            
            int soLuongDaTrongGio = 0;
            for (ChiTietPhieuDatThuoc item : dsChiTietPhieuDatThuoc) {
                if (item.getThuoc().getMaThuoc().equals(currentMaThuoc)) {
                    soLuongDaTrongGio = item.getSoLuong();
                    break;
                }
            }
            int soLuongTonThucTe = thuoc.getSoLuongTon();
            if (soLuongDaTrongGio + soLuong > soLuongTonThucTe) {
                showWarning("Số lượng tồn kho không đủ!"
                        + "\nTồn kho thực tế: " + soLuongTonThucTe
                        + "\nĐã có trong giỏ: " + soLuongDaTrongGio
                        + "\nSố lượng muốn thêm: " + soLuong);
                return;
            }
            
            boolean found = false;
            for (ChiTietPhieuDatThuoc item : dsChiTietPhieuDatThuoc) {
                if (item.getThuoc().getMaThuoc().equals(currentMaThuoc)) {
                    item.setSoLuong(item.getSoLuong() + soLuong);
                    found = true;
                    break;
                }
            }
            if (!found) {
                PhieuDatThuoc pdt = new PhieuDatThuoc(txtMaPhieuDat.getText());
                ChiTietPhieuDatThuoc ctpdt = new ChiTietPhieuDatThuoc(thuoc, pdt, thuoc.getGiaBan(), soLuong);
//                ChiTietPhieuDatThuoc ctpdt = new ChiTietPhieuDatThuoc(pdt, thuoc, thuoc.getGiaBan(), soLuong);
                dsChiTietPhieuDatThuoc.add(ctpdt);
            }
            updateSoLuongTon(rowSelected, soLuong);
            updateCartTable();
            calculateTotal();
            txtSoLuongNhap.setText("");
            showSuccess("Đã thêm vào giỏ hàng!");
        } catch (NumberFormatException e) {
            showError("Số lượng không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi: " + e.getMessage());
        }
    }

    private void updateSoLuongTon(int rowSelected, int soLuongDaChon) {
        if (rowSelected >= 0) {
            Object val = tblDanhSachThuoc.getValueAt(rowSelected, 6);
            int currentSoLuongTon = (val instanceof Integer) ? (Integer) val : Integer.parseInt(val.toString());
            int soLuongSau = currentSoLuongTon - soLuongDaChon;
            tblDanhSachThuoc.setValueAt(soLuongSau, rowSelected, 6);
        }
    }

    private void updateCartTable() {
        modelGioHang.setRowCount(0);
        int stt = 1;
        for (ChiTietPhieuDatThuoc item : dsChiTietPhieuDatThuoc) {
            modelGioHang.addRow(new Object[]{
                stt++,
                item.getThuoc().getTenThuoc(),
                item.getSoLuong(),
                String.format("%,.0f", item.getDonGia()),
                String.format("%,.0f", item.getThanhTien())
            });
        }
    }

    private void calculateTotal() {
        double tamTinh = 0;
        for (ChiTietPhieuDatThuoc item : dsChiTietPhieuDatThuoc) {
            tamTinh += item.getThanhTien();
        }
        Date ngayTinh = new Date();
        thueApDung = layThueApDung();
        khuyenMaiApDung = layKhuyenMaiApDung(ngayTinh);

        double phanTramGiam = khuyenMaiApDung != null ? khuyenMaiApDung.getPhanTramGiamGia() : 0;
        double tienGiam = tamTinh * phanTramGiam / 100;
        double tienSauGiam = tamTinh - tienGiam;
        double phanTramThue = thueApDung != null ? thueApDung.getPhanTramThue() : 0;
        tongTien = tienSauGiam + tienSauGiam * phanTramThue / 100;
        txtTongTien.setText(String.format("%,.0f", tongTien));
    }

    private Thue layThueApDung() {
        try {
            ArrayList<Thue> dsThue = thueDAO.getDsThue();
            return dsThue != null && !dsThue.isEmpty() ? dsThue.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private KhuyenMai layKhuyenMaiApDung(Date ngayTinh) {
        try {
            KhuyenMai ketQua = null;
            for (KhuyenMai km : khuyenMaiDAO.getDsKhuyenMai()) {
                if (isPromotionActive(ngayTinh, km)
                        && (ketQua == null || km.getPhanTramGiamGia() > ketQua.getPhanTramGiamGia())) {
                    ketQua = km;
                }
            }
            return ketQua;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void btnDeleteCartItemActionPerformed(ActionEvent evt) {
        int selectedRow = tblGioHang.getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Vui lòng chọn sản phẩm cần xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ChiTietPhieuDatThuoc itemToRemove = dsChiTietPhieuDatThuoc.get(selectedRow);
            String maThuoc = itemToRemove.getThuoc().getMaThuoc();
            int soLuong = itemToRemove.getSoLuong();
            
            for (int i = 0; i < tblDanhSachThuoc.getRowCount(); i++) {
                if (tblDanhSachThuoc.getValueAt(i, 1).equals(maThuoc)) {
                    Object val = tblDanhSachThuoc.getValueAt(i, 6);
                    int currentSoLuongTon = (val instanceof Integer) ? (Integer) val : Integer.parseInt(val.toString());
                    int soLuongMoi = currentSoLuongTon + soLuong;
                    tblDanhSachThuoc.setValueAt(soLuongMoi, i, 6);
                    break;
                }
            }
            dsChiTietPhieuDatThuoc.remove(selectedRow);
            updateCartTable();
            calculateTotal();
        }
    }

    private void btnDatThuoc(ActionEvent evt) throws SQLException {
        if (dsChiTietPhieuDatThuoc.size() > 0) {
        	// Tạo mã phiếu đặt thuốc tự động tại thời điểm đặt thuốc
        	generateMaPhieuDatThuoc();
        	String maPhieuDat = txtMaPhieuDat.getText();
        	Date ngayDat = new Date();
            thueApDung = layThueApDung();
            khuyenMaiApDung = layKhuyenMaiApDung(ngayDat);
        	String sdtKhachHang = txtSdtKH.getText().trim();
        	if (sdtKhachHang.isBlank()) {
        		JOptionPane.showMessageDialog(this, "Số điện thoại khách hàng không được để trống!");
        		return;
        	}
        	if (!sdtKhachHang.matches("0\\d{9}")) {
        		JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ! Vui lòng nhập đúng 10 chữ số bắt đầu bằng 0.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        		txtSdtKH.requestFocus();
        		return;
        	}
            String tenKhachHang = txtHoTenKH.getText().trim();
            if (!tenKhachHang.isEmpty() && !laTenKhachHangHopLe(tenKhachHang)) {
                JOptionPane.showMessageDialog(this, "Tên khách hàng chỉ được chứa chữ cái và khoảng trắng.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtHoTenKH.requestFocus();
                return;
            }
        	KhachHang khachHang = khachHangDAO.getKhachHangTheoSDT(sdtKhachHang);
        	if (khachHang == null) {
        		JOptionPane.showMessageDialog(this, "Khách hàng không có sẵn trong hệ thống!\nVui lòng ấn nút thêm khách hàng để thêm mới.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        		return;
        	}
        	String diaChi = txtDiaChi.getText();
        	if (diaChi.isBlank()) {
        		JOptionPane.showMessageDialog(this, "Địa chỉ của khách hàng không được để trống!");
        		return;
        	}
        	String hinhThucThanhToan = cboHinhThucThanhToan.getSelectedItem().toString();
        	String trangThai = "Chưa hoàn thành";
            NhanVien nhanVien = taiKhoan != null && taiKhoan.getNhanVien() != null
                    ? new NhanVien(taiKhoan.getNhanVien().getMaNV())
                    : null;
        	PhieuDatThuoc pdt = new PhieuDatThuoc(maPhieuDat, ngayDat, khachHang, nhanVien,
                    thueApDung, khuyenMaiApDung, diaChi, hinhThucThanhToan, trangThai);
    		for (ChiTietPhieuDatThuoc ctpdt : dsChiTietPhieuDatThuoc) {
    			ctpdt.getPhieuDatThuoc().setMaPhieuDat(maPhieuDat);
    		}
        	if (pdtDAO.taoPhieuDatVaTruTon(pdt, dsChiTietPhieuDatThuoc)) {
        		showSuccess("Đặt thuốc thành công. Tồn kho đã được cập nhật.");
        		resetForm();
        	}
        }
    }

    private void btnHuyActionPerformed(ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn hủy phiếu đặt này ?", "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) resetForm();
    }

    private boolean laTenKhachHangHopLe(String tenKhachHang) {
        return tenKhachHang != null && tenKhachHang.trim().matches("[\\p{L}\\s]+");
    }

    public void resetForm() {
        dsChiTietPhieuDatThuoc.clear();
        updateCartTable();
        txtMaThuoc.setText("");
        txtTenThuoc.setText("");
        txaThanhPhan.setText("");
        txtDonGia.setText("");
        lblHinhAnh.setIcon(null);
        lblHinhAnh.setText("Chọn thuốc để xem hình ảnh");
        txtSoLuongNhap.setText("");
        currentMaThuoc = "";
        
        txtSdtKH.setText("");
        txtHoTenKH.setText("");
        txtHoTenKH.setEditable(true);
        txtDiaChi.setText("");
        cboHinhThucThanhToan.setSelectedIndex(0);
        txtTongTien.setText("");
        tongTien = 0;
        thueApDung = null;
        khuyenMaiApDung = null;
        

        try { loadDataThuoc(); } catch (SQLException e) { e.printStackTrace(); }
    }

    private void btnReloadActionPerformed(ActionEvent evt) {
        try {
            txtTimKiem.setText("");
            cboLoaiTimKiem.setSelectedIndex(0);
            daCanhBaoKyTuTimKiem = false;
            tblDanhSachThuoc.setRowSorter(null);
            loadDataThuoc();
            tblDanhSachThuoc.setRowSorter(null);
            showInfo("Đã làm mới danh sách thuốc!");
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Lỗi: " + e.getMessage());
        }
    }

    private void locDanhSachThuoc() {
        String keyword = txtTimKiem.getText().trim();
        // Table Sorter giúp tìm kiếm dữ liệu ngay trên bảng
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelDanhSachThuoc);
        tblDanhSachThuoc.setRowSorter(sorter);
        if (keyword.length() == 0) {
            daCanhBaoKyTuTimKiem = false;
            sorter.setRowFilter(null);
        } else if (coKyTuDacBietTimKiem(keyword)) {
            sorter.setRowFilter(null);
            if (!daCanhBaoKyTuTimKiem) {
                daCanhBaoKyTuTimKiem = true;
                showWarning("Từ khóa tìm kiếm không được chứa ký tự đặc biệt: \\ [ ] { } ( ) + * ? ^ $ |");
            }
        } else {
            daCanhBaoKyTuTimKiem = false;
            String regex = "(?iu)" + Pattern.quote(keyword);
            int cotTimKiem = getCotTimKiemDangChon();
            if (cotTimKiem == -1) {
                sorter.setRowFilter(RowFilter.regexFilter(regex));
            } else {
                sorter.setRowFilter(RowFilter.regexFilter(regex, cotTimKiem));
            }
        }
    }

    private int getCotTimKiemDangChon() {
        Object selected = cboLoaiTimKiem.getSelectedItem();
        if (selected == null) {
            return -1;
        }

        String loaiTimKiem = selected.toString();
        if ("Mã".equals(loaiTimKiem)) {
            return 1;
        }
        if ("Tên".equals(loaiTimKiem)) {
            return 2;
        }
        return -1;
    }

    private boolean coKyTuDacBietTimKiem(String keyword) {
        return keyword.matches(".*[\\\\\\[\\]{}()+*?^$|].*");
    }

    private void loadKhuyenMai() {
        try {
            pnlNoiDungKhuyenMai.removeAll();
            ArrayList<KhuyenMai> dsKhuyenMai = khuyenMaiDAO.getDsKhuyenMai();
            Date now = new Date();
            int count = 0;
            for (KhuyenMai km : dsKhuyenMai) {
                if (km.getNgayBatDau() != null && km.getNgayKetThuc() != null) {
                    if (isPromotionActive(now, km)) {
                        JPanel kmCard = taoCardKhuyenMai(km.getTenKM(), km.getPhanTramGiamGia());
                        pnlNoiDungKhuyenMai.add(kmCard);
                        pnlNoiDungKhuyenMai.add(Box.createRigidArea(new Dimension(0, 8)));
                        count++;
                    }
                }
            }
            if (count == 0) {
                JLabel lblNoPromo = new JLabel("Hiện không có khuyến mãi");
                lblNoPromo.setFont(new Font("Roboto", Font.ITALIC, 13));
                lblNoPromo.setForeground(Color.GRAY);
                lblNoPromo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                lblNoPromo.setHorizontalAlignment(SwingConstants.CENTER);
                pnlNoiDungKhuyenMai.add(Box.createVerticalStrut(20));
                pnlNoiDungKhuyenMai.add(lblNoPromo);
            }
            pnlNoiDungKhuyenMai.revalidate();
            pnlNoiDungKhuyenMai.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Lỗi tải khuyến mãi");
        }
    }

    /**
     * Tạo một thẻ (Panel) hiển thị thông tin khuyến mãi đơn giản.
     * Thẻ bao gồm icon, tên chương trình và phần trăm giảm giá.
     * * @param tenKM           Tên của chương trình khuyến mãi
     * @param phanTramGiamGia Giá trị giảm giá (đơn vị %)
     * @return JPanel chứa giao diện của thẻ khuyến mãi
     */
    private JPanel taoCardKhuyenMai(String tenKM, double phanTramGiamGia) {
        // Khởi tạo Panel chính với BorderLayout, khoảng cách giữa các thành phần là 8px
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(new Color(255, 250, 240)); // Màu nền trắng kem (Floral White)
        
        // Tạo viền kép: Viền ngoài màu cam bo góc (2px) và viền trong trống (padding 12px 15px)
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 153, 0), 2, true),
            new javax.swing.border.EmptyBorder(12, 15, 12, 15)
        ));
        
        // Cố định kích thước thẻ là 300x80 pixels
        card.setMaximumSize(new Dimension(300, 80));
        card.setPreferredSize(new Dimension(300, 80));
        
        // --- Phần bên trái: Chứa Icon và Tên khuyến mãi ---
        JPanel leftPanel = new JPanel(new BorderLayout(8, 0));
        leftPanel.setBackground(new Color(255, 250, 240)); // Trùng màu nền với card
        
        // Hiển thị biểu tượng quà tặng bằng Emoji
        JLabel lblIcon = new JLabel("🎁");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        leftPanel.add(lblIcon, BorderLayout.WEST);
        
        // Hiển thị tên khuyến mãi (sử dụng HTML để hỗ trợ xuống dòng nếu văn bản quá dài)
        JLabel lblTen = new JLabel("<html>" + tenKM + "</html>");
        lblTen.setFont(new Font("Roboto", Font.BOLD, 13));
        lblTen.setForeground(new Color(51, 51, 51)); // Màu chữ xám đậm
        leftPanel.add(lblTen, BorderLayout.CENTER);
        
        // --- Phần bên phải: Chứa số phần trăm giảm giá ---
        JLabel lblGiam = new JLabel(String.format("-%.0f%%", phanTramGiamGia));
        lblGiam.setFont(new Font("Roboto", Font.BOLD, 22));
        lblGiam.setForeground(new Color(255, 51, 0)); // Màu đỏ cam nổi bật
        lblGiam.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Thêm các thành phần vào Card chính
        card.add(leftPanel, BorderLayout.CENTER);
        card.add(lblGiam, BorderLayout.EAST);
        
        // --- Hiệu ứng tương tác (Hover Effect) ---
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                // Đổi sang màu cam nhạt hơn khi đưa chuột vào
                Color hoverColor = new Color(255, 245, 230);
                card.setBackground(hoverColor);
                leftPanel.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                // Trả về màu nền ban đầu khi đưa chuột ra ngoài
                Color normalColor = new Color(255, 250, 240);
                card.setBackground(normalColor);
                leftPanel.setBackground(normalColor);
            }
        });
        
        return card;
    }

    /**
     * Kiểm tra một ngày cụ thể có nằm trong khoảng từ ngày bắt đầu đến ngày kết thúc hay không.
     * Hàm này chuẩn hóa thời gian về 00:00:00 (bắt đầu ngày) và 23:59:59 (cuối ngày) 
     * để so sánh chính xác chỉ dựa trên ngày.
     * * @param currentDate Ngày cần kiểm tra
     * @param startDate   Ngày bắt đầu khoảng
     * @param endDate     Ngày kết thúc khoảng
     * @return true nếu nằm trong khoảng (tính cả biên), ngược lại trả về false
     */
    private boolean isInDateRange(Date currentDate, Date startDate, Date endDate) {
        // Kiểm tra an toàn: nếu bất kỳ tham số nào null thì trả về false
        if (currentDate == null || startDate == null || endDate == null) return false;

        Calendar cal = Calendar.getInstance();
        
        // 1. Chuẩn hóa currentDate về bắt đầu ngày (00:00:00.000)
        cal.setTime(currentDate); 
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); 
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0);
        Date current = cal.getTime();
        
        // 2. Chuẩn hóa startDate về bắt đầu ngày (00:00:00.000)
        cal.setTime(startDate); 
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); 
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0);
        Date start = cal.getTime();
        
        // 3. Chuẩn hóa endDate về thời điểm cuối cùng của ngày (23:59:59.999)
        cal.setTime(endDate); 
        cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59); 
        cal.set(Calendar.SECOND, 59); cal.set(Calendar.MILLISECOND, 999);
        Date end = cal.getTime();
        
        // Trả về true nếu: start <= current <= end
        return (current.equals(start) || current.after(start)) && 
               (current.equals(end) || current.before(end));
    }

    private boolean isPromotionActive(Date currentDate, KhuyenMai khuyenMai) {
        if (khuyenMai == null || currentDate == null
                || khuyenMai.getNgayBatDau() == null || khuyenMai.getNgayKetThuc() == null) {
            return false;
        }
        if (!khuyenMai.isLapHangNam()) {
            return isInDateRange(currentDate, khuyenMai.getNgayBatDau(), khuyenMai.getNgayKetThuc());
        }
        return isInAnnualDateRange(currentDate, khuyenMai.getNgayBatDau(), khuyenMai.getNgayKetThuc());
    }

    private boolean isInAnnualDateRange(Date currentDate, Date startDate, Date endDate) {
        int current = monthDayValue(currentDate);
        int start = monthDayValue(startDate);
        int end = monthDayValue(endDate);
        return start <= end
                ? current >= start && current <= end
                : current >= start || current <= end;
    }

    private int monthDayValue(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    private void btnAddCustomerActionPerformed(ActionEvent evt) throws SQLException {
        ArrayList<KhachHang> dsKH = khachHangDAO.getDSKhachHang();
        int oldSize = dsKH.size();
        String soDienThoai = txtSdtKH.getText().trim();
        new DialogThemKhachHang(null, new FormQuanLyKhachHang(), soDienThoai).setVisible(true);
        ArrayList<KhachHang> newDsKH = khachHangDAO.getDSKhachHang();
        if (oldSize != newDsKH.size()) {
            KhachHang khNew = newDsKH.get(newDsKH.size() - 1);
            txtSdtKH.setText(khNew.getSoDienThoai());
            txtHoTenKH.setText(khNew.getHoTen());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }
    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}
