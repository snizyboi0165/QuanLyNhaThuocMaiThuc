package gui.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import dao.ThueDAO;
import entity.Thue;
import gui.form.FormThue;

public class DialogCapNhatThue extends JDialog {
    private JTextField txtMaThue;
    private JTextField txtTenThue;
    private JSpinner spinnerPhanTram;
    private ThueDAO thueDAO;
    private FormThue parentForm;
    private Thue thue;

    public DialogCapNhatThue(Frame parent, FormThue form, Thue thue) {
        super(parent, "Cập Nhật Thông Tin Thuế", true);
        this.parentForm = form;
        this.thue = thue;
        this.thueDAO = new ThueDAO();
        initComponents();
        loadDataToForm();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(500, 400);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblTitle = new JLabel("CẬP NHẬT THÔNG TIN THUẾ");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Mã thuế:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtMaThue = new JTextField();
        txtMaThue.setEnabled(false);
        txtMaThue.setBackground(new Color(240, 240, 240));
        formPanel.add(txtMaThue, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên thuế: *"), gbc);
        gbc.gridx = 1;
        txtTenThue = new JTextField();
        formPanel.add(txtTenThue, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Phần trăm thuế (%): *"), gbc);
        gbc.gridx = 1;
        spinnerPhanTram = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 100.0, 0.5));
        formPanel.add(spinnerPhanTram, gbc);
        
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(230, 245, 245));
        
        JButton btnCapNhat = createStyledButton("CẬP NHẬT", new Color(0, 0, 205), Color.WHITE);
        btnCapNhat.addActionListener(e -> xuLyCapNhat());
        
        JButton btnHuy = createStyledButton("HỦY", Color.WHITE, Color.DARK_GRAY);
        btnHuy.addActionListener(e -> dispose());
        
        buttonPanel.add(btnCapNhat);
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
        button.setBorder(new LineBorder(bgColor.darker(), 1, true));
        return button;
    }

    private void loadDataToForm() {
        txtMaThue.setText(thue.getMaThue());
        txtTenThue.setText(thue.getTenThue());
        spinnerPhanTram.setValue(thue.getPhanTramThue());
    }
    
    private void xuLyCapNhat() {
        if (txtTenThue.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên thuế không được rỗng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String maThue = txtMaThue.getText();
            String tenThue = txtTenThue.getText().trim();
            double phanTram = (Double) spinnerPhanTram.getValue();

            Thue thueCapNhat = new Thue(maThue, tenThue, phanTram);
            
            if (thueDAO.capNhatThue(thueCapNhat)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                parentForm.reloadTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}