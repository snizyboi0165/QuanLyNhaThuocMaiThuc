package gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import dao.DanhMucThuocDAO;
import dao.ThuocDAO;
import entity.DanhMucThuoc;
import entity.Thuoc;

public class DialogThemThuoc extends JDialog {
    private static final Color PRIMARY = new Color(0, 0, 205);
    private static final Color FOOTER_BG = new Color(230, 245, 245);
    private static final Color FIELD_BORDER = new Color(200, 200, 200);
    private static final Color WARNING_BG = new Color(255, 243, 205);
    private static final Color WARNING_BORDER = new Color(255, 193, 7);

    private JTextField txtTenThuoc;
    private JTextField txtDonViTinh;
    private JTextField txtGiaBan;
    private JTextField txtXuatXu;
    private JTextField txtHinhAnh;
    private JTextArea txtMoTa;
    private JTextArea txtThanhPhan;
    private JComboBox<String> cboxDanhMuc;

    private final ThuocDAO thuocDAO = new ThuocDAO();
    private final DanhMucThuocDAO danhMucDAO = new DanhMucThuocDAO();
    private ArrayList<DanhMucThuoc> dsDanhMuc = new ArrayList<>();
    private boolean isSuccess = false;
    private String maThuocMoi;

    public DialogThemThuoc(JFrame parent) {
        super(parent, "Thêm thuốc mới", true);
        initComponents();
        loadDanhMuc();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(760, 760);
        setResizable(false);
        getContentPane().setBackground(FOOTER_BG);
        setLayout(new BorderLayout(0, 0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
        titlePanel.setBackground(PRIMARY);
        titlePanel.setPreferredSize(new Dimension(760, 60));
        JLabel lblTitle = new JLabel("THÊM THUỐC MỚI");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setBackground(Color.WHITE);

        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        notePanel.setBackground(WARNING_BG);
        notePanel.setBorder(new LineBorder(WARNING_BORDER, 1));
        JLabel lblNote = new JLabel("Mã thuốc sẽ được tạo tự động (VD: TH00001). Tồn kho, NSX và HSD được nhập ở phiếu nhập theo lô.");
        lblNote.setFont(new Font("Roboto", Font.ITALIC, 12));
        lblNote.setForeground(new Color(102, 60, 0));
        notePanel.add(lblNote);
        centerPanel.add(notePanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(18, 28, 18, 28));

        txtTenThuoc = createTextField();
        cboxDanhMuc = createComboBox();
        txtDonViTinh = createTextField();
        txtXuatXu = createTextField();
        txtGiaBan = createTextField();
        txtHinhAnh = createTextField();
        txtThanhPhan = createTextArea(3);
        txtMoTa = createTextArea(3);

        int row = 0;
        addRow(formPanel, row++, "Tên thuốc:", txtTenThuoc);
        addRow(formPanel, row++, "Danh mục:", cboxDanhMuc);
        addRow(formPanel, row++, "Đơn vị tính:", txtDonViTinh);
        addRow(formPanel, row++, "Xuất xứ:", txtXuatXu);
        addRow(formPanel, row++, "Giá bán:", txtGiaBan);
        addRow(formPanel, row++, "Hình ảnh:", txtHinhAnh);
        addRow(formPanel, row++, "Thành phần:", wrapTextArea(txtThanhPhan));
        addRow(formPanel, row++, "Mô tả:", wrapTextArea(txtMoTa));

        centerPanel.add(formPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 16));
        buttonPanel.setBackground(FOOTER_BG);
        buttonPanel.setPreferredSize(new Dimension(760, 90));

        JButton btnLuu = createPrimaryButton("LƯU");
        btnLuu.setIcon(null);
        btnLuu.addActionListener(e -> luuThuoc());
        JButton btnHuy = createDangerButton("HỦY");
        btnHuy.setIcon(null);
        btnHuy.addActionListener(e -> dispose());

        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, int row, String label, java.awt.Component field) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.18;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Roboto", Font.BOLD, 13));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.82;
        panel.add(field, gbc);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Roboto", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(520, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(FIELD_BORDER, 1),
                new EmptyBorder(5, 10, 5, 10)));
        return field;
    }

    private JComboBox<String> createComboBox() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(new Font("Roboto", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(520, 38));
        combo.setBackground(Color.WHITE);
        return combo;
    }

    private JTextArea createTextArea(int rows) {
        JTextArea area = new JTextArea(rows, 20);
        area.setFont(new Font("Roboto", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(6, 10, 6, 10));
        return area;
    }

    private JScrollPane wrapTextArea(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(520, 92));
        scroll.setBorder(new LineBorder(FIELD_BORDER, 1));
        return scroll;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(150, 44));
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new LineBorder(PRIMARY, 2, true));
        return button;
    }

    private JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(150, 44));
        button.setBackground(new Color(220, 53, 69));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new LineBorder(new Color(220, 53, 69), 2, true));
        return button;
    }

    private void loadDanhMuc() {
        try {
            dsDanhMuc = danhMucDAO.getDsDanhMucThuoc();
            cboxDanhMuc.removeAllItems();
            for (DanhMucThuoc dm : dsDanhMuc) {
                cboxDanhMuc.addItem(dm.getTenDanhMuc());
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void luuThuoc() {
        if (!validateData()) {
            return;
        }
        try {
            DanhMucThuoc danhMuc = dsDanhMuc.get(cboxDanhMuc.getSelectedIndex());
            Thuoc thuoc = new Thuoc("", txtTenThuoc.getText().trim(), txtDonViTinh.getText().trim(),
                    Double.parseDouble(txtGiaBan.getText().trim()), 0, null, txtMoTa.getText().trim(), danhMuc,
                    txtHinhAnh.getText().trim(), txtThanhPhan.getText().trim(), null, txtXuatXu.getText().trim());
            maThuocMoi = thuocDAO.themThuoc(thuoc);
            isSuccess = maThuocMoi != null;
            if (isSuccess) {
                JOptionPane.showMessageDialog(this, "Thêm thuốc thành công!\nMã thuốc: " + maThuocMoi,
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } catch (SQLException | IllegalArgumentException e) {
            showError(e);
        }
    }

    private boolean validateData() {
        if (txtTenThuoc.getText().trim().isEmpty() || txtDonViTinh.getText().trim().isEmpty()
                || txtGiaBan.getText().trim().isEmpty() || txtXuatXu.getText().trim().isEmpty()
                || txtHinhAnh.getText().trim().isEmpty() || txtThanhPhan.getText().trim().isEmpty()
                || txtMoTa.getText().trim().isEmpty() || cboxDanhMuc.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin thuốc.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            if (Double.parseDouble(txtGiaBan.getText().trim()) < 0) {
                JOptionPane.showMessageDialog(this, "Giá bán không được âm.",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá bán phải là số.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    private void showError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMaThuocMoi() {
        return maThuocMoi;
    }
}
