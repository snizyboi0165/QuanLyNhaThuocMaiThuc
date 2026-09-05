package gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import dao.NhaCungCapDAO;
import dao.NhanVienDAO;
import dao.PhieuNhapThuocDAO;
import dao.ThuocDAO;
import entity.ChiTietNhapThuoc;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhapThuoc;
import entity.Thuoc;
import gui.form.FormPhieuNhapThuoc;

public class DialogThemPhieuNhap extends JDialog {
    private static final Color PRIMARY = new Color(0, 0, 205);
    private static final Color PAGE_BG = new Color(230, 245, 245);
    private static final Color FIELD_BORDER = new Color(200, 200, 200);
    private static final Color SUCCESS = new Color(46, 125, 50);

    private JComboBox<String> cboNhanVien;
    private JComboBox<String> cboNhaCungCap;
    private JComboBox<String> cboThuoc;
    private JTextField txtSoLo;
    private JDateChooser dcNgayNhap;
    private JDateChooser dcNgaySanXuat;
    private JDateChooser dcHanSuDung;
    private JSpinner spnSoLuong;
    private JTextField txtDonGia;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblTongDong;
    private JLabel lblTitle;
    private JButton btnLuu;

    private final ArrayList<ChiTietNhapThuoc> listChiTiet = new ArrayList<>();
    private final PhieuNhapThuocDAO pnhDAO = new PhieuNhapThuocDAO();
    private final NhanVienDAO nvDAO = new NhanVienDAO();
    private final NhaCungCapDAO nccDAO = new NhaCungCapDAO();
    private final ThuocDAO thuocDAO = new ThuocDAO();
    private final FormPhieuNhapThuoc parentForm;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private PhieuNhapThuoc phieuNhapHienTai;

    public DialogThemPhieuNhap(Frame parent, FormPhieuNhapThuoc form) {
        super(parent, "Thêm phiếu nhập theo lô", true);
        this.parentForm = form;
        initComponents();
        loadComboBoxData();
        setSize(1320, 780);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setResizable(false);
        getContentPane().setBackground(PAGE_BG);
        setLayout(new BorderLayout(0, 8));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 14));
        titlePanel.setBackground(PRIMARY);
        titlePanel.setPreferredSize(new Dimension(1320, 70));
        lblTitle = new JLabel("THÊM PHIẾU NHẬP THEO LÔ");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(PAGE_BG);

        JPanel criteriaPanel = createInputPanel();
        JPanel resultPanel = createTablePanel();

        GridBagConstraints gbcInput = new GridBagConstraints();
        gbcInput.gridx = 0;
        gbcInput.gridy = 0;
        gbcInput.weightx = 1;
        gbcInput.weighty = 0.40;
        gbcInput.fill = GridBagConstraints.BOTH;
        gbcInput.insets = new Insets(0, 0, 6, 0);
        contentPanel.add(criteriaPanel, gbcInput);

        GridBagConstraints gbcTable = new GridBagConstraints();
        gbcTable.gridx = 0;
        gbcTable.gridy = 1;
        gbcTable.weightx = 1;
        gbcTable.weighty = 0.60;
        gbcTable.fill = GridBagConstraints.BOTH;
        gbcTable.insets = new Insets(6, 0, 0, 0);
        contentPanel.add(resultPanel, gbcTable);

        add(contentPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 16));
        footerPanel.setBackground(PAGE_BG);
        footerPanel.setPreferredSize(new Dimension(1320, 76));

        btnLuu = createGreenButton("LƯU PHIẾU NHẬP");
        btnLuu.addActionListener(e -> xuLyThemPhieuNhap());
        JButton btnHuy = createSecondaryButton("HỦY");
        btnHuy.addActionListener(e -> dispose());

        footerPanel.add(btnLuu);
        footerPanel.add(btnHuy);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(12, 26, 12, 26)));

        JLabel lblSection = sectionLabel("Thông tin phiếu nhập");
        addSectionTitle(panel, lblSection, 0);

        cboNhanVien = createComboBox();
        cboNhaCungCap = createComboBox();
        dcNgayNhap = createDateChooser(new Date());
        cboThuoc = createComboBox();
        txtSoLo = createTextField();
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        spnSoLuong.setFont(new Font("Roboto", Font.PLAIN, 14));
        spnSoLuong.setPreferredSize(new Dimension(260, 34));
        dcNgaySanXuat = createDateChooser(null);
        dcHanSuDung = createDateChooser(null);
        txtDonGia = createTextField();

        addField(panel, 1, 0, "Nhân viên:", cboNhanVien);
        addField(panel, 1, 1, "Nhà cung cấp:", cboNhaCungCap);
        addField(panel, 2, 0, "Ngày nhập:", dcNgayNhap);
        addField(panel, 2, 1, "Thuốc:", cboThuoc);

        addSectionTitle(panel, sectionLabel("Thông tin lô thuốc"), 3);

        addField(panel, 4, 0, "Số lô:", txtSoLo);
        addField(panel, 4, 1, "Số lượng nhập:", spnSoLuong);
        addField(panel, 5, 0, "Ngày sản xuất:", dcNgaySanXuat);
        addField(panel, 5, 1, "Hạn sử dụng:", dcHanSuDung);
        addField(panel, 6, 0, "Đơn giá nhập:", txtDonGia);

        JButton btnThem = createPrimaryButton("THÊM DÒNG LÔ");
        btnThem.addActionListener(e -> themChiTiet());
        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.gridx = 0;
        gbcBtn.gridy = 7;
        gbcBtn.gridwidth = 4;
        gbcBtn.insets = new Insets(4, 0, 0, 0);
        gbcBtn.anchor = GridBagConstraints.CENTER;
        panel.add(btnThem, gbcBtn);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel resultPanel = new JPanel(new BorderLayout(0, 8));
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel resultHeader = new JPanel(new BorderLayout());
        resultHeader.setBackground(Color.WHITE);
        resultHeader.setBorder(new EmptyBorder(0, 26, 0, 22));
        lblTongDong = new JLabel("Chưa có dòng lô nào");
        lblTongDong.setFont(new Font("Roboto", Font.BOLD, 17));
        lblTongDong.setForeground(PRIMARY);
        resultHeader.add(lblTongDong, BorderLayout.WEST);

        JButton btnXoa = createBlueActionButton("XÓA DÒNG");
        btnXoa.addActionListener(e -> xoaDong());
        resultHeader.add(btnXoa, BorderLayout.EAST);
        resultPanel.add(resultHeader, BorderLayout.NORTH);

        String[] cols = { "Mã lô", "Số lô", "Mã thuốc", "Tên thuốc", "Ngày sản xuất", "Hạn sử dụng",
                "Số lượng", "Đơn giá", "Thành tiền" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setFont(new Font("Roboto", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 15));
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(225, 225, 225));
        table.setShowHorizontalLines(true);
        centerTableCells();

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        resultPanel.add(tableScroll, BorderLayout.CENTER);
        return resultPanel;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.BOLD, 19));
        label.setForeground(PRIMARY);
        return label;
    }

    private void addSectionTitle(JPanel panel, JLabel label, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(row == 0 ? 0 : 4, 0, 8, 0);
        panel.add(label, gbc);
    }

    private void addField(JPanel panel, int row, int column, String label, java.awt.Component field) {
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = column * 2;
        gbcLabel.gridy = row;
        gbcLabel.insets = new Insets(4, column == 0 ? 6 : 28, 4, 10);
        gbcLabel.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Roboto", Font.PLAIN, 14));
        panel.add(lbl, gbcLabel);

        GridBagConstraints gbcField = new GridBagConstraints();
        gbcField.gridx = column * 2 + 1;
        gbcField.gridy = row;
        gbcField.weightx = 1;
        gbcField.fill = GridBagConstraints.HORIZONTAL;
        gbcField.insets = new Insets(4, 0, 4, 6);
        panel.add(field, gbcField);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Roboto", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(320, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(FIELD_BORDER, 1),
                new EmptyBorder(4, 10, 4, 10)));
        return field;
    }

    private JComboBox<String> createComboBox() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(new Font("Roboto", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(320, 34));
        combo.setBackground(Color.WHITE);
        return combo;
    }

    private JDateChooser createDateChooser(Date date) {
        JDateChooser chooser = new JDateChooser();
        chooser.setDateFormatString("dd/MM/yyyy");
        chooser.setFont(new Font("Roboto", Font.PLAIN, 14));
        chooser.setPreferredSize(new Dimension(320, 34));
        chooser.setDate(date);
        chooser.setBackground(Color.WHITE);
        return chooser;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(190, 38));
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new LineBorder(PRIMARY, 2, true));
        return button;
    }

    private JButton createGreenButton(String text) {
        JButton button = createPrimaryButton(text);
        button.setPreferredSize(new Dimension(180, 44));
        button.setBackground(SUCCESS);
        button.setBorder(new LineBorder(SUCCESS, 2, true));
        return button;
    }

    private JButton createBlueActionButton(String text) {
        JButton button = createPrimaryButton(text);
        button.setPreferredSize(new Dimension(132, 40));
        button.setBackground(new Color(30, 144, 255));
        button.setBorder(new LineBorder(new Color(30, 144, 255), 2, true));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 44));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(80, 80, 80));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new LineBorder(FIELD_BORDER, 2, true));
        return button;
    }

    private void centerTableCells() {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        table.getColumnModel().getColumn(3).setPreferredWidth(220);
    }

    private void loadComboBoxData() {
        try {
            cboNhanVien.addItem("-- Chọn nhân viên --");
            for (NhanVien nv : nvDAO.getDSNhanVien()) {
                cboNhanVien.addItem(nv.getMaNV() + " - " + nv.getTenNV());
            }
            cboNhaCungCap.addItem("-- Chọn nhà cung cấp --");
            for (NhaCungCap ncc : nccDAO.getDSNhaCungCap()) {
                cboNhaCungCap.addItem(ncc.getMaNCC() + " - " + ncc.getTenNCC());
            }
            cboThuoc.addItem("-- Chọn thuốc --");
            for (Thuoc thuoc : thuocDAO.getDsThuoc()) {
                cboThuoc.addItem(thuoc.getMaThuoc() + " - " + thuoc.getTenThuoc());
            }
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void themChiTiet() {
        try {
            if (cboThuoc.getSelectedIndex() <= 0 || txtSoLo.getText().trim().isEmpty()
                    || dcHanSuDung.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thuốc, nhập số lô và hạn sử dụng.",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Date nsx = dcNgaySanXuat.getDate();
            Date hsd = dcHanSuDung.getDate();
            if (nsx != null && nsx.after(hsd)) {
                JOptionPane.showMessageDialog(this, "Ngày sản xuất phải trước hạn sử dụng.",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (txtDonGia.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đơn giá nhập.",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] parts = cboThuoc.getSelectedItem().toString().split(" - ", 2);
            String maThuoc = parts[0];
            String tenThuoc = parts.length > 1 ? parts[1] : "";
            String soLo = txtSoLo.getText().trim();
            int soLuong = (Integer) spnSoLuong.getValue();
            double donGia = Double.parseDouble(txtDonGia.getText().trim());

            ChiTietNhapThuoc ct = new ChiTietNhapThuoc("", null, soLo, maThuoc, nsx, hsd, soLuong, donGia);
            listChiTiet.add(ct);
            tableModel.addRow(new Object[] { "Tự động khi lưu", soLo, maThuoc, tenThuoc, format(nsx), format(hsd),
                    soLuong, String.format("%,.0f", donGia), String.format("%,.0f", ct.getThanhTien()) });
            updateSummary();

            txtSoLo.setText("");
            dcNgaySanXuat.setDate(null);
            dcHanSuDung.setDate(null);
            spnSoLuong.setValue(1);
            txtDonGia.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Đơn giá nhập phải là số.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void xoaDong() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            listChiTiet.remove(row);
            tableModel.removeRow(row);
            updateSummary();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng lô cần xóa.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void xuLyThemPhieuNhap() {
        try {
            if (cboNhanVien.getSelectedIndex() <= 0 || cboNhaCungCap.getSelectedIndex() <= 0
                    || dcNgayNhap.getDate() == null || listChiTiet.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin phiếu nhập và ít nhất một dòng lô.",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String maPN = phieuNhapHienTai == null ? pnhDAO.generateMaPhieuNhap() : phieuNhapHienTai.getMaPhieuNhap();
            String maNV = cboNhanVien.getSelectedItem().toString().split(" - ")[0];
            String maNCC = cboNhaCungCap.getSelectedItem().toString().split(" - ")[0];
            PhieuNhapThuoc pnh = new PhieuNhapThuoc(maPN, nvDAO.getNhanVienTheoMa(maNV),
                    nccDAO.getNhaCungCapTheoMa(maNCC), ganGioHienTai(dcNgayNhap.getDate()));
            for (ChiTietNhapThuoc ct : listChiTiet) {
                ct.setMaPhieuNhap(maPN);
            }
            boolean ok = phieuNhapHienTai == null
                    ? pnhDAO.themPhieuNhapThuoc(pnh, listChiTiet)
                    : pnhDAO.capNhatPhieuNhapThuoc(pnh, listChiTiet);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thành công: " + maPN,
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                parentForm.loadTableData();
                dispose();
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private Date ganGioHienTai(Date date) {
        Calendar selected = Calendar.getInstance();
        selected.setTime(date);

        Calendar now = Calendar.getInstance();
        selected.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
        selected.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
        selected.set(Calendar.SECOND, now.get(Calendar.SECOND));
        selected.set(Calendar.MILLISECOND, now.get(Calendar.MILLISECOND));
        return selected.getTime();
    }

    private String format(Date date) {
        return date == null ? "" : sdf.format(date);
    }

    protected void setEditMode(PhieuNhapThuoc phieuNhapHienTai) {
        this.phieuNhapHienTai = phieuNhapHienTai;
        setTitle("Cập nhật phiếu nhập theo lô");
        lblTitle.setText("CẬP NHẬT PHIẾU NHẬP THEO LÔ");
        btnLuu.setText("CẬP NHẬT");
        loadPhieuNhapHienTai();
    }

    protected void setPhieuNhapHienTai(PhieuNhapThuoc phieuNhapHienTai) {
        setEditMode(phieuNhapHienTai);
    }

    private void loadPhieuNhapHienTai() {
        try {
            if (phieuNhapHienTai.getNgayNhap() != null) {
                dcNgayNhap.setDate(phieuNhapHienTai.getNgayNhap());
            }
            if (phieuNhapHienTai.getNhanVien() != null) {
                selectCombo(cboNhanVien, phieuNhapHienTai.getNhanVien().getMaNV());
            }
            if (phieuNhapHienTai.getNhaCungCap() != null) {
                selectCombo(cboNhaCungCap, phieuNhapHienTai.getNhaCungCap().getMaNCC());
            }
            listChiTiet.clear();
            tableModel.setRowCount(0);
            for (ChiTietNhapThuoc ct : pnhDAO.getChiTietPhieuNhap(phieuNhapHienTai.getMaPhieuNhap())) {
                listChiTiet.add(ct);
                Thuoc thuoc = thuocDAO.getThuocTheoMaThuoc(ct.getMaThuoc());
                tableModel.addRow(new Object[] { ct.getMaLo(), ct.getSoLo(), ct.getMaThuoc(),
                        thuoc == null ? "" : thuoc.getTenThuoc(), format(ct.getNgaySanXuat()),
                        format(ct.getHanSuDung()), ct.getSoLuong(), String.format("%,.0f", ct.getDonGia()),
                        String.format("%,.0f", ct.getThanhTien()) });
            }
            updateSummary();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void updateSummary() {
        int tongSoLuong = 0;
        double tongTien = 0;
        for (ChiTietNhapThuoc ct : listChiTiet) {
            tongSoLuong += ct.getSoLuong();
            tongTien += ct.getThanhTien();
        }
        if (listChiTiet.isEmpty()) {
            lblTongDong.setText("Chưa có dòng lô nào");
        } else {
            lblTongDong.setText("Đã thêm " + listChiTiet.size() + " dòng lô | Tổng số lượng: "
                    + tongSoLuong + " | Tổng tiền: " + String.format("%,.0f", tongTien));
        }
    }

    private void selectCombo(JComboBox<String> combo, String prefix) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).startsWith(prefix)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void showError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
