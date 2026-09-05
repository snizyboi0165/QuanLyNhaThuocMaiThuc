package gui.form;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.KhuyenMaiDAO;
import dao.NhanVienDAO;
import dao.ThuocDAO;
import dao.ThueDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.Thue;
import entity.Thuoc;
import gui.dialog.DialogChiTietHoaDon;

public class FormTimKiemHoaDon extends JPanel 
{
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtMaHD;
    private JTextField txtMaNV;
    private JTextField txtMaKH;
    private JTextField txtMaThue;
    private JTextField txtMaKM;
    private JTextField txtMaPDT;
    private JTextField txtNgayLap;
    
   
    private JButton btnTimKiem;
    private JButton btnLamMoi;
    private JButton btnXuatHoaDon;
    private JButton btnXemChiTiet;
    
 
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JLabel lblKetQua;
    
    
    private HoaDonDAO hoaDonDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private ThuocDAO thuocDAO;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;
    private ThueDAO thueDAO;
    private KhuyenMaiDAO khuyenMaiDAO;
    
   
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    
    private Font labelFont = new Font("Roboto", Font.PLAIN, 14);
    private Font fieldFont = new Font("Roboto", Font.PLAIN, 14);
    private Font titleFont = new Font("Roboto", Font.BOLD, 24);
    private Font headerTableFont = new Font("Roboto", Font.BOLD, 16);
    
    public FormTimKiemHoaDon() {
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        thuocDAO = new ThuocDAO();
        khachHangDAO = new KhachHangDAO();
        nhanVienDAO = new NhanVienDAO();
        thueDAO = new ThueDAO();
        khuyenMaiDAO = new KhuyenMaiDAO();
        initComponents();
        hienThiTatCaHoaDon();
    }
    
    private void initComponents() {
        setBackground(new Color(230, 245, 245));
        setBorder(new LineBorder(new Color(230, 245, 245), 6, true));
        setMinimumSize(new Dimension(1130, 800));
        setPreferredSize(new Dimension(1130, 800));
        setLayout(new BorderLayout(0, 10));
        
        // Header Panel
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Center Panel (Search Form + Table)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(new Color(230, 245, 245));
        centerPanel.add(createSearchPanel(), BorderLayout.NORTH);
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * Tạo header panel với tiêu đề
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(1130, 70));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel lblTitle = new JLabel("TÌM KIẾM HÓA ĐƠN");
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblIcon = new JLabel(new FlatSVGIcon(getClass().getResource("/img/search.svg")));
        lblIcon.setBorder(new EmptyBorder(0, 20, 0, 0));
        
        headerPanel.add(lblIcon, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Tạo panel tìm kiếm với tiêu chí
     */
    private JPanel createSearchPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel lblSearchTitle = new JLabel("Tiêu chí tìm kiếm");
        lblSearchTitle.setFont(new Font("Roboto", Font.BOLD, 18));
        lblSearchTitle.setForeground(new Color(0, 0, 205));
        lblSearchTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        mainPanel.add(lblSearchTitle, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        // Row 1: Mã HĐ, Ngày lập, Mã Thuế
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.12;
        formPanel.add(createLabel("Mã HĐ:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.22;
        txtMaHD = createTextField();
        formPanel.add(txtMaHD, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.12;
        formPanel.add(createLabel("Ngày lập:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.22;
        txtNgayLap = createTextField();
        formPanel.add(txtNgayLap, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0.12;
        formPanel.add(createLabel("Mã thuế:"), gbc);
        
        gbc.gridx = 5; gbc.weightx = 0.22;
        txtMaThue = createTextField();
        formPanel.add(txtMaThue, gbc);
        
        // Row 2: Mã NV, Mã KH, Mã KM
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.12;
        formPanel.add(createLabel("Mã NV:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.22;
        txtMaNV = createTextField();
        formPanel.add(txtMaNV, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.12;
        formPanel.add(createLabel("Mã KH:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.22;
        txtMaKH = createTextField();
        formPanel.add(txtMaKH, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0.12;
        formPanel.add(createLabel("Mã KM:"), gbc);
        
        gbc.gridx = 5; gbc.weightx = 0.22;
        txtMaKM = createTextField();
        formPanel.add(txtMaKM, gbc);
        
        // Row 3: Mã PDT
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.12;
        formPanel.add(createLabel("Mã PDT:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.22;
        txtMaPDT = createTextField();
        formPanel.add(txtMaPDT, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    // button panel
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        
        // Nút Tìm kiếm
        btnTimKiem = new JButton("TÌM KIẾM");
        btnTimKiem.setFont(new Font("Roboto", Font.BOLD, 14));
        btnTimKiem.setPreferredSize(new Dimension(150, 40));
        btnTimKiem.setBackground(new Color(0, 0, 205));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTimKiem.setBorder(new LineBorder(new Color(0, 0, 205), 2, true));
        btnTimKiem.setIcon(new FlatSVGIcon(getClass().getResource("/img/search.svg")));
        btnTimKiem.addActionListener(e -> xuLyTimKiem());
        
        // Nút Làm mới
        btnLamMoi = new JButton("LÀM MỚI");
        btnLamMoi.setFont(new Font("Roboto", Font.BOLD, 14));
        btnLamMoi.setPreferredSize(new Dimension(150, 40));
        btnLamMoi.setBackground(Color.WHITE);
        btnLamMoi.setForeground(new Color(100, 100, 100));
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLamMoi.setBorder(new LineBorder(new Color(200, 200, 200), 2, true));
        btnLamMoi.setIcon(new FlatSVGIcon(getClass().getResource("/img/reload.svg")));
        btnLamMoi.addActionListener(e -> lamMoi());
        
        // Nút Xuất hóa đơn
        btnXuatHoaDon = new JButton("XUẤT HÓA ĐƠN");
        btnXuatHoaDon.setFont(new Font("Roboto", Font.BOLD, 14));
        btnXuatHoaDon.setPreferredSize(new Dimension(180, 40));
        btnXuatHoaDon.setBackground(new Color(46, 125, 50));
        btnXuatHoaDon.setForeground(Color.WHITE);
        btnXuatHoaDon.setFocusPainted(false);
        btnXuatHoaDon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXuatHoaDon.setBorder(new LineBorder(new Color(46, 125, 50), 2, true));
        btnXuatHoaDon.setIcon(new FlatSVGIcon(getClass().getResource("/img/bill.svg")));
        btnXuatHoaDon.addActionListener(e -> xuatHoaDon());
        
        buttonPanel.add(btnTimKiem);
        buttonPanel.add(btnLamMoi);
        buttonPanel.add(btnXuatHoaDon);
        
        return buttonPanel;
    }
    

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(new Color(230, 245, 245));
        
        // Result Info Panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        resultPanel.setPreferredSize(new Dimension(1130, 50));
        
        lblKetQua = new JLabel("Tìm thấy 0 kết quả");
        lblKetQua.setFont(new Font("Roboto", Font.BOLD, 16));
        lblKetQua.setForeground(new Color(0, 0, 205));
        lblKetQua.setBorder(new EmptyBorder(10, 20, 10, 10));
        
        btnXemChiTiet = new JButton("XEM CHI TIẾT");
        btnXemChiTiet.setFont(new Font("Roboto", Font.BOLD, 13));
        btnXemChiTiet.setPreferredSize(new Dimension(140, 35));
        btnXemChiTiet.setBackground(new Color(33, 150, 243));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.setFocusPainted(false);
        btnXemChiTiet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXemChiTiet.setBorder(new LineBorder(new Color(33, 150, 243), 2, true));
        btnXemChiTiet.addActionListener(e -> xemChiTiet());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 7));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnXemChiTiet);
        
        resultPanel.add(lblKetQua, BorderLayout.WEST);
        resultPanel.add(btnPanel, BorderLayout.EAST);
        
        tablePanel.add(resultPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Mã HĐ", "Ngày lập", "Mã Thuế", "Mã NV", "Mã KH", "Mã KM", "Mã PDT"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Roboto", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setFont(headerTableFont);
        table.getTableHeader().setBackground(new Color(0, 0, 205));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Căn giữa
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Chiều rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    // Style label
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(labelFont);
        return label;
    }
    
    // style jtextfield
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(fieldFont);
        textField.setPreferredSize(new Dimension(200, 35));
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }
    
    /**
     * Xử lý tìm kiếm hóa đơn
     */
    private void xuLyTimKiem() {
        try {
            // Lấy các tiêu chí tìm kiếm
            String maHD = txtMaHD.getText().trim();
            String maNV = txtMaNV.getText().trim();
            String maKH = txtMaKH.getText().trim();
            String maThue = txtMaThue.getText().trim();
            String maKM = txtMaKM.getText().trim();
            String maPDT = txtMaPDT.getText().trim();
            String ngayLap = txtNgayLap.getText().trim();
            
            // Lấy tất cả hóa đơn
            ArrayList<HoaDon> allHoaDon = hoaDonDAO.getDsHoaDon();
            ArrayList<HoaDon> ketQua = new ArrayList<>();
            
            // Lọc theo tiêu chí
            for (HoaDon hd : allHoaDon) {
                if (!maHD.isEmpty() && !hd.getMaHD().toLowerCase().contains(maHD.toLowerCase())) {
                    continue;
                }
                if (!maNV.isEmpty() && (hd.getNhanVien() == null || 
                    !hd.getNhanVien().getMaNV().toLowerCase().contains(maNV.toLowerCase()))) {
                    continue;
                }
                if (!maKH.isEmpty() && (hd.getKhachHang() == null || 
                    !hd.getKhachHang().getMaKH().toLowerCase().contains(maKH.toLowerCase()))) {
                    continue;
                }
                if (!maThue.isEmpty() && (hd.getThue() == null || 
                    !hd.getThue().getMaThue().toLowerCase().contains(maThue.toLowerCase()))) {
                    continue;
                }
                if (!maKM.isEmpty() && (hd.getKhuyenMai() == null || 
                    !hd.getKhuyenMai().getMaKM().toLowerCase().contains(maKM.toLowerCase()))) {
                    continue;
                }
                if (!maPDT.isEmpty() && (hd.getPhieuDatThuoc() == null || 
                    !hd.getPhieuDatThuoc().getMaPhieuDat().toLowerCase().contains(maPDT.toLowerCase()))) {
                    continue;
                }
                if (!ngayLap.isEmpty() && (hd.getNgayLap() == null || 
                    !hd.getNgayLap().toString().contains(ngayLap))) {
                    continue;
                }
                
                ketQua.add(hd);
            }
            
            // Hiển thị kết quả
            hienThiKetQua(ketQua);
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tìm kiếm: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Hiển thị kết quả tìm kiếm lên bảng
     */
    private void hienThiKetQua(ArrayList<HoaDon> dsHoaDon) {
        tableModel.setRowCount(0);
        
        for (HoaDon hd : dsHoaDon) {
            tableModel.addRow(new Object[]{
                hd.getMaHD(),
                hd.getNgayLap() != null ? sdf.format(hd.getNgayLap()) : "",
                hd.getThue() != null ? hd.getThue().getMaThue() : "",
                hd.getNhanVien() != null ? hd.getNhanVien().getMaNV() : "",
                hd.getKhachHang() != null ? hd.getKhachHang().getMaKH() : "",
                hd.getKhuyenMai() != null ? hd.getKhuyenMai().getMaKM() : "",
                hd.getPhieuDatThuoc() != null ? hd.getPhieuDatThuoc().getMaPhieuDat() : ""
            });
        }
        
        lblKetQua.setText("Tìm thấy " + dsHoaDon.size() + " kết quả");
        
        if (dsHoaDon.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy hóa đơn nào phù hợp!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Làm mới form
     */
    private void lamMoi() {
        txtMaHD.setText("");
        txtMaNV.setText("");
        txtMaKH.setText("");
        txtMaThue.setText("");
        txtMaKM.setText("");
        txtMaPDT.setText("");
        txtNgayLap.setText("");
        
        tableModel.setRowCount(0);
        lblKetQua.setText("Tìm thấy 0 kết quả");
        
        hienThiTatCaHoaDon();
    }
    
    /**
     * Xem chi tiết hóa đơn
     */
    private void xemChiTiet() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn hóa đơn cần xem!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maHD = tableModel.getValueAt(selectedRow, 0).toString();
        
        try {
            ArrayList<HoaDon> allHoaDon = hoaDonDAO.getDsHoaDon();
            HoaDon hd = null;
            
            for (HoaDon h : allHoaDon) {
                if (h.getMaHD().equals(maHD)) {
                    hd = h;
                    break;
                }
            }
            
            if (hd != null) {
                DialogChiTietHoaDon dialog = new DialogChiTietHoaDon(
                    (JFrame) SwingUtilities.getWindowAncestor(this),maHD
                );
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy hóa đơn này!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi lấy thông tin: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void xuatHoaDon() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "Không có dữ liệu để xuất!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn hóa đơn cần xuất!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        String maHD = tableModel.getValueAt(modelRow, 0).toString();
        try {
            String outputPath = taoFilePdfHoaDon(maHD);
            JOptionPane.showMessageDialog(this,
                "Xuất hóa đơn thành công!\nĐã lưu tại: " + outputPath,
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(outputPath));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xuất hóa đơn: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private String taoFilePdfHoaDon(String maHD) throws Exception {
        HoaDon hoaDon = hoaDonDAO.getHoaDonTheoMa(maHD);
        if (hoaDon == null) {
            throw new SQLException("Không tìm thấy hóa đơn " + maHD);
        }

        ArrayList<ChiTietHoaDon> dsChiTiet = chiTietHoaDonDAO.getChiTietHoaDonTheoMaHD(maHD);
        if (dsChiTiet.isEmpty()) {
            throw new SQLException("Hóa đơn " + maHD + " không có chi tiết sản phẩm");
        }

        NhanVien nhanVien = hoaDon.getNhanVien() != null
                ? nhanVienDAO.getNhanVienTheoMa(hoaDon.getNhanVien().getMaNV())
                : null;
        KhachHang khachHang = hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getMaKH() != null
                ? khachHangDAO.getKhachHangTheoMa(hoaDon.getKhachHang().getMaKH())
                : null;
        Thue thue = hoaDon.getThue() != null && hoaDon.getThue().getMaThue() != null
                ? thueDAO.getThueTheoMa(hoaDon.getThue().getMaThue())
                : null;
        KhuyenMai khuyenMai = hoaDon.getKhuyenMai() != null && hoaDon.getKhuyenMai().getMaKM() != null
                ? khuyenMaiDAO.getKhuyenMaiTheoMa(hoaDon.getKhuyenMai().getMaKM())
                : null;

        String folderPath = "src/LuuHoaDonInRa";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = "HoaDon_" + maHD + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
        String outputPath = folderPath + File.separator + fileName;

        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        com.itextpdf.text.Font fontStoreName = new com.itextpdf.text.Font(bf, 20, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 18, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12);
        com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);

        Paragraph storeName = new Paragraph("NHÀ THUỐC MAI THỨC", fontStoreName);
        storeName.setAlignment(Element.ALIGN_CENTER);
        document.add(storeName);

        Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        String tenNhanVien = nhanVien != null ? dinhDangTenNhanVien(nhanVien.getTenNV()) : safeText(getMaNhanVien(hoaDon));
        String tenKhachHang = khachHang != null ? khachHang.getHoTen() : "Khách lẻ";
        String soDienThoai = khachHang != null ? safeText(khachHang.getSoDienThoai()) : "";

        document.add(new Paragraph("Mã hóa đơn: " + hoaDon.getMaHD(), fontNormal));
        document.add(new Paragraph("Ngày lập: " + (hoaDon.getNgayLap() != null ? sdf.format(hoaDon.getNgayLap()) : ""), fontNormal));
        document.add(new Paragraph("Nhân viên: " + tenNhanVien, fontNormal));
        document.add(new Paragraph("Khách hàng: " + tenKhachHang, fontNormal));
        document.add(new Paragraph("Số điện thoại: " + soDienThoai, fontNormal));
        document.add(new Paragraph(" "));

        PdfPTable tablePdf = new PdfPTable(5);
        tablePdf.setWidthPercentage(100);
        tablePdf.setWidths(new float[] { 1f, 3f, 1.5f, 1.5f, 2f });

        String[] headers = { "STT", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền" };
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, fontBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tablePdf.addCell(cell);
        }

        double tamTinh = 0;
        int stt = 1;
        for (ChiTietHoaDon cthd : dsChiTiet) {
            Thuoc thuoc = cthd.getThuoc() != null ? thuocDAO.getThuocTheoMaThuoc(cthd.getThuoc().getMaThuoc()) : null;
            String tenThuoc = thuoc != null ? thuoc.getTenThuoc() : safeText(getMaThuoc(cthd));
            double thanhTien = cthd.getSoLuong() * cthd.getDonGia();
            tamTinh += thanhTien;

            addCell(tablePdf, String.valueOf(stt++), fontNormal, Element.ALIGN_CENTER);
            addCell(tablePdf, tenThuoc, fontNormal, Element.ALIGN_LEFT);
            addCell(tablePdf, String.valueOf(cthd.getSoLuong()), fontNormal, Element.ALIGN_CENTER);
            addCell(tablePdf, formatMoney(cthd.getDonGia()), fontNormal, Element.ALIGN_RIGHT);
            addCell(tablePdf, formatMoney(thanhTien), fontNormal, Element.ALIGN_RIGHT);
        }

        document.add(tablePdf);
        document.add(new Paragraph(" "));

        double tienGiamGia = khuyenMai != null ? tamTinh * khuyenMai.getPhanTramGiamGia() / 100 : 0;
        double tienSauGiamGia = tamTinh - tienGiamGia;
        double tienThue = thue != null ? tienSauGiamGia * thue.getPhanTramThue() / 100 : 0;
        double thanhToan = tienSauGiamGia + tienThue;

        document.add(new Paragraph("Tổng tiền: " + formatMoney(tamTinh), fontNormal));
        document.add(new Paragraph("Thuế: " + (thue != null ? String.format("%.0f%% (+%s)", thue.getPhanTramThue(), formatMoney(tienThue)) : formatMoney(0)), fontNormal));
        document.add(new Paragraph("Giảm giá: " + (khuyenMai != null ? String.format("%.0f%% (-%s)", khuyenMai.getPhanTramGiamGia(), formatMoney(tienGiamGia)) : formatMoney(0)), fontNormal));
        document.add(new Paragraph("Thành tiền: " + formatMoney(thanhToan), fontBold));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Cảm ơn quý khách đã mua hàng!", fontBold));
        document.close();

        return outputPath;
    }

    private void addCell(PdfPTable tablePdf, String text, com.itextpdf.text.Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        tablePdf.addCell(cell);
    }

    private String formatMoney(double amount) {
        return String.format("%,.0f VNĐ", amount);
    }

    private String safeText(String text) {
        return text == null ? "" : text;
    }

    private String getMaNhanVien(HoaDon hoaDon) {
        return hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getMaNV() : "";
    }

    private String getMaThuoc(ChiTietHoaDon chiTiet) {
        return chiTiet.getThuoc() != null ? chiTiet.getThuoc().getMaThuoc() : "";
    }

    private String dinhDangTenNhanVien(String tenNhanVien) {
        if (tenNhanVien == null || tenNhanVien.trim().isEmpty()) {
            return "N/A";
        }
        return tenNhanVien.replaceAll("\\s*\\(NV\\d+\\)\\s*$", "").trim();
    }
    
    private void hienThiTatCaHoaDon() {
        try {
            ArrayList<HoaDon> dsHoaDon = hoaDonDAO.getDsHoaDon();
            hienThiKetQua(dsHoaDon);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách hóa đơn: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
