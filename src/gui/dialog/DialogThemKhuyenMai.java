package gui.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import com.toedter.calendar.JDateChooser;
import dao.KhuyenMaiDAO;
import entity.KhuyenMai;
import gui.form.FormKhuyenMai;

import java.util.Date;

public class DialogThemKhuyenMai extends JDialog {
    private JTextField txtTenKM;
    private JDateChooser dateChooserBatDau;
    private JDateChooser dateChooserKetThuc;
    private JSpinner spinnerPhanTram;
    private KhuyenMaiDAO kmDAO;
    private FormKhuyenMai parentForm;

    public DialogThemKhuyenMai(Frame parent, FormKhuyenMai form) {
        super(parent, "Thêm Khuyến Mãi Mới", true);
        this.parentForm = form;
        this.kmDAO = new KhuyenMaiDAO();
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(550, 450);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblTitle = new JLabel("THÊM KHUYẾN MÃI MỚI");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);

        Font labelFont = new Font("Roboto", Font.PLAIN, 14);
        Font fieldFont = new Font("Roboto", Font.PLAIN, 14);

        // Tên Khuyến Mãi
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Tên khuyến mãi: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtTenKM = new JTextField();
        txtTenKM.setFont(fieldFont);
        txtTenKM.setPreferredSize(new Dimension(300, 35));
        formPanel.add(txtTenKM, gbc);

        // Ngày Bắt Đầu
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Ngày bắt đầu: *"), gbc);
        gbc.gridx = 1;
        dateChooserBatDau = new JDateChooser();
        dateChooserBatDau.setDateFormatString("dd/MM/yyyy");
        dateChooserBatDau.setFont(fieldFont);
        dateChooserBatDau.setPreferredSize(new Dimension(300, 35));
        formPanel.add(dateChooserBatDau, gbc);

        // Ngày Kết Thúc
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Ngày kết thúc: *"), gbc);
        gbc.gridx = 1;
        dateChooserKetThuc = new JDateChooser();
        dateChooserKetThuc.setDateFormatString("dd/MM/yyyy");
        dateChooserKetThuc.setFont(fieldFont);
        dateChooserKetThuc.setPreferredSize(new Dimension(300, 35));
        formPanel.add(dateChooserKetThuc, gbc);

        // Phần trăm giảm giá
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Phần trăm giảm giá (%): *"), gbc);
        gbc.gridx = 1;
        spinnerPhanTram = new JSpinner(new SpinnerNumberModel(5.0, 0.1, 100.0, 0.5));
        spinnerPhanTram.setFont(fieldFont);
        spinnerPhanTram.setPreferredSize(new Dimension(300, 35));
        formPanel.add(spinnerPhanTram, gbc);
        
        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(230, 245, 245));
        JButton btnXacNhan = createStyledButton("XÁC NHẬN", new Color(0, 0, 205), Color.WHITE);
        btnXacNhan.addActionListener(e -> xuLyThem());
        JButton btnHuy = createStyledButton("HỦY", Color.WHITE, Color.DARK_GRAY);
        btnHuy.addActionListener(e -> dispose());
        buttonPanel.add(btnXacNhan);
        buttonPanel.add(btnHuy);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(140, 40));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new LineBorder(bgColor.darker(), 2, true));
        return button;
    }

    private void xuLyThem() {
        if (!validateInput()) return;

        try {
            String maKM = kmDAO.generateMaKM();
            String tenKM = txtTenKM.getText().trim();
            Date ngayBatDau = dateChooserBatDau.getDate();
            Date ngayKetThuc = dateChooserKetThuc.getDate();
            double phanTram = (Double) spinnerPhanTram.getValue();

            KhuyenMai km = new KhuyenMai(maKM, tenKM, ngayBatDau, ngayKetThuc, phanTram);
            
            if (kmDAO.themKhuyenMai(km)) {
                JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                parentForm.reloadTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
        if (txtTenKM.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên khuyến mãi không được rỗng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dateChooserBatDau.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dateChooserKetThuc.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày kết thúc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dateChooserKetThuc.getDate().before(dateChooserBatDau.getDate())) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}