package gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.toedter.calendar.JDateChooser;

import dao.DanhMucThuocDAO;
import entity.DanhMucThuoc;
import entity.KhuyenMai;
import gui.form.FormDanhMucThuoc;

public class DialogCapNhatDanhMucThuoc extends JDialog {
	private FormDanhMucThuoc parentForm;
	private DanhMucThuoc dmt;
	private DanhMucThuocDAO dmtDAO;
	private JTextField txtMaDanhMuc;
	private JTextField txtTenDanhMuc;
	public DialogCapNhatDanhMucThuoc(Frame parent, FormDanhMucThuoc formDanhMucThuoc, DanhMucThuoc dmt) {
		super(parent,"CẬP NHẬT DANH MỤC THUỐC", true);
		this.parentForm = formDanhMucThuoc;
		this.dmt = dmt;
		this.dmtDAO = new DanhMucThuocDAO();
		initComponents();
		this.setLocationRelativeTo(null);
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

        // Mã danh mục
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Mã danh mục:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtMaDanhMuc = new JTextField();
        txtMaDanhMuc.setFont(fieldFont);
        txtMaDanhMuc.setPreferredSize(new Dimension(300, 35));
        txtMaDanhMuc.setEnabled(false); // Không cho sửa mã
        txtMaDanhMuc.setBackground(new Color(240, 240, 240));
        formPanel.add(txtMaDanhMuc, gbc);

        // Tên danh mục
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên danh mục: *"), gbc);
        gbc.gridx = 1;
        txtTenDanhMuc = new JTextField();
        txtTenDanhMuc.setFont(fieldFont);
        formPanel.add(txtTenDanhMuc, gbc);
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
        
        loadDataToForm();
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
    
    private void loadDataToForm() {
        txtMaDanhMuc.setText(dmt.getMaDanhMuc());
        txtTenDanhMuc.setText(dmt.getTenDanhMuc());
    }
    
    private void xuLyCapNhat() {

        try {
            String maDanhMuc = txtMaDanhMuc.getText();
            String tenDanhMuc = txtTenDanhMuc.getText().trim();

            DanhMucThuoc dmt = new DanhMucThuoc(maDanhMuc, tenDanhMuc);
            
            if (dmtDAO.capNhatDanhMuc(maDanhMuc,dmt)) {
                JOptionPane.showMessageDialog(this, "Cập nhật danh mục thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                parentForm.reloadTable(); 
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật danh mục thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

}
