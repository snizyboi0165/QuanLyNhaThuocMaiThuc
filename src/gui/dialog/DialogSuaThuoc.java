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

public class DialogSuaThuoc extends JDialog {
    private static final Color PRIMARY = new Color(0, 0, 205);
    private static final Color FOOTER_BG = new Color(230, 245, 245);
    private static final Color FIELD_BORDER = new Color(200, 200, 200);

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
    private final Thuoc thuocHienTai;
    private boolean isSuccess = false;

    public DialogSuaThuoc(JFrame parent, Thuoc thuoc) {
        super(parent, "Sửa thông tin thuốc", true);
        this.thuocHienTai = thuoc;
        initComponents();
        loadDanhMuc();
        loadDataToForm();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(620, 740);
        setResizable(false);
        getContentPane().setBackground(FOOTER_BG);
        setLayout(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
        titlePanel.setBackground(PRIMARY);
        titlePanel.setPreferredSize(new Dimension(620, 70));
        JLabel lblTitle = new JLabel("CẬP NHẬT THÔNG TIN THUỐC");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(26, 48, 22, 48));

        txtTenThuoc = createTextField();
        cboxDanhMuc = createComboBox();
        txtDonViTinh = createTextField();
        txtGiaBan = createTextField();
        txtXuatXu = createTextField();
        txtHinhAnh = createTextField();
        txtThanhPhan = createTextArea();
        txtMoTa = createTextArea();

        int row = 0;
        addRow(formPanel, row++, "Tên thuốc:", txtTenThuoc);
        addRow(formPanel, row++, "Danh mục:", cboxDanhMuc);
        addRow(formPanel, row++, "Đơn vị tính:", txtDonViTinh);
        addRow(formPanel, row++, "Giá bán:", txtGiaBan);
        addRow(formPanel, row++, "Xuất xứ:", txtXuatXu);
        addRow(formPanel, row++, "Hình ảnh:", txtHinhAnh);
        addRow(formPanel, row++, "Thành phần:", wrapTextArea(txtThanhPhan));
        addRow(formPanel, row++, "Mô tả:", wrapTextArea(txtMoTa));

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 20));
        buttonPanel.setBackground(FOOTER_BG);
        buttonPanel.setPreferredSize(new Dimension(620, 90));

        JButton btnLuu = createPrimaryButton("CẬP NHẬT");
        btnLuu.addActionListener(e -> capNhatThuoc());
        JButton btnHuy = createSecondaryButton("HỦY");
        btnHuy.addActionListener(e -> dispose());

        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, int row, String label, java.awt.Component field) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill = field instanceof JScrollPane ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.28;
        gbc.weighty = field instanceof JScrollPane ? 1.0 : 0.0;
        gbc.anchor = field instanceof JScrollPane ? GridBagConstraints.NORTHWEST : GridBagConstraints.CENTER;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Roboto", Font.PLAIN, 15));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.72;
        gbc.weighty = field instanceof JScrollPane ? 1.0 : 0.0;
        panel.add(field, gbc);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Roboto", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(360, 42));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(FIELD_BORDER, 1),
                new EmptyBorder(6, 12, 6, 12)));
        return field;
    }

    private JComboBox<String> createComboBox() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(new Font("Roboto", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(360, 42));
        combo.setBackground(Color.WHITE);
        return combo;
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea(4, 20);
        area.setFont(new Font("Roboto", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(6, 10, 6, 10));
        return area;
    }

    private JScrollPane wrapTextArea(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(360, 104));
        scroll.setBorder(new LineBorder(FIELD_BORDER, 1));
        return scroll;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 44));
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new LineBorder(PRIMARY, 2, true));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 44));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(100, 100, 100));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new LineBorder(FIELD_BORDER, 2, true));
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

    private void loadDataToForm() {
        if (thuocHienTai == null) {
            return;
        }
        txtTenThuoc.setText(thuocHienTai.getTenThuoc());
        txtDonViTinh.setText(thuocHienTai.getDonViTinh());
        txtGiaBan.setText(String.format("%.0f", thuocHienTai.getGiaBan()));
        txtXuatXu.setText(thuocHienTai.getXuatXu());
        txtHinhAnh.setText(thuocHienTai.getHinhAnh());
        txtThanhPhan.setText(thuocHienTai.getThanhPhan());
        txtMoTa.setText(thuocHienTai.getMoTa());
        if (thuocHienTai.getDanhMucThuoc() != null) {
            cboxDanhMuc.setSelectedItem(thuocHienTai.getDanhMucThuoc().getTenDanhMuc());
        }
    }

    private void capNhatThuoc() {
        if (!validateData()) {
            return;
        }
        try {
            DanhMucThuoc danhMuc = dsDanhMuc.get(cboxDanhMuc.getSelectedIndex());
            Thuoc thuoc = new Thuoc(thuocHienTai.getMaThuoc(), txtTenThuoc.getText().trim(),
                    txtDonViTinh.getText().trim(), Double.parseDouble(txtGiaBan.getText().trim()), 0, null,
                    txtMoTa.getText().trim(), danhMuc, txtHinhAnh.getText().trim(),
                    txtThanhPhan.getText().trim(), null, txtXuatXu.getText().trim());
            isSuccess = thuocDAO.capNhatThuoc(thuoc);
            if (isSuccess) {
                JOptionPane.showMessageDialog(this, "Cập nhật thuốc thành công.",
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
}
