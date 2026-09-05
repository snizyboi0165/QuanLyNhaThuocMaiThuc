package gui.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import com.toedter.calendar.JDateChooser;
import dao.KhuyenMaiDAO;
import entity.KhuyenMai;
import gui.form.FormKhuyenMai;

import java.util.Date;

public class DialogCapNhatKhuyenMai extends JDialog {
    private JTextField txtMaKM;
    private JTextField txtTenKM;
    private JDateChooser dateChooserBatDau;
    private JDateChooser dateChooserKetThuc;
    private JSpinner spinnerPhanTram;

    private KhuyenMaiDAO kmDAO;
    private FormKhuyenMai parentForm;
    private KhuyenMai khuyenMai; // Đối tượng khuyến mãi cần cập nhật

    public DialogCapNhatKhuyenMai(Frame parent, FormKhuyenMai form, KhuyenMai km) {
        super(parent, "Cập Nhật Thông Tin Khuyến Mãi", true);
        this.parentForm = form;
        this.khuyenMai = km;
        this.kmDAO = new KhuyenMaiDAO();
        
        initComponents();
        loadDataToForm(); // Tải dữ liệu của khuyến mãi vào form
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(550, 500);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));

        // === Title Panel ===
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblTitle = new JLabel("CẬP NHẬT THÔNG TIN KHUYẾN MÃI");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        // === Form Panel ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);

        Font labelFont = new Font("Roboto", Font.PLAIN, 14);
        Font fieldFont = new Font("Roboto", Font.PLAIN, 14);

        // Mã Khuyến Mãi
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Mã khuyến mãi:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtMaKM = new JTextField();
        txtMaKM.setFont(fieldFont);
        txtMaKM.setPreferredSize(new Dimension(300, 35));
        txtMaKM.setEnabled(false); // Không cho sửa mã
        txtMaKM.setBackground(new Color(240, 240, 240));
        formPanel.add(txtMaKM, gbc);

        // Tên Khuyến Mãi
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên khuyến mãi: *"), gbc);
        gbc.gridx = 1;
        txtTenKM = new JTextField();
        txtTenKM.setFont(fieldFont);
        formPanel.add(txtTenKM, gbc);

        // Ngày Bắt Đầu
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Ngày bắt đầu: *"), gbc);
        gbc.gridx = 1;
        dateChooserBatDau = new JDateChooser();
        dateChooserBatDau.setDateFormatString("dd/MM/yyyy");
        dateChooserBatDau.setFont(fieldFont);
        formPanel.add(dateChooserBatDau, gbc);

        // Ngày Kết Thúc
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Ngày kết thúc: *"), gbc);
        gbc.gridx = 1;
        dateChooserKetThuc = new JDateChooser();
        dateChooserKetThuc.setDateFormatString("dd/MM/yyyy");
        dateChooserKetThuc.setFont(fieldFont);
        formPanel.add(dateChooserKetThuc, gbc);

        // Phần trăm giảm giá
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Phần trăm giảm giá (%): *"), gbc);
        gbc.gridx = 1;
        spinnerPhanTram = new JSpinner(new SpinnerNumberModel(5.0, 0.1, 100.0, 0.5));
        spinnerPhanTram.setFont(fieldFont);
        formPanel.add(spinnerPhanTram, gbc);
        
        add(formPanel, BorderLayout.CENTER);

        // === Button Panel ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(230, 245, 245));
        JButton btnCapNhat = createStyledButton("CẬP NHẬT", new Color(0, 0, 205), Color.WHITE);
        btnCapNhat.setBackground(new Color(0, 0, 205));
        btnCapNhat.addActionListener(e -> xuLyCapNhat());
        JButton btnHuy = createStyledButton("HỦY", Color.WHITE, Color.DARK_GRAY);
        btnHuy.addActionListener(e -> dispose());
        buttonPanel.add(btnCapNhat);
        buttonPanel.add(btnHuy);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadDataToForm() {
        txtMaKM.setText(khuyenMai.getMaKM());
        txtTenKM.setText(khuyenMai.getTenKM());
        dateChooserBatDau.setDate(khuyenMai.getNgayBatDau());
        dateChooserKetThuc.setDate(khuyenMai.getNgayKetThuc());
        spinnerPhanTram.setValue(khuyenMai.getPhanTramGiamGia());
    }
    
    private void xuLyCapNhat() {
        if (!validateInput()) return;

        try {
            String maKM = txtMaKM.getText();
            String tenKM = txtTenKM.getText().trim();
            Date ngayBatDau = dateChooserBatDau.getDate();
            Date ngayKetThuc = dateChooserKetThuc.getDate();
            double phanTram = (Double) spinnerPhanTram.getValue();

            KhuyenMai kmCapNhat = new KhuyenMai(maKM, tenKM, ngayBatDau, ngayKetThuc, phanTram);
            
            if (kmDAO.capNhatKhuyenMai(kmCapNhat)) {
                JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                parentForm.reloadTable(); // Tải lại bảng ở form chính
                dispose(); // Đóng dialog
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
        // Các bước kiểm tra dữ liệu tương tự như DialogThemKhuyenMai
        if (txtTenKM.getText().trim().isEmpty() || dateChooserBatDau.getDate() == null || dateChooserKetThuc.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường có dấu *!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dateChooserKetThuc.getDate().before(dateChooserBatDau.getDate())) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
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
}