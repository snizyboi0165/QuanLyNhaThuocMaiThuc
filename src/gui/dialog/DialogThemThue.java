package gui.dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import dao.ThueDAO;
import entity.Thue;
import gui.form.FormThue;

public class DialogThemThue extends JDialog {
    private JTextField txtTenThue;
    private JSpinner spinnerPhanTram;
    private ThueDAO thueDAO;
    private FormThue parentForm;

    public DialogThemThue(Frame parent, FormThue form) {
        super(parent, "Thêm Mức Thuế Mới", true);
        this.parentForm = form;
        this.thueDAO = new ThueDAO();
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(500, 350);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(500, 60));
        JLabel lblTitle = new JLabel("THÊM MỨC THUẾ MỚI");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);

        Font labelFont = new Font("Roboto", Font.PLAIN, 14);
        Font fieldFont = new Font("Roboto", Font.PLAIN, 14);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Tên thuế: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtTenThue = new JTextField();
        txtTenThue.setFont(fieldFont);
        txtTenThue.setPreferredSize(new Dimension(300, 35));
        formPanel.add(txtTenThue, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Phần trăm thuế (%): *"), gbc);
        gbc.gridx = 1;
        spinnerPhanTram = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 100.0, 0.5));
        spinnerPhanTram.setFont(fieldFont);
        spinnerPhanTram.setPreferredSize(new Dimension(300, 35));
        formPanel.add(spinnerPhanTram, gbc);
        
        add(formPanel, BorderLayout.CENTER);

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
        button.setBorder(new LineBorder(bgColor.darker(), 1, true));
        return button;
    }

    private void xuLyThem() {
        if (txtTenThue.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên thuế không được rỗng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String maThue = thueDAO.generateMaThue();
            String tenThue = txtTenThue.getText().trim();
            double phanTram = (Double) spinnerPhanTram.getValue();

            Thue thue = new Thue(maThue, tenThue, phanTram);
            
            if (thueDAO.themThue(thue)) {
                JOptionPane.showMessageDialog(this, "Thêm thuế thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                parentForm.reloadTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thuế thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}